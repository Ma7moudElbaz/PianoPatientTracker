package com.cat.pianopatienttracker.regional_product;

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
import com.cat.pianopatienttracker.regional_product.dashboard.brand.DashboardFragment;
import com.cat.pianopatienttracker.regional_product.dashboard.regional.DashboardRegionalFragment;
import com.cat.pianopatienttracker.regional_product.ranking.RankingFragment;
import com.cat.pianopatienttracker.regional_product.progress.ProgressFragment;
import com.cat.pianopatienttracker.regional_product.shared.Brand_item;
import com.cat.pianopatienttracker.regional_product.shared.City_item;
import com.cat.pianopatienttracker.regional_product.shared.Country_item;
import com.cat.pianopatienttracker.regional_product.profile.ProfileFragment;
import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.network.Webservice;
import com.cat.pianopatienttracker.regional_product.shared.Region_item;
import com.cat.pianopatienttracker.regional_product.shared.Sector_item;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

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

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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
        setContentView(R.layout.activity_admin_home);

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        int id = menuItem.getItemId();

        if (id == R.id.navigation_dashboard) {
            if (role.equals("manager")) {
                setContentFragment(new DashboardFragment());
            } else if (role.equals("regional")) {
                setContentFragment(new DashboardRegionalFragment());
            } else if (role.equals("admin")) {
                setContentFragment(new DashboardFragment());
            }
        } else if (id == R.id.navigation_rank) {
            setContentFragment(new RankingFragment());
        } else if (id == R.id.navigation_target) {
            setContentFragment(new ProgressFragment());
        } else if (id == R.id.navigation_users) {
            setContentFragment(new ProfileFragment());
        }

        return true;
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
                        JSONArray dataArr = responseObject.getJSONArray("data");
                        setCountriesList(dataArr);

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

    public void setCountriesList(JSONArray list) {
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                final JSONArray productsData = currentObject.getJSONArray("products");
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

        if (role.equals("manager")) {
            setContentFragment(new DashboardFragment());
        } else if (role.equals("regional")) {
            setContentFragment(new DashboardRegionalFragment());
        } else if (role.equals("admin")) {
            setContentFragment(new DashboardFragment());
        }
    }

    ArrayList<Brand_item> setBrandsList(JSONArray list) {

        ArrayList<Brand_item> brands_list = new ArrayList<>();
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final JSONObject brands = currentObject.getJSONObject("brands");
                final int id = brands.getInt("id");
                final String name = brands.getString("name");
                final String imageUrl = brands.getString("image");

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
}