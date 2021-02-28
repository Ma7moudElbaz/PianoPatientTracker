package com.cat.pianopatienttracker.flm.progress;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cat.pianopatienttracker.login.LoginActivity;
import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.admin_manager_regional.shared.bottom_sheets.BottomSheet_country_brand_fragment;
import com.cat.pianopatienttracker.admin_manager_regional.progress.progress_flm.ProgressFlm_adapter;
import com.cat.pianopatienttracker.admin_manager_regional.progress.progress_flm.ProgressFlm_item;
import com.cat.pianopatienttracker.flm.Flm_home;
import com.cat.pianopatienttracker.network.Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlmProgressFragment extends Fragment implements BottomSheet_country_brand_fragment.ItemClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flm_progress, container, false);
    }


    public void showCountriesBrandsBottomSheet() {
        BottomSheet_country_brand_fragment countriesBrandsBottomSheet =
                new BottomSheet_country_brand_fragment(activity.getCountries_list(), activity.getSelectedCountryIndex(), activity.getSelectedBrandIndex());
        countriesBrandsBottomSheet.setTargetFragment(this, 300);
        countriesBrandsBottomSheet.show(getFragmentManager(), "country_brand");
    }

    private ProgressDialog dialog;
    String accessToken;

    ArrayList<ProgressFlm_item> progress_list = new ArrayList<>();
    ProgressFlm_adapter progress_adapter;

    RecyclerView progressRecycler;

    Flm_home activity;
    ImageView selectCountryBrand;

    TextView targetNo, targetPercent, marketNo, marketPercent;
    ContentLoadingProgressBar targetProgress, marketProgress;


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

        targetNo = view.findViewById(R.id.targetNo);
        targetPercent = view.findViewById(R.id.targetPercent);
        marketNo = view.findViewById(R.id.marketNo);
        marketPercent = view.findViewById(R.id.marketPercent);
        targetProgress = view.findViewById(R.id.targetProgress);
        marketProgress = view.findViewById(R.id.marketProgress);


        selectCountryBrand = view.findViewById(R.id.selectCountry);
        selectCountryBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountriesBrandsBottomSheet();
            }
        });

        accessToken = activity.getAccessToken();
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);

        getProgress();
    }

    public void getProgress() {
        dialog.show();

        Webservice.getInstance().getApi().getProgress(accessToken, activity.getSelectedCountryId(), activity.getSelectedBrandId()).enqueue(new Callback<ResponseBody>() {
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
        progressRecycler = getView().findViewById(R.id.recycler);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        progressRecycler.setLayoutManager(layoutManager);

        progress_adapter = new ProgressFlm_adapter(getActivity(), progress_list);
        progressRecycler.setAdapter(progress_adapter);

    }

    @Override
    public void countryBrandOnItemClick(int selectedCountryIndex, int selectedBrandIndex, String selectedCountryName, String selectedBrandName) {
        activity.setSelectedCountryIndex(selectedCountryIndex);
        activity.setSelectedBrandIndex(selectedBrandIndex);
        activity.setSelectedCountryName(selectedCountryName);
        activity.setSelectedBrandName(selectedBrandName);

        getProgress();

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