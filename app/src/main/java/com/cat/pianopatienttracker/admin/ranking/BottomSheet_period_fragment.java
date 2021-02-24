package com.cat.pianopatienttracker.admin.ranking;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.cat.pianopatienttracker.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheet_period_fragment extends BottomSheetDialogFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_period, container, false);
    }

    TextView yearlyBtn, monthlyBtn, ytlBtn, submitBtn;
    Spinner yearsSpinner, monthsSpinner;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        yearlyBtn = view.findViewById(R.id.yearly_btn);
        monthlyBtn = view.findViewById(R.id.monthly_btn);
        ytlBtn = view.findViewById(R.id.ytl_btn);

        submitBtn = view.findViewById(R.id.submit_btn);

        yearsSpinner = view.findViewById(R.id.years_spinner);
        monthsSpinner = view.findViewById(R.id.months_spinner);

        yearlyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearlyBtnClicked();
            }
        });


        ytlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ytlBtnClicked();
            }
        });

        monthlyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthlyBtnClicked();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void yearlyBtnClicked() {
        setButtonSelected(0);
        yearsSpinner.setVisibility(View.VISIBLE);
        monthsSpinner.setVisibility(View.GONE);
    }

    private void ytlBtnClicked() {
        setButtonSelected(1);
        yearsSpinner.setVisibility(View.GONE);
        monthsSpinner.setVisibility(View.GONE);

    }

    private void monthlyBtnClicked() {
        setButtonSelected(2);
        yearsSpinner.setVisibility(View.GONE);
        monthsSpinner.setVisibility(View.VISIBLE);

    }

    private void setButtonSelected(int buttonClicked) {

        yearlyBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
        yearlyBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.more_light_gray));

        ytlBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
        ytlBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.more_light_gray));

        monthlyBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
        monthlyBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.more_light_gray));


        switch (buttonClicked) {
            case 0:
                yearlyBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                yearlyBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_blue));
                return;
            case 1:
                ytlBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                ytlBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_blue));
                return;
            case 2:
                monthlyBtn.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                monthlyBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_blue));
                return;
        }

    }

}
