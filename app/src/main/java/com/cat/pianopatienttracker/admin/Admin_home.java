package com.cat.pianopatienttracker.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.cat.pianopatienttracker.admin.dashboard.brand.DashboardFragment;
import com.cat.pianopatienttracker.admin.dashboard.regional.DashboardRegionalFragment;
import com.cat.pianopatienttracker.admin.ranking.RankingFragment;
import com.cat.pianopatienttracker.admin.target.TargetFragment;
import com.cat.pianopatienttracker.admin.users.UsersFragment;
import com.cat.pianopatienttracker.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Admin_home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    public void setContentFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contentLayout, fragment);
        fragmentTransaction.commit();
    }

    private int selectedCountryId = 0;
    private int selectedBrandId = 0;
    private String accessToken = "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9kZXYucHRyYWNrZXIub3JnXC9hcGlcL2F1dGhcL2xvZ2luIiwiaWF0IjoxNjEzOTkzMDY1LCJleHAiOjE2MTQ0MjUwNjUsIm5iZiI6MTYxMzk5MzA2NSwianRpIjoiQXZHdHhuQzhDYlVPSnYwVyIsInN1YiI6NiwicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.u8piW4bYBzC2TKLbnakCnvBiHGDH3cOPCOPc17ZO8-I";
//    private String accessToken = "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9kZXYucHRyYWNrZXIub3JnXC9hcGlcL2F1dGhcL2xvZ2luIiwiaWF0IjoxNjE0MDc2NTM0LCJleHAiOjE2MTQ1MDg1MzQsIm5iZiI6MTYxNDA3NjUzNCwianRpIjoibHVwcXNmcXBMTnluUEExZSIsInN1YiI6NywicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.s_km3VMyEixccz6LbQ2BAOcrVWqtSGP6Wx-2BoH7ffc";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        setContentFragment(new DashboardFragment());
//        setContentFragment(new DashboardRegionalFragment());
        bottomNavigationView = findViewById(R.id.btm_nav);


        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        int id = menuItem.getItemId();

        if (id == R.id.navigation_dashboard) {
            setContentFragment(new DashboardFragment());
//            setContentFragment(new DashboardRegionalFragment());
        } else if (id == R.id.navigation_rank) {
            setContentFragment(new RankingFragment());
        } else if (id == R.id.navigation_target) {
            setContentFragment(new TargetFragment());
        } else if (id == R.id.navigation_users) {
            setContentFragment(new UsersFragment());
        }

        return true;
    }
}