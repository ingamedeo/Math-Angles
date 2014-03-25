package com.ingamedeo.mathangles;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

public class MenuActivity extends PreferenceActivity {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.menu);

        //Only API 11 > >
        int currentAPIVersion = android.os.Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {

            // RUN THE CODE SPECIFIC TO THE API LEVELS ABOVE HONEYCOMB (API 11+)
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true); //Modify Action Bar. Back Button
        }

        //Old Way to add preferences..Works on GingerBread.
        addPreferencesFromResource(R.xml.settings);

        //getFragmentManager().beginTransaction().replace(android.R.id.content, new MenuFragment()).commit();

    }
    /*
    public static class MenuFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
        }
    }
    */
    public boolean onOptionsItemSelected(MenuItem item) { //Action Bar
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
