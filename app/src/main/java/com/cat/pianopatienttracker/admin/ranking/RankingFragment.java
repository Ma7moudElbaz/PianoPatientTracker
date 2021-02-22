package com.cat.pianopatienttracker.admin.ranking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cat.pianopatienttracker.LoginActivity;
import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.admin.Admin_home;
import com.cat.pianopatienttracker.network.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

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

    Admin_home activity;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (Admin_home) getActivity();

        accessToken = "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9kZXYucHRyYWNrZXIub3JnXC9hcGlcL2F1dGhcL2xvZ2luIiwiaWF0IjoxNjEzOTkzMDY1LCJleHAiOjE2MTQ0MjUwNjUsIm5iZiI6MTYxMzk5MzA2NSwianRpIjoiQXZHdHhuQzhDYlVPSnYwVyIsInN1YiI6NiwicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.u8piW4bYBzC2TKLbnakCnvBiHGDH3cOPCOPc17ZO8-I";
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);

        //hospitals,reps,sectors,doctors
        getRanking("hospitals");
    }


    public void getRanking(String type) {
        dialog.show();

        Webservice.getInstance().getApi().getRanking(accessToken, activity.getSelectedCountryId(), activity.getSelectedBrandId(),type).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    JSONObject responseObject = null;
                    try {
                        responseObject = new JSONObject(response.body().string());
                        JSONArray rankingArr = responseObject.getJSONArray("data");

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

}