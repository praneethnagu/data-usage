package com.iiitb.datausage.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.iiitb.datausage.R;

public class NoSimActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_sim);

        getSupportActionBar().setTitle("No SIM detected!");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        setResult(RESULT_OK);
    }
}
