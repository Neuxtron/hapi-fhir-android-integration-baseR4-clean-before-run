package ca.uhn.fhir.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ca.uhn.fhir.android.test.R;

public class AddPatientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
    }
}