package com.novartis.global.gbl.onlinepatienttracker.flm.requests;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.novartis.global.gbl.onlinepatienttracker.LoginActivity;
import com.novartis.global.gbl.onlinepatienttracker.R;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.bottom_sheets.BottomSheet_country_brand_fragment;
import com.novartis.global.gbl.onlinepatienttracker.flm.Flm_home;
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

public class FlmRequestsFragment extends Fragment implements BottomSheet_country_brand_fragment.ItemClickListener, Request_adapter.OnRequestListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flm_requests, container, false);
    }

    public void showCountriesBrandsBottomSheet() {
        BottomSheet_country_brand_fragment countriesBrandsBottomSheet =
                new BottomSheet_country_brand_fragment(activity.getCountries_list(), activity.getSelectedCountryIndex(), activity.getSelectedBrandIndex());
        countriesBrandsBottomSheet.setTargetFragment(this, 300);
        countriesBrandsBottomSheet.show(getFragmentManager(), "country_brand");
    }

    private ProgressDialog dialog;
    String accessToken;

    ArrayList<Request_item> requests_list = new ArrayList<>();
    Request_adapter requests_adapter;


    RecyclerView requestRecycler;

    Flm_home activity;


    ImageView selectCountryBrand;


    RelativeLayout selectCountryBrand_cont;
    ImageView selectedCountry_img,selectedProduct_img;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (Flm_home) getActivity();


        selectCountryBrand_cont = view.findViewById(R.id.selectCountryBrand_cont);
        selectedCountry_img = view.findViewById(R.id.selectedCountry_img);
        selectedProduct_img = view.findViewById(R.id.selectedProduct_img);

        selectCountryBrand_cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountriesBrandsBottomSheet();
            }
        });

        setCountryBrandImage();

        accessToken = activity.getAccessToken();

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);

        selectCountryBrand = view.findViewById(R.id.selectCountry);
        selectCountryBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountriesBrandsBottomSheet();
            }
        });

        getRequests();
    }

    public void getRequests() {
        dialog.show();
        Webservice.getInstance().getApi().getRequests(accessToken, activity.getSelectedBrandId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    JSONObject responseObject = null;
                    try {
                        responseObject = new JSONObject(response.body().string());
                        JSONArray dataArr = responseObject.getJSONArray("data");
                        setRequestsList(dataArr);

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

    public void setRequestsList(JSONArray list) {
        requests_list.clear();
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String process = currentObject.getString("process");
                final JSONObject patientObject = currentObject.getJSONObject("patient");
                final int patientId = patientObject.getInt("id");
                final String hospital = patientObject.getString("hospital");
                final String sector = patientObject.getString("sector");
                final String doctor = patientObject.getString("doctor");
                final String dose = patientObject.getString("dose") + " mg";
                final String byName = patientObject.getString("rep_name");


                requests_list.add(new Request_item(id, patientId, hospital, sector, doctor, dose, byName, process));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initRepHomeRecyclerView();
    }

    private void initRepHomeRecyclerView() {
        requestRecycler = getView().findViewById(R.id.recycler);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        requestRecycler.setLayoutManager(layoutManager);

        requests_adapter = new Request_adapter(getActivity(), requests_list, accessToken, activity.getSelectedBrandId(), this);
        requestRecycler.setAdapter(requests_adapter);
    }


    public void confirmRequest(int patientId) {
        dialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("brand_id",String.valueOf(activity.getSelectedBrandId()));
        map.put("patient_id",String.valueOf(patientId));
        Webservice.getInstance().getApi().confirRequest(accessToken,map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    JSONObject responseObject = null;
                    try {
                        responseObject = new JSONObject(response.body().string());
                        Toast.makeText(activity, "Confirmation Success", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(i);
                    getActivity().finish();
                }
                dialog.dismiss();
                getRequests();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onConfirmRequestClick(int patientId) {
        confirmRequest(patientId);
    }

    @Override
    public void countryBrandOnItemClick(int selectedCountryIndex, int selectedBrandIndex, String selectedCountryName, String selectedBrandName) {
        activity.setSelectedCountryIndex(selectedCountryIndex);
        activity.setSelectedBrandIndex(selectedBrandIndex);
        activity.setSelectedCountryName(selectedCountryName);
        activity.setSelectedBrandName(selectedBrandName);

        getRequests();
        setCountryBrandImage();
    }


    void setCountryBrandImage() {
        String countryImageName = "round_"+activity.getSelectedCountryName().toLowerCase();
        String brandImageName = "round_"+activity.getSelectedBrandName().toLowerCase();
        int countryImgDrawable = getResources().getIdentifier(countryImageName, "drawable", activity.getPackageName());
        int brandImgDrawable = getResources().getIdentifier(brandImageName, "drawable", activity.getPackageName());
        Glide.with(this).load(countryImgDrawable).into(selectedCountry_img);
        Glide.with(this).load(brandImgDrawable).into(selectedProduct_img);
    }
}