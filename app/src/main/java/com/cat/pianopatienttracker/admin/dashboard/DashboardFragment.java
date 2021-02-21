package com.cat.pianopatienttracker.admin.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.cat.pianopatienttracker.LoginActivity;
import com.cat.pianopatienttracker.admin.CountriesSpinnerAdapter;
import com.cat.pianopatienttracker.admin.BrandsSpinnerAdapter;
import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.network.Webservice;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import co.lujun.androidtagview.ColorFactory;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardFragment extends Fragment {


    private ProgressDialog dialog;
    String accessToken;
    int selectedTagPosition = 0;

    ArrayList<Brand_item> brands_list = new ArrayList<>();
    ArrayList<Country_item> countries_list = new ArrayList<>();

    ArrayList<String> brandsTagList = new ArrayList<>();
    ArrayList<String> targetTagList = new ArrayList<>();

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    Spinner countriesSpinner, brandsSpinner;
    TagContainerLayout brandsTagView, targetTagView;
    AnimatedPieView productChart, dosesChart, targetChart;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);

        accessToken = "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9kZXYucHRyYWNrZXIub3JnXC9hcGlcL2F1dGhcL2xvZ2luIiwiaWF0IjoxNjEzNTYwNzMyLCJleHAiOjE2MTM5OTI3MzIsIm5iZiI6MTYxMzU2MDczMiwianRpIjoiQnRBQWswa0lJelZzYnRIUCIsInN1YiI6NiwicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.hxt-l-9E-raEsqWIJSno5ERjI1ozRJ5wtjeVzDJ-a0Q";


        productChart = view.findViewById(R.id.productChart);
        dosesChart = view.findViewById(R.id.dosesChart);
        targetChart = view.findViewById(R.id.targetChart);

        countriesSpinner = view.findViewById(R.id.countriesSpinner);
        brandsSpinner = view.findViewById(R.id.brandsSpinner);
        brandsTagView = view.findViewById(R.id.brandTagView);
        targetTagView = view.findViewById(R.id.targetTagView);
        brandsTagView.setTheme(ColorFactory.NONE);


        targetTagView.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                setTargetChart(targetChartItemArr.get(position));
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });

        brandsTagView.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                Log.d("TAG", brandChartItemArr.get(position).toString());

                setBrandChart(brandChartItemArr.get(position));


            }

            void setSelectedTag(int position) {

                if (selectedTagPosition == position) {
                    return;
                } else {
                    brandsTagView.getTagView(position).setTagBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    brandsTagView.getTagView(position).setTagTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                    brandsTagView.getTagView(selectedTagPosition).setTagBackgroundColor(ContextCompat.getColor(getActivity(), R.color.more_light_gray));
                    brandsTagView.getTagView(selectedTagPosition).setTagTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
                }
                selectedTagPosition = position;

            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });


        getCountries();

    }

    public void getDashboard() {
//        dialog.show();

        int selectedCountryId = countries_list.get(countriesSpinner.getSelectedItemPosition()).getId();
        int selectedBrandId = brands_list.get(brandsSpinner.getSelectedItemPosition()).getId();

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

                ranking_rep_list.add(new Ranking_dashboard_item(id,name,patientsNo));
            }

            for (int i = 0; i < sectorsList.length(); i++) {
                JSONObject currentObject = repsList.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                final int patientsNo = currentObject.getInt("p_count");

                ranking_sector_list.add(new Ranking_dashboard_item(id,name,patientsNo));
            }

            for (int i = 0; i < hospitalsList.length(); i++) {
                JSONObject currentObject = repsList.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                final int patientsNo = currentObject.getInt("p_count");

                ranking_hospital_list.add(new Ranking_dashboard_item(id,name,patientsNo));
            }

            for (int i = 0; i < doctorsList.length(); i++) {
                JSONObject currentObject = repsList.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                final int patientsNo = currentObject.getInt("p_count");

                ranking_doctor_list.add(new Ranking_dashboard_item(id,name,patientsNo));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBrandsChartTags(JSONArray list) {
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final String title = currentObject.getString("name");
                brandsTagList.add(title);
                brandChartItemArr.add(currentObject.getJSONArray("child"));

            }
            brandsTagView.setTags(brandsTagList);
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
                targetTagList.add(title);

                targetChartItemArr.add(currentObject);

            }
            targetTagView.setTags(targetTagList);
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

            final double actualTarget = currentObject.getDouble("actual_target");
            final double totalTarget = currentObject.getDouble("total_target");
            targetChartConfig.addData(new SimplePieInfo(actualTarget, ContextCompat.getColor(getActivity(), brandsChartColors[0]), "Actual Target : " + df2.format(actualTarget)));
            targetChartConfig.addData(new SimplePieInfo(totalTarget, ContextCompat.getColor(getActivity(), brandsChartColors[1]), "Total Target : " + df2.format(totalTarget)));

            targetChart.applyConfig(targetChartConfig);
            targetChart.start();


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

    public void getBrands() {
//        dialog.show();

        Webservice.getInstance().getApi().getBrands(accessToken).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    JSONObject responseObject = null;
                    try {
                        responseObject = new JSONObject(response.body().string());
                        JSONArray brandsArr = responseObject.getJSONArray("data");
                        setBrands(brandsArr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                    getActivity().finish();
                }
//                dialog.dismiss();
                getDashboard();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }

    public void setBrands(JSONArray list) {
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String title = currentObject.getString("name");
                final String imageUrl = currentObject.getString("image");
                brands_list.add(new Brand_item(id, title, imageUrl));

            }
            initBrandsSpinner();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initBrandsSpinner() {
        BrandsSpinnerAdapter brandsSpinnerAdapter = new BrandsSpinnerAdapter(getActivity(), brands_list);
        brandsSpinner.setAdapter(brandsSpinnerAdapter);

    }

    public void getCountries() {
        dialog.show();

        Webservice.getInstance().getApi().getCountries(accessToken).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    JSONObject responseObject = null;
                    try {
                        responseObject = new JSONObject(response.body().string());
                        JSONObject responseObjectdata = responseObject.getJSONObject("data");
                        JSONArray countriesArr = responseObjectdata.getJSONArray("data");
                        setCountries(countriesArr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
//                dialog.dismiss();
                getBrands();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }

    public void setCountries(JSONArray list) {
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String title = currentObject.getString("name");
                countries_list.add(new Country_item(id, title));
            }
            initCountriesSpinner();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initCountriesSpinner() {
        CountriesSpinnerAdapter countriesSpinnerAdapter = new CountriesSpinnerAdapter(getActivity(), countries_list);
        countriesSpinner.setAdapter(countriesSpinnerAdapter);

    }
}