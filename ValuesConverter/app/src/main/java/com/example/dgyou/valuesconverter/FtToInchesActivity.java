package com.example.dgyou.valuesconverter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.NumberFormat;

public class FtToInchesActivity extends AppCompatActivity {

    // GUI instances
    private TextView ftTextView;
    private TextView inchTextView;
    private EditText ftEditText;
    private EditText inchEditText;
    private Button ftSaveButton;

    private SharedPreferences sharedPreferences;

    // keys for saving values into the SharedPreference file
    private String FT;
    private String INCH;

    // variables to hold the value of the EditTexts
    private double ftValue = 0.0;
    private double inchValue = 0.0;

    // conversion rate
    private final int RATE = 12;

    // NumberFormat object to format the values for displaying on EditTexts
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft_to_inches);

        // references to GUI components
        ftTextView = (TextView) findViewById(R.id.ftTextView);
        inchTextView = (TextView) findViewById(R.id.inchTextView);
        ftEditText = (EditText) findViewById(R.id.ftEditText);
        inchEditText = (EditText) findViewById(R.id.inchEditText);
        ftSaveButton = (Button) findViewById(R.id.ftSaveButton);

        // SharedPreferences initiator
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // register listener to save button to save values to sharedpreferences file
        ftSaveButton.setOnClickListener(saveClickListener);

        // register listener for the SeekBar to change values when user moves the SeekBar
        ftEditText.addTextChangedListener(feetChangedListener);
        inchEditText.addTextChangedListener(inchChangedListener);

        // initialize SharedPreference file keys
        FT = getResources().getString(R.string.key_feet);
        INCH = getResources().getString(R.string.key_inches);

        // set the TextView's initial text so the words don't show
        ftTextView.setText("");
        inchTextView.setText("");
    }

    private final View.OnClickListener saveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            savePreferences();
        }
    };

    private final TextWatcher feetChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                ftValue = Double.parseDouble(s.toString());
            } catch (NumberFormatException e) {
                ftTextView.setText("");
                ftValue = 0.0;
            }

            calculateInches();
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    private final TextWatcher inchChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                inchValue = Double.parseDouble(s.toString());
            } catch (NumberFormatException e) {
                inchTextView.setText("");
                inchValue = 0.0;
            }

            calculateFeet();
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    private void calculateInches() {
        inchValue = ftValue * RATE;
        inchTextView.setText(numberFormat.format(inchValue));
    }

    private void calculateFeet() {
        ftValue = inchValue / RATE;
        ftTextView.setText(numberFormat.format(ftValue));
    }

    private void savePreferences() {
        String feet = Double.toString(ftValue);
        String inches = Double.toString(inchValue);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FT, feet);
        editor.putString(INCH, inches);
        editor.apply();
    }
}
