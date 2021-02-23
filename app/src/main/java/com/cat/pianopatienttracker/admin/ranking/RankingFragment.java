package com.cat.pianopatienttracker.admin.ranking;

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
import android.widget.TextView;
import android.widget.Toast;

import com.cat.pianopatienttracker.LoginActivity;
import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.admin.Admin_home;
import com.cat.pianopatienttracker.network.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RankingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ranking, container, false);
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

    TextView ranking_hospitals_btn,ranking_reps_btn,ranking_sectors_btn,ranking_doctors_btn;
    TextView filter_period;

    Admin_home activity;

    public void showPeriodBottomSheet(View view) {
        BottomSheet_period_fragment periodBottomSheet =
                BottomSheet_period_fragment.newInstance();
        periodBottomSheet.show(getChildFragmentManager(),"period");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (Admin_home) getActivity();

        ranking_hospitals_btn =  view.findViewById(R.id.ranking_hospitals_btn);
        ranking_reps_btn =  view.findViewById(R.id.ranking_reps_btn);
        ranking_sectors_btn =  view.findViewById(R.id.ranking_sectors_btn);
        ranking_doctors_btn =  view.findViewById(R.id.ranking_doctors_btn);

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




        filter_period =  view.findViewById(R.id.filter_period);
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
        getRanking("reps");
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
                        }else if (type.equals("reps")){
                            setRankingRepsList(rankingArr);
                        }else if (type.equals("sectors")){
                            setRankingSectorsList(rankingArr);;
                        }else if (type.equals("doctors")){
                            setRankingDoctorsList(rankingArr);;
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


                final int doctorsNo = 55;
//                final int doctorsNo = currentObject.getInt("d_count");
                final int patientsNo = currentObject.getInt("p_count");

                ranking_hospitals_list.add(new Ranking_hospitals_item(id, name, sectorString, address,doctorsNo,patientsNo));
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

                ranking_reps_list.add(new Ranking_reps_item(id, name,patientsNo ));
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

//                final int doctorsNo = currentObject.getInt("d_count");
//                final int hospitalsNo = currentObject.getInt("h_count");

                final int doctorsNo = 332;
                final int hospitalsNo = 20;

                ranking_sectors_list.add(new Ranking_sectors_item(id, name,doctorsNo,hospitalsNo,patientsNo));
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
//                final String hospital = currentObject.getString("hospital");
                final String hospital = "hospital";
                final String address = currentObject.getString("address");
                final int patientsNo = currentObject.getInt("p_count");

                ranking_doctors_list.add(new Ranking_doctors_item(id, name,hospital,address,patientsNo));
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

        ranking_hospitals_adapter = new Ranking_hospitals_adapter(getActivity(), ranking_hospitals_list);
        rankingRecycler.setAdapter(ranking_hospitals_adapter);
    }

    private void initRankingRepsAdapter() {
        ranking_hospitals_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));
        ranking_reps_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_blue));
        ranking_sectors_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));
        ranking_doctors_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));

        ranking_reps_adapter = new Ranking_reps_adapter(getActivity(), ranking_reps_list);
        rankingRecycler.setAdapter(ranking_reps_adapter);
    }

    private void initRankingSectorsAdapter() {
        ranking_hospitals_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));
        ranking_reps_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));
        ranking_sectors_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_blue));
        ranking_doctors_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));

        ranking_sectors_adapter= new Ranking_sectors_adapter(getActivity(), ranking_sectors_list);
        rankingRecycler.setAdapter(ranking_sectors_adapter);
    }

    private void initRankingDoctorsAdapter() {
        ranking_hospitals_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));
        ranking_reps_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));
        ranking_sectors_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray));
        ranking_doctors_btn.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_blue));

        ranking_doctors_adapter= new Ranking_doctors_adapter(getActivity(), ranking_doctors_list);
        rankingRecycler.setAdapter(ranking_doctors_adapter);
    }


}