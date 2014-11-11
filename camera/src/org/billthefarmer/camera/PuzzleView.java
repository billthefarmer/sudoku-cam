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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
// import android.view.Gravity;
import android.view.View;
// import android.widget.FrameLayout;

public class PuzzleView extends View
{
    private Paint paint;

    private int width;
    private int height;

    private int imageWidth;
    private int imageHeight;

    private int angle;

    private boolean detected;
    private boolean valid;

    private int[][] rect;
    private int[][] puzzle;
    private byte[] pixels;

    private int[] colours;

    public PuzzleView(Context context)
    {
        super(context);

	paint = new Paint();
    }

    // public void setAspect(Camera.Size size)
    // {
    // 	FrameLayout.LayoutParams params =
    // 	    (FrameLayout.LayoutParams) getLayoutParams();
    // 	params.height = width * size.width / size.height; // portrait mode only
    // 	params.gravity = Gravity.CENTER_VERTICAL;
    // 	setLayoutParams(params);
    // }

    public void setData(int angle, boolean detected, boolean valid,
			int[][] rect, int[][]puzzle, byte[] pixels,
			int width, int height)
    {
	this.angle = angle;
	this.detected = detected;
	this.valid = valid;
	this.rect = rect;
	this.puzzle = puzzle;
	this.pixels = pixels;
	imageWidth = width;
	imageHeight = height;

	makeColours(pixels, width, height);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh)
    {
	width = w;
	height = h;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
	paint.setColor(Color.GREEN);
	paint.setStyle(Paint.Style.STROKE);
	paint.setStrokeWidth(3);

	int offset = (width - imageHeight) / 2;

	if (colours != null)
	    canvas.drawBitmap(colours, 0, imageHeight, offset, 0,
			      imageHeight, imageWidth, false, null);

	// canvas.translate(0, height / 4);

	if (detected)
	{
	    canvas.drawLine(rect[0][0], rect[0][1],
			    rect[1][0], rect[1][1], paint);
	    canvas.drawLine(rect[1][0], rect[1][1],
			    rect[2][0], rect[2][1], paint);
	    canvas.drawLine(rect[2][0], rect[2][1],
			    rect[3][0], rect[3][1], paint);
	    canvas.drawLine(rect[3][0], rect[3][1],
			    rect[0][0], rect[0][1], paint);
	}
    }

    void makeColours(byte[] pixels, int width, int height)
    {
	if (colours == null || colours.length != (width * height))
	    colours = new int[width * height];

	for (int y = 0; y < height; y++)
	    for (int x = 0; x < width; x++)
		colours[y + ((width - x - 1) * height)] = 
		    Color.rgb(pixels[(x * 3) + (y * width * 3) + 0],
			      pixels[(x * 3) + (y * width * 3) + 1],
			      pixels[(x * 3) + (y * width * 3) + 2]);
    }
}

