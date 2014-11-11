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

public class GreyscaleRenderer
{
    private byte[] pixels;


    public GreyscaleRenderer()
    {
    }

    public byte[] renderGreyscale(byte[] yuv, int width, int height)
    {
	int size = width * height * 3;

	if (pixels == null || pixels.length != size)
	    pixels = new byte[size];

        int inputOffset = 0;

        for (int y = 0; y < height; y++)
        {
            int outputOffset = y * width * 3;
            for (int x = 0; x < width; x++)
            {
                byte grey = yuv[inputOffset + x];
                pixels[outputOffset + (x * 3) + 0] = grey;
		pixels[outputOffset + (x * 3) + 1] = grey;
		pixels[outputOffset + (x * 3) + 2] = grey;
            }
            inputOffset += width;
        }

        return pixels;
    }
}
