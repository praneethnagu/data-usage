package com.iiitb.datausage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iiitb.datausage.Model.AppDataModel;
import com.iiitb.datausage.R;

import java.util.List;

/**
 * Created by Bala Manoj on 20-10-2016.
 */
public class ApplicationDataAdapter extends ArrayAdapter<AppDataModel>
{
    public ApplicationDataAdapter(Context context, List<AppDataModel> applications)
    {
        super(context, 0, applications);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the data item for this position
        AppDataModel applicationDataModel = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_application_data, parent, false);
        }
        // Lookup view for data population
        TextView applicationName = (TextView) convertView.findViewById(R.id.appName);
        TextView applicationTX = (TextView) convertView.findViewById(R.id.appTX);
        TextView applicationRX = (TextView) convertView.findViewById(R.id.appRX);
        TextView applicationTotal = (TextView) convertView.findViewById(R.id.appTotal);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        // Populate the data into the template view using the data object
        applicationName.setText("Application Name: " + applicationDataModel.getApplicationName());
        applicationTX.setText("Bytes Transmitted: " + applicationDataModel.getTX());
        applicationRX.setText("Bytes Received: " + applicationDataModel.getRX());
        applicationTotal.setText("Total Bytes: " + applicationDataModel.getTotal());
        imageView.setImageDrawable(applicationDataModel.getIcon());

        // Return the completed view to render on screen
        return convertView;
    }

}
