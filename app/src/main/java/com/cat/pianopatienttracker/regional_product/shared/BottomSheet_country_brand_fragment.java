package com.cat.pianopatienttracker.regional_product.shared;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cat.pianopatienttracker.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class BottomSheet_country_brand_fragment extends BottomSheetDialogFragment {

    boolean cIsInit = true;
    boolean bIsInit = true;
    ArrayList<Country_Brand_item> countriesBrands_list;
    int selectedCountryIndex;
    int selectedBrandIndex;

//    private ItemClickListener mListener;

//    public static BottomSheet_country_brand_fragment newInstance() {
//        return new BottomSheet_country_brand_fragment();
//    }

    public BottomSheet_country_brand_fragment(
            ArrayList<Country_Brand_item> countriesBrands_list, int selectedCountryIndex, int selectedBrandIndex) {
        this.countriesBrands_list = countriesBrands_list;
        this.selectedCountryIndex = selectedCountryIndex;
        this.selectedBrandIndex = selectedBrandIndex;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_country_product, container, false);
    }

    Spinner country_spinner, brand_spinner;
    TextView submit_btn;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        country_spinner = view.findViewById(R.id.country_spinner);
        brand_spinner = view.findViewById(R.id.brand_spinner);
        submit_btn = view.findViewById(R.id.submit_btn);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int selectedCountryIndex = countriesBrands_list.get(country_spinner.getSelectedItemPosition()).getId();
//                int selectedBrandIndex = countriesBrands_list.get(country_spinner.getSelectedItemPosition()).getBrand_list()
//                        .get(brand_spinner.getSelectedItemPosition()).getId();
                int selectedCountryIndex = country_spinner.getSelectedItemPosition();
                int selectedBrandIndex = brand_spinner.getSelectedItemPosition();
                sendBackResult(selectedCountryIndex, selectedBrandIndex);
//                mListener.onItemClick(selectedCountryId, selectedBrandId);
            }
        });


        initCountriesSpinner();

        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initBrandsSpinner(position);
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


    public void initCountriesSpinner() {
        CountriesSpinnerAdapter countriesSpinnerAdapter = new CountriesSpinnerAdapter(getActivity(), countriesBrands_list);
        country_spinner.setAdapter(countriesSpinnerAdapter);
        if (cIsInit) {
            country_spinner.setSelection(selectedCountryIndex);
            cIsInit = false;
        }

    }

    public void initBrandsSpinner(int position) {

        BrandsSpinnerAdapter brandsSpinnerAdapter = new BrandsSpinnerAdapter(getActivity(), countriesBrands_list.get(position).getBrand_list());
        brand_spinner.setAdapter(brandsSpinnerAdapter);
        if (bIsInit) {
            brand_spinner.setSelection(selectedBrandIndex);
            bIsInit = false;
        }

    }


    public interface ItemClickListener {
        void onItemClick(int selectedCountryIndex, int selectedBrandIndex);
    }

    public void sendBackResult(int selectedCountryIndex, int selectedBrandIndex) {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        ItemClickListener listener = (ItemClickListener) getTargetFragment();
        listener.onItemClick(selectedCountryIndex, selectedBrandIndex);
        dismiss();
    }
}
