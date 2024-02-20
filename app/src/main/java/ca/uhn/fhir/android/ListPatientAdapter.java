package ca.uhn.fhir.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import ca.uhn.fhir.android.test.R;

public class ListPatientAdapter extends RecyclerView.Adapter<ListPatientAdapter.PatientListViewHolder> {
    private final List<PatientItem> patientList;
    private final LayoutInflater inflater;

    public ListPatientAdapter(Context context, List<PatientItem> patientList) {
        this.patientList = patientList;;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PatientListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_patient_item, parent, false);
        return new PatientListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientListViewHolder holder, int position) {
        final PatientItem patientItem = patientList.get(position);

        TextView tvName =  holder.itemView.findViewById(R.id.name);
        TextView tvAge =  holder.itemView.findViewById(R.id.age);
        TextView tvPob =  holder.itemView.findViewById(R.id.pob);
        TextView tvStatus =  holder.itemView.findViewById(R.id.status);

        tvName.setText(patientItem.name);
        tvAge.setText(getFormattedAge(patientItem.dob));
        tvPob.setText(String.format("%s, %s", patientItem.city, patientItem.country));
        tvStatus.setText(patientItem.risk);
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public static class PatientListViewHolder extends RecyclerView.ViewHolder {
        public PatientListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private String getFormattedAge(LocalDate dob) {
        if (dob == null) return "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            final Period between = Period.between(dob, LocalDate.now());
            if (between.getYears() > 0) return Integer.toString(between.getYears()) + " years";
            if (between.getMonths() > 0) return Integer.toString(between.getMonths()) + " months";
            return Integer.toString(between.getDays()) + " days";
        }
        return "";
    }
}
