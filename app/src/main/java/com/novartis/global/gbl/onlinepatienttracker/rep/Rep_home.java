package com.novartis.global.gbl.onlinepatienttracker.rep;

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

import com.novartis.global.gbl.onlinepatienttracker.login.LoginActivity;
import com.novartis.global.gbl.onlinepatienttracker.R;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.spinners.Brand_item;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.spinners.City_item;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.spinners.Country_item;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.spinners.Region_item;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.spinners.Sector_item;
import com.novartis.global.gbl.onlinepatienttracker.network.Webservice;
import com.novartis.global.gbl.onlinepatienttracker.rep.home.RepHomeFragment;
import com.novartis.global.gbl.onlinepatienttracker.rep.profile.RepProfileFragment;
import com.novartis.global.gbl.onlinepatienttracker.rep.ranking.RepRankingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Rep_home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


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

    String selectedCountryName = "";
    String selectedBrandName = "";

    public String getSelectedCountryName() {
        return selectedCountryName;
    }

    public void setSelectedCountryName(String selectedCountryName) {
        this.selectedCountryName = selectedCountryName;
    }

    public String getSelectedBrandName() {
        return selectedBrandName;
    }

    public void setSelectedBrandName(String selectedBrandName) {
        this.selectedBrandName = selectedBrandName;
    }

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
    int userId;

    public int getUserId() {
        return userId;
    }

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
        setContentView(R.layout.activity_rep_home);

        accessToken = getIntent().getStringExtra("accessToken");
        userId = getIntent().getIntExtra("userId",0);
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
                        setCountriesList(countriesArr, brandsArr);

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

    public void setCountriesList(JSONArray countriesList, JSONArray brandsList) {
        try {

            for (int i = 0; i < countriesList.length(); i++) {
                JSONObject currentObject = countriesList.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                final String iso = currentObject.getString("iso");
                final JSONArray productsData = brandsList;
                final JSONArray regionsData = currentObject.getJSONArray("regions");

                ArrayList<Brand_item> brands_list = new ArrayList<>();
                ArrayList<Region_item> regions_list = new ArrayList<>();
                brands_list = setBrandsList(productsData);
                regions_list = setRegionsList(regionsData);

                countries_list.add(new Country_item(id, name,iso, brands_list, regions_list));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSelectedCountryId(countries_list.get(0).getId());
        setSelectedBrandId(countries_list.get(0).getBrand_list().get(0).getId());

        setSelectedCountryBrandName();


        setContentFragment(new RepHomeFragment());

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

    void setSelectedCountryBrandName() {
         selectedCountryName = countries_list.get(selectedCountryIndex).getIso();
         selectedBrandName = countries_list.get(selectedCountryIndex).getBrand_list().get(selectedBrandIndex).getName();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.navigation_home) {
            setContentFragment(new RepHomeFragment());
        } else if (id == R.id.navigation_rank) {
            setContentFragment(new RepRankingFragment());
        } else if (id == R.id.navigation_profile) {
            setContentFragment(new RepProfileFragment());
        }
        return true;
    }

}