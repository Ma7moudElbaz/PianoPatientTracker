package com.novartis.global.gbl.onlinepatienttracker.rep.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.novartis.global.gbl.onlinepatienttracker.LoginActivity;
import com.novartis.global.gbl.onlinepatienttracker.R;
import com.novartis.global.gbl.onlinepatienttracker.network.Webservice;
import com.novartis.global.gbl.onlinepatienttracker.rep.Rep_home;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RepHomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rep_home, container, false);
    }

    private ProgressDialog dialog;
    String accessToken;
    int userId;

    ArrayList<RepHome_item> repHome_list = new ArrayList<>();
    RepHome_adapter repHome_adapter;


    RecyclerView repHomeRecycler;

    Rep_home activity;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (Rep_home) getActivity();

        accessToken = activity.getAccessToken();
        userId = activity.getUserId();

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);

        getMyData();

    }

    public void getMyData() {
        dialog.show();
        Webservice.getInstance().getApi().getMyProfile(accessToken).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    JSONObject responseObject = null;
                    try {
                        responseObject = new JSONObject(response.body().string());
                        JSONArray dataArr = responseObject.getJSONArray("total");
                        setRepHomeList(dataArr);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                    getActivity().finish();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void setRepHomeList(JSONArray list) {

        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("brand_id");
                final String name = currentObject.getString("brand_name");
                final String imageUrl = currentObject.getString("brand_image");
                final int actualTarget = currentObject.getInt("actual_target");
                final int totalTarget = currentObject.getInt("total_target");
                final double targetPercent = currentObject.getDouble("target_percent");
                final int actualMarket = currentObject.getInt("market_actual");
                final int totalMarket = currentObject.getInt("market_all");
                final double marketPercent = currentObject.getDouble("market_share");


                repHome_list.add(new RepHome_item(id, name, imageUrl, actualTarget, totalTarget, targetPercent, actualMarket, totalMarket, marketPercent));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initRepHomeRecyclerView();
    }

    private void initRepHomeRecyclerView() {
        repHomeRecycler = getView().findViewById(R.id.recycler);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        repHomeRecycler.setLayoutManager(layoutManager);

        repHome_adapter = new RepHome_adapter(getActivity(), repHome_list, accessToken, activity.getCountries_list(),userId);
        repHomeRecycler.setAdapter(repHome_adapter);

    }
}