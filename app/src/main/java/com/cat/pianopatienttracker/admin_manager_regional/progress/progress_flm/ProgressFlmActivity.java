package com.cat.pianopatienttracker.admin_manager_regional.progress.progress_flm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.pianopatienttracker.login.LoginActivity;
import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.network.Webservice;
import com.cat.pianopatienttracker.admin_manager_regional.Admin_home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgressFlmActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    String accessToken;

    ArrayList<ProgressFlm_item> progress_list = new ArrayList<>();
    ProgressFlm_adapter progress_adapter;

    RecyclerView progressRecycler;

    Admin_home activity = new Admin_home();
    ImageView back;

    TextView targetNo, targetPercent, marketNo, marketPercent,name;
    ContentLoadingProgressBar targetProgress, marketProgress;

    int countryId,brandId,flmId;
    String flmName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_flm);


        name = findViewById(R.id.name);
        targetNo = findViewById(R.id.targetNo);
        targetPercent = findViewById(R.id.targetPercent);
        marketNo = findViewById(R.id.marketNo);
        marketPercent = findViewById(R.id.marketPercent);
        targetProgress = findViewById(R.id.targetProgress);
        marketProgress = findViewById(R.id.marketProgress);


        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        countryId = getIntent().getIntExtra("countryId",0);
        brandId = getIntent().getIntExtra("brandId",0);
        flmId = getIntent().getIntExtra("flmId",0);
        flmName = getIntent().getStringExtra("flmName");
        accessToken = getIntent().getStringExtra("accessToken");
        dialog = new ProgressDialog(ProgressFlmActivity.this);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);

        getProgress();
    }

    public void getProgress() {
        dialog.show();

        Webservice.getInstance().getApi().getProgressFlm(accessToken, countryId, brandId,flmId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    JSONObject responseObject = null;
                    try {
                        responseObject = new JSONObject(response.body().string());
                        JSONObject myDataObj = responseObject.getJSONObject("total");
                        JSONArray progressArr = responseObject.getJSONArray("data");

                        setTotalData(myDataObj);

                        setProgressList(progressArr);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {
                    Intent i = new Intent(ProgressFlmActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                dialog.dismiss();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProgressFlmActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void setTotalData(JSONObject totalData) {

        try {
            String targetNoTxt = totalData.getString("actual_target") + " of " + totalData.getString("total_target");
            String targetPercentTxt = totalData.getString("target_percent") + "%";
            String marketNoTxt = totalData.getString("market_actual") + " of " + totalData.getString("market_all");
            String marketPercentTxt = totalData.getString("market_share") + "%";
            targetNo.setText(targetNoTxt);
            targetPercent.setText(targetPercentTxt);
            targetProgress.setProgress((int) totalData.getDouble("target_percent"));
            marketNo.setText(marketNoTxt);
            marketPercent.setText(marketPercentTxt);
            marketProgress.setProgress((int) totalData.getDouble("market_share"));

            name.setText(flmName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setProgressList(JSONArray list) {
        progress_list.clear();
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int userId = currentObject.getInt("user_id");
                final String name = currentObject.getString("name");
                final String address = currentObject.getString("area_name");
                final int actualTarget = currentObject.getInt("actual_target");
                final int totalTarget = currentObject.getInt("total_target");
                final double targetPercent = currentObject.getDouble("target_percent");
                final int actualMarket = currentObject.getInt("market_actual");
                final int totalMarket = currentObject.getInt("market_all");
                final double marketPercent = currentObject.getDouble("market_share");


                progress_list.add(new ProgressFlm_item(userId, name, address, actualTarget, totalTarget, targetPercent, actualMarket, totalMarket, marketPercent));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initProgressRecyclerView();
    }

    private void initProgressRecyclerView() {
        progressRecycler = findViewById(R.id.recycler);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ProgressFlmActivity.this, LinearLayoutManager.VERTICAL, false);
        progressRecycler.setLayoutManager(layoutManager);

        progress_adapter = new ProgressFlm_adapter(ProgressFlmActivity.this, progress_list);
        progressRecycler.setAdapter(progress_adapter);

    }
}