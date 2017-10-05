package com.iiitb.datausage.Activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.iiitb.datausage.Model.StaticDataModel;
import com.iiitb.datausage.Model.StaticVariables;
import com.iiitb.datausage.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ChangePasswordActivity extends AppCompatActivity
{
    String TAG = "ChangePasswordActivity";

    EditText currentP;
    EditText newP;
    EditText retypeP;

    ChangePasswordActivityAsyncTask changePasswordActivityAsyncTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setTitle("Change Password");

        currentP = (EditText) findViewById(R.id.currentPasswordEditText);
        newP = (EditText) findViewById(R.id.newPasswordEditText);
        retypeP = (EditText) findViewById(R.id.retypeNewPasswordEditText);
    }

    public void changePassword(View view)
    {
        String c = currentP.getText().toString();
        String n = newP.getText().toString();
        String r = retypeP.getText().toString();

        if(c.equals("") || n.equals("") || r.equals(""))
        {
            Log.d(TAG, "Please enter all the fields");
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_LONG).show();

            return;
        }

        //Retreive userid and password from SharedPrefs
        String user = "";
        String spp = "";
        if(c.equals(spp))
        {
            if(n.equals(r))
            {
                //send details to the Server
                if(changePasswordActivityAsyncTask != null)
                {
                    changePasswordActivityAsyncTask.cancel(true);
                }
                changePasswordActivityAsyncTask = new ChangePasswordActivityAsyncTask(n);
                changePasswordActivityAsyncTask.execute();
            }
            else
            {
                Log.d(TAG, "Please enter same password in the New Password and Retype Password fields");
                Toast.makeText(this, "Please enter same password in the New Password and Retype Password fields", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Log.d(TAG, "Current Password is not correct");
            Toast.makeText(this, "Current Password is not correct", Toast.LENGTH_LONG).show();
        }
    }

    public class ChangePasswordActivityAsyncTask extends AsyncTask<String, String, String>
    {
        String n  = "";

        ChangePasswordActivityAsyncTask()
        {

        }

        ChangePasswordActivityAsyncTask(String n)
        {
            this.n = n;
        }

        @Override
        protected String doInBackground(String... params)
        {
            String serverURL = StaticVariables.serverURL + "change_password/";
            try
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("mobileNumber", "");
                jsonObject.put("newPassword", n);

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

                Log.d(TAG, "ResponseCode: " + responseCode);

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
                Log.d(TAG,"Data from the Server: " + line);

                return line;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            if(s.equals("success"))
            {
                Log.d(TAG, "Success in change password");
                Toast.makeText(getApplicationContext(), "Success in change password", Toast.LENGTH_LONG).show();
            }
            else if(s.equals("failure"))
            {
                Log.d(TAG, "Failure in change password");
                Toast.makeText(getApplicationContext(), "Faliure in change password", Toast.LENGTH_LONG).show();
            }
            else
            {
                Log.d(TAG, "Unknown error in change password");
                Toast.makeText(getApplicationContext(), "Unknown error in change password", Toast.LENGTH_LONG).show();
            }
            changePasswordActivityAsyncTask.cancel(true);
        }
    }
}
