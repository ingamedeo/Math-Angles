package com.ingamedeo.mathangles;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //Modify Action Bar. Back Button

    }

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
