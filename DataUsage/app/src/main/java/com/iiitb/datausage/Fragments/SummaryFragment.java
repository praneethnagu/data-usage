package com.iiitb.datausage.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.iiitb.datausage.Model.StaticDataModel;
import com.iiitb.datausage.Model.StaticVariables;
import com.iiitb.datausage.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

        Log.d(  "ms", "Hello" +ms);
        Log.d(  "hours", "How" +hours);

        double avgHourData = 0;
        double avgWeekData = 0;
        double avgMonthData = 0;

        long total = StaticDataModel.totalTX + StaticDataModel.totalRX;

        Log.d(  "total", "Hello " +total);

        String result = StaticVariables.dataConverter(total);
        String tokens[] = {"0", "0"};
        if(result.length() > 0)
        {
            tokens = result.split(" ");
        }

        double value = Double.parseDouble(tokens[0]);
        String unit = tokens[1];

        Log.d(  "Value", "Hello " + value);
        int days = (int) (hours/24);
        double week = 7*value/days;
        if (hours > 0)
        {
            avgHourData = value / hours;
            avgWeekData = week;
            avgMonthData = avgHourData * 24 * 30;
        }

        hourTextView.setText(String.format("%.2f", avgHourData)+ " " + unit);
        dayTextView.setText(dataUnitConvert(avgWeekData, unit));
        monthTextView.setText(dataUnitConvert(avgMonthData, unit));
    }

    public Double[] weekData(long time, double value){

        int days = (int) (time/24);
        double monData, tueData, wedData, thuData, friData, satData, sunData;
        //Get current day
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String day = new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime());

        double week = 7*value/days;
        monData = 0.12 * week;
        tueData = 0.09 * week;
        wedData = 0.08 * week;
        thuData = 0.10 * week;
        friData = 0.15 * week;
        satData = 0.22 * week;
        sunData = 0.24 * week;

        Double[] weekdata = new Double[7];
        weekdata[0] = monData;
        weekdata[1] = tueData;
        weekdata[2] = wedData;
        weekdata[3] = thuData;
        weekdata[4] = friData;
        weekdata[5] = satData;
        weekdata[6] = sunData;

        return weekdata;
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