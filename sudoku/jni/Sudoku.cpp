////////////////////////////////////////////////////////////////////////////////
//
//  Sudoku - An Android Sudoku library.
//
//  Copyright (C) 2014	Bill Farmer
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Bill Farmer	 william j farmer [at] yahoo [dot] co [dot] uk.
//
///////////////////////////////////////////////////////////////////////////////

#include "Sudoku.h"

// Global static instance of Sudoku object

Sudoku sudoku(NULL);

// Global pointer to rotated bitmap byte array

BYTE *pixels = NULL;

//////////////////////////////////////////////////////////////////////////
// Function:	init()
// Purpose:	Create Sudoku object
//
//////////////////////////////////////////////////////////////////////////
void
Java_org_billthefarmer_sudoku_Sudoku_init(JNIEnv *env,
					  jobject obj)
{
    // Not used, replaced by static instance
    // sudoku = new Sudoku();
}

//////////////////////////////////////////////////////////////////////////
// Function:	process()
// Purpose:	Run the process to find a sudoku puzzle in the supplied
//		bitmap.
// Parameters:	jdata - byte array rgb bitmap to be processed
//		width - width of bitmap
//		height - height of bitmap
//		resolution - resolution of bitmap
//
// Returns:	The bitmap after it has been rotated and possibly drawn on
//////////////////////////////////////////////////////////////////////////

jbyteArray
Java_org_billthefarmer_sudoku_Sudoku_process(JNIEnv *env,
					     jobject obj,
					     jbyteArray jdata,
					     jint width,
					     jint height,
					     jint resolution)
{
    SIZE size;

    // Create a new byte array

    if (pixels == NULL)
	pixels = new BYTE[width * height * 4];

    // Get the bitmap data

    jbyte *data = env->GetByteArrayElements(jdata, NULL);
    jint length = env->GetArrayLength(jdata);

    // Get 32 bit references to the bitmaps

    LPCOLORREF rgb = (LPCOLORREF) data;
    LPCOLORREF rot = (LPCOLORREF) pixels;

    // Rotate the bitmap

    for (int y = 0; y < height; y++)
	for (int x = 0; x < width; x++)
	    rot[y + (((width - 1) - x) * height)] = rgb[x + (y * width)];

    // Reverse the height and width dimensions

    size.cx = height;
    size.cy = width;

    // Call the Sudoku OCR code

    sudoku.process(pixels, size, resolution);

    // Release the input bitmap and overwrite the same jave byte array
    // with the rotated and drawn on bitmap. This is to avoid creating
    // a new java byte array each time

    env->ReleaseByteArrayElements(jdata, data, 0);
    env->SetByteArrayRegion(jdata, 0, length, (jbyte *) pixels);

    return jdata;
}

//////////////////////////////////////////////////////////////////////////
// Function:	getAngle()
// Purpose:	Get the angle of the sudoku puzzle
//
// Returns:	The angle of the sudoku puzzle
//////////////////////////////////////////////////////////////////////////

jint
Java_org_billthefarmer_sudoku_Sudoku_getAngle(JNIEnv *env,
					      jobject obj)
{
    return sudoku.strongestLine.theta;
}

//////////////////////////////////////////////////////////////////////////
// Function:	getRect()
// Purpose:	Get the four corners of the puzzle as a int array. This is
//		possibly the easiest way to pass the data back as using
//		points or two dimensional arrays involves multiple Java
//		objects
// Parameters:	jrect - Java int array to fill in
//
// Returns:	true if successful, false otherwise
//////////////////////////////////////////////////////////////////////////

jboolean
Java_org_billthefarmer_sudoku_Sudoku_getRect(JNIEnv *env,
					     jobject obj,
					     jintArray jrect)
{
    if (sudoku.rectDetected)
    {
	jint *rect = env->GetIntArrayElements(jrect, NULL);
	jint length = env->GetArrayLength(jrect);

	if (length == 8)
	{
	    rect[0] = sudoku.rect[0].x;
	    rect[1] = sudoku.rect[0].y;
	    rect[2] = sudoku.rect[1].x;
	    rect[3] = sudoku.rect[1].y;
	    rect[4] = sudoku.rect[2].x;
	    rect[5] = sudoku.rect[2].y;
	    rect[6] = sudoku.rect[3].x;
	    rect[7] = sudoku.rect[3].y;

	    env->ReleaseIntArrayElements(jrect, rect, 0);
	    return true;
	}
    }

    return false;
}

//////////////////////////////////////////////////////////////////////////
// Function:	getPuzzle()
// Purpose:	Get the puzzle as an int array
// Parameters:	jpuzzle - Java int array to fill in
//
// Returns:	true if successful, false otherwise
//////////////////////////////////////////////////////////////////////////

jboolean
Java_org_billthefarmer_sudoku_Sudoku_getPuzzle(JNIEnv *env,
					       jobject obj,
					       jintArray jpuzzle)
{
    if (sudoku.ocrValid)
    {
	jint *puzzle = env->GetIntArrayElements(jpuzzle, NULL);
	jint length = env->GetArrayLength(jpuzzle);

	if (length == 81)
	{
	    for (int y = 0; y < 9; y++)
		for (int x = 0; x < 9; x++)
		    puzzle[x + (y * 9)] = sudoku.puzzle[y][x];

	    return true;
	}
    }

    return false;
}
   

void
Java_org_billthefarmer_sudoku_Sudoku_release(JNIEnv *env,
					     jobject obj)
{
    // Not used, can't delete a static instance
    // delete sudoku;
}

/////////////////////////////////////////////////////////////////////////////
// Sudoku construction/destruction
//
/////////////////////////////////////////////////////////////////////////////
Sudoku::Sudoku(void *dummy)
{
    display.black = FALSE;
    display.white = FALSE;
    display.rotation = FALSE;
    display.whiteCandidates = FALSE;
    display.whiteArea = FALSE;
    display.lineDetect = FALSE;
    display.grid = TRUE;
    display.OCRresult = TRUE;
    display.tempSolution = FALSE;
    display.finalSolution = FALSE;
    display.smallFont = FALSE;
}

Sudoku::~Sudoku()
{
}

//////////////////////////////////////////////////////////////////////////
// Function:	process()
// Purpose:	Do the OCR and sudoku puzzle decoding
// Parameters:	data - byte array bitmap from device camera
//		size - dimensions of bitmap
//		resolution- resolution of bitmap
//
//////////////////////////////////////////////////////////////////////////
void Sudoku::process(BYTE data[], SIZE size, DWORD resolution)
{
    switch(resolution)
    {
    case 8:
    case 16:
	// ignore
	break;
    case 24:
    case 32:
	{
	    SudBitmap sud(resolution, data, size, &display);
	    sud.Monochrome();
	    if (sud.HoughTransformCenter(&strongestLine))
	    if (rectDetected = sud.DetectRect(strongestLine.theta))
	    {
		sud.GetRect(rect);
		if (ocrValid = sud.OCR())
		{
		    sud.GetPuzzle(puzzle);
		    if (sudSolved = sud.Solve())
			sud.DisplaySolution();
		}
	    }
	    break;
	}
    default:
	//unknown format
	break;
    }
}

