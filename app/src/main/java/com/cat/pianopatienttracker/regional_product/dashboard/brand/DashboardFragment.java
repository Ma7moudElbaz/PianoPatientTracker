package com.cat.pianopatienttracker.regional_product.dashboard.brand;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.cat.pianopatienttracker.LoginActivity;
import com.cat.pianopatienttracker.regional_product.Admin_home;
import com.cat.pianopatienttracker.regional_product.bottom_sheet.BottomSheet_country_brand_fragment;
import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.network.Webservice;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardFragment extends Fragment implements BottomSheet_country_brand_fragment.ItemClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    public void showCountriesBrandsBottomSheet() {
        BottomSheet_country_brand_fragment countriesBrandsBottomSheet =
                new BottomSheet_country_brand_fragment(activity.getCountries_list(),activity.getSelectedCountryIndex(),activity.getSelectedBrandIndex());
        countriesBrandsBottomSheet.setTargetFragment(this, 300);
        countriesBrandsBottomSheet.show(getFragmentManager(), "country_brand");
    }


    private ProgressDialog dialog;
    String accessToken;


    int[] brandsChartColors = new int[]{R.color.colorAccent, R.color.dark_gray};
    int[] dosesChartColors = new int[]{R.color.colorAccent, R.color.dark_gray, R.color.red, R.color.light_blue};


    ArrayList<JSONArray> brandChartItemArr = new ArrayList<>();
    ArrayList<JSONObject> targetChartItemArr = new ArrayList<>();


    private static DecimalFormat df2 = new DecimalFormat("#.##");
    AnimatedPieViewConfig productChartConfig, dosesChartConfig, targetChartConfig;


    ArrayList<Ranking_dashboard_item> ranking_rep_list = new ArrayList<>();
    ArrayList<Ranking_dashboard_item> ranking_sector_list = new ArrayList<>();
    ArrayList<Ranking_dashboard_item> ranking_hospital_list = new ArrayList<>();
    ArrayList<Ranking_dashboard_item> ranking_doctor_list = new ArrayList<>();

    RecyclerView rankingRecycler;
    Ranking_dashboard_adapter ranking_adapter;

    TextView rankingRepsBtn, rankingDoctorsBtn, rankingSectorsBtn, rankingHospitalsBtn;
    TextView targetTotal;
    ImageView selectCountryBrand;


    ChipCloud brandsTagChip, targetTagChip;
    AnimatedPieView productChart, dosesChart, targetChart;

    Admin_home activity;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (Admin_home) getActivity();


        accessToken = activity.getAccessToken();

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);





        targetTotal = view.findViewById(R.id.target_total_tv);
        rankingRepsBtn = view.findViewById(R.id.ranking_reps_btn);
        rankingDoctorsBtn = view.findViewById(R.id.ranking_doctors_btn);
        rankingSectorsBtn = view.findViewById(R.id.ranking_sectors_btn);
        rankingHospitalsBtn = view.findViewById(R.id.ranking_hospitals_btn);
        selectCountryBrand = view.findViewById(R.id.selectCountry);
        selectCountryBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountriesBrandsBottomSheet();

            }
        });

        rankingRepsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRankingRepAdapter();
            }
        });

        rankingDoctorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRankingDoctorsAdapter();
            }
        });

        rankingSectorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRankingSectorsAdapter();
            }
        });

        rankingHospitalsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRankingHospitalsAdapter();
            }
        });
        productChart = view.findViewById(R.id.productChart);
        dosesChart = view.findViewById(R.id.dosesChart);
        targetChart = view.findViewById(R.id.targetChart);


        brandsTagChip = view.findViewById(R.id.brandTagChip);
        targetTagChip = view.findViewById(R.id.targetTagChip);


        brandsTagChip.setChipListener(new ChipListener() {
            @Override
            public void chipSelected(int index) {
                setBrandChart(brandChartItemArr.get(index));
            }

            @Override
            public void chipDeselected(int index) {

            }
        });

        targetTagChip.setChipListener(new ChipListener() {
            @Override
            public void chipSelected(int index) {
                setTargetChart(targetChartItemArr.get(index));
            }

            @Override
            public void chipDeselected(int index) {

            }
        });

    }

    public void getDashboard() {
        dialog.show();

        int selectedCountryId = activity.getSelectedCountryId();
        int selectedBrandId = activity.getSelectedBrandId();

        activity.setSelectedCountryId(selectedCountryId);
        activity.setSelectedBrandId(selectedBrandId);

        Webservice.getInstance().getApi().getDashboard(accessToken, selectedCountryId, selectedBrandId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    JSONObject responseObject = null;
                    try {
                        responseObject = new JSONObject(response.body().string());
                        JSONObject brandObject = responseObject.getJSONObject("brand");
                        JSONArray brandChartArr = brandObject.getJSONArray("levels");
                        JSONArray dosesChartArr = responseObject.getJSONArray("doses");
                        JSONArray targetChartArr = responseObject.getJSONArray("target");
                        JSONObject rankingObject = responseObject.getJSONObject("ranking");
                        setBrandsChartTags(brandChartArr);
                        setTargetChartTags(targetChartArr);
                        setDosesChart(dosesChartArr);
                        setRankingData(rankingObject);

//                        Log.d("TAG", responseObject.toString());
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

    public void setRankingData(JSONObject rankingObj) {
        try {

            JSONArray repsList = rankingObj.getJSONArray("rep");
            JSONArray sectorsList = rankingObj.getJSONArray("sector");
            JSONArray hospitalsList = rankingObj.getJSONArray("hospitals");
            JSONArray doctorsList = rankingObj.getJSONArray("doctors");

            for (int i = 0; i < repsList.length(); i++) {
                JSONObject currentObject = repsList.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                final int patientsNo = currentObject.getInt("p_count");

                ranking_rep_list.add(new Ranking_dashboard_item(id, name, patientsNo));
            }

            for (int i = 0; i < sectorsList.length(); i++) {
                JSONObject currentObject = sectorsList.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                final int patientsNo = currentObject.getInt("p_count");

                ranking_sector_list.add(new Ranking_dashboard_item(id, name, patientsNo));
            }

            for (int i = 0; i < hospitalsList.length(); i++) {
                JSONObject currentObject = hospitalsList.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                final int patientsNo = currentObject.getInt("p_count");

                ranking_hospital_list.add(new Ranking_dashboard_item(id, name, patientsNo));
            }

            for (int i = 0; i < doctorsList.length(); i++) {
                JSONObject currentObject = doctorsList.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                final int patientsNo = currentObject.getInt("p_count");

                ranking_doctor_list.add(new Ranking_dashboard_item(id, name, patientsNo));
            }

//            Log.e("Hospital", ranking_hospital_list.get(0).getName());
//            Log.e("Rep", ranking_rep_list.get(0).getName());
//            Log.e("doctor", ranking_doctor_list.get(0).getName());
//            Log.e("sector", ranking_sector_list.get(0).getName());

            initRankingRecyclerView();
//            ranking_list = ranking_rep_list;
//            ranking_adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBrandsChartTags(JSONArray list) {
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final String title = currentObject.getString("name");
                brandsTagChip.addChip(title);
                brandChartItemArr.add(currentObject.getJSONArray("child"));
            }
            brandsTagChip.setSelectedChip(0);
            setBrandChart(brandChartItemArr.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBrandChart(JSONArray list) {


        productChartConfig = new AnimatedPieViewConfig();


        productChartConfig
                .animatePie(true)
                .strokeMode(true)
                .floatUpDuration(500)
                .floatDownDuration(500)
                .splitAngle(0.5f)
                .duration(1000)
                .drawText(true)
                .textSize(34)
                .autoSize(true)
                .pieRadius(200)
                .legendsWith((ViewGroup) getView().findViewById(R.id.brands_legends));


        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);

                final String name = currentObject.getString("name");
                final double value = currentObject.getInt("total");
                productChartConfig.addData(new SimplePieInfo(value, ContextCompat.getColor(getActivity(), brandsChartColors[i]), name + " : " + df2.format(value)));

            }

            productChart.applyConfig(productChartConfig);
            productChart.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTargetChartTags(JSONArray list) {
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final String title = currentObject.getString("year");

                targetTagChip.addChip(title);
                targetChartItemArr.add(currentObject);

            }
            targetTagChip.setSelectedChip(0);
            setTargetChart(targetChartItemArr.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTargetChart(JSONObject currentObject) {


        targetChartConfig = new AnimatedPieViewConfig();


        targetChartConfig
                .animatePie(true)
                .strokeMode(true)
                .floatUpDuration(500)
                .floatDownDuration(500)
                .splitAngle(0.5f)
                .duration(1000)
                .drawText(true)
                .textSize(34)
                .autoSize(true)
                .pieRadius(200)
                .legendsWith((ViewGroup) getView().findViewById(R.id.target_legends));


        try {

            final int actualTarget = currentObject.getInt("actual_target");
            final int totalTarget = currentObject.getInt("total_target");
            final int notAchievedTarget = totalTarget - actualTarget;
            targetChartConfig.addData(new SimplePieInfo(actualTarget, ContextCompat.getColor(getActivity(), brandsChartColors[0]), "Actual Target : " + df2.format(actualTarget)));
            targetChartConfig.addData(new SimplePieInfo(notAchievedTarget, ContextCompat.getColor(getActivity(), brandsChartColors[1]), "  " + df2.format(notAchievedTarget)));

            targetChart.applyConfig(targetChartConfig);
            targetChart.start();

            targetTotal.setText(String.valueOf(totalTarget));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDosesChart(JSONArray list) {

        dosesChartConfig = new AnimatedPieViewConfig();
        dosesChartConfig
                .animatePie(true)
                .strokeMode(true)
                .floatUpDuration(500)
                .floatDownDuration(500)
                .splitAngle(0.5f)
                .duration(1000)
                .drawText(true)
                .textSize(34)
                .autoSize(true)
                .pieRadius(200)
                .legendsWith((ViewGroup) getView().findViewById(R.id.doses_legends));
        ;

        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);

                final String name = currentObject.getString("name");
                final double value = currentObject.getInt("total");
                dosesChartConfig.addData(new SimplePieInfo(value, ContextCompat.getColor(getActivity(), dosesChartColors[i]), name + " : " + df2.format(value)));

            }

            dosesChart.applyConfig(dosesChartConfig);
            dosesChart.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRankingRecyclerView() {
        rankingRecycler = getView().findViewById(R.id.ranking_dashboard_recycler);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rankingRecycler.setLayoutManager(layoutManager);
        initRankingRepAdapter();

    }

    private void initRankingRepAdapter() {
        ranking_adapter = new Ranking_dashboard_adapter(getActivity(), ranking_rep_list);
        rankingRecycler.setAdapter(ranking_adapter);
        setRankingButtons(0);
    }

    private void initRankingDoctorsAdapter() {
        ranking_adapter = new Ranking_dashboard_adapter(getActivity(), ranking_doctor_list);
        rankingRecycler.setAdapter(ranking_adapter);
        setRankingButtons(1);
    }

    private void initRankingSectorsAdapter() {
        ranking_adapter = new Ranking_dashboard_adapter(getActivity(), ranking_sector_list);
        rankingRecycler.setAdapter(ranking_adapter);
        setRankingButtons(2);
    }

    private void initRankingHospitalsAdapter() {
        ranking_adapter = new Ranking_dashboard_adapter(getActivity(), ranking_hospital_list);
        rankingRecycler.setAdapter(ranking_adapter);
        setRankingButtons(3);
    }

    private void setRankingButtons(int buttonClicked) {

        rankingRepsBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
        rankingRepsBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.more_light_gray));

        rankingDoctorsBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
        rankingDoctorsBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.more_light_gray));

        rankingSectorsBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
        rankingSectorsBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.more_light_gray));

        rankingHospitalsBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
        rankingHospitalsBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.more_light_gray));

        switch (buttonClicked) {
            case 0:
                rankingRepsBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                rankingRepsBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                return;
            case 1:
                rankingDoctorsBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                rankingDoctorsBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                return;
            case 2:
                rankingSectorsBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                rankingSectorsBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                return;
            case 3:
                rankingHospitalsBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                rankingHospitalsBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                return;
        }

    }

    @Override
    public void countryBrandOnItemClick(int selectedCountryIndex, int selectedBrandIndex) {
        activity.setSelectedCountryIndex(selectedCountryIndex);
        activity.setSelectedBrandIndex(selectedBrandIndex);

        getDashboard();

    }

}