package com.example.dgyou.valuesconverter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextWatcher;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.NumberFormat;

public class KmToMilesActivity extends AppCompatActivity {

    // keys for saving values to SharedPreferences
    private String KM;
    private String MILE;

    // GUI instances
    private TextView kmTextView;
    private TextView mileTextView;
    private EditText kmEditText;
    private EditText mileEditText;
    private Button kmSaveButton;

    // variables for the EditText values
    private double kmValue = 0.0;
    private double mileValue = 0.0;
    private final double RATE = 1.609;

    // NumberFormat object for formatting values to EditTexts
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance();

    // SharedPreferences for writing to
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_km_to_miles);

        // references to GUI components
        kmTextView = (TextView) findViewById(R.id.kmTextView);
        mileTextView = (TextView) findViewById(R.id.mileTextView);
        kmEditText = (EditText) findViewById(R.id.kmEditText);
        mileEditText = (EditText) findViewById(R.id.mileEditText);
        kmSaveButton = (Button) findViewById(R.id.kmSaveButton);

        // SharedPreferences initiator
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // register listener to save button to save values to sharedpreferences file
        kmSaveButton.setOnClickListener(saveButtonListener);

        // register listener for the SeekBar to change values when user changes the EditText value
        kmEditText.addTextChangedListener(kmsChangedListener);
        mileEditText.addTextChangedListener(milesChangedListener);

        // initialize SharedPreference file keys
        KM = getResources().getString(R.string.key_kms);
        MILE = getResources().getString(R.string.key_miles);

        // set the TextView's initial text so the words don't show
        kmTextView.setText("");
        mileTextView.setText("");

    }

    private View.OnClickListener saveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            savePreferences();
        }
    };

    private final TextWatcher kmsChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                kmValue = Double.parseDouble(s.toString());
            } catch (NumberFormatException e) {
                kmTextView.setText("");
                kmValue = 0.0;
            }

            calculateMiles();
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    private final TextWatcher milesChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                mileValue = Double.parseDouble(s.toString());
            } catch (NumberFormatException e) {
                mileTextView.setText("");
                mileValue = 0.0;
            }

            calculateKms();
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    private void calculateMiles() {
        mileValue = kmValue / RATE;
        mileTextView.setText(numberFormat.format(mileValue));
    }

    private void calculateKms() {
        kmValue = mileValue * RATE;
        kmTextView.setText(numberFormat.format(kmValue));
    }

    private void savePreferences() {
        String kms = Double.toString(kmValue);
        String miles = Double.toString(mileValue);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KM, kms);
        editor.putString(MILE, miles);
        editor.apply();
    }
}
