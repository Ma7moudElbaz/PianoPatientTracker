package com.cat.pianopatienttracker.admin_manager_regional.ranking.ranking_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.admin_manager_regional.Admin_home;
import com.cat.pianopatienttracker.admin_manager_regional.ranking.Ranking_doctors_adapter;
import com.cat.pianopatienttracker.admin_manager_regional.ranking.Ranking_doctors_item;
import com.cat.pianopatienttracker.admin_manager_regional.ranking.Ranking_hospitals_adapter;
import com.cat.pianopatienttracker.admin_manager_regional.ranking.Ranking_hospitals_item;
import com.cat.pianopatienttracker.admin_manager_regional.ranking.Ranking_reps_adapter;
import com.cat.pianopatienttracker.admin_manager_regional.ranking.Ranking_reps_item;
import com.cat.pianopatienttracker.admin_manager_regional.ranking.Ranking_sectors_adapter;
import com.cat.pianopatienttracker.admin_manager_regional.ranking.Ranking_sectors_item;
import com.cat.pianopatienttracker.login.LoginActivity;
import com.cat.pianopatienttracker.network.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingDetails extends AppCompatActivity {


    private ProgressDialog dialog;
    String accessToken,name,detailsType;
    int selectedCountryId,selectedBrandId,selectedItemId;

    RecyclerView rankingRecycler;

    ArrayList<RankingDetails_hospitals_item> ranking_hospitals_list = new ArrayList<>();
    RankingDetails_hospitals_adapter ranking_hospitals_adapter;


    TextView name_txt;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_details);

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


        dialog = new ProgressDialog(RankingDetails.this);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);

        //hospitals,reps,sectors,doctors
        getRanking(detailsType);
    }

    public void getRanking(String type) {
        Map<String, String> filterMap = new HashMap<>();
        filterMap.put("brand_id", String.valueOf(selectedBrandId));
        filterMap.put("country_id", String.valueOf(selectedCountryId));
        filterMap.put("type",detailsType);

        if (detailsType.equals("reps")){
            filterMap.put("user_id", String.valueOf(selectedItemId));
        }else if (detailsType.equals("sectors")){
            filterMap.put("sector_id", String.valueOf(selectedItemId));
        }
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
                    Intent i = new Intent(RankingDetails.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                dialog.dismiss();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RankingDetails.this, "Network error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void setRankingDetailsList(JSONArray list) {
        ranking_hospitals_list.clear();
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                final String address = currentObject.getString("address");
                final JSONObject sector = currentObject.getJSONObject("sector");
                String sectorString = sector.getString("name");
                final int doctorsNo = currentObject.getInt("doctors_count");
                final int patientsNo = currentObject.getInt("p_count");

                ranking_hospitals_list.add(new RankingDetails_hospitals_item(id, name, sectorString, address, doctorsNo, patientsNo));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        initRankingRecyclerView();
    }

    private void initRankingRecyclerView() {
        rankingRecycler = findViewById(R.id.recycler);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(RankingDetails.this, LinearLayoutManager.VERTICAL, false);
        rankingRecycler.setLayoutManager(layoutManager);

        ranking_hospitals_adapter = new RankingDetails_hospitals_adapter(RankingDetails.this, ranking_hospitals_list);
        rankingRecycler.setAdapter(ranking_hospitals_adapter);

    }
}