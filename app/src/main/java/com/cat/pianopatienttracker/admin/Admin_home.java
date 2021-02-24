package com.cat.pianopatienttracker.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.cat.pianopatienttracker.LoginActivity;
import com.cat.pianopatienttracker.admin.dashboard.brand.Brand_item;
import com.cat.pianopatienttracker.admin.dashboard.brand.DashboardFragment;
import com.cat.pianopatienttracker.admin.dashboard.regional.DashboardRegionalFragment;
import com.cat.pianopatienttracker.admin.ranking.RankingFragment;
import com.cat.pianopatienttracker.admin.progress.ProgressFragment;
import com.cat.pianopatienttracker.admin.users.UsersFragment;
import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.network.Webservice;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


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
        setSelectedCountryId(countriesBrands_list.get(selectedCountryIndex).getId());
    }

    public int getSelectedBrandIndex() {
        return selectedBrandIndex;
    }

    public void setSelectedBrandIndex(int selectedBrandIndex) {
        this.selectedBrandIndex = selectedBrandIndex;
        setSelectedBrandId(countriesBrands_list.get(getSelectedCountryIndex()).getBrand_list().get(selectedBrandIndex).getId());
    }

    //admin
//    private String role = "admin";
//    private String accessToken = "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9kZXYucHRyYWNrZXIub3JnXC9hcGlcL2F1dGhcL2xvZ2luIiwiaWF0IjoxNjEzOTkzMDY1LCJleHAiOjE2MTQ0MjUwNjUsIm5iZiI6MTYxMzk5MzA2NSwianRpIjoiQXZHdHhuQzhDYlVPSnYwVyIsInN1YiI6NiwicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.u8piW4bYBzC2TKLbnakCnvBiHGDH3cOPCOPc17ZO8-I";
    //regional
//    private String role = "regional";
//    private String accessToken = "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9kZXYucHRyYWNrZXIub3JnXC9hcGlcL2F1dGhcL2xvZ2luIiwiaWF0IjoxNjE0MDc2NTM0LCJleHAiOjE2MTQ1MDg1MzQsIm5iZiI6MTYxNDA3NjUzNCwianRpIjoibHVwcXNmcXBMTnluUEExZSIsInN1YiI6NywicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.s_km3VMyEixccz6LbQ2BAOcrVWqtSGP6Wx-2BoH7ffc";
    //manager
    private String role = "manager";
    private String accessToken = "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9kZXYucHRyYWNrZXIub3JnXC9hcGlcL2F1dGhcL2xvZ2luIiwiaWF0IjoxNjE0MDk2OTg2LCJleHAiOjE2MTQ1Mjg5ODYsIm5iZiI6MTYxNDA5Njk4NiwianRpIjoiUTVxbTdiRmthb0ZtNHgwdyIsInN1YiI6MywicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.sLRE_VnEMe1uv77kbF0wYch0ocGOjOAcpoCg3XfNmYk";

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public void setBottomNavigationView(BottomNavigationView bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
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

    ArrayList<Country_Brand_item> countriesBrands_list = new ArrayList<>();

    public ArrayList<Country_Brand_item> getCountriesBrands_list() {
        return countriesBrands_list;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);


        getMyData();

//        setContentFragment(new DashboardFragment());
//        setContentFragment(new DashboardRegionalFragment());
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
            setContentFragment(new UsersFragment());
        }

        return true;
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
                        setCountriesBrandsList(dataArr);

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

    public void setCountriesBrandsList(JSONArray list) {
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String name = currentObject.getString("name");
                final JSONArray productsData = currentObject.getJSONArray("products");

                ArrayList<Brand_item> brands_list = new ArrayList<>();
                brands_list = setBrandsList(productsData);

                countriesBrands_list.add(new Country_Brand_item(id, name, brands_list));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSelectedCountryId(countriesBrands_list.get(0).getId());
        setSelectedBrandId(countriesBrands_list.get(0).getBrand_list().get(0).getId());

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
}