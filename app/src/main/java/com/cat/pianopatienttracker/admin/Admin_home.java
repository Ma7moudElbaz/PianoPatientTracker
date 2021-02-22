package com.cat.pianopatienttracker.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.cat.pianopatienttracker.admin.dashboard.DashboardFragment;
import com.cat.pianopatienttracker.admin.ranking.RankingFragment;
import com.cat.pianopatienttracker.admin.sectors.SectorsFragment;
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
        bottomNavigationView = findViewById(R.id.btm_nav);


        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        int id = menuItem.getItemId();

        if (id == R.id.navigation_dashboard) {
            setContentFragment(new DashboardFragment());
        } else if (id == R.id.navigation_rank) {
            setContentFragment(new RankingFragment());
        } else if (id == R.id.navigation_sector) {
            setContentFragment(new SectorsFragment());
        } else if (id == R.id.navigation_target) {
            setContentFragment(new TargetFragment());
        } else if (id == R.id.navigation_users) {
            setContentFragment(new UsersFragment());
        }

        return true;
    }
}