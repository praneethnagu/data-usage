package com.iiitb.datausage.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iiitb.datausage.Model.StaticVariables;
import com.iiitb.datausage.R;
import com.iiitb.datausage.Services.Validators;

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

public class LoginActivity extends AppCompatActivity
{
    LoginActivityPostClass loginActivityPostClass;
    TextView registrationScreen;
    TextView forgotPasswordScreen;
    EditText idEditText;
    EditText passwordEditText;
    Validators validators = new Validators();
    String result = null; // update this variable with the value returned by the server

    String LOGIN_ACTIVITY_POST_TAG = "LoginActivity PostClass";

    int NO_SIM_REQUEST_EXIT = 10;
    int HOME_REQUEST_EXIT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        TelephonyManager telephony = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if(telephony.getSimState() == TelephonyManager.SIM_STATE_ABSENT)
        {
            Toast.makeText(this, "Please insert SIM to continue using the application", Toast.LENGTH_LONG).show();
            Log.d("Login Activity", "onCreate() Please insert SIM to continue using the application");
            startActivityForResult(new Intent(this, NoSimActivity.class), NO_SIM_REQUEST_EXIT);
        }

        registrationScreen = (TextView) findViewById(R.id.link_to_register);
        forgotPasswordScreen = (TextView) findViewById(R.id.link_to_password);
        idEditText = (EditText)findViewById(R.id.id_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);

        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String retreivedMobileNumber = sharedPreferences.getString("mobileNumber", null);
        if(retreivedMobileNumber != null)
        {
            Log.d("LoginActivity","onCreate() User already registered so directly display the Home");
            Log.d("LoginActivity","onCreate() SharedPreference: " + retreivedMobileNumber);
            Intent intent = new Intent(this, TabsHomeActivity.class);
            startActivity(intent);

            finish(); //closing the login
        }

        registrationScreen .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(),RegistrationActivity.class);
                startActivity(intent);
            }
        });


        forgotPasswordScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Handle the forgot password
                /*
                    1) Call the Server
                    2) Server sends an email to user's registered mail ID
                    3) If server returns success then display the toast
                    4) If server returns fail then display some unknown error has occurred
                 */

                /*if(result.equals("success"))
                {
                    Log.d("LoginActivity","onCreate() Handle the Forgot Password");
                    Toast.makeText(getApplicationContext(), "Password details has been sent to your registered Email", Toast.LENGTH_LONG).show();
                }
                else if(result.equals("fail"))
                {
                    Log.d("LoginActivity","onCreate() Forgot Password Module at Server Failed");
                }
                else
                {
                    Log.e("LoginActivity", "onCreate() Forgot Password Module some unknown error");
                }*/

                Toast.makeText(getBaseContext(), "Coming soon...", Toast.LENGTH_LONG).show();
            }
        });
    }

    //finishing the parent activity after child activity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == NO_SIM_REQUEST_EXIT || requestCode == HOME_REQUEST_EXIT)
        {
            if(resultCode == RESULT_OK)
            {
                finish();
            }
        }
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        TelephonyManager telephony = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if(telephony.getSimState() == TelephonyManager.SIM_STATE_ABSENT)
        {
            Toast.makeText(this, "Please insert SIM to continue using the application", Toast.LENGTH_LONG).show();
            Log.d("Login Activity", "onCreate() Please insert SIM to continue using the application");
            startActivityForResult(new Intent(this, NoSimActivity.class), NO_SIM_REQUEST_EXIT);
        }
    }

    public void login(View v)
    {
        String id = idEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        //Toast.makeText(getApplicationContext(),"ID: " + id  + " Password: " + password, Toast.LENGTH_LONG).show();

        //Number validation
        if(validators.isValidMobile(id))
        {
            Log.d("LoginActivity","login() Validation: ID is valid");
        }
        else
        {
            Log.e("LoginActivity", "login() Validation: ID is invalid");
        }

        /*
            1) Send the id and password values to the server
            2) Server authenticates with the values present in the database
            3) Server returns success if found in the database
            4) Server returns fail if not found in the database
         */

        if(loginActivityPostClass != null)
        {
            loginActivityPostClass.cancel(true);
        }
        loginActivityPostClass = new LoginActivityPostClass();
        loginActivityPostClass.execute();

    }

    //AsyncTask

    public class LoginActivityPostClass extends AsyncTask<String,String,String>
    {
        String serverURL = StaticVariables.serverURL + "login/";
        @Override
        protected String doInBackground(String... params)
        {
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

                Log.d(LOGIN_ACTIVITY_POST_TAG, "ResponseCode: " + responseCode);

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
                Log.d("LoginActivity","Data from the Server: " + line);
                result = line;

                return result;

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
            if(s == null)
            {
                Log.i("LoginActivity", "onPostExecute() Some error has occurred at Server");
                Toast.makeText(getApplicationContext(),"Please try again!", Toast.LENGTH_LONG).show();
            }
            else if(s.equals("success"))
            {
                Log.i("LoginActivity", "onPostExecute() User in Authentic Display the Home page");

                Toast.makeText(getApplicationContext(),"Login Successful!", Toast.LENGTH_LONG).show();

                SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String retrievedMobileNumber = sharedPreferences.getString("mobileNumber", null);

                StaticVariables.number = idEditText.getText().toString();
                if(retrievedMobileNumber != null)
                {
                    Log.i("LoginActivity","Shared Preference found");
                    Log.i("LoginActivity","SharedPreference: " + retrievedMobileNumber);
                }
                else
                {
                    Log.i("LoginActivity","First time User so store his mobileNumber in SharedPreference");
                    editor.putString("mobileNumber", idEditText.getText().toString());
                    editor.apply();
                }

                Intent intent = new Intent(getApplicationContext(), TabsHomeActivity.class);
                startActivity(intent);

                finish();
            }
            else if(s.equals("failure"))
            {
                Log.e("LoginActivity", "User in not Authentic");
                Toast.makeText(getApplicationContext(),"Invalid Credentials!", Toast.LENGTH_LONG).show();
            }
            else
            {
                Log.e("LoginActivity", "Data from Server: " + s);
                Toast.makeText(getApplicationContext(),"Please try again!", Toast.LENGTH_LONG).show();
            }

            loginActivityPostClass.cancel(true);
        }

        JSONObject getDetails()
        {
            JSONObject jsonObject = null;
            try
            {
                jsonObject = new JSONObject();
                jsonObject.put("mobileNumber", idEditText.getText().toString());
                jsonObject.put("password",passwordEditText.getText().toString());
            }
            catch(JSONException e)
            {
                Log.e("JSONError LoginActivity", e.toString());
            }

            Log.d(LOGIN_ACTIVITY_POST_TAG, jsonObject.toString());

            return jsonObject;
        }
    }
}
