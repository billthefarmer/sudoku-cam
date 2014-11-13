////////////////////////////////////////////////////////////////////////////////
//
//  Camera - An Android Camera app.
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

package org.billthefarmer.camera;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;

import org.billthefarmer.sudoku.Sudoku;

public class PreviewHandler extends Handler
    implements Camera.PreviewCallback
{
    static final public int PROCESS_OCR = 0;

    private YuvConverter converter;
    private PuzzleView view;
    private Sudoku sudoku;

    private int angle;
    private int[][] rect;
    private int[][] puzzle;

    private long count;

    public PreviewHandler(PuzzleView view)
    {
	super();

	this.view = view;

	converter = new YuvConverter(view.getContext());
	sudoku = new Sudoku();

	rect = new int[4][2];
	puzzle = new int[9][9];
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera)
    {
	Camera.Size size = camera.getParameters().getPreviewSize();

	if (data != null)
	{
	    if (count % 5 == 0)
	    {
		Message message =
		    Message.obtain(this, PROCESS_OCR, size.width,
				   size.height, data);
		message.sendToTarget();
	    }

	    count++;
	}
    }

    @Override
    public void handleMessage(Message message)
    {
	// process incoming messages here

	switch (message.what)
	{
	case PROCESS_OCR:
	    if (true)
	    {
		int width = message.arg1;
		int height =  message.arg2;
		byte[] data = (byte[]) message.obj;
		byte[] pixels = converter.convertToRGB(data, width, height);
		byte[] result = sudoku.process(pixels, width, height, 32);

		int angle = sudoku.getAngle();
		boolean detected = sudoku.getPoints(rect);
		boolean valid = sudoku.getPuzzleValues(puzzle);

		Bitmap bitmap = converter.convertBytes(result, width, height);
		view.setData(angle, detected, valid, rect, puzzle, bitmap);
		view.postInvalidate();
	    }
	    break;

	default:
	    break;
	}
    }
}
