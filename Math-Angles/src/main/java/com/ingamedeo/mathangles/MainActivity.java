package com.ingamedeo.mathangles;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends Activity {

    private static final int RESULT_SETTINGS = 1;
    SharedPreferences sharedPrefs;
    int numberofdigits;
    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText gradi = (EditText) findViewById(R.id.gradi);
        final EditText primi = (EditText) findViewById(R.id.primi);
        final EditText secondi = (EditText) findViewById(R.id.secondi);
        Button todec = (Button) findViewById(R.id.todec);
        final EditText decimal = (EditText) findViewById(R.id.decimal);
        final EditText rad = (EditText) findViewById(R.id.rad);

        // Get preferences. Inizialization.
        sharedPrefs = PreferenceManager // Create a Preferences Object. This is used to retrive preferences from menu.
                .getDefaultSharedPreferences(this);
        numberofdigits = Integer.parseInt(sharedPrefs.getString("prefDigits", "6")); //Number of digits from menu preference.
        df = new DecimalFormat();
        df.setMaximumFractionDigits(numberofdigits);

        //decimal.clearFocus();
        //gradi.requestFocus();
        todec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmpty(gradi)==true && isEmpty(primi)==true && isEmpty(secondi)==true) { //If all are empty..then show an error
                    mostratoast(getResources().getString(R.string.toast_message));
                }
                else {
                    if (isEmpty(gradi)) {
                        gradi.setText("00");
                    }
                    if (isEmpty(primi)) {
                        primi.setText("00");
                    }
                    if (isEmpty(secondi)) {
                        secondi.setText("00");
                    }
                    if(Integer.parseInt(primi.getText().toString())>60 || Integer.parseInt(secondi.getText().toString())>60) {
                        mostralert(getResources().getString(R.string.alert_title),getResources().getString(R.string.alert_text));
                    }
                    float resultdecimal = convertodec(Integer.parseInt(gradi.getText().toString()),Integer.parseInt(primi.getText().toString()),Integer.parseInt(secondi.getText().toString()));
                    decimal.setText(df.format(resultdecimal));
                    rad.setText(df.format(convertorad(resultdecimal)));
                    normalizzprimisecondi();
                }
            }
        });
        todec.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clear();
                return true; //Activates vibration and no autoclick after long click.
            }
        });
    }

    private void clear() {
        EditText gradi = (EditText) findViewById(R.id.gradi);
        EditText primi = (EditText) findViewById(R.id.primi);
        EditText secondi = (EditText) findViewById(R.id.secondi);
        EditText decimal = (EditText) findViewById(R.id.decimal);
        EditText rad = (EditText) findViewById(R.id.rad);
        gradi.setText("");
        primi.setText("");
        secondi.setText("");
        decimal.setText("");
        rad.setText("");
    }

    private float convertodec(float gradis, float primis, float secondis) { //Converts from sexagesimal to decimal
        float result = gradis + primis/60 + secondis/3600; //Remember! To get float output..all values should be float.
        return result;
    }

    private float convertorad(float decimal) {
        float pi = (float) Math.PI;
        float result = ((decimal*pi)/180);
        return result;
    }

    private void normalizzprimisecondi() {
        EditText gradit = (EditText) findViewById(R.id.gradi);
        EditText primit = (EditText) findViewById(R.id.primi);
        EditText secondit = (EditText) findViewById(R.id.secondi);
        int gradi = Integer.parseInt(gradit.getText().toString());
        int primi = Integer.parseInt(primit.getText().toString());
        int secondi = Integer.parseInt(secondit.getText().toString());
        while (secondi>=60) {
            secondi = secondi - 60;
            primi = primi +1;
        }
        while (primi>=60) {
            primi = primi - 60;
            gradi = gradi +1;
        }
        gradit.setText(String.valueOf(gradi)); //WTF...I don't really know why I should explicity convert all this int to string? Bah...
        primit.setText(String.valueOf(primi));
        secondit.setText(String.valueOf(secondi));

    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }
    
    public void mostratoast(String testotoast) {
        Context context = getApplicationContext();

        CharSequence text = testotoast;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void mostralert(String titolo, String messaggio) {

        //New Way to create Dialogs ;)
        new AlertDialog.Builder(this)
                .setTitle(titolo)
                .setMessage(messaggio)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(R.drawable.ic_launcher)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.action_settings:
                // Single menu item is selected do something
                // Ex: launching new activity/screen or show alert message
                Intent i = new Intent(getBaseContext(), MenuActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
                return true;
            case R.id.exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //When you update the settings in menu, this updates them in the app.
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                sharedPrefs = PreferenceManager // Create a Preferences Object. This is used to retrive preferences from menu.
                        .getDefaultSharedPreferences(this);
                numberofdigits = Integer.parseInt(sharedPrefs.getString("prefDigits", "6")); //Number of digits from menu preference.
                df.setMaximumFractionDigits(numberofdigits);
                break;

        }

    }

}
