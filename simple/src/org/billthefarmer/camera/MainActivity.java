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
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	// Inflate the menu; this adds items to the action bar if it
	// is present.
	getMenuInflater().inflate(R.menu.main_menu, menu);
	return true;
    }

    // On options item

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	// Get id

	int id = item.getItemId();
	switch (id)
	{
	case R.id.menu_camera:
	    return onCameraClick(item);

	default:
	    return false;
	}
    }

    // On camera click

    private boolean onCameraClick(MenuItem item)
    {
	Intent intent = new Intent(this, CameraActivity.class);
	startActivity(intent);

	return true;
    }
}
