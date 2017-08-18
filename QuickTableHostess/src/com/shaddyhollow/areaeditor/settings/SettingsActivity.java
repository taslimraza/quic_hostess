package com.shaddyhollow.areaeditor.settings;

import android.app.ActivityManager;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.widget.Toast;

import com.shaddyhollow.areaeditor.R;
import com.shaddyhollow.robospice.BaseRoboSpiceActivity;

public class SettingsActivity extends BaseRoboSpiceActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (ActivityManager.isUserAMonkey()) {
            finish();
        }

        int prefResourceID = R.xml.hostess_preferences;
        try {
        	getResources().getResourceName(prefResourceID);
        } catch (NotFoundException e) {
        	Toast.makeText(this, "Unable to launch preferences", Toast.LENGTH_SHORT).show();
        	return;
        }
        	
        SettingsFragment fragment = SettingsFragment.newInstance(contentManager, prefResourceID);
        getFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
    }

}
