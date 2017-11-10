package com.iiitb.datausage.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.iiitb.datausage.Model.StaticDataModel;
import com.iiitb.datausage.Model.StaticVariables;
import com.iiitb.datausage.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MobileFragment extends Fragment
{
    TextView tx;
    TextView rx;

    //Pie Chart Variables
    PieChart mChart;
    // we're going to display pie chart for Wifi and Mobile Data Usage
    private long[] yValues = {StaticDataModel.mobileTX, StaticDataModel.mobileRX};
    private String[] xValues = {"Data Transmitted", "Data Received"};

    // colors for different sections in pieChart
    public static  final int[] MY_COLORS =
    {
            Color.rgb(255,105,180), Color.rgb(0,191,255) ,Color.rgb(220,20,60), Color.rgb(65,105,225), Color.rgb(255,255,51)
    };


    public MobileFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_mobile, container, false);

        //Piechart code
        mChart = (PieChart) view.findViewById(R.id.mobilePieChart);
        //   mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setRotationEnabled(true);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h)
            {
                // display msg when value selected
                if (e == null)
                    return;

                Toast.makeText(getActivity(), xValues[e.getXIndex()] + " is " + e.getVal() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected()
            {

            }
        });

        // setting sample Data for Pie Chart
        setDataForPieChart();

        tx = (TextView) view.findViewById(R.id.tx_label);
        rx = (TextView) view.findViewById(R.id.rx_label);

        tx.setText(StaticVariables.dataConverter(StaticDataModel.mobileTX));
        rx.setText(StaticVariables.dataConverter(StaticDataModel.mobileRX));

        return view;
    }

    //Functions for Pie Chart
    public void setDataForPieChart()
    {
        long total = yValues[0] + yValues[1];
        double tx = yValues[0] /(total * 1.0) * 100;
        double rx = yValues[1]/(total * 1.0) * 100;
        yValues[0] = Math.round(tx);
        yValues[1] = Math.round(rx);

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < yValues.length; i++)
            yVals1.add(new Entry(yValues[i], i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xValues.length; i++)
            xVals.add(xValues[i]);

        // create pieDataSet
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // adding colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        // Added My Own colors
        for (int c : MY_COLORS)
            colors.add(c);

        dataSet.setColors(colors);

        //  create pie data object and set xValues and yValues and set it to the pieChart
        PieData data = new PieData(xVals, dataSet);
        //   data.setValueFormatter(new DefaultValueFormatter());
        //   data.setValueFormatter(new PercentFormatter());

        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // refresh/update pie chart
        mChart.invalidate();

        // animate piechart
        mChart.animateXY(1400, 1400);

        // Legends to show on bottom of the graph
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
    }


    public class MyValueFormatter implements ValueFormatter
    {
        private DecimalFormat mFormat;

        public MyValueFormatter()
        {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal if needed
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler)
        {
            // write your logic here
            return mFormat.format(value) + "%"; // e.g. append a dollar-sign
        }
    }

}