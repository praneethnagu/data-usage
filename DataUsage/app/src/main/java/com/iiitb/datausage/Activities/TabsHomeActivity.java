package com.iiitb.datausage.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.iiitb.datausage.Fragments.AppsFragment;
import com.iiitb.datausage.Fragments.HomeMobileFragment;
import com.iiitb.datausage.Fragments.HomeTotalFragment;
import com.iiitb.datausage.Fragments.HomeWifiFragment;
import com.iiitb.datausage.Fragments.MobileFragment;
import com.iiitb.datausage.Fragments.SummaryFragment;
import com.iiitb.datausage.Fragments.TotalFragment;
import com.iiitb.datausage.Fragments.WifiFragment;
import com.iiitb.datausage.Interfaces.AsyncResponse;
import com.iiitb.datausage.Model.AppDataModel;
import com.iiitb.datausage.Model.ApplicationDataModel;
import com.iiitb.datausage.Model.StaticDataModel;
import com.iiitb.datausage.R;
import com.iiitb.datausage.Services.SendDataToServerService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TabsHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    String TAG = "TabsHomeActivity";

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

    TabsHomeActivityAsyncTask tabsHomeActivityAsyncTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(tabsHomeActivityAsyncTask != null)
        {
            tabsHomeActivityAsyncTask.cancel(true);
        }
        tabsHomeActivityAsyncTask = new TabsHomeActivityAsyncTask(this);
        tabsHomeActivityAsyncTask.execute();

        FragmentManager fm = getSupportFragmentManager();
        /*fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
//                Log.e("b")
//                if(getSupportFragmentManager().getBackStackEntryCount() == 0) finish();
            }
        });*/
        Log.d(TAG, "Starting the Service");
        startService(new Intent(this, SendDataToServerService.class));
    }

    //Setting icons for the tabs
    private void setupTabIcons()
    {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_summary_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_mobile_data_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_wifi_icon);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_total_data_icon);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_apps_icon);
    }

    //Adding the fragments to the Tab
    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SummaryFragment(), "Summary");
        adapter.addFragment(new HomeMobileFragment(), "Mobile");
        adapter.addFragment(new HomeWifiFragment(), "Wifi");
        adapter.addFragment(new HomeTotalFragment(), "Total");
        adapter.addFragment(new AppsFragment(), "Apps");
        viewPager.setAdapter(adapter);
    }

    //Building the tabs
    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager)
        {
            super(manager);
        }

        @Override
        public Fragment getItem(int position)
        {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount()
        {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title)
        {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed()
    {


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {

            drawer.closeDrawer(GravityCompat.START);
        }
        else if (getFragmentManager().getBackStackEntryCount() > 0) {
            Log.e("ad","adasdasd");

            getFragmentManager().popBackStack();
        }
        else
        {
            Log.e("ad","ewrwer");

            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tabs_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                finishAndRemoveTask();
            }
            else
            {
                finish();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about)
        {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_change_password)
        {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout)
        {
            //Clear the shared Prefs

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                finishAndRemoveTask();
            }
            else
            {
                finish();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Async Task to get the app data
    public class TabsHomeActivityAsyncTask extends AsyncTask<String, Integer, List<AppDataModel>>
    {
        Context context;

        ProgressDialog mProgressDialog;

        TabsHomeActivityAsyncTask()
        {

        }

        TabsHomeActivityAsyncTask(Context context)
        {
            this.context = context;
        }


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            // Create ProgressBar
            mProgressDialog = new ProgressDialog(context);
            // Set your ProgressBar Title
            mProgressDialog.setTitle("Loading");
            //mProgressDialog.setIcon(R.drawable.dwnload);
            // Set your ProgressBar Message
            mProgressDialog.setMessage("Loading the apps, Please Wait!");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // Show ProgressBar
            mProgressDialog.setCancelable(false);
            //  mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        @Override
        protected List<AppDataModel> doInBackground(String... params)
        {
            String result = null;
            int totalApps = 0;

            List<AppDataModel> appList = new ArrayList<AppDataModel>();
            AppDataModel app = null;

            PackageManager pm = getPackageManager();
            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
            totalApps = packages.size();
            int count = 0;
            for (ApplicationInfo packageInfo : packages)
            {
                count++;
                int uid = packageInfo.uid;
                String applicationName = packageInfo.loadLabel(pm).toString();
                Drawable icon = pm.getApplicationIcon(packageInfo);
                long applicationTX = TrafficStats.getUidTxBytes(packageInfo.uid);
                long applicationRX = TrafficStats.getUidRxBytes(packageInfo.uid);
                long applicationTotal = applicationTX + applicationRX;

                publishProgress( (int) count * 100/ totalApps);

                app = new AppDataModel(applicationName, applicationTX, applicationRX, icon);
                appList.add(app);
            }
            Collections.sort(appList, new AppDataComparator());

            return appList;
        }


        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
            mProgressDialog.setProgress(values[0]);
            //textView.setText(values[0]);
        }

        @Override
        protected void onPostExecute(List<AppDataModel> appList)
        {
            StaticDataModel.appsList = appList;
            StaticDataModel.totalTX = TrafficStats.getTotalTxBytes();
            StaticDataModel.totalRX = TrafficStats.getTotalRxBytes();
            StaticDataModel.mobileTX = TrafficStats.getMobileTxBytes();
            StaticDataModel.mobileRX = TrafficStats.getMobileRxBytes();
            StaticDataModel.wifiTX = StaticDataModel.totalTX - StaticDataModel.mobileTX;
            StaticDataModel.wifiRX = StaticDataModel.totalRX - StaticDataModel.mobileRX;

            Log.d(TAG,"Static Data Model has been populated! Checking it's size: " + StaticDataModel.appsList.size());
            Log.d(TAG, "Adding the fragments");

            mProgressDialog.dismiss();

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons();

            tabsHomeActivityAsyncTask.cancel(true);
        }
    }
}

class AppDataComparator implements Comparator<AppDataModel>
{
    public int compare(AppDataModel x, AppDataModel y)
    {
        if(x.getTotal() < y.getTotal())
        {
            return 1;
        }
        else if(x.getTotal() > y.getTotal())
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }
}

