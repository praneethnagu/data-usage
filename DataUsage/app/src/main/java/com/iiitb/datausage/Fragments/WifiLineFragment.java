package com.iiitb.datausage.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.iiitb.datausage.Model.StaticDataModel;
import com.iiitb.datausage.Model.StaticVariables;
import com.iiitb.datausage.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ABHIJNU on 11/9/2017.
 */

public class WifiLineFragment extends Fragment {

    private LineChart mChart;

    private long[] yValues = {StaticDataModel.wifiTX, StaticDataModel.wifiRX};
    private String[] xValues = {"Data Transmitted", "Data Received"};

    public WifiLineFragment()
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
        View view = inflater.inflate(R.layout.fragment_wifi_line, container, false);

        mChart = (LineChart) view.findViewById(R.id.linechart);

        // add data
        setData();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        // no description text
        mChart.setDescription("Line Chart");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");
        // enable touch gestures
        mChart.setTouchEnabled(true);
        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.animateXY(1400, 1400);

        return view;
    }

    private ArrayList<String> setXAxisValues(){
        ArrayList<String> xVals = new ArrayList<>();
        xVals.add("Mon");
        xVals.add("Tue");
        xVals.add("Wed");
        xVals.add("Thu");
        xVals.add("Fri");
        xVals.add("Sat");
        xVals.add("Sun");

        return xVals;
    }

    private ArrayList<Entry> setYAxisValues(){

        long ms = SystemClock.elapsedRealtime();
        long hours = ms / (1000 * 60 * 60);
        long total = StaticDataModel.wifiTX + StaticDataModel.wifiRX;

        String result = StaticVariables.dataConverter(total);
        String tokens[] = {"0", "0"};
        if(result.length() > 0)
        {
            tokens = result.split(" ");
        }

        double value = Double.parseDouble(tokens[0]);
        String unit = tokens[1];
        int days = (int) (hours/24);
        double week = 7*value/days;
        Double[] weekdata = new Double[7];
        weekdata[0] = 0.12 * week;
        weekdata[1] = 0.09 * week;
        weekdata[2] = 0.08 * week;
        weekdata[3] = 0.10 * week;
        weekdata[4] = 0.15 * week;
        weekdata[5] = 0.22 * week;
        weekdata[6] = 0.24 * week;
        //weekData(hours, value);
        ArrayList<Entry> yVals = new ArrayList<>();
        yVals.add(new Entry(weekdata[0].floatValue(), 0));
        yVals.add(new Entry(weekdata[1].floatValue(), 1));
        yVals.add(new Entry(weekdata[2].floatValue(), 2));
        yVals.add(new Entry(weekdata[3].floatValue(), 3));
        yVals.add(new Entry(weekdata[4].floatValue(), 4));
        yVals.add(new Entry(weekdata[5].floatValue(), 5));
        yVals.add(new Entry(weekdata[6].floatValue(), 6));

        return yVals;
    }


    private void setData() {
        ArrayList<String> xVals = setXAxisValues();

        ArrayList<Entry> yVals = setYAxisValues();

        LineDataSet set1;

        // create a dataset and give it a type
        long total = StaticDataModel.wifiTX + StaticDataModel.wifiRX;

        String result = StaticVariables.dataConverter(total);
        String tokens[] = {"0", "0"};
        if(result.length() > 0)
        {
            tokens = result.split(" ");
        }

        double value = Double.parseDouble(tokens[0]);
        String unit = tokens[1];

        set1 = new LineDataSet(yVals, "WiFi (" + unit + ")");
        set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        // set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.setData(data);

    }
}
