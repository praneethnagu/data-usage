package com.iiitb.datausage.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.iiitb.datausage.Model.StaticDataModel;
import com.iiitb.datausage.R;

import java.util.ArrayList;

/**
 * Created by ABHIJNU on 11/8/2017.
 */

public class MobileBarFragment extends Fragment {

    private long[] yValues = {StaticDataModel.mobileTX, StaticDataModel.mobileRX};
    private String[] xValues = {"Data Transmitted", "Data Received"};

    public MobileBarFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mobile_bar, container, false);

        BarChart chart = (BarChart) view.findViewById(R.id.mobileBarchart);

        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("");
        chart.animateXY(1400, 1400);
        chart.invalidate();

        if (container != null) {
            container.removeAllViews();
        }

        return view;
    }

    private ArrayList<IBarDataSet> getDataSet() {
        ArrayList<IBarDataSet> dataSets = null;

        yValues[0] = StaticDataModel.mobileTX;
        yValues[1] = StaticDataModel.mobileRX;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        long x = yValues[0]/(1024*1024);
        BarEntry v1e1 = new BarEntry(x, 0); // Jan
        valueSet1.add(v1e1);
        //BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        //valueSet1.add(v1e2);
        //BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
        //valueSet1.add(v1e3);
        //BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
        //valueSet1.add(v1e4);
        //BarEntry v1e5 = new BarEntry(90.000f, 4); // May
        //valueSet1.add(v1e5);
        //BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
        //valueSet1.add(v1e6);

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        long y = yValues[1]/(1024*1024);
        BarEntry v2e1 = new BarEntry(y, 0); // Jan
        valueSet2.add(v2e1);
        //BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
        //valueSet2.add(v2e2);
        //BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
        //valueSet2.add(v2e3);
        //BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
        //valueSet2.add(v2e4);
        //BarEntry v2e5 = new BarEntry(20.000f, 4); // May
        //valueSet2.add(v2e5);
        //BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
        //valueSet2.add(v2e6);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Data Transmitted");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Data Received");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }


    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("in MB");
        //xAxis.add("FEB");
        //xAxis.add("MAR");
        //xAxis.add("APR");
        //xAxis.add("MAY");
        //xAxis.add("JUN");
        return xAxis;
    }

    }
