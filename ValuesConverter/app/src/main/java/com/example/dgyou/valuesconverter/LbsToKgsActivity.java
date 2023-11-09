package com.example.dgyou.valuesconverter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.NumberFormat;

public class LbsToKgsActivity extends AppCompatActivity {

    // keys for saving values into the SharedPreference file
    private String LBS;
    private String KGS;

    // GUI instances
    private SeekBar weightSeekBar;
    private TextView kgsTextView;
    private Button kgsSaveButton;

    // NumberFormat object to format the values to display on the TextView
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance();

    // SharedPreferences object
    SharedPreferences sharedPreferences;

    // variables to hold the values
    private int lbsValue = 0;
    private double kgsValue = 0.0;
    private final double RATE = 2.205;

    // variable to hold the formatted version of the calculated value
    private String kgsFormatValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lbs_to_kgs);

        // references to GUI components
        weightSeekBar = (SeekBar) findViewById(R.id.lbsSeekBar);
        kgsTextView = (TextView) findViewById(R.id.kgsTextView);
        kgsSaveButton = (Button) findViewById(R.id.kgsSaveButton);

        // SharedPreferences initiator
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // register listener for the button to save values into the SharedPreference file
        kgsSaveButton.setOnClickListener(saveButtonListener);

        // register listener for the SeekBar to change values when user moves the SeekBar
        weightSeekBar.setOnSeekBarChangeListener(weightChangedListener);

        // initialize SharedPreference file keys
        LBS = getResources().getString(R.string.key_pounds);
        KGS = getResources().getString(R.string.key_kilos);

        // set the TextView's initial text so the words small text doesn't show
        if (kgsFormatValue == null) {
            kgsFormatValue = "0";
            kgsTextView.setText(numberFormat.format(lbsValue) + " Lbs = " + kgsFormatValue + " Kgs");
        }
    }

    private final SeekBar.OnSeekBarChangeListener weightChangedListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            lbsValue = progress;
            calculate();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    };

    private View.OnClickListener saveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            savePreferences();
        }
    };

    private void calculate() {
        kgsValue = lbsValue / RATE;
        kgsFormatValue = String.format("%.1f", kgsValue);
        kgsTextView.setText(numberFormat.format(lbsValue) + " Lbs = " + kgsFormatValue + " Kgs");
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String pounds = Integer.toString(lbsValue);
        String kilograms = Double.toString(kgsValue);
        editor.putString(LBS, pounds);
        editor.putString(KGS, kilograms);
        editor.apply();
    }
}
