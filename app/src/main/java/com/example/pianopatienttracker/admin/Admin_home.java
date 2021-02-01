package com.example.pianopatienttracker.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.pianopatienttracker.admin.dashboard.DashboardFragment;
import com.example.pianopatienttracker.admin.ranking.RankingFragment;
import com.example.pianopatienttracker.admin.sectors.SectorsFragment;
import com.example.pianopatienttracker.admin.target.TargetFragment;
import com.example.pianopatienttracker.admin.users.UsersFragment;
import com.example.pianopatienttracker.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Admin_home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    public void setContentFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contentLayout, fragment);
        fragmentTransaction.commit();
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
        }else if (id == R.id.navigation_rank) {
            setContentFragment(new RankingFragment());
        }else if (id == R.id.navigation_sector) {
            setContentFragment(new SectorsFragment());
        }else if (id == R.id.navigation_target) {
            setContentFragment(new TargetFragment());
        }else if (id == R.id.navigation_users) {
            setContentFragment(new UsersFragment());
        }

        return true;
    }
}