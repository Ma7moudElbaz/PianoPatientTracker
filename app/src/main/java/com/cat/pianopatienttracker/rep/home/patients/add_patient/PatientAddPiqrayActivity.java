package com.cat.pianopatienttracker.rep.home.patients.add_patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.admin_manager_regional.shared.spinners.Brand_item;
import com.cat.pianopatienttracker.admin_manager_regional.shared.spinners.City_item;
import com.cat.pianopatienttracker.admin_manager_regional.shared.spinners.Country_item;
import com.cat.pianopatienttracker.admin_manager_regional.shared.spinners.Region_item;

import java.util.ArrayList;
import java.util.List;

public class PatientAddPiqrayActivity extends AppCompatActivity {
    int brandId;
    String accessToken;

    List<Country_item> country_items;

    ArrayList<String> citiesList = new ArrayList<>();

    Spinner citySpinner, hospitalSpinner, doctorSpinner, lineSpinner, doseSpinner;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_add_piqray);
        citiesList.add("Select City");

        citySpinner = findViewById(R.id.city_spinner);
        hospitalSpinner = findViewById(R.id.hospital_spinner);
        doctorSpinner = findViewById(R.id.doctor_spinner);
        lineSpinner = findViewById(R.id.line_spinner);
        doseSpinner = findViewById(R.id.dose_spinner);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        brandId = getIntent().getIntExtra("brandId", 0);
        accessToken = getIntent().getStringExtra("accessToken");
        country_items = (ArrayList<Country_item>) getIntent().getSerializableExtra("countries");

         setCitiesData();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, citiesList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(arrayAdapter);
    }

    public void setCitiesData() {
        for (int i = 0; i < country_items.size(); i++) {
            ArrayList<Region_item> regions_items = country_items.get(i).getRegion_list();
            for (int r = 1; r < regions_items.size(); r++) {
                ArrayList<City_item> cities_items = regions_items.get(r).getCity_list();
                for (int c = 1; c < cities_items.size(); c++) {
                    citiesList.add(cities_items.get(c).getName());
                }
            }
        }
    }
}