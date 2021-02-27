package com.cat.pianopatienttracker.admin_manager_regional.shared.bottom_sheets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.admin_manager_regional.shared.spinners.Brand_item;
import com.cat.pianopatienttracker.admin_manager_regional.shared.spinners.BrandsSpinnerAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class BottomSheet_brand_fragment extends BottomSheetDialogFragment {

    boolean bIsInit = true;
    ArrayList<Brand_item> brands_list;
    int selectedBrandIndex;

//    private ItemClickListener mListener;

//    public static BottomSheet_country_brand_fragment newInstance() {
//        return new BottomSheet_country_brand_fragment();
//    }

    public BottomSheet_brand_fragment(
            ArrayList<Brand_item> brands_list, int selectedBrandIndex) {
        this.brands_list = brands_list;
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
                int selectedBrandIndex = brand_spinner.getSelectedItemPosition();
                sendBackResult(selectedBrandIndex);
            }
        });


        initBrandsSpinner();


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




    public void initBrandsSpinner() {

        BrandsSpinnerAdapter brandsSpinnerAdapter = new BrandsSpinnerAdapter(getActivity(), brands_list);
        brand_spinner.setAdapter(brandsSpinnerAdapter);
        if (bIsInit) {
            brand_spinner.setSelection(selectedBrandIndex);
            bIsInit = false;
        }

    }


    public interface ItemClickListener {
        void brandOnItemClick(int selectedBrandIndex);
    }

    public void sendBackResult(int selectedBrandIndex) {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        ItemClickListener listener = (ItemClickListener) getTargetFragment();
        listener.brandOnItemClick(selectedBrandIndex);
        dismiss();
    }
}
