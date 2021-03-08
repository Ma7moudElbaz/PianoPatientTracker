package com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.ranking;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.novartis.global.gbl.onlinepatienttracker.LoginActivity;
import com.novartis.global.gbl.onlinepatienttracker.R;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.Admin_home;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.bottom_sheets.BottomSheet_country_brand_fragment;
import com.novartis.global.gbl.onlinepatienttracker.network.Webservice;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.bottom_sheets.BottomSheet_filter_doctors_fragment;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.bottom_sheets.BottomSheet_filter_hospitals_fragment;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.bottom_sheets.BottomSheet_filter_rep_fragment;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.bottom_sheets.BottomSheet_period_fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RankingFragment extends Fragment implements BottomSheet_country_brand_fragment.ItemClickListener
        , BottomSheet_period_fragment.ItemClickListener, BottomSheet_filter_rep_fragment.ItemClickListener
        , BottomSheet_filter_doctors_fragment.ItemClickListener, BottomSheet_filter_hospitals_fragment.ItemClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ranking, container, false);
    }

    public void showCountriesBrandsBottomSheet() {
        BottomSheet_country_brand_fragment bottomSheet =
                new BottomSheet_country_brand_fragment(activity.getCountries_list(), activity.getSelectedCountryIndex(), activity.getSelectedBrandIndex());
        bottomSheet.setTargetFragment(this, 300);
        bottomSheet.show(getFragmentManager(), "country_brand");
    }

    public void showRepsFilterBottomSheet() {
        BottomSheet_filter_rep_fragment bottomSheet =
                new BottomSheet_filter_rep_fragment(activity.getCountries_list(), activity.getSelectedCountryIndex());
        bottomSheet.setTargetFragment(this, 300);
        bottomSheet.show(getFragmentManager(), "reps");
    }

    public void showDoctorsFilterBottomSheet() {
        BottomSheet_filter_doctors_fragment bottomSheet =
                new BottomSheet_filter_doctors_fragment(activity.getCountries_list(), activity.getSelectedCountryIndex());
        bottomSheet.setTargetFragment(this, 300);
        bottomSheet.show(getFragmentManager(), "doctors");
    }

    public void showHospitalsFilterBottomSheet() {
        BottomSheet_filter_hospitals_fragment bottomSheet =
                new BottomSheet_filter_hospitals_fragment(activity.getCountries_list(), activity.getSelectedCountryIndex(), activity.getSectors_list());
        bottomSheet.setTargetFragment(this, 300);
        bottomSheet.show(getFragmentManager(), "hospitals");
    }

    public void showPeriodBottomSheet(View view) {
        BottomSheet_period_fragment bottomSheet =
                new BottomSheet_period_fragment();
        bottomSheet.setTargetFragment(this, 300);
        bottomSheet.show(getFragmentManager(), "period");
    }


    private ProgressDialog dialog;
    String accessToken;

    RecyclerView rankingRecycler;

    ArrayList<Ranking_hospitals_item> ranking_hospitals_list = new ArrayList<>();
    Ranking_hospitals_adapter ranking_hospitals_adapter;

    ArrayList<Ranking_reps_item> ranking_reps_list = new ArrayList<>();
    Ranking_reps_adapter ranking_reps_adapter;

    ArrayList<Ranking_sectors_item> ranking_sectors_list = new ArrayList<>();
    Ranking_sectors_adapter ranking_sectors_adapter;

    ArrayList<Ranking_doctors_item> ranking_doctors_list = new ArrayList<>();
    Ranking_doctors_adapter ranking_doctors_adapter;

    TextView ranking_hospitals_btn, ranking_reps_btn, ranking_sectors_btn, ranking_doctors_btn;
    TextView filter_period, filters;
    String selectedTab = "reps";

    Admin_home activity;


    ImageView selectCountryBrand;


    RelativeLayout selectCountryBrand_cont;
    ImageView selectedCountry_img, selectedProduct_img;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (Admin_home) getActivity();


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


        selectCountryBrand = view.findViewById(R.id.selectCountry);
        selectCountryBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountriesBrandsBottomSheet();
            }
        });

        ranking_hospitals_btn = view.findViewById(R.id.ranking_hospitals_btn);
        ranking_reps_btn = view.findViewById(R.id.ranking_reps_btn);
        ranking_sectors_btn = view.findViewById(R.id.ranking_sectors_btn);
        ranking_doctors_btn = view.findViewById(R.id.ranking_doctors_btn);

        ranking_hospitals_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRanking("hospitals");
            }
        });

        ranking_reps_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRanking("reps");
            }
        });

        ranking_sectors_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRanking("sectors");
            }
        });

        ranking_doctors_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRanking("doctors");
            }
        });


        filters = view.findViewById(R.id.filters);
        filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTab.equals("reps")) {
                    showRepsFilterBottomSheet();
                } else if (selectedTab.equals("doctors")) {
                    showDoctorsFilterBottomSheet();
                } else if (selectedTab.equals("hospitals")) {
                    showHospitalsFilterBottomSheet();
                }
            }
        });
        filter_period = view.findViewById(R.id.filter_period);
        filter_period.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPeriodBottomSheet(view);
            }
        });

        accessToken = activity.getAccessToken();
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);

        //hospitals,reps,sectors,doctors
        initRankingRecyclerView();
        getRanking(selectedTab);
    }


    public void getRanking(String type) {
        dialog.show();

        Webservice.getInstance().getApi().getRanking(accessToken, activity.getSelectedCountryId(), activity.getSelectedBrandId(), type).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    JSONObject responseObject = null;
                    try {
                        responseObject = new JSONObject(response.body().string());
                        JSONArray rankingArr = responseObject.getJSONArray("data");

                        if (type.equals("hospitals")) {
                            setRankingHospitalsList(rankingArr);
                        } else if (type.equals("reps")) {
                            setRankingRepsList(rankingArr);
                        } else if (type.equals("sectors")) {
                            setRankingSectorsList(rankingArr);
                            ;
                        } else if (type.equals("doctors")) {
                            setRankingDoctorsList(rankingArr);
                            ;
                        }

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


    public void getRankingFiltered(String type, Map<String, String> filter) {
        dialog.show();

        Webservice.getInstance().getApi().getRankingFiltered(accessToken, activity.getSelectedCountryId(), activity.getSelectedBrandId(), type, filter).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    JSONObject responseObject = null;
                    try {
                        responseObject = new JSONObject(response.body().string());
                        JSONArray rankingArr = responseObject.getJSONArray("data");

                        if (type.equals("hospitals")) {
                            setRankingHospitalsList(rankingArr);
                        } else if (type.equals("reps")) {
                            setRankingRepsList(rankingArr);
                        } else if (type.equals("sectors")) {
                            setRankingSectorsList(rankingArr);
                            ;
                        } else if (type.equals("doctors")) {
                            setRankingDoctorsList(rankingArr);
                            ;
                        }

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


    public void setRankingHospitalsList(JSONArray list) {
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

                ranking_hospitals_list.add(new Ranking_hospitals_item(id, name, sectorString, address, doctorsNo, patientsNo));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initRankingHospitalsAdapter();
    }

    public void setRankingRepsList(JSONArray list) {
        ranking_reps_list.clear();
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                final int patientsNo = currentObject.getInt("p_count");

                ranking_reps_list.add(new Ranking_reps_item(id, name, patientsNo));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initRankingRepsAdapter();
    }

    public void setRankingSectorsList(JSONArray list) {
        ranking_sectors_list.clear();
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                final int patientsNo = currentObject.getInt("p_count");

                final int doctorsNo = currentObject.getInt("doctor_count");
                final int hospitalsNo = currentObject.getInt("hospitals_count");


                ranking_sectors_list.add(new Ranking_sectors_item(id, name, doctorsNo, hospitalsNo, patientsNo));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initRankingSectorsAdapter();
    }


    public void setRankingDoctorsList(JSONArray list) {
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
        initRankingDoctorsAdapter();
    }

    private void initRankingRecyclerView() {
        rankingRecycler = getView().findViewById(R.id.ranking_recycler);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rankingRecycler.setLayoutManager(layoutManager);

    }

    private void initRankingHospitalsAdapter() {
        ranking_hospitals_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_blue));
        ranking_reps_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));
        ranking_sectors_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));
        ranking_doctors_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));

        filters.setVisibility(View.VISIBLE);
        selectedTab = "hospitals";

        ranking_hospitals_adapter = new Ranking_hospitals_adapter(getActivity(), ranking_hospitals_list, accessToken, activity.getSelectedCountryId(), activity.getSelectedBrandId());
        rankingRecycler.setAdapter(ranking_hospitals_adapter);
    }

    private void initRankingRepsAdapter() {
        ranking_hospitals_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));
        ranking_reps_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_blue));
        ranking_sectors_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));
        ranking_doctors_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));

        filters.setVisibility(View.VISIBLE);
        selectedTab = "reps";

        ranking_reps_adapter = new Ranking_reps_adapter(getActivity(), ranking_reps_list, accessToken, activity.getSelectedCountryId(), activity.getSelectedBrandId());
        rankingRecycler.setAdapter(ranking_reps_adapter);
    }

    private void initRankingSectorsAdapter() {
        ranking_hospitals_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));
        ranking_reps_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));
        ranking_sectors_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_blue));
        ranking_doctors_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));

        filters.setVisibility(View.GONE);
        selectedTab = "sectors";

        ranking_sectors_adapter = new Ranking_sectors_adapter(getActivity(), ranking_sectors_list, accessToken, activity.getSelectedCountryId(), activity.getSelectedBrandId());
        rankingRecycler.setAdapter(ranking_sectors_adapter);
    }

    private void initRankingDoctorsAdapter() {
        ranking_hospitals_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));
        ranking_reps_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));
        ranking_sectors_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));
        ranking_doctors_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_blue));

        filters.setVisibility(View.VISIBLE);
        selectedTab = "doctors";

        ranking_doctors_adapter = new Ranking_doctors_adapter(getActivity(), ranking_doctors_list);
        rankingRecycler.setAdapter(ranking_doctors_adapter);
    }


    @Override
    public void periodOnItemClick(int filterType, int year, int month) {
        Map<String, String> filterMap = new HashMap<>();
        if (filterType == 0) {
            filterMap.put("year", String.valueOf(year));
            getRankingFiltered(selectedTab, filterMap);
        } else if (filterType == 1) {
            getRanking(selectedTab);
        } else if (filterType == 2) {
            filterMap.put("year", String.valueOf(year));
            filterMap.put("month", String.valueOf(month));
            getRankingFiltered(selectedTab, filterMap);
        }
    }

    @Override
    public void repFilterOnItemClick(Map<String, String> filterMap) {
        getRankingFiltered(selectedTab, filterMap);
    }

    @Override
    public void doctorsFilterOnItemClick(Map<String, String> filterMap) {
        getRankingFiltered(selectedTab, filterMap);
    }

    @Override
    public void hospitalsFilterOnItemClick(Map<String, String> filterMap) {
        getRankingFiltered(selectedTab, filterMap);
    }

    @Override
    public void countryBrandOnItemClick(int selectedCountryIndex, int selectedBrandIndex, String selectedCountryName, String selectedBrandName) {
        activity.setSelectedCountryIndex(selectedCountryIndex);
        activity.setSelectedBrandIndex(selectedBrandIndex);
        activity.setSelectedCountryName(selectedCountryName);
        activity.setSelectedBrandName(selectedBrandName);

        getRanking(selectedTab);
        setCountryBrandImage();
    }


    void setCountryBrandImage() {
        String countryImageName = "round_" + activity.getSelectedCountryName().toLowerCase();
        String brandImageName = "round_" + activity.getSelectedBrandName().toLowerCase();
        int countryImgDrawable = getResources().getIdentifier(countryImageName, "drawable", activity.getPackageName());
        int brandImgDrawable = getResources().getIdentifier(brandImageName, "drawable", activity.getPackageName());
        Glide.with(this).load(countryImgDrawable).into(selectedCountry_img);
        Glide.with(this).load(brandImgDrawable).into(selectedProduct_img);
    }
}