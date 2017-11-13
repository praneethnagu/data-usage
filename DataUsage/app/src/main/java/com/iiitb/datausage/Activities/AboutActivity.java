package com.iiitb.datausage.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.iiitb.datausage.R;

public class AboutActivity extends AppCompatActivity
{
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setTitle("About");

        webView = (WebView) findViewById(R.id.webView);
        String summary = "<html>" +
                            "<body>" +
                                "<p> Mobile Data Usage provides an insight to the user regarding his data consumption through Mobile data and WIFI.</p>" +
                                "<p> Developed by students of IIIT-B</p>" +
                                "<ul>" +
                                    "<li>Praneeth</li>" +
                                    "<li>Abhijnu</li>" +

                                "</ul>" +
                                "<p> Guided by Prof V. Sridhar and Mtech grads</p>" +
                            "</body>" +
                        "</html>";
        webView.loadData(summary, "text/html; charset=utf-8", "utf-8");
    }
}
