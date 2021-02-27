package com.cat.pianopatienttracker.rep.home.patients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cat.pianopatienttracker.LoginActivity;
import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.network.Webservice;
import com.cat.pianopatienttracker.rep.Rep_home;
import com.cat.pianopatienttracker.rep.home.RepHome_adapter;
import com.cat.pianopatienttracker.rep.home.RepHome_item;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientsActivity extends AppCompatActivity implements Patients_adapter.OnPatientClickListener {

    private ProgressDialog dialog;
    String accessToken;

    ArrayList<Patient_item> patients_list = new ArrayList<>();
    Patients_adapter patients_adapter;


    RecyclerView patientsRecycler;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        accessToken = getIntent().getStringExtra("accessToken");

        dialog = new ProgressDialog(PatientsActivity.this);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);

        getPatients();
    }

    public void DropUpdatePatients(Map<String, String> map) {
        dialog.show();
        Webservice.getInstance().getApi().dropUpdateJakaviPatients(accessToken,map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    JSONObject responseObject = null;
                    try {
                        responseObject = new JSONObject(response.body().string());
                        Toast.makeText(PatientsActivity.this, "Done Successfully", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {
                    Intent i = new Intent(PatientsActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PatientsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void getPatients() {
        dialog.show();
        Webservice.getInstance().getApi().getJakaviPatients(accessToken).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    JSONObject responseObject = null;
                    try {
                        responseObject = new JSONObject(response.body().string());
                        JSONArray dataArr = responseObject.getJSONArray("data");
                        setPatientsList(dataArr);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {
                    Intent i = new Intent(PatientsActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PatientsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void setPatientsList(JSONArray list) {

        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String hospital = currentObject.getString("hospital");
                final String sector = currentObject.getString("sector");
                final String doctor = currentObject.getString("doctor");
                final String dose = currentObject.getString("dose") + " mg";

                patients_list.add(new Patient_item(id, hospital, sector, doctor, dose));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initPatientsRecyclerView();
    }

    private void initPatientsRecyclerView() {
        patientsRecycler = findViewById(R.id.recycler);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        patientsRecycler.setLayoutManager(layoutManager);

        patients_adapter = new Patients_adapter(this, patients_list, this);
        patientsRecycler.setAdapter(patients_adapter);

    }

    @Override
    public void onDropPatientClick(int patientId) {
        Map<String, String> map = new HashMap<>();
        map.put("patient_id",String.valueOf(patientId));
        map.put("process","dropped");
        DropUpdatePatients(map);
    }

    @Override
    public void onUpdatePatientClick(int patientId) {
        Toast.makeText(this, "Patient" + patientId + "Updated", Toast.LENGTH_SHORT).show();
    }
}