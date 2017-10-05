package com.iiitb.datausage.Services;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.iiitb.datausage.Activities.NoSimActivity;
import com.iiitb.datausage.Database.DatabaseHandler;
import com.iiitb.datausage.Model.DatabaseModel;
import com.iiitb.datausage.Model.StaticVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SendDataToServerService extends Service implements LocationListener
{
    DatabaseHandler databaseHandler = null;

    TestPostClass testPostClass = null;
    double latitude = 0;
    double longitude = 0;

    public SendDataToServerService()
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        TelephonyManager telephony = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if(telephony.getSimState() == TelephonyManager.SIM_STATE_ABSENT)
        {
            Toast.makeText(this, "Please insert SIM to continue using the application", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), NoSimActivity.class));
        }

        databaseHandler = new DatabaseHandler(getApplicationContext());

        getLocation();

        if(testPostClass != null)
        {
            testPostClass.cancel(true);
        }
        testPostClass = new TestPostClass(databaseHandler);
        testPostClass.execute();

        return START_NOT_STICKY;
    }

    //Schedule to start the service after 1 minute
    @Override
    public void onDestroy()
    {
        //Start Service after 30 minutes
        if(testPostClass != null)
        {
            testPostClass.cancel(true);
        }
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(
                alarm.RTC_WAKEUP,
                System.currentTimeMillis() + (1000 * 60 * 1),
                PendingIntent.getService(this, 0, new Intent(this, SendDataToServerService.class), 0)
        );
    }

    //To get the GPS location
    public void getLocation()
    {
        final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
        final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

        Location location = null;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!isNetworkEnabled && !isGPSEnabled)
        {
            Log.d("Service", "GPS and Network both are disabled");
            //showSettingsAlert();
        }
        else
        {
            if(isNetworkEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locationManager != null)
                {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null)
                    {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.d("Service","Got the coordinates from Network: latitude: " + location.getLatitude() + " longitude: " + location.getLongitude());
                    }
                }
            }

            if(isGPSEnabled)
            {
                if (location == null)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null)
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.d("Service","Got the coordinates from GPS: latitude: " + location.getLatitude() + " longitude: " + location.getLongitude());
                        }
                    }
                }
            }
        }

    }

    //To display the settings if GPS isn't on
    public void showSettingsAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    //LocationListener Methods
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    //AsyncTask

    public class TestPostClass extends AsyncTask<String,String,String>
    {
        DatabaseHandler databaseHandler = null;

        TestPostClass()
        {

        }

        TestPostClass(DatabaseHandler databaseHandler)
        {
            this.databaseHandler = databaseHandler;
        }

        @Override
        protected String doInBackground(String... params)
        {
            String serverURL = StaticVariables.serverURL + "new_sync";
            try
            {
                JSONObject jsonObject = getDetails();

                URL url = new URL(serverURL);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                //connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");

                connection.setDoOutput(true);
                OutputStream os = connection.getOutputStream();
                os.write(jsonObject.toString().getBytes("UTF-8"));
                os.close();

                int responseCode = connection.getResponseCode();

                Log.i("Service", "ResponseCode: " + responseCode);

                InputStream is = connection.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null)
                {
                    total.append(line);
                }
                is.close();
                line = total.toString();
                line = line.trim();

                Log.i("Service", "Value read from Server: " + line);
                return line;

            }
            catch (MalformedURLException e)
            {
                //e.printStackTrace();
            }
            catch (IOException e)
            {
                //e.printStackTrace();
            }
            catch(Exception e)
            {
                //e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s)
        {
            if(s == null)
            {
                Log.i("Service", "Some unknown error occurred");
            }
            else if(s.equals("success"))
            {
                Log.i("Service","Data sent successfully to Server");
            }
            else if(s.equals("failure"))
            {
                Log.i("Service", "Failure at the server");
            }
            else
            {
                Log.i("Service", "Unknown Error: " + s);
            }
            stopSelf();
            testPostClass.cancel(true);
        }


        JSONObject getDetails()
        {
            long totalTX = 0;
            long totalRX = 0;
            long total = 0;
            long cellTX = 0;
            long cellRX = 0;
            long cellTotal = 0;
            long wifiTX = 0;
            long wifiRX = 0;
            long wifiTotal = 0;

            //new code

            long nextTotalTX = 0;
            long nextTotalRX = 0;
            long nextTotal = 0;
            long nextCellTX = 0;
            long nextCellRX = 0;
            long nextCellTotal = 0;
            long nextWifiTX = 0;
            long nextWifiRX = 0;
            long nextWifiTotal = 0;

            long sharedTotalTX = 0;
            long sharedTotalRX = 0;
            long sharedTotal = 0;
            long sharedCellTX = 0;
            long sharedCellRX = 0;
            long sharedCellTotal = 0;
            long sharedWifiTX = 0;
            long sharedWifiRX = 0;
            long sharedWifiTotal = 0;

            //new code

            totalTX = TrafficStats.getTotalTxBytes();
            totalRX = TrafficStats.getTotalRxBytes();
            total = totalTX + totalRX;
            cellTX = TrafficStats.getMobileTxBytes();
            cellRX = TrafficStats.getMobileRxBytes();
            cellTotal = cellTX + cellRX;
            wifiTX = totalTX - cellTX;
            wifiRX = totalRX - cellRX;
            wifiTotal = wifiTX + wifiRX;

            //New Code begins

            SharedPreferences sharedPreferences = getSharedPreferences("DATA_USAGE_PREF", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            sharedCellTX = sharedPreferences.getLong("cellTX", -1);
            sharedCellRX = sharedPreferences.getLong("cellRX", -1);
            sharedTotalTX = sharedPreferences.getLong("totalTX", -1);
            sharedTotalRX = sharedPreferences.getLong("totalRX", -1);

            sharedTotal = sharedTotalRX + sharedTotalTX;

            if(sharedCellTX == -1 || sharedCellRX == -1 ||
                    sharedTotalTX == -1 || sharedTotalRX == -1 || total < sharedTotal)
            {
                Log.i("Service", "No shared Pref Found for data");

                //Implies this is the first set of data
                editor.putLong("cellTX", cellTX);
                editor.putLong("cellRX", cellRX);
                editor.putLong("totalTX", totalTX);
                editor.putLong("totalRX", totalRX);
                editor.apply();

                nextTotalTX = totalTX;
                nextTotalRX = totalRX;
                nextTotal = total;
                nextCellTX = cellTX;
                nextCellRX = cellRX;
                nextCellTotal = cellTotal;
                nextWifiTX = wifiTX;
                nextWifiRX = wifiRX;
                nextWifiTotal = wifiTotal;

            }
            else
            {
                Log.i("Service", "shared Pref Found for data");

                //Apply the subtraction Algorithm
                if(sharedCellTX < 0) { sharedCellTX = 0; }
                if(sharedCellRX < 0) { sharedCellRX = 0; }
                if(sharedTotalTX < 0) { sharedTotalTX = 0; }
                if(sharedTotalRX < 0) { sharedTotalRX = 0; }

                sharedTotal = sharedCellTX + sharedCellRX;
                sharedCellTotal = sharedCellTX + sharedCellRX;
                sharedWifiTX = sharedTotalTX - sharedCellTX;
                sharedWifiRX = sharedTotalRX - sharedCellRX;
                sharedWifiTotal = sharedWifiTX + sharedWifiRX;

                nextTotalTX = totalTX - sharedTotalTX;
                nextTotalRX = totalRX - sharedTotalRX;
                nextTotal = nextTotalTX + nextTotalRX;
                nextCellTX = cellTX - sharedCellTX;
                nextCellRX = cellRX - sharedCellRX;
                nextCellTotal = nextCellTX + nextCellRX;
                //nextCellTotal = cellTotal - sharedCellTotal;
                nextWifiTX = wifiTX - sharedWifiTX;
                nextWifiRX = wifiRX - sharedWifiRX;
                nextWifiTotal = nextWifiTX + nextWifiRX;
                //nextWifiTotal = wifiTotal - sharedWifiTotal;

                editor.putLong("cellTX", cellTX);
                editor.putLong("cellRX", cellRX);
                editor.putLong("totalTX", totalTX);
                editor.putLong("totalRX", totalRX);
                editor.apply();
            }

            //New Code ends

            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONObject applicationJSONObject = null;

            PackageManager pm = getPackageManager();
            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(date);


            for (ApplicationInfo packageInfo : packages)
            {
                int uid = packageInfo.uid;

                String applicationName = "NA";
                try
                {
                    applicationName = packageInfo.loadLabel(pm).toString();
                }
                catch(Exception e)
                {
                    Log.i("Service", "Exception in extracting package name");
                }
                //String applicationName = packageInfo.loadLabel(pm).toString();
                long applicationTX = TrafficStats.getUidTxBytes(packageInfo.uid);
                long applicationRX = TrafficStats.getUidRxBytes(packageInfo.uid);
                long applicationTotal = applicationTX + applicationRX;

                long nextApplicationTX = 0;
                long nextApplicationRX = 0;
                long nextApplicationTotal = 0;

                long dbApplicationTX = 0;
                long dbApplicationRX = 0;
                long dbApplicationTotal = 0;

                date = calendar.getTime();
                formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(date);

                //New code begins


                DatabaseModel app = databaseHandler.retrieveAppData(uid);
                if(app == null)
                {
                    //First time insert
                    DatabaseModel databaseModel = new DatabaseModel(uid, applicationTX, applicationRX, applicationName);
                    boolean insertStatus = databaseHandler.insertAppData(databaseModel);
                    //Log.d("Service", "First time database insert of App with uid : " + uid);

                    nextApplicationTX = applicationTX;
                    nextApplicationRX = applicationRX;
                    nextApplicationTotal = nextApplicationTX + nextApplicationRX;
                }
                else
                {
                    //Log.d("Service", "App already exists in DB so update it");
                    if(app.getTx() < 0) { app.setTx(0); }
                    if(app.getRx() < 0) { app.setRx(0); }
                    nextApplicationTX = applicationTX - app.getTx();
                    nextApplicationRX = applicationRX - app.getRx();
                    nextApplicationTotal = nextApplicationTX + nextApplicationRX;

                    app.setTx(applicationTX);
                    app.setRx(applicationRX);
                    boolean updateStatus = databaseHandler.updateAppData(app);
                    //Log.d("Service", "The update status of the app with uid:  " + app.getUid() + " is: " + updateStatus);
                }

                //New code ends

                try
                {
                    applicationJSONObject = new JSONObject();
                    applicationJSONObject.put("uid", uid);
                    applicationJSONObject.put("applicationName", applicationName);
                    applicationJSONObject.put("applicationTotal", nextApplicationTotal);
                    applicationJSONObject.put("applicationRX", nextApplicationRX);
                    applicationJSONObject.put("applicationTX", nextApplicationTX);
                    applicationJSONObject.put("date", formatted);
                    applicationJSONObject.put("latitude", String.valueOf(latitude));
                    applicationJSONObject.put("longitude", String.valueOf(longitude));
                    applicationJSONObject.put("ip", "To be done by the Server"); //update this
                }
                catch(JSONException e)
                {
                    Log.i("Service", "Exception in the application data: " +  e.toString());
                }

                jsonArray.put(applicationJSONObject);
            }

            Log.d("Service", "Displaying the list of app stored in database");
            databaseHandler.retrieveAppsData();

            databaseHandler.close();

            SharedPreferences sp = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE);
            SharedPreferences.Editor ed = sp.edit();
            String mobileNumber = sp.getString("mobileNumber", null);
            TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();

            if(mobileNumber == null)
            {
                mobileNumber = StaticVariables.number;
                Log.i("SP MobileNumber", " This shouldn't enter");
            }

            try
            {
                //New Code 11 Nov
                jsonObject = new JSONObject();
                jsonObject.put("mobileNumber", mobileNumber);
                jsonObject.put("imei",imei);
                TelephonyManager telephony = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                if(telephony != null)
                {
                    jsonObject.put("isp", telephony.getNetworkOperatorName());
                }
                else
                {
                    jsonObject.put("isp", "No operator");
                }


                JSONObject totalDataJSONObject = new JSONObject();
                JSONArray totalDataJSONArray = new JSONArray();

                //totalDataJSONObject.put("mobileNumber", mobileNumber);
                totalDataJSONObject.put("total",nextTotal);
                totalDataJSONObject.put("totalRX",nextTotalRX);
                totalDataJSONObject.put("totalTX",nextTotalTX);
                totalDataJSONObject.put("wifiTotal",nextWifiTotal);
                totalDataJSONObject.put("wifiRX",nextWifiRX);
                totalDataJSONObject.put("wifiTX",nextWifiTX);
                totalDataJSONObject.put("cellTotal",nextCellTotal);
                totalDataJSONObject.put("cellRX",nextCellRX);
                totalDataJSONObject.put("cellTX",nextCellTX);
                totalDataJSONObject.put("latitude", String.valueOf(latitude));
                totalDataJSONObject.put("longitude",String.valueOf(longitude));
                totalDataJSONObject.put("date", formatted);


                Log.i("First", jsonObject.toString());

                totalDataJSONArray.put(totalDataJSONObject);

                jsonObject.put("totalData", totalDataJSONArray);
                Log.i("Second", jsonObject.toString());

                jsonObject.put("applicationData",jsonArray);
                Log.i("Third", jsonObject.toString());

                checkDetails(jsonObject);

                //New Code ends
            }
            catch(JSONException e)
            {
                Log.e("Error", e.toString());
            }

            Log.i("ServerData", "JSON Object Constructed: " + jsonObject.toString());

            return jsonObject;
        }

        public void checkDetails(JSONObject jsonObject)
        {
            try
            {
                Log.i("MobileNumber", jsonObject.getString("mobileNumber"));
                Log.i("IMEI", jsonObject.getString("imei"));
                Log.i("ISP", jsonObject.getString("isp"));
                Log.i("TotalData", jsonObject.getJSONArray("totalData").toString());
                Log.i("ApplicationData", jsonObject.getJSONArray("applicationData").toString());
            }
            catch(Exception e)
            {
                e.printStackTrace();
                Log.i("checkDetails()", "Exception");
            }

        }
    }
}
