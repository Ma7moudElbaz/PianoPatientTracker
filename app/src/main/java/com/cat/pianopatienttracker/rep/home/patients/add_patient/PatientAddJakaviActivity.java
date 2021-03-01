package com.cat.pianopatienttracker.rep.home.patients.add_patient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.admin_manager_regional.shared.spinners.City_item;
import com.cat.pianopatienttracker.admin_manager_regional.shared.spinners.Country_item;
import com.cat.pianopatienttracker.admin_manager_regional.shared.spinners.Region_item;
import com.cat.pianopatienttracker.login.LoginActivity;
import com.cat.pianopatienttracker.network.Webservice;

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

public class PatientAddJakaviActivity extends AppCompatActivity {
    int brandId, userId;
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

    RelativeLayout doseContainer;
    Spinner citySpinner, hospitalSpinner, doctorSpinner;
    Spinner mpnGvhdSpinner, mpnGvhdChildSpinner, lineSpinner, managementSpinner, doseSpinner;
    ImageView back;
    TextView addBtn;


    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_add_jakavi);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);

        doseContainer = findViewById(R.id.dose_container);
        citySpinner = findViewById(R.id.city_spinner);
        hospitalSpinner = findViewById(R.id.hospital_spinner);
        doctorSpinner = findViewById(R.id.doctor_spinner);
        mpnGvhdSpinner = findViewById(R.id.mpn_gvhd_spinner);
        mpnGvhdChildSpinner = findViewById(R.id.mpn_gvhd_child_spinner);
        lineSpinner = findViewById(R.id.line_spinner);
        managementSpinner = findViewById(R.id.management_spinner);
        doseSpinner = findViewById(R.id.dose_spinner);
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
        userId = getIntent().getIntExtra("userId", 0);
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

        mpnGvhdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initMpnGvhdChildSpinner(position);
                initLineSpinner(position,mpnGvhdChildSpinner.getSelectedItemPosition());
                initManagement(position, lineSpinner.getSelectedItemPosition());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mpnGvhdChildSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initLineSpinner(mpnGvhdSpinner.getSelectedItemPosition(), mpnGvhdChildSpinner.getSelectedItemPosition());
                initManagement(mpnGvhdSpinner.getSelectedItemPosition(), mpnGvhdChildSpinner.getSelectedItemPosition());
                initDose(managementSpinner.getSelectedItem().toString().toLowerCase());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initManagement(mpnGvhdSpinner.getSelectedItemPosition(), mpnGvhdChildSpinner.getSelectedItemPosition());
                initDose(managementSpinner.getSelectedItem().toString().toLowerCase());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        managementSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initDose(managementSpinner.getSelectedItem().toString().toLowerCase());
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

        int selectedManagement = managementSpinner.getSelectedItemPosition();

        int cityId = citiesIdList.get(selectedCityIndex);
        int hospitalId = hospitalIdList.get(selectedHospitalIndex);
        int sectorId = hospitalSectorIdList.get(selectedHospitalIndex);
        int doctorId = doctorIdList.get(selectedDoctor);
        Map<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(userId));
        map.put("city_id", String.valueOf(cityId));
        map.put("sector_id", String.valueOf(sectorId));
        map.put("hospital_id", String.valueOf(hospitalId));
        map.put("doctor_id", String.valueOf(doctorId));

        if (mpnGvhdSpinner.getSelectedItemPosition() == 1) {
            map.put("is_mpn", "1");
            map.put("is_gvhd", "0");
            map.put("mpn_type", mpnGvhdChildSpinner.getSelectedItem().toString().toLowerCase());
            if (mpnGvhdChildSpinner.getSelectedItemPosition() == 1) {
                String mfType = lineSpinner.getSelectedItem().toString().toLowerCase();
                if (mfType.equals("low risk")){
                    map.put("mf_type", "low_risk");
                }else if (mfType.equals("high Risk")){
                    map.put("mf_type", "high_Risk");
                }else {
                    map.put("mf_type", mfType);
                }

            } else if (mpnGvhdChildSpinner.getSelectedItemPosition() == 2) {
                map.put("pv_type", String.valueOf(lineSpinner.getSelectedItemPosition()));
            }
        } else if (mpnGvhdSpinner.getSelectedItemPosition() == 2) {
            map.put("is_mpn", "0");
            map.put("is_gvhd", "1");
            map.put("gvhd_type", mpnGvhdChildSpinner.getSelectedItem().toString().toLowerCase());
        }
        String management = managementSpinner.getSelectedItem().toString().toLowerCase();
        if (management.equals("newly diagnosed")) {
            map.put("current_management", "newly_diagnosed");
        }else if (management.equals("wait &amp; watch")) {
            map.put("current_management", "wait_watch");
        }else {
            map.put("current_management", management);
        }

        if (management.equals("jakavi")){
            map.put("dose", doseSpinner.getSelectedItem().toString());
        }

        Log.e("TAG", map.toString());

        dialog.show();
        Webservice.getInstance().getApi().addJakaviPatient(accessToken, map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    JSONObject responseObject = null;
                    try {
                        Toast.makeText(PatientAddJakaviActivity.this, "data added successfully", Toast.LENGTH_SHORT).show();
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


    private void initMpnGvhdChildSpinner(int mpnGvhdPosition) {
        ArrayAdapter<String> arrayAdapter;

        if (mpnGvhdPosition == 0) {
            mpnGvhdChildSpinner.setVisibility(View.GONE);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.empty_arr));
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mpnGvhdChildSpinner.setAdapter(arrayAdapter);

        } else if (mpnGvhdPosition == 1) {
            mpnGvhdChildSpinner.setVisibility(View.VISIBLE);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.jakavi_mf_pv));
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mpnGvhdChildSpinner.setAdapter(arrayAdapter);
        } else {
            mpnGvhdChildSpinner.setVisibility(View.VISIBLE);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.jakavi_acute_chronic));
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mpnGvhdChildSpinner.setAdapter(arrayAdapter);
        }
    }

    private void initLineSpinner(int mpnGvhdPosition, int mpnGvhdChildPosition) {
        ArrayAdapter<String> arrayAdapter;

        if (mpnGvhdPosition == 0 || mpnGvhdPosition == 2) {
            lineSpinner.setVisibility(View.GONE);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.empty_arr));
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lineSpinner.setAdapter(arrayAdapter);
        } else if (mpnGvhdPosition == 1) {
            if (mpnGvhdChildPosition == 0) {
                lineSpinner.setVisibility(View.GONE);
                arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.empty_arr));
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lineSpinner.setAdapter(arrayAdapter);
            }
            else if (mpnGvhdChildPosition == 1) {
                lineSpinner.setVisibility(View.VISIBLE);
                arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.jakavi_mf_lines));
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lineSpinner.setAdapter(arrayAdapter);
            } else if (mpnGvhdChildPosition == 2) {
                lineSpinner.setVisibility(View.VISIBLE);
                arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.jakavi_pv_lines));
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lineSpinner.setAdapter(arrayAdapter);
            }
        }
    }

    private void initManagement(int mpnGvhdPostion, int mpnGvhdChildPostion) {
        ArrayAdapter<String> arrayAdapter;
        if (mpnGvhdPostion == 0 || mpnGvhdChildPostion == 0) {
            managementSpinner.setVisibility(View.GONE);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.empty_arr));
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            managementSpinner.setAdapter(arrayAdapter);
        } else if (mpnGvhdPostion == 1 && mpnGvhdChildPostion == 1) {
            managementSpinner.setVisibility(View.VISIBLE);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.jakavi_mf_managemnt));
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            managementSpinner.setAdapter(arrayAdapter);
        } else if (mpnGvhdPostion == 1 && mpnGvhdChildPostion == 2) {
            managementSpinner.setVisibility(View.VISIBLE);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.jakavi_pv_managemnt));
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            managementSpinner.setAdapter(arrayAdapter);
        } else if (mpnGvhdPostion == 1) {
            managementSpinner.setVisibility(View.VISIBLE);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.jakavi_acute_chronic_managemnt));
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            managementSpinner.setAdapter(arrayAdapter);
        }

    }

    private void initDose(String management) {

        if (management.equals("jakavi")) {
            doseContainer.setVisibility(View.VISIBLE);
        } else {
            doseContainer.setVisibility(View.GONE);
        }
    }


    private boolean validateFields() {
//        if (citySpinner.getSelectedItemPosition() == 0 || hospitalSpinner.getSelectedItemPosition() == 0
//                || doctorSpinner.getSelectedItemPosition() == 0 || itpSaaSpinner.getSelectedItemPosition() == 0
//                || lineSpinner.getSelectedItemPosition() == 0 || managementSpinner.getSelectedItemPosition() == 0) {
//            Toast.makeText(this, "Please Fill all fields", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (itpSaaSpinner.getSelectedItemPosition() == 1 && adultPedSpinner.getSelectedItemPosition() == 0) {
//            Toast.makeText(this, "Please Fill all fields", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (itpSaaSpinner.getSelectedItemPosition() == 1 && lineSpinner.getSelectedItemPosition() > 1
//                && managementSpinner.getSelectedItemPosition() == 3 && doseSpinner.getSelectedItemPosition() == 0) {
//            Toast.makeText(this, "Please Fill all fields", Toast.LENGTH_SHORT).show();
//            return false;
//        } else {
//            return true;
//        }
        return true;
    }


}