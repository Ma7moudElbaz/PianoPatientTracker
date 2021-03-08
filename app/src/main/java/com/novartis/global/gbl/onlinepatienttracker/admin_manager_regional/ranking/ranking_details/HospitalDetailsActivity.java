package com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.ranking.ranking_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.novartis.global.gbl.onlinepatienttracker.R;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.ranking.Ranking_doctors_adapter;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.ranking.Ranking_doctors_item;
import com.novartis.global.gbl.onlinepatienttracker.LoginActivity;
import com.novartis.global.gbl.onlinepatienttracker.network.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalDetailsActivity extends AppCompatActivity {


    private ProgressDialog dialog;
    String accessToken,name,detailsType;
    int selectedCountryId,selectedBrandId,selectedItemId;

    RecyclerView rankingRecycler;

    ArrayList<Ranking_doctors_item> ranking_doctors_list = new ArrayList<>();
    Ranking_doctors_adapter ranking_doctors_adapter;


    TextView name_txt;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_details);

        accessToken = getIntent().getStringExtra("accessToken");
        name = getIntent().getStringExtra("name");
        detailsType = getIntent().getStringExtra("detailsType");


        selectedCountryId = getIntent().getIntExtra("countryId",0);
        selectedBrandId = getIntent().getIntExtra("brandId",0);
        selectedItemId = getIntent().getIntExtra("itemId",0);

        back = findViewById(R.id.back);
        name_txt = findViewById(R.id.name_txt);

        name_txt.setText(name);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        dialog = new ProgressDialog(HospitalDetailsActivity.this);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);

        //hospitals,reps,sectors,doctors
        getRanking(detailsType);
    }

    public void getRanking(String type) {
        Map<String, String> filterMap = new HashMap<>();
        filterMap.put("brand_id", String.valueOf(selectedBrandId));
        filterMap.put("country_id", String.valueOf(selectedCountryId));
        filterMap.put("type","hospitals");
        filterMap.put("hospital_id",String.valueOf(selectedItemId));

        dialog.show();

        Webservice.getInstance().getApi().getRankingDetails(accessToken,filterMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    JSONObject responseObject = null;
                    try {
                        responseObject = new JSONObject(response.body().string());
                        JSONArray rankingArr = responseObject.getJSONArray("data");
                        setRankingDetailsList(rankingArr);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {
                    Intent i = new Intent(HospitalDetailsActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                dialog.dismiss();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(HospitalDetailsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void setRankingDetailsList(JSONArray list) {
        ranking_doctors_list.clear();
        try {


            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                final String address = currentObject.getString("address");
                final int patientsNo = currentObject.getInt("p_count");

                final JSONArray hospitalsArr = currentObject.getJSONArray("hospitals");
                String hospital = "";
                for (int j = 0; j < hospitalsArr.length(); j++) {
                    JSONObject currentHospitalObject = hospitalsArr.getJSONObject(j);
                    hospital += currentHospitalObject.getString("name");
                    if (j != (hospitalsArr.length() - 1)) {
                        hospital += ", ";
                    }
                }

                ranking_doctors_list.add(new Ranking_doctors_item(id, name, hospital, address, patientsNo));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        initRankingRecyclerView();
    }

    private void initRankingRecyclerView() {
        rankingRecycler = findViewById(R.id.recycler);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(HospitalDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
        rankingRecycler.setLayoutManager(layoutManager);

        ranking_doctors_adapter = new Ranking_doctors_adapter(HospitalDetailsActivity.this, ranking_doctors_list);
        rankingRecycler.setAdapter(ranking_doctors_adapter);

    }
}