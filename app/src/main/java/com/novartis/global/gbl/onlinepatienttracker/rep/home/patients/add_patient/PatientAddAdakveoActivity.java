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

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.novartis.global.gbl.onlinepatienttracker.R;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.spinners.City_item;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.spinners.Country_item;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.spinners.Region_item;
import com.novartis.global.gbl.onlinepatienttracker.LoginActivity;
import com.novartis.global.gbl.onlinepatienttracker.network.Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientAddAdakveoActivity extends AppCompatActivity {

    boolean isAdakveoSelected = false;
    boolean isVoxelatorSelected = false;
    boolean isManagementEmpty = true;
    boolean isComorbiditiesEmpty = true;

    String managementItems = "";
    String comorbiditiesItems = "";


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

    Spinner citySpinner, hospitalSpinner, doctorSpinner, vocSpinner, adakveoStatusSpinner, dpoStatusSpinner;
    MultiSpinnerSearch comorbiditiesSpinner, managementSpinner;
    ImageView back;
    TextView addBtn;


    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_add_adakveo);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);

        citySpinner = findViewById(R.id.city_spinner);
        hospitalSpinner = findViewById(R.id.hospital_spinner);
        doctorSpinner = findViewById(R.id.doctor_spinner);
        comorbiditiesSpinner = findViewById(R.id.comorbidities_spinner);
        initComorbiditiesSpinner();
        managementSpinner = findViewById(R.id.management_spinner);
        initManagementSpinner();
        vocSpinner = findViewById(R.id.voc_spinner);
        adakveoStatusSpinner = findViewById(R.id.adakveo_status_spinner);
        dpoStatusSpinner = findViewById(R.id.dpo_status_spinner);
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

    private void initComorbiditiesSpinner() {
        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.adakveo_comorbidities));
        final List<KeyPairBoolData> listArrayData = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(list.get(i));
            h.setSelected(false);
            listArrayData.add(h);
        }
        comorbiditiesSpinner = findViewById(R.id.comorbidities_spinner);
        comorbiditiesSpinner.setSearchEnabled(false);
        comorbiditiesSpinner.setShowSelectAllButton(true);
        comorbiditiesSpinner.setClearText("Close & Clear");
        comorbiditiesSpinner.setItems(listArrayData, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                int selectedItemsCount = 0;
                comorbiditiesItems = "";
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        selectedItemsCount++;
                        comorbiditiesItems = comorbiditiesItems + items.get(i).getName() + ",";
                        Log.i("TAG", comorbiditiesItems);
                    }
                }

                if (selectedItemsCount == 0) {
                    isComorbiditiesEmpty = true;
                } else if (selectedItemsCount > 0) {
                    isComorbiditiesEmpty = false;
                }

            }
        });

    }

    private void initManagementSpinner() {
        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.adakveo_management));
        final List<KeyPairBoolData> listArrayData = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(list.get(i));
            h.setSelected(false);
            listArrayData.add(h);
        }
        managementSpinner = findViewById(R.id.management_spinner);
        managementSpinner.setSearchEnabled(false);
        managementSpinner.setShowSelectAllButton(true);
        managementSpinner.setClearText("Close & Clear");
        managementSpinner.setItems(listArrayData, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                int selectedItemsCount = 0;
                managementItems = "";
                isAdakveoSelected = false;
                isVoxelatorSelected = false;
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        selectedItemsCount++;
                        managementItems = managementItems + items.get(i).getName() + ",";
                        if (items.get(i).getName().toLowerCase().equals("adakveo")) {
                            isAdakveoSelected = true;
                        }
                        if (items.get(i).getName().toLowerCase().equals("voxelotor")) {
                            isVoxelatorSelected = true;
                        }
                        Log.i("TAG", managementItems);
                    }
                }

                if (isAdakveoSelected) {
                    adakveoStatusSpinner.setVisibility(View.VISIBLE);
                    dpoStatusSpinner.setVisibility(View.VISIBLE);
                    adakveoStatusSpinner.setSelection(0);
                    dpoStatusSpinner.setSelection(0);
                } else if (isVoxelatorSelected) {
                    adakveoStatusSpinner.setVisibility(View.VISIBLE);
                    dpoStatusSpinner.setVisibility(View.GONE);
                    adakveoStatusSpinner.setSelection(0);
                    dpoStatusSpinner.setSelection(0);
                } else {
                    adakveoStatusSpinner.setVisibility(View.GONE);
                    dpoStatusSpinner.setVisibility(View.GONE);
                    adakveoStatusSpinner.setSelection(0);
                    dpoStatusSpinner.setSelection(0);
                }

                if (selectedItemsCount == 0) {
                    isManagementEmpty = true;
                } else if (selectedItemsCount > 0) {
                    isManagementEmpty = false;
                }
            }
        });

    }

    private void addPatient() throws JSONException {
        int selectedCityIndex = citySpinner.getSelectedItemPosition();
        int selectedHospitalIndex = hospitalSpinner.getSelectedItemPosition();
        int selectedDoctor = doctorSpinner.getSelectedItemPosition();


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
        map.put("voc_freq", vocSpinner.getSelectedItem().toString());
        map.put("comorbidities", comorbiditiesSpinner.getSelectedItem().toString().toLowerCase());

        if (isAdakveoSelected && isVoxelatorSelected) {
            map.put("is_adakveo", "1");
            map.put("is_voxelator", "1");
            map.put("adakveo_status", adakveoStatusSpinner.getSelectedItem().toString().toLowerCase());
            map.put("dpo_status", dpoStatusSpinner.getSelectedItem().toString().toLowerCase());
        } else {
            if (isAdakveoSelected) {
                map.put("is_adakveo", "1");
                map.put("is_voxelator", "0");
                map.put("adakveo_status", adakveoStatusSpinner.getSelectedItem().toString().toLowerCase());
                map.put("dpo_status", dpoStatusSpinner.getSelectedItem().toString().toLowerCase());
            } else if (isVoxelatorSelected) {
                map.put("is_adakveo", "0");
                map.put("is_voxelator", "1");
                map.put("adakveo_status", adakveoStatusSpinner.getSelectedItem().toString().toLowerCase());
            } else {
                map.put("is_adakveo", "0");
                map.put("is_voxelator", "0");
            }
        }

        map.put("current_management", managementSpinner.getSelectedItem().toString().toLowerCase());


        Log.e("TAG", map.toString());

        dialog.show();
        Webservice.getInstance().getApi().addAdakvieoPatient(accessToken, map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    JSONObject responseObject = null;
                    try {
                        Toast.makeText(PatientAddAdakveoActivity.this, "data added successfully", Toast.LENGTH_SHORT).show();
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
                || doctorSpinner.getSelectedItemPosition() == 0 || vocSpinner.getSelectedItemPosition() == 0 || isComorbiditiesEmpty || isManagementEmpty) {
            Toast.makeText(this, "Please Fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (isAdakveoSelected && adakveoStatusSpinner.getSelectedItemPosition() == 0 && dpoStatusSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please Fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (isVoxelatorSelected && adakveoStatusSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please Fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}