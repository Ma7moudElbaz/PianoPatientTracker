package com.cat.pianopatienttracker.flm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;

import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.flm.profile.FlmProfileFragment;
import com.cat.pianopatienttracker.flm.progress.FlmProgressFragment;
import com.cat.pianopatienttracker.flm.ranking.FlmRankingFragment;
import com.cat.pianopatienttracker.flm.requests.FlmRequestsFragment;
import com.cat.pianopatienttracker.rep.home.RepHomeFragment;
import com.cat.pianopatienttracker.rep.profile.RepProfileFragment;
import com.cat.pianopatienttracker.rep.ranking.RepRankingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Flm_home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

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
        setContentView(R.layout.activity_flm_home);

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

        if (id == R.id.navigation_dashboard) {
            setContentFragment(new RepHomeFragment());
        } else if (id == R.id.navigation_rank) {
            setContentFragment(new FlmRankingFragment());
        } else if (id == R.id.navigation_progress) {
            setContentFragment(new FlmProgressFragment());
        }else if (id == R.id.navigation_requests) {
            setContentFragment(new FlmRequestsFragment());
        }else if (id == R.id.navigation_profile) {
            setContentFragment(new FlmProfileFragment());
        }
        return true;
    }
}