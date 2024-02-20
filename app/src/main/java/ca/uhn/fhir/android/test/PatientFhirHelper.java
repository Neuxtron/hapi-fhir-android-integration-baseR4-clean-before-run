package ca.uhn.fhir.android.test;

//import org.hl7.fhir.dstu3.model.Bundle;
//import org.hl7.fhir.dstu3.model.Patient;
import android.util.Log;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
//import ca.uhn.fhir.model.dstu2.resource.Bundle;
//import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.util.BundleUtil;

public class PatientFhirHelper {

//    private static final String DSTU2_URL = BuildConfig.API_URL + "baseDstu2";
//    private static final String DSTU2_URL = BuildConfig.API_URL + "baseDstu3";
//    private static final String DSTU2_URL = BuildConfig.API_URL + "baseR5";
//    private static final String DSTU2_URL = "https://hapi.fhir.org/" + "baseDstu2";
//    private static final String DSTU2_URL = BuildConfig.API_URL + "baseR4";
    private static final String DSTU2_URL = "https://hapi.fhir.org/" + "baseR4";

    private IGenericClient client;
    private FhirContext ctx;

    public PatientFhirHelper() {
        ctx = FhirContext.forR4();
        ctx.getRestfulClientFactory().setSocketTimeout(200 * 1000);
        client = ctx.newRestfulGenericClient(DSTU2_URL);
    }

    public List<Patient> getPatients() {
        // Invoke the client
        Log.d("rijal", "getPatients from " + DSTU2_URL);
        Bundle bundle = client.search().forResource(Patient.class)
                .where(new TokenClientParam("gender").exactly().code("unknown"))
                .prettyPrint()
                .returnBundle(Bundle.class)
                .execute();
        Log.d("rijal", "return BundleUtil");
        return BundleUtil.toListOfResourcesOfType(ctx, bundle, Patient.class);
    }

    public void testAddPatient() {
        // Create a patient object
        Patient patient = new Patient();
        patient.addIdentifier().setSystem("http://acme.org/mrns").setValue("12345");
        patient.addName().setFamily("Jameson").addGiven("J").addGiven("Jonah");
        patient.setGender(Enumerations.AdministrativeGender.MALE);

        // Give the patient a temporary UUID so that other resources in
        // the transaction can refer to it
        patient.setId(IdType.newRandomUuid());

        // Create an observation object
        Observation observation = new Observation();
        observation.setStatus(Observation.ObservationStatus.FINAL);
        observation
                .getCode()
                .addCoding()
                .setSystem("http://loinc.org")
                .setCode("789-8")
                .setDisplay("Erythrocytes [#/volume] in Blood by Automated count");
        observation.setValue(new Quantity()
                .setValue(4.12)
                .setUnit("10 trillion/L")
                .setSystem("http://unitsofmeasure.org")
                .setCode("10*12/L"));

        // The observation refers to the patient using the ID, which is already
        // set to a temporary UUID
        observation.setSubject(new Reference(patient.getIdElement().getValue()));

        // Create a bundle that will be used as a transaction
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.TRANSACTION);

        // Add the patient as an entry. This entry is a POST with an
        // If-None-Exist header (conditional create) meaning that it
        // will only be created if there isn't already a Patient with
        // the identifier 12345
        bundle.addEntry()
                .setFullUrl(patient.getIdElement().getValue())
                .setResource(patient)
                .getRequest()
                .setUrl("Patient")
                .setIfNoneExist("identifier=http://acme.org/mrns|12345")
                .setMethod(Bundle.HTTPVerb.POST);

        // Add the observation. This entry is a POST with no header
        // (normal create) meaning that it will be created even if
        // a similar resource already exists.
        bundle.addEntry()
                .setResource(observation)
                .getRequest()
                .setUrl("Observation")
                .setMethod(Bundle.HTTPVerb.POST);

        // Log the request
        FhirContext ctx = FhirContext.forR4();
        System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

        // Create a client and post the transaction to the server
        IGenericClient client = ctx.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        Bundle resp = client.transaction().withBundle(bundle).execute();

        // Log the response
        System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));

    }

    public IGenericClient getClient() {
        return client;
    }

}
