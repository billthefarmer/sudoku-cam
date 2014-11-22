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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.view.View;

public class PuzzleView extends View
{
    private Paint paint;

    private int width;
    private int height;

    private int resolution;

    private int angle;

    private boolean detected;
    private boolean valid;

    private int[][] rect;
    private int[][] puzzle;
    private Bitmap bitmap;

    private int[] colours;

    public PuzzleView(Context context)
    {
        super(context);

	paint = new Paint();
    }

    public synchronized void setData(int angle, boolean detected, boolean valid,
				     int[][] rect, int[][] puzzle,
				     Bitmap bitmap)
    {
	this.angle = angle;
	this.detected = detected;
	this.valid = valid;
	this.rect = rect;
	this.puzzle = puzzle;
	this.bitmap = bitmap;
   }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh)
    {
	width = w;
	height = h;
    }

    @Override
    public synchronized void onDraw(Canvas canvas)
    {
	paint.setColor(Color.GREEN);
	paint.setStyle(Paint.Style.STROKE);
	paint.setStrokeWidth(1);

	if (bitmap != null)

	{
	    int offset = (width - bitmap.getWidth()) / 2;
	    canvas.drawBitmap(bitmap, offset, 0, null);
	}
    }
}
