package com.cat.pianopatienttracker.flm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.cat.pianopatienttracker.LoginActivity;
import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.admin_manager_regional.dashboard.brand.DashboardFragment;
import com.cat.pianopatienttracker.admin_manager_regional.dashboard.regional.DashboardRegionalFragment;
import com.cat.pianopatienttracker.admin_manager_regional.shared.Brand_item;
import com.cat.pianopatienttracker.admin_manager_regional.shared.City_item;
import com.cat.pianopatienttracker.admin_manager_regional.shared.Country_item;
import com.cat.pianopatienttracker.admin_manager_regional.shared.Region_item;
import com.cat.pianopatienttracker.admin_manager_regional.shared.Sector_item;
import com.cat.pianopatienttracker.flm.dashboard.FlmDashboardFragment;
import com.cat.pianopatienttracker.flm.profile.FlmProfileFragment;
import com.cat.pianopatienttracker.flm.progress.FlmProgressFragment;
import com.cat.pianopatienttracker.flm.ranking.FlmRankingFragment;
import com.cat.pianopatienttracker.flm.requests.FlmRequestsFragment;
import com.cat.pianopatienttracker.network.Webservice;
import com.cat.pianopatienttracker.rep.home.RepHomeFragment;
import com.cat.pianopatienttracker.rep.profile.RepProfileFragment;
import com.cat.pianopatienttracker.rep.ranking.RepRankingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Flm_home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    public void updateStatusBarColor(String color) {// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    public void setContentFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contentLayout, fragment);
        fragmentTransaction.commit();
    }


    private int selectedCountryId = 0;
    private int selectedBrandId = 0;

    int selectedCountryIndex = 0;
    int selectedBrandIndex = 0;

    public int getSelectedCountryIndex() {
        return selectedCountryIndex;
    }

    public void setSelectedCountryIndex(int selectedCountryIndex) {
        this.selectedCountryIndex = selectedCountryIndex;
        setSelectedCountryId(countries_list.get(selectedCountryIndex).getId());
    }

    public int getSelectedBrandIndex() {
        return selectedBrandIndex;
    }

    public void setSelectedBrandIndex(int selectedBrandIndex) {
        this.selectedBrandIndex = selectedBrandIndex;
        setSelectedBrandId(countries_list.get(getSelectedCountryIndex()).getBrand_list().get(selectedBrandIndex).getId());
    }

    private String accessToken, userName, role, roleName;

    public String getUserName() {
        return userName;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getAccessToken() {
        return accessToken;
    }


    public int getSelectedCountryId() {
        return selectedCountryId;
    }

    public void setSelectedCountryId(int selectedCountryId) {
        this.selectedCountryId = selectedCountryId;
    }

    public int getSelectedBrandId() {
        return selectedBrandId;
    }

    public void setSelectedBrandId(int selectedBrandId) {
        this.selectedBrandId = selectedBrandId;
    }

    BottomNavigationView bottomNavigationView;

    private ProgressDialog dialog;

    ArrayList<Country_item> countries_list = new ArrayList<>();
    ArrayList<Sector_item> sectors_list = new ArrayList<>();

    public ArrayList<Country_item> getCountries_list() {
        return countries_list;
    }

    public ArrayList<Sector_item> getSectors_list() {
        return sectors_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flm_home);

        accessToken = getIntent().getStringExtra("accessToken");
        userName = getIntent().getStringExtra("userName");
        role = getIntent().getStringExtra("role");
        roleName = getIntent().getStringExtra("roleName");

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);


        getMyData();
        getSectorsData();

        bottomNavigationView = findViewById(R.id.btm_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    public void getSectorsData() {
        Webservice.getInstance().getApi().getSectors(accessToken).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    JSONObject responseObject = null;
                    try {
                        responseObject = new JSONObject(response.body().string());
                        JSONObject dataObj = responseObject.getJSONObject("data");
                        JSONArray dataArr = dataObj.getJSONArray("data");
                        setSectorsList(dataArr);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {
                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    void setSectorsList(JSONArray list) {
        sectors_list.add(new Sector_item(0, "Select Sector"));
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                sectors_list.add(new Sector_item(id, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                        JSONArray countriesArr = responseObject.getJSONArray("data");
                        JSONArray brandsArr = responseObject.getJSONArray("total");
                        setCountriesList(countriesArr,brandsArr);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {
                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Network error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void setCountriesList(JSONArray countriesList,JSONArray brandsList) {
        try {

            for (int i = 0; i < countriesList.length(); i++) {
                JSONObject currentObject = countriesList.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                final JSONArray productsData = brandsList;
                final JSONArray regionsData = currentObject.getJSONArray("regions");

                ArrayList<Brand_item> brands_list = new ArrayList<>();
                ArrayList<Region_item> regions_list = new ArrayList<>();
                brands_list = setBrandsList(productsData);
                regions_list = setRegionsList(regionsData);

                countries_list.add(new Country_item(id, name, brands_list, regions_list));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSelectedCountryId(countries_list.get(0).getId());
        setSelectedBrandId(countries_list.get(0).getBrand_list().get(0).getId());


        setContentFragment(new FlmDashboardFragment());

    }

    ArrayList<Brand_item> setBrandsList(JSONArray list) {

        ArrayList<Brand_item> brands_list = new ArrayList<>();
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("brand_id");
                final String name = currentObject.getString("brand_name");
                final String imageUrl = currentObject.getString("brand_image");

                brands_list.add(new Brand_item(id, name, imageUrl));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return brands_list;
    }

    ArrayList<Region_item> setRegionsList(JSONArray list) {

        ArrayList<City_item> cities_firstItemList = new ArrayList<>();
        cities_firstItemList.add(new City_item(0, "no regions selected"));

        ArrayList<Region_item> regions_list = new ArrayList<>();
        regions_list.add(new Region_item(0, "Select Region", cities_firstItemList));
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                final JSONArray citiesData = currentObject.getJSONArray("cities");
                ArrayList<City_item> cities_list = new ArrayList<>();
                cities_list = setCitiesList(citiesData);

                regions_list.add(new Region_item(id, name, cities_list));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return regions_list;
    }

    ArrayList<City_item> setCitiesList(JSONArray list) {

        ArrayList<City_item> cities_list = new ArrayList<>();
        cities_list.add(new City_item(0, "Select City"));
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");

                cities_list.add(new City_item(id, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cities_list;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        int id = menuItem.getItemId();

        if (id == R.id.navigation_dashboard) {
            setContentFragment(new FlmDashboardFragment());
        } else if (id == R.id.navigation_rank) {
            setContentFragment(new FlmRankingFragment());
        } else if (id == R.id.navigation_progress) {
            setContentFragment(new FlmProgressFragment());
        } else if (id == R.id.navigation_requests) {
            setContentFragment(new FlmRequestsFragment());
        } else if (id == R.id.navigation_profile) {
            setContentFragment(new FlmProfileFragment());
        }
        return true;
    }
}