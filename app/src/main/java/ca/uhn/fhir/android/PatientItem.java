package ca.uhn.fhir.android;

import java.time.LocalDate;

public final class PatientItem {
    final String id;
    final String name;
    final String gender;
    LocalDate dob = null;
    final String phone;
    final String city;
    final String country;
    final Boolean isActive;
    String risk = "";

    public PatientItem(
        String id,
        String name,
        String gender,
        LocalDate dob,
        String phone,
        String city,
        String country,
        Boolean isActive,
        String risk
    ) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.phone = phone;
        this.city = city;
        this.country = country;
        this.isActive = isActive;
        this.risk = risk;
    }
}
