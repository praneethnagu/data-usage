package com.iiitb.datausage.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iiitb.datausage.Model.StaticDataModel;
import com.iiitb.datausage.Model.StaticVariables;
import com.iiitb.datausage.R;

public class SummaryFragment extends Fragment
{
    TextView hourTextView;
    TextView dayTextView;
    TextView monthTextView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_summary, container, false);

        hourTextView = (TextView) view.findViewById(R.id.hourValue_tv);
        dayTextView = (TextView) view.findViewById(R.id.dayValue_tv);
        monthTextView = (TextView) view.findViewById(R.id.monthValue_tv);

        //Populate the text fields
        setAverage();

        return view;
    }

    //For the average data consumption
    public void setAverage()
    {
        long ms = SystemClock.elapsedRealtime();
        long hours = ms / (1000 * 60 * 60);

        double avgHourData = 0;
        double avgDayData = 0;
        double avgMonthData = 0;

        long total = StaticDataModel.totalTX + StaticDataModel.totalRX;

        String result = StaticVariables.dataConverter(total);
        String tokens[] = {"0", "0"};
        if(result.length() > 0)
        {
            tokens = result.split(" ");
        }

        double value = Double.parseDouble(tokens[0]);
        String unit = tokens[1];

        if (hours > 0)
        {
            avgHourData = value / hours;
            avgDayData = avgHourData * 24;
            avgMonthData = avgDayData * 30;
        }

        hourTextView.setText(String.format("%.2f", avgHourData)+ " " + unit);
        dayTextView.setText(dataUnitConvert(avgDayData, unit));
        monthTextView.setText(dataUnitConvert(avgMonthData, unit));
    }

    public String dataUnitConvert(double total, String unit)
    {
        String result = "";
        double value = total;

        long KB = 1024;
        long MB = 1024;
        long GB = 1024;
        long TB = 1024;

        switch(unit)
        {
            case "Bytes" :  if(total < KB)
            {
                result = total + " Bytes";
                value = total;
                unit = "Bytes";
            }
                if(value > MB)
                {
                    value = total * 1.0/KB;
                    result = value + " KB";
                    unit = "KB";
                }
                if(value > GB)
                {
                    value = total * 1.0/MB;
                    result = value + " MB";
                    unit = "MB";
                }
                if(value > TB)
                {
                    value = total * 1.0/GB;
                    result = value + " GB";
                    unit = "GB";
                }
                break;

            case "KB"   :   if(total < MB)
            {
                result = total + " KB";
                value = total;
                unit = "KB";
            }
                if(value > GB)
                {
                    value = total * 1.0/MB;
                    result = value + " MB";
                    unit = "MB";
                }
                if(value > TB)
                {
                    value = total * 1.0/GB;
                    result = value + " GB";
                    unit = "GB";
                }
                break;

            case "MB"   :   if(total < GB)
            {
                result = total + " MB";
                value = total;
                unit = "MB";
            }
                if(value > TB)
                {
                    value = total * 1.0/GB;
                    result = value + " GB";
                    unit = "GB";
                }
                break;


            case "GB"   :   if(total < TB)
            {
                result = total + " GB";
                value = total;
                unit = "GB";
            }
                break;

        }

        result = String.format("%.2f", value) + " " + unit;

        return result;
    }
}