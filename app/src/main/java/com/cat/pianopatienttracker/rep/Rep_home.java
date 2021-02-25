package com.cat.pianopatienttracker.rep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;

import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.admin_manager_regional.dashboard.brand.DashboardFragment;
import com.cat.pianopatienttracker.admin_manager_regional.dashboard.regional.DashboardRegionalFragment;
import com.cat.pianopatienttracker.admin_manager_regional.profile.ProfileFragment;
import com.cat.pianopatienttracker.admin_manager_regional.progress.ProgressFragment;
import com.cat.pianopatienttracker.admin_manager_regional.ranking.RankingFragment;
import com.cat.pianopatienttracker.rep.home.RepHomeFragment;
import com.cat.pianopatienttracker.rep.profile.RepProfileFragment;
import com.cat.pianopatienttracker.rep.ranking.RepRankingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Rep_home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    public void setContentFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contentLayout, fragment);
        fragmentTransaction.commit();
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

    private String accessToken, userName, role, roleName;
    BottomNavigationView bottomNavigationView;

    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_home);

        accessToken = getIntent().getStringExtra("accessToken");
        userName = getIntent().getStringExtra("userName");
        role = getIntent().getStringExtra("role");
        roleName = getIntent().getStringExtra("roleName");


        bottomNavigationView = findViewById(R.id.btm_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
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