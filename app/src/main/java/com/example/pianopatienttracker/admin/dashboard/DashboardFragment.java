package com.example.pianopatienttracker.admin.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.example.pianopatienttracker.admin.FlagSpinnerAdapter;
import com.example.pianopatienttracker.admin.ProductSpinnerAdapter;
import com.example.pianopatienttracker.R;

public class DashboardFragment extends Fragment {

    String[] countryNames = {"SA", "SA"};
    int flags[] = {R.drawable.flag_sa, R.drawable.flag_sa};

    int products[] = {R.drawable.jakavi_logo, R.drawable.revolade_logo, R.drawable.adakveo_logo
            , R.drawable.tasigna_logo, R.drawable.palbociclib_logo, R.drawable.abemaciclib_logo
            , R.drawable.piqary_logo,R.drawable.afinitor_logo, R.drawable.kisqali_logo};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    Spinner flagSpinner, productSpinner;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        flagSpinner = view.findViewById(R.id.flagSpinnner);
        productSpinner = view.findViewById(R.id.productSpinner);

        FlagSpinnerAdapter flagSpinnerAdapter = new FlagSpinnerAdapter(view.getContext(), flags, countryNames);
        flagSpinner.setAdapter(flagSpinnerAdapter);

        ProductSpinnerAdapter productSpinnerAdapter = new ProductSpinnerAdapter(view.getContext(), products);
        productSpinner.setAdapter(productSpinnerAdapter);
    }

}