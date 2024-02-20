package ca.uhn.fhir.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import ca.uhn.fhir.android.test.PatientFhirHelper;

import ca.uhn.fhir.android.test.GetPatientsTask;
import ca.uhn.fhir.android.test.R;

public class ListPatientsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_patients);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable Night Mode

//        final TextView tv = (TextView) findViewById(R.id.textview);
//        tv.setTextSize(20);
//        tv.setText("Fetching data...");

        GetPatientsTask task = new GetPatientsTask(this);
        task.execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Adding new Patient..", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                PatientFhirHelper fhir = new PatientFhirHelper();

                Intent intent = new Intent(ListPatientsActivity.this, AddPatientActivity.class);
                ListPatientsActivity.this.startActivity(intent);
            }
        });
    }
}
