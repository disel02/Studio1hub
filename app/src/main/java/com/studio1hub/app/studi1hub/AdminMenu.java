package com.studio1hub.app.studi1hub;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

public class AdminMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    Button btnemp,btnproject,btncust;
    android.support.v7.widget.Toolbar toolbar;
    Fragment fragment=null;
    String login ;
    FragmentManager fragmentManager;
    FragmentTransaction ft;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private boolean internetConnected = true;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager=getSupportFragmentManager();
        ft=fragmentManager.beginTransaction();

        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        login = prefs.getString("logintype", null);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(login.equals("admin"))
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer);

            fragment=new AdminMenuFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
        }
        else if(login.equals("employee"))
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer_emp);

            fragment=new AdminProjectFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
        }
        else
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer_cust);

            fragment=new AdminProjectFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
        }

    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

        }

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 1) {
            getFragmentManager().popBackStack();
            if (exit) {
                finish(); // finish activity
            } else {
                Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            editor.putString("loginkey", "0");
            editor.apply();
            Intent intent = new Intent(AdminMenu.this, LoginScreen.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_exit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        fragmentManager=getSupportFragmentManager();
        ft=fragmentManager.beginTransaction();

        int id = item.getItemId();

            if (id == R.id.nav_add_emp) {
               editor.putString("menukey", "employee");
                editor.putString("fromtextclick", "0");
                editor.putBoolean("fromUser", false);
                editor.apply();
                fragment=new AddEmpFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            } else if (id == R.id.nav_emp_management) {
                editor.putString("menukey", "employee");
                editor.apply();
                fragment=new AdminEmpFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            }
            else if (id == R.id.nav_emp_verification) {
                editor.putString("menukey", "employee");
                editor.apply();
                fragment=new VerifiedEmpFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            }
            else if (id == R.id.nav_cust_verification) {
                editor.putString("menukey", "customer");
                editor.apply();
                fragment=new VerifiedEmpFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            }
            else if (id == R.id.nav_add_cust) {
                editor.putString("menukey", "customer");
                editor.putString("fromtextclick", "0");
                editor.putBoolean("fromUser", false);
                editor.apply();
                fragment=new AddEmpFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            } else if (id == R.id.nav_cust_management) {
                editor.putString("menukey", "customer");
                editor.apply();
                fragment=new AdminEmpFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            } else if (id == R.id.nav_add_project) {
                editor.putString("menukey", "project");
                editor.putString("fromtextclick", "0");
                editor.putBoolean("fromUser", false);
                editor.apply();
                fragment = new AddProjectFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            } else if (id == R.id.nav_project_master) {
                editor.putString("menukey", "project");
                editor.putString("isbtnclick", "0");
                editor.apply();
                fragment=new AdminProjectFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            } else if (id == R.id.nav_add_payment) {
                editor.putString("fromtextclick", "0");
                editor.putBoolean("fromUser", false);
                editor.apply();
                fragment = new AddPaymentFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            } else if (id == R.id.nav_show_payment) {
                fragment = new PaymentFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            } else if (id == R.id.nav_payment_master) {
                fragment = new PaymentMasterFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            } else if (id == R.id.nav_feedback) {
                Toast.makeText(this, "under construction", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_view_profile) {   // employee
                editor.putString("fromtextclick", "0");
                editor.putBoolean("fromUser", true);
                editor.putString("menukey", "employee");
                editor.apply();
                fragment=new AddEmpFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            } else if (id == R.id.nav_leave_apply) {
                fragment=new LeaveApplyFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            } else if (id == R.id.nav_attendance) {
                Toast.makeText(this, "under construction", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_payment_history) {
                Toast.makeText(this, "under construction", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_current_project) {
                fragment=new CurrentProjectFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            }else if (id == R.id.nav_assigned_projects) {
                editor.putString("fromnav", "1");
                editor.putInt("projectkey", 1);
                editor.apply();
                fragment=new AdminProjectFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            } else if (id == R.id.nav_inprogress_projects) {
                editor.putString("fromnav", "1");
                editor.putInt("projectkey", 3);
                editor.apply();
                fragment=new AdminProjectFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            } else if (id == R.id.nav_uploaded_projects) {
                editor.putString("fromnav", "1");
                editor.putInt("projectkey", 1);
                editor.apply();
                fragment=new AdminProjectFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            } else if (id == R.id.nav_completed_projects) {
                editor.putString("fromnav", "1");
                editor.putInt("projectkey", 6);
                editor.apply();
                fragment=new AdminProjectFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            } else if (id == R.id.nav_canceled_projects) {
                editor.putString("fromnav", "1");
                editor.putInt("projectkey", 7);
                editor.apply();
                fragment=new AdminProjectFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            } else if (id == R.id.nav_req_projects) {
                editor.putString("fromnav", "1");
                editor.putInt("projectkey", 2);
                editor.apply();
                fragment=new AdminProjectFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            } else if (id == R.id.nav_payment_refund) {   // customer
                editor.putString("paymentkey", "refund");
                editor.apply();
                fragment = new PaymentFragment();
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            }else if (id == R.id.nav_home) {   // customer
                if(login.equals("admin"))
                {
                    fragment=new AdminMenuFragment();
                    ft.replace(R.id.screen_area,fragment,"Fragment One");
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else
                {
                    fragment=new AdminProjectFragment();
                    ft.replace(R.id.screen_area,fragment,"Fragment One");
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }

        if (fragment != null)
        {
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction ft=fragmentManager.beginTransaction();
            ft.replace(R.id.screen_area,fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private void registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, internetFilter);
    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = getConnectivityStatusString(context);
            setSnackbarMessage(status, false);
        }
    };

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

    private void setSnackbarMessage(String status, boolean showBar) {
        String internetStatus = "";
        if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {
            internetStatus = "you are now";
        } else {
            internetStatus = "you are now ";
        }
        snackbar = Snackbar.make(drawer, internetStatus, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        if (internetStatus.equalsIgnoreCase("you are now ")) {
            if (internetConnected) {
                snackbar.setAction("offline", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
                internetConnected = false;
            }
        } else {
            if (!internetConnected) {
                snackbar.setAction("online", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setActionTextColor(Color.GREEN);
                internetConnected = true;
                snackbar.show();
            }
        }
    }
}
