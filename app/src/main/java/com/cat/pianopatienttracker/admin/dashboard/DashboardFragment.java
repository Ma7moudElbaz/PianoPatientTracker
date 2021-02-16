package com.cat.pianopatienttracker.admin.dashboard;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

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

    ArrayList<Brand_item> brands_list = new ArrayList<>();
    ArrayList<Country_item> countries_list = new ArrayList<>();


    String[] countryNames = {"SA", "SA"};
    int flags[] = {R.drawable.flag_sa, R.drawable.flag_sa};
    String[] tagList = new String[]{"Hello1", "Hello2", "Hello3", "Hello4", "Hello5", "Hello6", "Hello7"};

    int products[] = {R.drawable.jakavi_logo, R.drawable.revolade_logo, R.drawable.adakveo_logo
            , R.drawable.tasigna_logo, R.drawable.palbociclib_logo, R.drawable.abemaciclib_logo
            , R.drawable.piqary_logo, R.drawable.afinitor_logo, R.drawable.kisqali_logo};


    private static DecimalFormat df2 = new DecimalFormat("#.##");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    Spinner countriesSpinner, productSpinner;
    TagContainerLayout tagView;
    AnimatedPieView productChart, dosesChart;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);

        accessToken = "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9kZXYucHRyYWNrZXIub3JnXC9hcGlcL2F1dGhcL2xvZ2luIiwiaWF0IjoxNjEzNDg5NTgxLCJleHAiOjE2MTM0OTMxODEsIm5iZiI6MTYxMzQ4OTU4MSwianRpIjoiOHhGSHk2b2Izc3pFZUd0RSIsInN1YiI6MSwicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.Mx4LiVf1ekU-DPyjlW9xSx7EJuUS_NzifyJGPzusWAk";

        countriesSpinner = view.findViewById(R.id.countriesSpinner);
        productSpinner = view.findViewById(R.id.productSpinner);
        tagView = view.findViewById(R.id.tagview);
        tagView.setTheme(ColorFactory.NONE);
        tagView.setTags(tagList);

        tagView.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                resetTags(position);


            }

            void resetTags(int position) {
                for (int i = 0; i < tagList.length; i++) {
                    if (i == position) {
                        tagView.getTagView(position).setTagBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                        tagView.getTagView(position).setTagTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    } else if (i != position) {
                        tagView.getTagView(i).setTagBackgroundColor(ContextCompat.getColor(getActivity(), R.color.more_light_gray));
                        tagView.getTagView(i).setTagTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
                    }
                }
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


        productChart = view.findViewById(R.id.productChart);
        AnimatedPieViewConfig productChartConfig = new AnimatedPieViewConfig();


        productChartConfig
                .animatePie(true)
                .strokeMode(true)
                .floatUpDuration(500)
                .floatDownDuration(500)
                .splitAngle(1f)
                .duration(1000)
                .drawText(true)
                .textSize(34)
                .autoSize(true)
                .pieRadius(200);


        productChartConfig.addData(new SimplePieInfo(50, ContextCompat.getColor(getActivity(), R.color.colorAccent), "Jakavi : " + df2.format(50) + " %"))
                .addData(new SimplePieInfo(20, ContextCompat.getColor(getActivity(), R.color.dark_gray), "Others : " + df2.format(20) + " %"));

        productChart.applyConfig(productChartConfig);
        productChart.start();


        dosesChart = view.findViewById(R.id.dosesChart);
        AnimatedPieViewConfig dosesChartConfig = new AnimatedPieViewConfig();
        dosesChartConfig
                .animatePie(true)
                .strokeMode(true)
                .floatUpDuration(500)
                .floatDownDuration(500)
                .splitAngle(1f)
                .duration(1000)
                .drawText(true)
                .textSize(34)
                .autoSize(true)
                .pieRadius(200);


        dosesChartConfig.addData(new SimplePieInfo(35, ContextCompat.getColor(getActivity(), R.color.colorAccent), "dose 5 : " + df2.format(25) + " %"))
                .addData(new SimplePieInfo(25, ContextCompat.getColor(getActivity(), R.color.dark_gray), "dose 10 : " + df2.format(25) + " %"))
                .addData(new SimplePieInfo(95, ContextCompat.getColor(getActivity(), R.color.red), "dose 15 : " + df2.format(95) + " %"))
                .addData(new SimplePieInfo(36, ContextCompat.getColor(getActivity(), R.color.light_blue), "dose 20 : " + df2.format(36) + " %"));

        dosesChart.applyConfig(dosesChartConfig);
        dosesChart.start();


        getBrands();
        getCountries();

    }


    public void getBrands() {
        dialog.show();

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
                } else {

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
        productSpinner.setAdapter(brandsSpinnerAdapter);

    }


    public void getCountries() {
//        dialog.show();

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