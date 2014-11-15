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
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;
import android.util.Log;

public class YuvConverter
{
    static final private String TAG = "YuvConverter";

    private RenderScript rs;
    private ScriptIntrinsicYuvToRGB siYuvToRGB;
    private Allocation yuvIn;
    private Allocation rgbOut;

    private byte[] pixels;
    private Bitmap bitmap;

    public YuvConverter(Context context)
    {
	rs = RenderScript.create(context);
	siYuvToRGB =
	    ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
    }

    public byte[] convertToRGB(byte[] yuv, int width, int height)
    {
	int size = (width * height * 4);

	if (yuvIn == null || pixels.length != size)
	{
	    Type.Builder yuvType = new Type.Builder(rs, Element.U8(rs))
		.setX(yuv.length);
	    yuvIn = Allocation.createTyped(rs, yuvType.create());

	    Type.Builder rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs))
		.setX(width)
		.setY(height);
	    rgbOut = Allocation.createTyped(rs, rgbaType.create());
	    pixels = new byte[size];
	}

	yuvIn.copyFrom(yuv);
	siYuvToRGB.setInput(yuvIn);
	siYuvToRGB.forEach(rgbOut);
	rgbOut.copyTo(pixels);

	return pixels;
    }

    public Bitmap convertBytes(byte[] rgb, int width, int height)
    {
	int size = (width * height * 4);

	if (rgbOut != null || rgbOut.getBytesSize() != size)
	{
	    Type.Builder rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs))
		.setX(width)
		.setY(height);
	    rgbOut = Allocation.createTyped(rs, rgbaType.create(),
					    Allocation.USAGE_SCRIPT);
	    bitmap = Bitmap.createBitmap(width, height,
					 Bitmap.Config.ARGB_8888);
	}

	rgbOut.copyFrom(rgb);
	rgbOut.copyTo(bitmap);

	return bitmap;
    }
}
