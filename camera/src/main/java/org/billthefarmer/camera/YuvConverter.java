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
    private ScriptIntrinsicYuvToRGB si;
    private Allocation yuvIn;
    private Allocation rgbIn;
    private Allocation rgbOut;

    private byte[] pixels;
    private Bitmap bitmap;

    public YuvConverter(Context context)
    {
	rs = RenderScript.create(context);
	si = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
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
	si.setInput(yuvIn);
	si.forEach(rgbOut);
	rgbOut.copyTo(pixels);

	return pixels;
    }

    public Bitmap convertRGB(byte[] rgb, int width, int height)
    {
	int size = (width * height * 4);

	if (rgbIn == null || rgbIn.getBytesSize() != size)
	{
	    Type.Builder rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs))
		.setX(width)
		.setY(height);
	    rgbIn = Allocation.createTyped(rs, rgbaType.create());
	    bitmap = Bitmap.createBitmap(width, height,
					 Bitmap.Config.ARGB_8888);
	}

	// This is just using the copyFrom and copyTo methods to write
	// the byte array into a bitmap

	rgbIn.copyFrom(rgb);
	rgbIn.copyTo(bitmap);
	return bitmap;
    }
    /*
    public byte[] rotateRGB(byte rgb[], int width, int height)
    {
	int size = (width * height * 4);

	if (rotIn == null || rotIn.getBytesSize() != size)
	{
	    Type.Builder rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs))
		.setX(width)
		.setY(height);
	    rotIn = Allocation.createTyped(rs, rgbaType.create());

	    rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs))
		.setX(height)
		.setY(width);
	    rotOut = Allocation.createTyped(rs, rgbaType.create());

	    bytes = new byte[size];
	}

	// This doesn't work because the input and output allocations
	// have to be identical. You could possibly cheat by telling
	// porkies, but doing it in native code is easier

	Log.d(TAG, "size = " + size + ", width = " + width +
	      ", height = " + height);

	rotIn.copyFrom(rgb);
	scriptCRotate.set_in(rotIn);
	scriptCRotate.set_out(rotOut);
	scriptCRotate.set_width(width);
	scriptCRotate.set_height(height);
	scriptCRotate.forEach_rotate(rotIn, rotOut);
	rotOut.copyTo(bytes);

	return bytes;
    }
    */
}
