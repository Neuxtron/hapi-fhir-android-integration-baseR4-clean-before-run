package ca.uhn.fhir.android.test;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

//import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.r4.model.Patient;

import java.lang.ref.WeakReference;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

//import ca.uhn.fhir.android.ListCitizensActivity;
//import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;

import ca.uhn.fhir.android.ListPatientsActivity;
//import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.android.PatientItem;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;

public class GetPatientsTask extends AsyncTask<Void, Object, List<Patient>> {

    private static final String TAG = "GetPatientsTask";

    private final PatientFhirHelper fhir = new PatientFhirHelper();
    private WeakReference<ListPatientsActivity> activity;

    public GetPatientsTask(ListPatientsActivity activity) {
        this.activity = new WeakReference<ListPatientsActivity>(activity);
    }

    @Override
    protected List<Patient> doInBackground(Void... args) {
        final List<Patient> patients = new ArrayList<Patient>();
        Log.d("rijal", "doInBackground");

        try {
            Log.d("rijal", "patients.addAll");
            patients.addAll(fhir.getPatients());
        } catch (FhirClientConnectionException e) {
            String message = "Failed to connect to Hapi FHIR server";
            Log.d("rijal", "Failed to connect to Hapi FHIR server");

            if (e.getMessage() != null) {
                message = e.getMessage();
            }

            Log.e(TAG, message, e);
        }

        Log.d("rijal", "return patients empty: " + patients.isEmpty() + ", " + patients.toString());
        return patients;
    }

    @Override
    protected void onPostExecute(List<Patient> patients) {
        Log.d("rijal", "Found the following patients...");
        StringBuilder b = new StringBuilder("Found the following patients...").append('\n');
        for (Patient patient : patients) {
//            Log.d("rijal", patient.getText().getDiv().getValueAsString());
//            b.append(patient.getText().getDiv().getValueAsString()).append('\n');
//            try {
//                Log.d("rijal2", patient.getText().getDiv().toString());
//            } catch (Exception e) {e.printStackTrace();}
            try {
                Log.d("AAAAAAAAA", "Making patient item...");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDate dob = patient.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    PatientItem patientItem = new PatientItem(
                        patient.getId(),
                        patient.getName().toString(),
                        patient.getGender().toString(),
                        dob,
                        "---",
                        patient.getAddress().get(0).getCity(),
                        patient.getAddress().get(0).getCountry(),
                        patient.getActive(),
                        "Unknown"
                    );
                }

            } catch (Exception e) {
                Log.d("AAAAAAAAA", "Failed to make patient item");
                e.printStackTrace();
            }
        }

//        Log.d("rijal3", String.valueOf(Html.fromHtml("<html>"+b.toString()+"</html>")));
//        final TextView tv = (TextView) activity.get().findViewById(R.id.textview);
//        tv.setText(Html.fromHtml("<html>"+b.toString()+"</html>"));
//        if (activity.isEnqueued()) {
//            final TextView tv = (TextView) activity.get().findViewById(R.id.textview);
//            tv.setText(Html.fromHtml("<html>"+b.toString()+"</html>"));
//        }
    }

}
