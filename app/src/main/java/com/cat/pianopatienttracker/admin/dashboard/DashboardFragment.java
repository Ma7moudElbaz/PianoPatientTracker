package com.cat.pianopatienttracker.admin.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.cat.pianopatienttracker.admin.FlagSpinnerAdapter;
import com.cat.pianopatienttracker.admin.ProductSpinnerAdapter;
import com.cat.pianopatienttracker.R;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import java.text.DecimalFormat;

import co.lujun.androidtagview.TagContainerLayout;

public class DashboardFragment extends Fragment {


    String[] countryNames = {"SA", "SA"};
    int flags[] = {R.drawable.flag_sa, R.drawable.flag_sa};
    String[] tagList = new String[]{"Hello1", "Hello2", "Hello3", "Hello2", "Hello3", "Hello2", "Hello3"};

    int products[] = {R.drawable.jakavi_logo, R.drawable.revolade_logo, R.drawable.adakveo_logo
            , R.drawable.tasigna_logo, R.drawable.palbociclib_logo, R.drawable.abemaciclib_logo
            , R.drawable.piqary_logo, R.drawable.afinitor_logo, R.drawable.kisqali_logo};


    private static DecimalFormat df2 = new DecimalFormat("#.##");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    Spinner flagSpinner, productSpinner;
    TagContainerLayout tagView;
    AnimatedPieView productChart,dosesChart;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        flagSpinner = view.findViewById(R.id.flagSpinner);
        productSpinner = view.findViewById(R.id.productSpinner);
        tagView = view.findViewById(R.id.tagview);

        FlagSpinnerAdapter flagSpinnerAdapter = new FlagSpinnerAdapter(view.getContext(), flags, countryNames);
        flagSpinner.setAdapter(flagSpinnerAdapter);

        ProductSpinnerAdapter productSpinnerAdapter = new ProductSpinnerAdapter(view.getContext(), products);
        productSpinner.setAdapter(productSpinnerAdapter);

        tagView.setTags(tagList);


        productChart = view.findViewById(R.id.productChart);
        AnimatedPieViewConfig productChartConfig = new AnimatedPieViewConfig();


        productChartConfig
                .animatePie(true)
                .strokeMode(true)
                .floatUpDuration(500)
                .floatDownDuration(500)
                .splitAngle(1f)
                .duration(1000)
                .drawText(true)
                .textSize(34)
                .autoSize(true)
                .pieRadius(200);


        productChartConfig.addData(new SimplePieInfo(50, ContextCompat.getColor(getActivity(), R.color.colorAccent), "Jakavi : " + df2.format(50) + " %"))
                .addData(new SimplePieInfo(20, ContextCompat.getColor(getActivity(), R.color.dark_gray), "Others : " + df2.format(20) + " %"));

        productChart.applyConfig(productChartConfig);
        productChart.start();



        dosesChart = view.findViewById(R.id.dosesChart);
        AnimatedPieViewConfig dosesChartConfig = new AnimatedPieViewConfig();
        dosesChartConfig
                .animatePie(true)
                .strokeMode(true)
                .floatUpDuration(500)
                .floatDownDuration(500)
                .splitAngle(1f)
                .duration(1000)
                .drawText(true)
                .textSize(34)
                .autoSize(true)
                .pieRadius(200);


        dosesChartConfig.addData(new SimplePieInfo(35, ContextCompat.getColor(getActivity(), R.color.colorAccent), "dose 5 : " + df2.format(25) + " %"))
                .addData(new SimplePieInfo(25, ContextCompat.getColor(getActivity(), R.color.dark_gray), "dose 10 : " + df2.format(25) + " %"))
                .addData(new SimplePieInfo(95, ContextCompat.getColor(getActivity(), R.color.red), "dose 15 : " + df2.format(95) + " %"))
                .addData(new SimplePieInfo(36, ContextCompat.getColor(getActivity(), R.color.light_blue), "dose 20 : " + df2.format(36) + " %"))

        ;

        dosesChart.applyConfig(dosesChartConfig);
        dosesChart.start();


    }


}