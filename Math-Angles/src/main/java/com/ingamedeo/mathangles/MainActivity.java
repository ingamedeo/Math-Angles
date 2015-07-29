package com.ingamedeo.mathangles;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_SETTINGS = 1;
    SharedPreferences sharedPrefs;
    int numberofdigits;
    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        //double t = 3.958;
        //fromdectosess(String.valueOf(t));
        //decimal.clearFocus();
        //gradi.requestFocus();
        todec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //From sessagesimali to everything else
                if (isEmpty(gradi) && isEmpty(primi) && isEmpty(secondi)) { //If all are empty..check for decimal
                    if (isEmpty(decimal)) { //If empty check for rad
                        if (isEmpty(rad)) { //If rad is empty show error
                            mostratoast(getResources().getString(R.string.toast_message));
                        } else { //If FOUND rad
                            decimal.setText(df.format(convertfromrad(Float.parseFloat(rad.getText().toString()))));
                            ArrayList<Integer> returnsess = fromdectosess(String.valueOf(convertfromrad(Float.parseFloat(rad.getText().toString()))));
                            for (int t=0; t < returnsess.size(); t++) {
                                if (t==0) {
                                    gradi.setText(String.valueOf(returnsess.get(t)));
                                } else if (t==1) {
                                    primi.setText(String.valueOf(returnsess.get(t)));
                                } else if (t==2) {
                                    secondi.setText(String.valueOf(returnsess.get(t)));
                                }
                            }
                        }
                    } else { //If FOUND decimal
                        ArrayList<Integer> returnsess = fromdectosess(decimal.getText().toString());
                        for (int t=0; t < returnsess.size(); t++) {
                            if (t==0) {
                                gradi.setText(String.valueOf(returnsess.get(t)));
                            } else if (t==1) {
                                primi.setText(String.valueOf(returnsess.get(t)));
                            } else if (t==2) {
                                secondi.setText(String.valueOf(returnsess.get(t)));
                            }
                        }
                        rad.setText(df.format(convertorad(Float.parseFloat(decimal.getText().toString()))));
                    }
                }
                else { //If FOUND gradi
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

    /*
    Xg = (Xr *180) / P
    Xr = (Xg * P) / 180
     */

    //Result is sessadecimali
    private float convertfromrad(float rad) {
        float pi = (float) Math.PI;
        float result = ((rad*180)/pi);
        return result;
    }


    private ArrayList<Integer> fromdectosess(String decimal) {
        ArrayList<Integer> ris = new ArrayList<Integer>();

        for (int i=0; i<3; i++) { //I need to cycle 3 times...to get gradi, primi, secondi
            //Split decimal part
            String[] parts = decimal.split("\\."); //Split - parte intera e parte decimale
            String parteintera = parts[0]; //Prendo la parte intera
            ris.add(Integer.parseInt(parteintera)); //Metto nell'arraylist

            if (parts.length > 1) { //Se è > di 1, c'è qualcosa dopo la virgola
                String partedec = "0." + parts[1]; //ottengo solo es. 20 ... num. decimale è 0.20
                Double p = Double.parseDouble(partedec) * 60; //Moltiplico per 60
                decimal = p.toString(); //Setto questo nuovo numero come numero da scansionare al prossimo passaggio.
            } else {
                break;
            }
        }

        /*
        //print out
        for (int j=0; j < ris.size(); j++) {
            System.out.println(ris.get(j));
        }
        */

        return ris;
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
