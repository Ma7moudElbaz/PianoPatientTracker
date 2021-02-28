package com.cat.pianopatienttracker.rep.home.patients.add_patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;

import com.cat.pianopatienttracker.R;

public class PatientAddPiqrayActivity extends AppCompatActivity {

    Spinner citySpinner,hospitalSpinner,doctorSpinner,lineSpinner,doseSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_add_piqray);
    }
}