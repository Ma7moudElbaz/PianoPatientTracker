package com.novartis.global.gbl.onlinepatienttracker.rep.home.patients.add_patient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.novartis.global.gbl.onlinepatienttracker.R;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.spinners.City_item;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.spinners.Country_item;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.spinners.Region_item;
import com.novartis.global.gbl.onlinepatienttracker.login.LoginActivity;
import com.novartis.global.gbl.onlinepatienttracker.network.Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientAddPalbociclibActivity extends AppCompatActivity {

    int brandId;
    String accessToken;

    List<Country_item> country_items;

    ArrayList<String> citiesList = new ArrayList<>();
    ArrayList<Integer> citiesIdList = new ArrayList<>();

    ArrayList<Hospital_item> hospitalsDoctorsList = new ArrayList<>();

    ArrayList<String> hospitalsList = new ArrayList<>();
    ArrayList<Integer> hospitalIdList = new ArrayList<>();
    ArrayList<Integer> hospitalSectorIdList = new ArrayList<>();

    ArrayList<String> doctorsList = new ArrayList<>();
    ArrayList<Integer> doctorIdList = new ArrayList<>();

    Spinner citySpinner, hospitalSpinner, doctorSpinner;
    Spinner prePostSpinner, medicalSurgicalSpinner, lineSpinner, aiFulSpinner;
    ImageView back;
    TextView addBtn;


    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_add_palbociclib);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);

        citySpinner = findViewById(R.id.city_spinner);
        hospitalSpinner = findViewById(R.id.hospital_spinner);
        doctorSpinner = findViewById(R.id.doctor_spinner);
        prePostSpinner = findViewById(R.id.pre_post_spinner);
        medicalSurgicalSpinner = findViewById(R.id.medical_surgical_spinner);
        lineSpinner = findViewById(R.id.line_spinner);
        aiFulSpinner = findViewById(R.id.ai_ful_spinner);
        back = findViewById(R.id.back);
        addBtn = findViewById(R.id.add_btn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    try {
                        addPatient();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        brandId = getIntent().getIntExtra("brandId", 0);
        accessToken = getIntent().getStringExtra("accessToken");
        country_items = (ArrayList<Country_item>) getIntent().getSerializableExtra("countries");

        setCitiesData();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, citiesList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(arrayAdapter);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    hospitalsList.clear();
                    hospitalIdList.clear();
                    initHospitalsSpinner();
                } else if (position > 0) {
                    getHospitalsDoctors(citiesIdList.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        hospitalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    doctorsList.clear();
                    doctorIdList.clear();
                    initDoctorsSpinner();
                } else if (position > 0) {
                    setDoctorsList(hospitalsDoctorsList.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        prePostSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    medicalSurgicalSpinner.setVisibility(View.VISIBLE);
                } else {
                    medicalSurgicalSpinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getHospitalsDoctors(int areaId) {
        dialog.show();
        Webservice.getInstance().getApi().getHospitals(accessToken, areaId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    JSONObject responseObject = null;
                    try {
                        responseObject = new JSONObject(response.body().string());
                        JSONObject dataObj = responseObject.getJSONObject("data");
                        JSONArray hospitalsArr = dataObj.getJSONArray("data");
                        setHospitalsDoctorsList(hospitalsArr);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {
                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Network error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void addPatient() throws JSONException {
        int selectedCityIndex = citySpinner.getSelectedItemPosition();
        int selectedHospitalIndex = hospitalSpinner.getSelectedItemPosition();
        int selectedDoctor = doctorSpinner.getSelectedItemPosition();


        int selectedPrePost = prePostSpinner.getSelectedItemPosition();
        int selectedAiFul = aiFulSpinner.getSelectedItemPosition();

        int cityId = citiesIdList.get(selectedCityIndex);
        int hospitalId = hospitalIdList.get(selectedHospitalIndex);
        int sectorId = hospitalSectorIdList.get(selectedHospitalIndex);
        int doctorId = doctorIdList.get(selectedDoctor);
        Map<String, String> map = new HashMap<>();
        map.put("brand_id", String.valueOf(brandId));
        map.put("city_id", String.valueOf(cityId));
        map.put("sector_id", String.valueOf(sectorId));
        map.put("hospital_id", String.valueOf(hospitalId));
        map.put("doctor_id", String.valueOf(doctorId));

        JSONObject patientObj = new JSONObject();

        if (selectedPrePost == 1) {
            patientObj.put("is_pre", 1);
            patientObj.put("is_post", 0);
            patientObj.put("pre_type", medicalSurgicalSpinner.getSelectedItem().toString().toLowerCase());
        } else if (selectedPrePost == 2) {
            patientObj.put("is_pre", 0);
            patientObj.put("is_post", 1);
        }
        if (selectedAiFul == 1) {
            patientObj.put("is_ai", 1);
            patientObj.put("is_ful", 0);
            patientObj.put("current_management", "ai");
        } else if (selectedAiFul == 2) {
            patientObj.put("is_ai", 0);
            patientObj.put("is_ful", 1);
            patientObj.put("current_management", "ful");
        }
        patientObj.put("current_management_line", String.valueOf(lineSpinner.getSelectedItemPosition()));

        map.put("patient_details", patientObj.toString());

        Log.e("TAG", map.toString());

        dialog.show();
        Webservice.getInstance().getApi().addBcPatient(accessToken, map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    JSONObject responseObject = null;
                    try {
                        Toast.makeText(PatientAddPalbociclibActivity.this, "data added successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {
                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Network error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void setCitiesData() {
        citiesList.add("Select City");
        citiesIdList.add(0);
        for (int i = 0; i < country_items.size(); i++) {
            ArrayList<Region_item> regions_items = country_items.get(i).getRegion_list();
            for (int r = 1; r < regions_items.size(); r++) {
                ArrayList<City_item> cities_items = regions_items.get(r).getCity_list();
                for (int c = 1; c < cities_items.size(); c++) {
                    citiesList.add(cities_items.get(c).getName());
                    citiesIdList.add(cities_items.get(c).getId());
                }
            }
        }
    }

    private void setHospitalsDoctorsList(JSONArray list) {
        hospitalsDoctorsList.clear();
        ArrayList<Doctor_item> doctorsListInitial = new ArrayList<>();
        doctorsListInitial.add(new Doctor_item(0, "Select Doctor"));
        hospitalsDoctorsList.add(new Hospital_item(0, 0, "Select Hospital", doctorsListInitial));
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                final int sectorId = currentObject.getInt("sector_id");
                final JSONArray doctorsArr = currentObject.getJSONArray("doctors");
                ArrayList<Doctor_item> doctorsList = setDoctorsList(doctorsArr);
                hospitalsDoctorsList.add(new Hospital_item(id, sectorId, name, doctorsList));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setHospitalList();
    }

    ArrayList<Doctor_item> setDoctorsList(JSONArray list) {
        ArrayList<Doctor_item> doctorsList = new ArrayList<>();
        doctorsList.add(new Doctor_item(0, "Select Doctor"));
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                doctorsList.add(new Doctor_item(id, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doctorsList;
    }

    private void setHospitalList() {
        hospitalsList.clear();
        hospitalIdList.clear();
        hospitalSectorIdList.clear();
        try {
            for (int i = 0; i < hospitalsDoctorsList.size(); i++) {
                final String name = hospitalsDoctorsList.get(i).getName();
                final int id = hospitalsDoctorsList.get(i).getId();
                final int sectorId = hospitalsDoctorsList.get(i).getSectorId();
                hospitalsList.add(name);
                hospitalIdList.add(id);
                hospitalSectorIdList.add(sectorId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initHospitalsSpinner();
    }

    private void initHospitalsSpinner() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hospitalsList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hospitalSpinner.setAdapter(arrayAdapter);
    }

    private void setDoctorsList(Hospital_item item) {
        doctorsList.clear();
        doctorIdList.clear();
        ArrayList<Doctor_item> doctors = item.getDoctors_list();
        try {

            for (int i = 0; i < doctors.size(); i++) {
                final int id = doctors.get(i).getId();
                final String name = doctors.get(i).getName();
                doctorsList.add(name);
                doctorIdList.add(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initDoctorsSpinner();
    }

    private void initDoctorsSpinner() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, doctorsList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorSpinner.setAdapter(arrayAdapter);
    }

    private boolean validateFields() {

        if (citySpinner.getSelectedItemPosition() == 0 || hospitalSpinner.getSelectedItemPosition() == 0
                || doctorSpinner.getSelectedItemPosition() == 0 || lineSpinner.getSelectedItemPosition() == 0
                || prePostSpinner.getSelectedItemPosition() == 0|| aiFulSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please Fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }else if(prePostSpinner.getSelectedItemPosition() == 1 && medicalSurgicalSpinner.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Please Fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}