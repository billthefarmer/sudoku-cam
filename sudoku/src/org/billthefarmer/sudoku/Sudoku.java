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

package org.billthefarmer.sudoku;

import android.util.Log;

// Sudoku

public class Sudoku
{
    private static final String TAG = "Sudoku";

    private int rect[];
    private int puzzle[];

    public Sudoku()
    {
	init();

	rect = new int[8];
	puzzle = new int[81];
    }

    public boolean getPoints(int points[][])
    {
	if (points.length == 4)
	{
	    if (getRect(rect) && rect.length == 8)
	    {
		points[0][0] = rect[0];
		points[0][1] = rect[1];
		points[1][0] = rect[2];
		points[1][1] = rect[3];
		points[2][0] = rect[4];
		points[2][1] = rect[5];
		points[3][0] = rect[6];
		points[3][1] = rect[7];

		return true;
	    }
	}

	return false;
    }

    public boolean getPuzzleValues(int values[][])
    {
	if (values.length == 9 && values[0].length == 9)
	{
	    if (getPuzzle(puzzle) && puzzle.length == 81)
	    {
		for (int y = 0; y < 9; y++)
		    for (int x = 0; x < 9; x++)
			values[y][x] = puzzle[x + (y * 9)];

		return true;
	    }
	}

	return false;
    }

    private native void init();
    public native byte[] process(byte[] data, int width, int height,
				 int resolution);
    public native int getAngle();
    private native boolean getRect(int rect[]);
    private native boolean getPuzzle(int puzzle[]);
    public native void release();

    // Load sudoku library

    static
    {
	System.loadLibrary("sudoku");
    }
}
