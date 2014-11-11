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

import java.io.IOException;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

// A basic Camera preview class
public class CameraPreview extends SurfaceView
    implements SurfaceHolder.Callback
{
    static final private String TAG = "CameraPreview";

    private SurfaceHolder holder;
    private LooperThread thread;
    private Camera camera;

    public CameraPreview(Context context)
    {
        super(context);

	// Install a SurfaceHolder.Callback so we get notified when
        // the underlying surface is created and destroyed.
        holder = getHolder();
        holder.addCallback(this);
    }

    public void setCamera(Camera camera)
    {
	this.camera = camera;
    }

    public void setThread(LooperThread thread)
    {
	this.thread = thread;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        // The Surface has been created, now tell the camera where to
        // draw the preview.
        try
        {
            camera.setPreviewDisplay(holder);
	    camera.setPreviewCallback(thread.handler);
            camera.startPreview();
        }
        catch (IOException e)
        {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        // empty. Take care of releasing the Camera preview in your
        // activity.
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
    {
        // If your preview can change or rotate, take care of those
        // events here.  Make sure to stop the preview before resizing
        // or reformatting it.

        if (holder.getSurface() == null)
        {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try
        {
            camera.stopPreview();
        }

        catch (Exception e)
        {
            // ignore: tried to stop a non-existent preview
        }
	Camera.Parameters cameraParams = camera.getParameters();
	cameraParams.setPreviewSize(640, 480);
	camera.setParameters(cameraParams);

	Camera.Size size = camera.getParameters().getPreviewSize();
        FrameLayout.LayoutParams params =
	    (FrameLayout.LayoutParams) getLayoutParams();
        params.height = w * size.width / size.height; // portrait mode only
	params.gravity = Gravity.CENTER_VERTICAL;
        setLayoutParams(params);

        // start preview with new settings
        try
        {
            camera.setPreviewDisplay(holder);
	    camera.setPreviewCallback(thread.handler);
            camera.startPreview();

        }

        catch (Exception e)
        {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}
