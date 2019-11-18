package com.studio1hub.app.studi1hub;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.studio1hub.app.studi1hub.Request.AddEmpRequest;
import com.studio1hub.app.studi1hub.Request.AdminUserRequest;
import com.studio1hub.app.studi1hub.Request.CityRequest;
import com.studio1hub.app.studi1hub.Request.CountryRequest;
import com.studio1hub.app.studi1hub.Request.EditEmpRequest;
import com.studio1hub.app.studi1hub.Request.ListAdapterClass;
import com.studio1hub.app.studi1hub.Request.ShowEmpRequest;
import com.studio1hub.app.studi1hub.Request.StateRequest;
import com.studio1hub.app.studi1hub.Request.VerifiedEmpCustRequest;
import com.studio1hub.app.studi1hub.Request.subjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.studio1hub.app.studi1hub.Request.DeleteCache.deleteCache;

public class AddEmpFragment extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    TextView tvdetail, tvid;
    String menukey, istextclick, logintype, profile_change, fname, mname, lname, email, phone, dob, address, city="", pincode, country="",state="", type, uid, uname;
    LinearLayout llhide,llhide2;
    Button btnempproject, btntrack, btnsubmit, btnsetdate,btncountry,btnstate,btncity,btnreject,btnverified;
    EditText etdob, etfname, etmname, etlname, etemail, etphone, etaddress, etcity, etpincode, etcountry,etstate;
    Fragment fragment;
    DatePickerDialog datePickerDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+";
    ProgressBar pbar;
    ArrayList<String> Lcountry=new ArrayList<>();
    ArrayList<String> Lstate=new ArrayList<>();
    ArrayList<String> Lcity=new ArrayList<>();
    Boolean fromUser,fromVerification;
    String username,verified;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    String xyz="xyz";
    String tmp="",tmp2="";
    SpinnerDialog spinnerDialog1,spinnerDialog2,spinnerDialog3;
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_emp, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editor = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE).edit();

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        menukey = prefs.getString("menukey", null);
        istextclick = prefs.getString("fromtextclick", "0");
        logintype = prefs.getString("logintype", null);
        profile_change = prefs.getString("changerequest", "0");
        fromUser = prefs.getBoolean("fromUser", false);
     //   fromVerification = prefs.getBoolean("fromVerification", false);
        username = prefs.getString("username", null);

        tvdetail = (TextView) view.findViewById(R.id.tvdetail);
        tvid = (TextView) view.findViewById(R.id.tvid);
        llhide = (LinearLayout) view.findViewById(R.id.llhide);
        llhide2 = (LinearLayout) view.findViewById(R.id.llhide2);
        btnempproject = (Button) view.findViewById(R.id.btnempproject);
        btntrack = (Button) view.findViewById(R.id.btntrack);
        btnsubmit = (Button) view.findViewById(R.id.btnsubmit);
        btnreject = (Button) view.findViewById(R.id.btnreject);
        btnverified = (Button) view.findViewById(R.id.btnverified);
        btnsetdate = (Button) view.findViewById(R.id.btnsetdate);
        etdob = (EditText) view.findViewById(R.id.etdob);
        etfname = (EditText) view.findViewById(R.id.etfname);
        etmname = (EditText) view.findViewById(R.id.etmname);
        etlname = (EditText) view.findViewById(R.id.etlname);
        etphone = (EditText) view.findViewById(R.id.etphone);
        etaddress = (EditText) view.findViewById(R.id.etaddress);
        etcity = (EditText) view.findViewById(R.id.etcity);
        etstate = (EditText) view.findViewById(R.id.etstate);
        etemail = (EditText) view.findViewById(R.id.etemail);
        etpincode = (EditText) view.findViewById(R.id.etpincode);
        etcountry = (EditText) view.findViewById(R.id.etcountry);
        pbar = (ProgressBar) view.findViewById(R.id.pbar);
        btncountry=(Button)view.findViewById(R.id.btncountry);
        btnstate=(Button)view.findViewById(R.id.btnstate);
        btncity=(Button)view.findViewById(R.id.btncity);

        btnsetdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                etdob.setText(year + "-" + month + "-" + day);
                                dob = year + "-" + month + "-" + day;
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        if (istextclick.equals("1")) {
            llhide.setVisibility(View.VISIBLE);
            llhide2.setVisibility(View.GONE);
            tvdetail.setText(menukey + " id :");
            btnsubmit.setText("edit");
            tvid.setVisibility(View.VISIBLE);
            uid = getArguments().getString("idkey");
            ShowDetails();
            if (menukey.equals("employee")) {
                tvid.setText("E-" + uid);
                type = "E";
            } else {
                btntrack.setVisibility(View.GONE);
                tvid.setText("C-" + uid);
                type = "C";
            }
        }
        else if (istextclick.equals("2")) {
            llhide.setVisibility(View.GONE);
            llhide2.setVisibility(View.VISIBLE);
            btnsubmit.setVisibility(View.GONE);
            tvdetail.setText(menukey + " id :");
            tvid.setVisibility(View.VISIBLE);
            uid = getArguments().getString("idkey");
            ShowDetails();
            if (menukey.equals("employee")) {
                tvid.setText("E-" + uid);
                type = "E";
            } else {
                btntrack.setVisibility(View.GONE);
                tvid.setText("C-" + uid);
                type = "C";
            }
        }
        else if (fromUser) {        // employee or customer change profile request
            llhide.setVisibility(View.GONE);
            llhide2.setVisibility(View.GONE);
            tvdetail.setText("Id :");
            btnsubmit.setText("send to verify");
            tvid.setVisibility(View.VISIBLE);
            uid=username;
            ShowDetails();
        }
        else {
            pbar.setVisibility(View.GONE);
            llhide.setVisibility(View.GONE);
            llhide2.setVisibility(View.GONE);
            tvdetail.setText("fill " + menukey + " detail");
            if (menukey.equals("employee")) {
                type = "E";
            } else {
                type = "C";
            }
        }

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = etfname.getText().toString().trim();
                mname = etmname.getText().toString().trim();
                lname = etlname.getText().toString().trim();
                phone = etphone.getText().toString().trim();
                address = etaddress.getText().toString().trim();
                city = etcity.getText().toString().trim();
                state = etstate.getText().toString().trim();
                pincode = etpincode.getText().toString().trim();
                country = etcountry.getText().toString().trim();
                email = etemail.getText().toString().trim();
                dob = etdob.getText().toString().trim();
                deleteCache(getActivity());
                if (fromUser)
                {
                    if (!email.isEmpty() && !fname.isEmpty() && !mname.isEmpty() && !lname.isEmpty() && !phone.isEmpty() && !dob.isEmpty() && !city.isEmpty() && !address.isEmpty() && !pincode.isEmpty() && !country.isEmpty() && !state.isEmpty()) {
                        if (EmailValidation(email)) {
                            pbar.setVisibility(View.VISIBLE);
                            if (fromUser) {
                                verified="1";
                                EditDetails();
                            } else
                                FillDetails();
                        } else {
                            Toast.makeText(getActivity(), "please enter valid Email Id", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(),
                                "Please enter the credentials!", Toast.LENGTH_LONG)
                                .show();
                    }
                }
                else {
                    if (!email.isEmpty() && !fname.isEmpty()) {
                        if (EmailValidation(email)) {
                            pbar.setVisibility(View.VISIBLE);
                            if (istextclick.equals("1")) {
                                verified="0";
                                EditDetails();
                            } else
                                FillDetails();
                        } else {
                            Toast.makeText(getActivity(), "please enter valid Email Id", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(),
                                "Please enter the * credentials!", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }
        });

        btnreject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verified="0";
                verifiedRecord();
            }
        });

        btnverified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verified="2";
                verifiedRecord();
            }
        });

        btnempproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("isbtnclick", "1");
                editor.apply();
                final FragmentManager fragmentManager = getFragmentManager();
                fragment = new AdminProjectFragment();
                Bundle args = new Bundle();
                args.putString("idkey", uid);
                fragment.setArguments(args);
                FragmentTransaction t = fragmentManager.beginTransaction();
                t.replace(R.id.screen_area, fragment, "fragment two");
                t.addToBackStack(null);
                t.commit();
            }
        });

        btntrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Under construction", Toast.LENGTH_SHORT).show();
            }
        });

        //------------------------------------------------------------------------------------------

        getCountry();

        tmp= etcountry.getText().toString().trim();

        if (!tmp.equals(""))
        {
            getState();
        }

        tmp= etstate.getText().toString().trim();
        if (!tmp.equals(""))
        {
            getCity();
        }

        spinnerDialog1=new SpinnerDialog(getActivity(),Lcountry,"select country");
        spinnerDialog1.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                country=s;
                etcountry.setText(country);
                getState();
            }
        });

        spinnerDialog2=new SpinnerDialog(getActivity(),Lstate,"select state");
        spinnerDialog2.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                state=s;
                etstate.setText(state);
                getCity();
            }
        });

        spinnerDialog3=new SpinnerDialog(getActivity(),Lcity,"select city");
        spinnerDialog3.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                city=s;
                etcity.setText(city);
            }
        });

        btncountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog1.showSpinerDialog();
            }
        });

        btnstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 tmp= etcountry.getText().toString().trim();
                if (tmp.equals(""))
                {
                    Toast.makeText(getActivity(), "please select country", Toast.LENGTH_SHORT).show();
                    etstate.setText("");
                    etcity.setText("");
                }
                else
                    spinnerDialog2.showSpinerDialog();
            }
        });

        btncity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmp= etcountry.getText().toString().trim();
                tmp2= etstate.getText().toString().trim();
                if (tmp.equals(""))
                {
                    Toast.makeText(getActivity(), "please select country", Toast.LENGTH_SHORT).show();
                    etstate.setText("");
                    etcity.setText("");
                }
                else if (tmp2.equals(""))
                {
                    Toast.makeText(getActivity(), "please select state ", Toast.LENGTH_SHORT).show();
                    etcity.setText("");
                }
                else
                    spinnerDialog3.showSpinerDialog();
            }
        });

        //------------------------------------------------------------------------------------------

    }

    public void ShowDetails() {
        final Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Boolean error = jsonResponse.getBoolean("error");

                    if (!error) {
                        pbar.setVisibility(View.GONE);
                        JSONObject user = jsonResponse.getJSONObject("user");
                        if (fromUser)
                        {
                            tvid.setText(user.getString("id"));
                        }
                        etfname.setText(user.getString("fname"));
                        etmname.setText(user.getString("mname"));
                        etlname.setText(user.getString("lname"));
                        etemail.setText(user.getString("email"));
                        etphone.setText(user.getString("phone"));
                        etdob.setText(user.getString("dob"));
                        etaddress.setText(user.getString("address"));
                        etcity.setText(user.getString("city"));
                        etpincode.setText(user.getString("pincode"));
                        etcountry.setText(user.getString("country"));
                        etstate.setText(user.getString("state"));
                        getState();
                        getCity();
                    } else {
                        pbar.setVisibility(View.GONE);
                        String errorMsg = jsonResponse.getString("error_msg");
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    pbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        };
        ShowEmpRequest showEmpRequest = new ShowEmpRequest(uid, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(showEmpRequest);
    }

    public void verifiedRecord() {
        deleteCache(getActivity());
        final Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Boolean error = jsonResponse.getBoolean("error");

                    if (!error) {
                        pbar.setVisibility(View.GONE);
                        if (verified.equals("0"))
                            Toast.makeText(getActivity(), "Record Rejected", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(), "Record Verified", Toast.LENGTH_SHORT).show();

                        final FragmentManager fragmentManager = getFragmentManager();
                        final FragmentTransaction ft = fragmentManager.beginTransaction();
                        fragment=new VerifiedEmpFragment();
                        ft.replace(R.id.screen_area,fragment,"Fragment One");
                        ft.addToBackStack(null);
                        ft.commit();
                    } else {
                        pbar.setVisibility(View.GONE);
                        String errorMsg = jsonResponse.getString("error_msg");
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    pbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        };
        VerifiedEmpCustRequest verifiedEmpCustRequest = new VerifiedEmpCustRequest(uid,verified, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(verifiedEmpCustRequest);
    }

    public void FillDetails() {
        final Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Boolean error = jsonResponse.getBoolean("error");

                    if (!error) {
                        pbar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "stored successfully", Toast.LENGTH_SHORT).show();
                        etfname.setText("");
                        etmname.setText("");
                        etlname.setText("");
                        etemail.setText("");
                        etphone.setText("");
                        etdob.setText("");
                        etaddress.setText("");
                        etcity.setText("");
                        etpincode.setText("");
                        etcountry.setText("");
                    } else {
                        pbar.setVisibility(View.GONE);
                        String errorMsg = jsonResponse.getString("error_msg");
                        if (errorMsg.contains("Duplicate entry")) {
                            Toast.makeText(getActivity(), "Already Registered ", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    pbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        };
        AddEmpRequest addEmpRequest = new AddEmpRequest(fname, mname, lname, email, phone, address, city, pincode, country, state,type, dob, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(addEmpRequest);
    }

    public void EditDetails() {
        final Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Boolean error = jsonResponse.getBoolean("error");

                    if (!error) {
                        pbar.setVisibility(View.GONE);
                        if (fromUser)
                            Toast.makeText(getActivity(), "send to verification", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(), "Edit successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        pbar.setVisibility(View.GONE);
                        String errorMsg = jsonResponse.getString("error_msg");
                        if (errorMsg.contains("Duplicate entry")) {
                            Toast.makeText(getActivity(), "Already Registered ", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    pbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        };
        EditEmpRequest editEmpRequest = new EditEmpRequest(fname, mname, lname, email, phone, address, city, pincode, country,state, uid, dob,verified, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(editEmpRequest);
    }

    public void getCountry()
    {
        Lcountry.clear();
        deleteCache(getActivity());
        final Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Lcountry.add(jsonObject.getString("cityCountry"));
                    }
                }
               catch (ArrayIndexOutOfBoundsException exception)
                {
                    Toast.makeText(getActivity(), ""+exception, Toast.LENGTH_SHORT).show();
                }
               catch (JSONException e1) {

                    Toast.makeText(getActivity(), "Empty"+e1, Toast.LENGTH_SHORT).show();
                    e1.printStackTrace();
                }
            }
        };
        CountryRequest countryRequest = new CountryRequest(xyz,responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(countryRequest);
    }

    public void getState()
    {
        deleteCache(getActivity());
        country = etcountry.getText().toString().trim();
        final Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Lstate.add(jsonObject.getString("cityState"));
                    }
                }
                catch (ArrayIndexOutOfBoundsException exception)
                {
                    Toast.makeText(getActivity(), ""+exception, Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e1) {

                    Toast.makeText(getActivity(), "Empty"+e1, Toast.LENGTH_SHORT).show();
                    e1.printStackTrace();
                }
            }
        };
        StateRequest stateRequest = new StateRequest(country,responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stateRequest);
    }

    public void getCity()
    {
        Lcity.clear();
        deleteCache(getActivity());
        country = etcountry.getText().toString().trim();
        state = etstate.getText().toString().trim();
        final Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Lcity.add(jsonObject.getString("cityName"));
                    }
                }
                catch (ArrayIndexOutOfBoundsException exception)
                {
                    Toast.makeText(getActivity(), ""+exception, Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e1) {

                    Toast.makeText(getActivity(), "Empty"+e1, Toast.LENGTH_SHORT).show();
                    e1.printStackTrace();
                }
            }
        };
        CityRequest cityRequest = new CityRequest(country,state,responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(cityRequest);
    }

    public boolean EmailValidation(String email) {
        if (email.matches(emailPattern) && email.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

}
