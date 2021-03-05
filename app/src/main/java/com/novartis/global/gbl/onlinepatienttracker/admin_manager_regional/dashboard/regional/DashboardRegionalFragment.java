package com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.dashboard.regional;

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

import com.novartis.global.gbl.onlinepatienttracker.login.LoginActivity;
import com.novartis.global.gbl.onlinepatienttracker.R;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.Admin_home;
import com.novartis.global.gbl.onlinepatienttracker.network.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRegionalFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_regional, container, false);
    }

    private ProgressDialog dialog;
    String accessToken;

    ArrayList<DashboardRegional_item> dashboardRegional_list = new ArrayList<>();
    DashboardRegional_adapter dashboardRegional_adapter;


    RecyclerView dashboardRegionalRecycler;

    Admin_home activity;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (Admin_home) getActivity();


        accessToken = activity.getAccessToken();

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
                        JSONArray dataArr = responseObject.getJSONArray("data");
                        setDashboardRegionalList(dataArr);

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

    public void setDashboardRegionalList(JSONArray list) {
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                final String iso = currentObject.getString("iso");
                final JSONArray productsData = currentObject.getJSONArray("products");

                ArrayList<ProductTarget_item> productsTarget_list = new ArrayList<>();
                productsTarget_list = setProductsTargerList(productsData);


                dashboardRegional_list.add(new DashboardRegional_item(id, name,iso, productsTarget_list));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initDashboardRegionalRecyclerView();
    }


    ArrayList<ProductTarget_item> setProductsTargerList(JSONArray list) {

        ArrayList<ProductTarget_item> productsTarget_list = new ArrayList<>();
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final JSONObject brands = currentObject.getJSONObject("brands");
                final String name = brands.getString("name");
                final int actualTarget = currentObject.getInt("actual_target");
                final int totalTarget = currentObject.getInt("total_target");
                final double targetPercent = currentObject.getDouble("target_percent");
                final int actualMarket = currentObject.getInt("market_actual");
                final int totalMarket = currentObject.getInt("market_all");
                final double marketPercent = currentObject.getDouble("market_share");


                productsTarget_list.add(new ProductTarget_item(id, name, actualTarget, totalTarget, targetPercent, actualMarket, totalMarket, marketPercent));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productsTarget_list;
    }

    private void initDashboardRegionalRecyclerView() {
        dashboardRegionalRecycler = getView().findViewById(R.id.recycler);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        dashboardRegionalRecycler.setLayoutManager(layoutManager);

        dashboardRegional_adapter = new DashboardRegional_adapter(getActivity(), dashboardRegional_list);
        dashboardRegionalRecycler.setAdapter(dashboardRegional_adapter);

    }
}