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

import android.os.Looper;
import android.util.Log;

import java.lang.Thread;

class LooperThread extends Thread
{
    static final private String TAG = "LooperThread";

    public Looper looper;
    public PuzzleView view;
    public PreviewHandler handler;

    // This is all a bit obtuse because this thread doesn't need a
    // view but the handler does so it can create a converter, and so
    // it can pass the view data and invalidate it, and the handler
    // has to be created from here so it runs on this thread

    public LooperThread(PuzzleView view)
    {
	super(TAG);

	this.view = view;
    }

    public void run()
    {
	Looper.prepare();

	looper = Looper.myLooper();

	handler = new PreviewHandler(view);

	Looper.loop();
    }
}
