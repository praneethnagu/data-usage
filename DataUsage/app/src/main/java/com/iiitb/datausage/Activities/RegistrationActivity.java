package com.iiitb.datausage.Activities;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegistrationActivity extends AppCompatActivity
{
    RegistrationActivityPostClass registrationActivityPostClass;
    String result = "success"; // update this variable with the value returned by the server
    Validators validators = new Validators();
    EditText nameEditText;
    EditText dobEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText professionEditText;
    EditText mobileNumberEditText;
    EditText wifiUsageEditText;
    EditText dataUsageEditText;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView loginScreen;

    String REGISTRATION_ACTIVITY_POST_TAG = "RegistrationActivity PostClass";
    String REGISTRATION_ACTIVITY_VALIDATION_TAG = "RegistrationActivity Validation";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().setTitle("Registration");

        dobEditText = (EditText) findViewById(R.id.dob_edit_text);

        loginScreen = (TextView) findViewById(R.id.link_to_login);
        loginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Closing the RegistrationActivity so that the LoginActivity is visible
                finish();
            }
        });
    }

    public void register(View v)
    {
        nameEditText = (EditText) findViewById(R.id.reg_name);
        dobEditText = (EditText) findViewById(R.id.dob_edit_text);
        emailEditText = (EditText) findViewById(R.id.reg_email);
        passwordEditText = (EditText) findViewById(R.id.reg_password);
        professionEditText = (EditText) findViewById(R.id.reg_profession);
        mobileNumberEditText = (EditText) findViewById(R.id.reg_mobile_number);
        wifiUsageEditText = (EditText) findViewById(R.id.reg_wifi_usage);
        dataUsageEditText = (EditText) findViewById(R.id.reg_data_usage);

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        int selectedRadioButtonID = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedRadioButtonID);

        boolean validate = false;

        String name = "";
        String gender = "";
        String dob = "";
        String email = "";
        String password = "";
        String profession = "";
        String mobileNumber = "";
        String wifiUsage = "";
        String dataUsage = "";

        name = nameEditText.getText().toString();
        gender = radioButton.getText().toString();
        dob = dobEditText.getText().toString();
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        profession = professionEditText.getText().toString();
        mobileNumber = mobileNumberEditText.getText().toString();
        wifiUsage = wifiUsageEditText.getText().toString();
        dataUsage = dataUsageEditText.getText().toString();

        Toast.makeText(getApplicationContext(),
                "Name: " + name + "\nGender: " + gender + "\ndob: " + dob + "\nEmail: " + email + "\nPassword:" + password + "\nProfession: " + profession + "\nMobileNumber: " + mobileNumber + "\nWifi: " + wifiUsage + "\nData: " + dataUsage,
                Toast.LENGTH_LONG).show();

        SimpleDateFormat format = new SimpleDateFormat ("dd-MM-yyyy");
        Date dateOfBirth;
        try
        {
            dateOfBirth = format.parse(dob);
            Log.d("Registration Activity","Parsing Successful: " + dateOfBirth);
        }
        catch (ParseException e)
        {
            Log.e("RegistrationActivity","Cannot parse the date present in the dob Edit Text");
            Toast.makeText(this, "Cannot parse the date present in the dob Edit Text", Toast.LENGTH_LONG).show();
        }

        //Email ID Validation
        if(validators.isValidMail(email))
        {
            Log.d("RegistrationActivity","Email is valid");
        }
        else
        {
            Log.e("RegistrationActivity", "Email is not invalid");
            Toast.makeText(this, "Please enter valid email ID", Toast.LENGTH_LONG).show();
            return;
        }

        //Mobile Number Validation
        if(validators.isValidMobile(mobileNumber))
        {
            Log.d("RegistrationActivity","Mobile Number is valid");
        }
        else
        {
            Log.e("RegistrationActivity", "Mobile Number is invalid");
            Toast.makeText(this, "Please enter valid mobile number", Toast.LENGTH_LONG).show();
            return;
        }

        if(name.equals("") || password.equals("") || profession.equals("") || wifiUsage.equals("") || dataUsage.equals(""))
        {
            Log.e("RegistrationActivity", "Please fill in all the fields");
            Toast.makeText(this, "Please fill in all the fields to continue", Toast.LENGTH_LONG).show();
            return;
        }

        /*
            1) Send all the values to the Server
            2) Receive a value success when the Server successfully stored the details in the Server
            3) Receive a value fail when Server fails to store the details
            4) If success then display the login page
            5) If fail then ask to register again i.e try again
         */

        if(registrationActivityPostClass != null)
        {
            registrationActivityPostClass.cancel(true);
        }
        registrationActivityPostClass = new RegistrationActivityPostClass();
        registrationActivityPostClass.execute();
    }

    public void getDate(View v)
    {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener()
                {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        dobEditText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                },
                mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    //Async Task

    public class RegistrationActivityPostClass extends AsyncTask<String,String,String>
    {
        String serverURL = StaticVariables.serverURL + "registration/";

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

                Log.d("RegistrationActivity", " Value read from Server: " + line);

                int responseCode = connection.getResponseCode();

                Log.d(REGISTRATION_ACTIVITY_POST_TAG, "ResponseCode: " + responseCode);

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
            if(s == null)
            {
                Log.d("RegistrationActivity", "Some error has occurred");
            }
            else if(s.equals("success"))
            {
                Log.d("RegistrationActivity", "Registration Successful");
                Toast.makeText(getApplicationContext(), "Registration Successful, Please Login to continue!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                finish(); //Display the Login page to the user
            }
            else if(s.equals("failure"))
            {
                Log.e("RegistrationActivity", "Registration Unsuccessful at Server! Please try again!");
            }
            else if(s.equals("duplicate"))
            {
                Toast.makeText(getApplicationContext(), "User Already Exists! Please try different Mobile Number", Toast.LENGTH_LONG).show();
                Log.e("RegistrationActivity", "User Already Exists! Please try different Mobile Number");
            }
            else
            {
                Log.e("RegistrationActivity", "register(): Some unknown error at Server");
            }

            registrationActivityPostClass.cancel(true);
        }

        JSONObject getDetails()
        {
            JSONObject jsonObject = null;
            try
            {
                jsonObject = new JSONObject();
                jsonObject.put("name",nameEditText.getText().toString());
                jsonObject.put("gender", radioButton.getText().toString());
                jsonObject.put("dob", dobEditText.getText().toString());
                jsonObject.put("email", emailEditText.getText().toString());
                jsonObject.put("password", passwordEditText.getText().toString());
                jsonObject.put("profession", professionEditText.getText().toString());
                jsonObject.put("mobileNumber", mobileNumberEditText.getText().toString());
                jsonObject.put("wifiUsage", wifiUsageEditText.getText().toString());
                jsonObject.put("dataUsage", dataUsageEditText.getText().toString());

            }
            catch(JSONException e)
            {
                Log.e("RegistrationActivity", e.toString());
            }

            Log.d(REGISTRATION_ACTIVITY_POST_TAG, jsonObject.toString());

            return jsonObject;
        }
    }
}
