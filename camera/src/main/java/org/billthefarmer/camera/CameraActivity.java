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

import android.app.Activity;
import android.hardware.Camera;
import android.media.MediaActionSound;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class CameraActivity extends Activity
    implements View.OnClickListener, Camera.PictureCallback
{
    static final private String TAG = "CameraActivity";

    private int cameraId;
    private Camera camera;
    private CameraPreview preview;
    private MediaActionSound sound;
    private PuzzleView view;

    private LooperThread thread;

    // public TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Create our Preview view and set it as the content of our
        // activity.
        preview = new CameraPreview(this);
        setContentView(preview);

	view = new PuzzleView(this);
        FrameLayout.LayoutParams params = new
	    FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                     ViewGroup.LayoutParams.MATCH_PARENT);
	addContentView(view, params);

        Button button = new Button(this);
        params = new
	    FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                     ViewGroup.LayoutParams.WRAP_CONTENT);
	params.bottomMargin = 128;
	params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;

	button.setBackgroundResource(R.drawable.colour_button);
        button.setOnClickListener(this);
	addContentView(button, params);

	thread = new LooperThread(view);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // Create an instance of Camera
        camera = Camera.open(cameraId);

	// Orient the camera
    	camera.setDisplayOrientation(90);

	// Set preview camera
	preview.setCamera(camera);
	preview.setThread(thread);

	// Load up shutter sound
	sound = new MediaActionSound();
	sound.load(MediaActionSound.SHUTTER_CLICK);

	view.setSystemUiVisibility(// View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
				   View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
				   View.SYSTEM_UI_FLAG_LOW_PROFILE |
				   View.SYSTEM_UI_FLAG_FULLSCREEN);

	try
	{
	    thread.start();
	}

	catch (Exception e)
	{
	    // Ignore, thread already running
	}
    }

    @Override
    public void onPause()
    {
        super.onPause();

        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.

        if (camera != null)
        {
	    try
	    {
		camera.setPreviewCallback(null);
		camera.stopPreview();
		camera.release();
		camera = null;
	    }

	    catch (Exception e)
	    {
		// Ignore, no preview running
	    }
	}

	// Release sound resources
	sound.release();

	if (thread != null)
	{
	    thread.looper.quit();
	}
    }

    @Override
    public void onClick(View v)
    {
	// get an image from the camera
	camera.takePicture(null, null, this);
	// camera.startPreview();
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera)
    {
	sound.play(MediaActionSound.SHUTTER_CLICK);
    }
}
