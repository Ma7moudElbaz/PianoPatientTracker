package com.cat.pianopatienttracker.regional_product.profile;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.adroitandroid.chipcloud.ChipCloud;
import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.regional_product.Admin_home;
import com.cat.pianopatienttracker.regional_product.dashboard.brand.Ranking_dashboard_item;
import com.cat.pianopatienttracker.regional_product.shared.Brand_item;
import com.cat.pianopatienttracker.regional_product.shared.City_item;
import com.cat.pianopatienttracker.regional_product.shared.Country_item;
import com.cat.pianopatienttracker.regional_product.shared.Region_item;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    Admin_home activity;

    TextView name, role;
    ChipCloud products, cities;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (Admin_home) getActivity();

        name = view.findViewById(R.id.name);
        role = view.findViewById(R.id.role);
        products = view.findViewById(R.id.productsChipTag);
        cities = view.findViewById(R.id.citiesChipTag);
        setData();
    }

    void setData() {
        name.setText(activity.getName());
        role.setText(activity.getRole());
        setProductCitiesData();
    }

    public void setProductCitiesData() {
        ArrayList<Country_item> country_items = activity.getCountries_list();
        for (int i = 0; i < country_items.size(); i++) {

            //set products chips
            ArrayList<Brand_item> product_items = country_items.get(i).getBrand_list();
            for (int p = 0; p < product_items.size(); p++) {
                products.addChip(product_items.get(p).getName());
            }

            //set cities chips
            ArrayList<Region_item> regions_items = country_items.get(i).getRegion_list();
            for (int r = 1; r < regions_items.size(); r++) {
                ArrayList<City_item> cities_items = regions_items.get(r).getCity_list();
                for (int c = 1; c < cities_items.size(); c++) {
                    cities.addChip(cities_items.get(c).getName());
                }
            }
        }
    }
}