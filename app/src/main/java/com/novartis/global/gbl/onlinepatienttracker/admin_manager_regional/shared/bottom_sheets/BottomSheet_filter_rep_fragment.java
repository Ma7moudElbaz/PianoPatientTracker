package com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.bottom_sheets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.novartis.global.gbl.onlinepatienttracker.R;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.spinners.CitiesSpinnerAdapter;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.spinners.Country_item;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.spinners.RegionsSpinnerAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BottomSheet_filter_rep_fragment extends BottomSheetDialogFragment {

    ArrayList<Country_item> countries_list;
    int selectedCountryIndex;

//    private ItemClickListener mListener;

//    public static BottomSheet_country_brand_fragment newInstance() {
//        return new BottomSheet_country_brand_fragment();
//    }

    public BottomSheet_filter_rep_fragment(
            ArrayList<Country_item> countries_list, int selectedCountryIndex) {
        this.countries_list = countries_list;
        this.selectedCountryIndex = selectedCountryIndex;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_filter_reps, container, false);
    }

    Spinner region_spinner, city_spinner;
    EditText search;
    TextView submit_btn;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search = view.findViewById(R.id.search);
        region_spinner = view.findViewById(R.id.region_spinner);
        city_spinner = view.findViewById(R.id.city_spinner);
        submit_btn = view.findViewById(R.id.submit_btn);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<>();
                int selectedRegionPosition =region_spinner.getSelectedItemPosition();
                int selectedCityPosition =city_spinner.getSelectedItemPosition();
                if (search.getText().length()!=0){
                    map.put("name",search.getText().toString());
                }
                if (selectedRegionPosition!=0){
                    String regionIdStr=String.valueOf(countries_list.get(selectedCountryIndex).getRegion_list().get(selectedRegionPosition).getId());
                    map.put("region",regionIdStr);
                }
                if (selectedCityPosition!=0){
                    String cityIdStr=String.valueOf(countries_list.get(selectedCountryIndex).getRegion_list().get(selectedRegionPosition).getCity_list().get(selectedCityPosition).getId());
                    map.put("city",cityIdStr);
                }
                sendBackResult(map);
            }
        });


        initRegionsSpinner();

        region_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initCitiesSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        if (context instanceof ItemClickListener) {
//            mListener = (ItemClickListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement ItemClickListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }


    public void initRegionsSpinner() {
        RegionsSpinnerAdapter regionsSpinnerAdapter = new RegionsSpinnerAdapter(getActivity(), countries_list.get(selectedCountryIndex).getRegion_list());
        region_spinner.setAdapter(regionsSpinnerAdapter);


    }

    public void initCitiesSpinner(int position) {

        CitiesSpinnerAdapter citiesSpinnerAdapter = new CitiesSpinnerAdapter(getActivity(), countries_list.get(selectedCountryIndex).getRegion_list().get(position).getCity_list());
        city_spinner.setAdapter(citiesSpinnerAdapter);


    }


    public interface ItemClickListener {
        void repFilterOnItemClick(Map<String, String> filterMap);
    }

    public void sendBackResult(Map<String, String> filterMap) {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        ItemClickListener listener = (ItemClickListener) getTargetFragment();
        listener.repFilterOnItemClick(filterMap);
        dismiss();
    }
}
