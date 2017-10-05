package com.iiitb.datausage.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.iiitb.datausage.Model.StaticVariables;
import com.iiitb.datausage.Services.SendDataToServerService;

/**
 * Created by Bala Manoj on 20-10-2016.
 */
public class AutoStartBroadcastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("Receiver", "Reboot preformed! Starting the Service!");
        Toast.makeText(context, "Reboot preformed! Starting the Service! Number: " + StaticVariables.number, Toast.LENGTH_LONG).show();

        SharedPreferences sharedPreferences = context.getSharedPreferences("LOGIN_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String retreivedMobileNumber = sharedPreferences.getString("mobileNumber", null);

        if(retreivedMobileNumber != null)
        {
            context.startService(new Intent(context, SendDataToServerService.class));
        }

    }
}
