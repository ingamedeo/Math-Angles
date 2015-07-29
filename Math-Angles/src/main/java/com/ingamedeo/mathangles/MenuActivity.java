package com.ingamedeo.mathangles;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MenuActivity extends AppCompatPreferenceActivity {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbarSetup();

        //Old Way to add preferences..Works on GingerBread.
        addPreferencesFromResource(R.xml.settings);

        Preference myPref = (Preference) findPreference("about");
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                LinkAlertDialog.create(MenuActivity.this).show();
                return true;
            }
        });

    }

    private void toolbarSetup() {
        AppBarLayout appBarLayout;
        Toolbar bar;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
            appBarLayout = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
            bar = (Toolbar) appBarLayout.findViewById(R.id.toolbar);
            root.addView(appBarLayout, 0); // insert at top
        } else {
            ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            ListView content = (ListView) root.getChildAt(0);

            root.removeAllViews();

            appBarLayout = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
            bar = (Toolbar) appBarLayout.findViewById(R.id.toolbar);

            int height;
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }else{
                height = appBarLayout.getHeight();
            }

            content.setPadding(0, height, 0, 0);

            root.addView(content);
            root.addView(appBarLayout);
        }

        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static class LinkAlertDialog {

        public static AlertDialog create(Context context) {
            final TextView message = new TextView(context);
            message.setPadding(20,20,20,0);
            String s = context.getResources().getString(R.string.app_name)+" has been developed by <b>Amedeo Arch</b>!<br><br>Check out my apps on the <a href=\"https://play.google.com/store/apps/developer?id=Amedeo+Arch\">Play Store</a>!";
            message.setText(Html.fromHtml(s));
            message.setMovementMethod(LinkMovementMethod.getInstance());

            return new AlertDialog.Builder(context)
                    .setTitle("About")
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, null)
                    .setView(message)
                    .create();
        }
    }
}
