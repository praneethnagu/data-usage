package com.iiitb.datausage.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.iiitb.datausage.R;

/**
 * Created by ABHIJNU on 11/3/2017.
 */

public class HomeMobileFragment extends Fragment implements View.OnClickListener{

    private Button buttonPie, buttonBar, buttonLine;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_mobile, container, false);

        buttonPie = (Button) view.findViewById(R.id.buttonPie);
        buttonBar = (Button) view.findViewById(R.id.buttonBar);
        buttonLine = (Button) view.findViewById(R.id.buttonLine);

        buttonPie.setOnClickListener(this);
        buttonBar.setOnClickListener(this);
        buttonLine.setOnClickListener(this);



        return view;

    }

    @Override
    public void onClick(View v) {
        if (v == buttonPie){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            //int i = ((ViewGroup)getView().getParent()).getId();
            //ft.replace(i,new MobileFragment());
            ft.replace(R.id.home_mobile_frag, new MobileFragment());
            ft.addToBackStack(null);
//            Log.e("asd",fm.getBackStackEntryCount()+"");
            ft.commit();
        }
        else if (v == buttonBar){
            FragmentManager fm = getFragmentManager();
            //fm.addOnBackStackChangedListener((FragmentManager.OnBackStackChangedListener) this);
            FragmentTransaction ft = fm.beginTransaction();
            //int i = ((ViewGroup)getView().getParent()).getId();
            //ft.replace(i,new MobileBarFragment());
            ft.replace(R.id.home_mobile_frag , new MobileBarFragment());
            ft.addToBackStack(null);
            ft.commit();
        }
        else if (v == buttonLine){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            //int i = ((ViewGroup)getView().getParent()).getId();
            //ft.replace(i,new MobileFragment());
            ft.replace(R.id.home_mobile_frag, new MobileLineFragment());
            ft.addToBackStack(null);
            ft.commit();
        }
    }

}