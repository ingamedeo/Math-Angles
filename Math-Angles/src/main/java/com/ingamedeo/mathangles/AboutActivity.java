package com.ingamedeo.mathangles;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends Activity {

    Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Button ratebutt = (Button) findViewById(R.id.ratebutt);

        //Rate Intent ;)
        ratebutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("market://details?id=" + getPackageName()));
                startActivity(i);
            }
        });

        //Only API 11 > >
        int currentAPIVersion = android.os.Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {

            // RUN THE CODE SPECIFIC TO THE API LEVELS ABOVE HONEYCOMB (API 11+)
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true); //Modify Action Bar. Back Button
        }

        TextView aboutbox = (TextView) findViewById(R.id.aboutbox);
        TextView authorbox = (TextView) findViewById(R.id.authorbox);

        //Put assets folder in: src/main/assets/
        Typeface font = Typeface.createFromAsset(c.getAssets(), "fonts/font.otf");
        aboutbox.setTypeface(font);
        authorbox.setTypeface(font);

        final TypeWriter writer = new TypeWriter(this, aboutbox);
        writer.setCharacterDelay(50);
        writer.animateText(getResources().getString(R.string.info_text));

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
