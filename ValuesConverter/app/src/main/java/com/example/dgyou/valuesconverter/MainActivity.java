package com.example.dgyou.valuesconverter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.*;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // SharedPreferences for saving converted values
    SharedPreferences preferences;

    // GUI instances
    private Button kmCalcButton;
    private Button ftCalcButton;
    private Button lbsCalcButton;

    private TextView kmSavedValue;
    private TextView ftSavedValue;
    private TextView lbsSavedValue;

    // keys for reading from SharedPreferences file
    private String KM;
    private String MILE;
    private String FT;
    private String INCH;
    private String LBS;
    private String KGS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // references to GUI components
        kmCalcButton = (Button) findViewById(R.id.kmCalcButton);
        ftCalcButton = (Button) findViewById(R.id.ftCalcButton);
        lbsCalcButton = (Button) findViewById(R.id.lbsCalcButton);
        kmSavedValue = (TextView) findViewById(R.id.kmSavedValue);
        ftSavedValue = (TextView) findViewById(R.id.ftSavedValue);
        lbsSavedValue = (TextView) findViewById(R.id.lbsSavedValue);

        // initiates SharedPreferences
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // register listener for SharedPreference changes
        preferences.registerOnSharedPreferenceChangeListener(preferencesChangedListener);

        // initialize SharedPreference file keys
        KM = getResources().getString(R.string.key_kms);
        MILE = getResources().getString(R.string.key_miles);
        FT = getResources().getString(R.string.key_feet);
        INCH = getResources().getString(R.string.key_inches);
        LBS = getResources().getString(R.string.key_pounds);
        KGS = getResources().getString(R.string.key_kilos);

        // set the textviews with the saved values in the SharedPreferences file
        String kmValue = preferences.getString(KM, "0");
        String mileValue = preferences.getString(MILE, "0");
        String ftValue = preferences.getString(FT, "0");
        String inchValue = preferences.getString(INCH, "0");
        String lbsValue = preferences.getString(LBS, "0");
        String kgsValue = preferences.getString(KGS, "0");
        kgsValue = String.format("%.2f", Double.parseDouble(kgsValue));
        mileValue = String.format("%.2f", Double.parseDouble(mileValue));
        inchValue = String.format("%.2f", Double.parseDouble(inchValue));

        kmSavedValue.setText(kmValue + " kms = " + mileValue + " miles");
        ftSavedValue.setText(ftValue + " feet = " + inchValue + " inches");
        lbsSavedValue.setText(lbsValue + " Lbs = " + kgsValue + " Kgs");

        // set click listeners for the buttons to switch to the appropriate activities
        kmCalcButton.setOnClickListener(kmClickListener);
        ftCalcButton.setOnClickListener(ftClickListener);
        lbsCalcButton.setOnClickListener(lbsClickListener);

    }


    private final View.OnClickListener kmClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent kmIntent = new Intent(MainActivity.this, KmToMilesActivity.class);
                    startActivity(kmIntent);
                }
            };

    private final View.OnClickListener ftClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ftIntent = new Intent(MainActivity.this, FtToInchesActivity.class);
                    startActivity(ftIntent);
                }
            };

    private final View.OnClickListener lbsClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent lbsIntent = new Intent(MainActivity.this, LbsToKgsActivity.class);
                    startActivity(lbsIntent);
                }
            };

    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangedListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                    if (key.equals(KM) || key.equals(MILE)) {
                        String kmValue = preferences.getString(KM, "0");
                        String mileValue = preferences.getString(MILE, "0");
                        mileValue = String.format("%.2f", Double.parseDouble(mileValue));
                        kmValue = String.format("%.2f", Double.parseDouble(kmValue));
                        kmSavedValue.setText(kmValue + " kms = " + mileValue + " miles");
                    }
                    if (key.equals(FT) || key.equals(INCH)) {
                        String ftValue = preferences.getString(FT, "0");
                        String inchValue = preferences.getString(INCH, "0");
                        inchValue = String.format("%.2f", Double.parseDouble(inchValue));
                        ftValue = String.format("%.2f", Double.parseDouble(ftValue));
                        ftSavedValue.setText(ftValue + " feet = " + inchValue + " inches");
                    }
                    if (key.equals(KGS) || key.equals(LBS)) {
                        String lbsValue = preferences.getString(LBS, "0");
                        String kgsValue = preferences.getString(KGS, "0");
                        kgsValue = String.format("%.2f", Double.parseDouble(kgsValue));
                        lbsValue = String.format("%.2f", Double.parseDouble(lbsValue));
                        lbsSavedValue.setText(lbsValue + " Lbs = " + kgsValue + " Kgs");
                    }
                }
            };
}
