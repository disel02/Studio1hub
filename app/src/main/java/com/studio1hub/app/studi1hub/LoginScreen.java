package com.studio1hub.app.studi1hub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import com.studio1hub.app.studi1hub.Request.LoginRequest;

import static com.studio1hub.app.studi1hub.Request.DeleteCache.deleteCache;

public class LoginScreen extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    Button btnlogin;
    EditText etusername, etpassword;
    TextView tvlinktoregister;
    String email, password;
    ProgressBar pbar;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private boolean internetConnected = true;
    RelativeLayout rllogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        btnlogin = (Button) findViewById(R.id.btnlogin);
        etusername = (EditText) findViewById(R.id.etusername);
        etpassword = (EditText) findViewById(R.id.etpassword);
        tvlinktoregister = (TextView) findViewById(R.id.tvlinktoregister);
        pbar = (ProgressBar) findViewById(R.id.pbar);
        rllogin = (RelativeLayout) findViewById(R.id.rllogin);

        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        tvlinktoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                email = etusername.getText().toString().trim();
                password = etpassword.getText().toString().trim();
                deleteCache(LoginScreen.this);
                if (isNetworkAvailable()) {
                    if (!email.isEmpty() && !password.isEmpty()) {
                        if (email.matches("\\d+")) {
                            editor.putString("username", email);
                            editor.putString("id", "isEmailORid");
                            editor.apply();
                            pbar.setVisibility(View.VISIBLE);
                            checkLogin(email, password);
                        } else if (EmailValidation(email)) {
                            editor.putString("username", email);
                            editor.putString("email", "isEmailORid");
                            editor.apply();
                            pbar.setVisibility(View.VISIBLE);
                            checkLogin(email, password);
                        } else {
                            Toast.makeText(LoginScreen.this, "please enter valid Id", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter the credentials!", Toast.LENGTH_LONG)
                                .show();
                    }
                } else
                    Snackbar.make(view, "you are now", Snackbar.LENGTH_SHORT)
                            .setAction("offline", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    snackbar.dismiss();
                                }
                            })
                            .setActionTextColor(Color.RED)
                            .show();
            }

        });
    }

    private void checkLogin(final String email, final String password) {

        if (email.equals("118") && password.equals("123")) {
            editor.putString("logintype", "admin");
            editor.apply();
            loginMethod();
        } else if (email.equals("119") && password.equals("123")) {
            editor.putString("logintype", "employee");
            editor.apply();
            loginMethod();
        } else if (email.equals("120") && password.equals("123")) {
            editor.putString("logintype", "customer");
            editor.apply();
            loginMethod();
        }
        else
        {
            pbar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Sorry! Wrong Authentication", Toast.LENGTH_LONG).show();
        }

        /*
        final Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Boolean error = jsonResponse.getBoolean("error");

                    if (!error) {
                        JSONObject user = jsonResponse.getJSONObject("user");
                        String fname = user.getString("fname");
                        String type = user.getString("type");
                        if (type.equals("A")) {
                            editor.putString("logintype", "admin");
                            editor.apply();
                            loginMethod();
                        } else if (type.equals("E")) {
                            editor.putString("logintype", "employee");
                            editor.apply();
                            loginMethod();
                        } else if (type.equals("C")) {
                            editor.putString("logintype", "customer");
                            editor.apply();
                            loginMethod();
                        }
                    } else {
                        pbar.setVisibility(View.GONE);
                        String errorMsg = jsonResponse.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    pbar.setVisibility(View.GONE);
                    Toast.makeText(LoginScreen.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        };
        LoginRequest loginRequest = new LoginRequest(email, password, responseListner);
        RequestQueue queue = Volley.newRequestQueue(LoginScreen.this);
        queue.add(loginRequest);
        */
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
        snackbar = Snackbar.make(rllogin, internetStatus, Snackbar.LENGTH_LONG);
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

    public void loginMethod() {
        editor.putString("loginkey", "1");
        editor.apply();
        pbar.setVisibility(View.GONE);
        Intent intent = new Intent(LoginScreen.this, AdminMenu.class);
        startActivity(intent);
        finish();
    }

    public boolean EmailValidation(String email) {
        if (email.matches(emailPattern) && email.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
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
    }
}
