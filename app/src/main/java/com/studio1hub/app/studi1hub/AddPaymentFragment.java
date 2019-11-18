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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.studio1hub.app.studi1hub.Request.AddPaymentRequest;
import com.studio1hub.app.studi1hub.Request.AddProjectRequest;
import com.studio1hub.app.studi1hub.Request.CustListRequest;
import com.studio1hub.app.studi1hub.Request.EditEmpRequest;
import com.studio1hub.app.studi1hub.Request.EditPaymentRequest;
import com.studio1hub.app.studi1hub.Request.ProjectListRequest;
import com.studio1hub.app.studi1hub.Request.ShowPaymentRequest;
import com.studio1hub.app.studi1hub.Request.ShowProjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.studio1hub.app.studi1hub.Request.DeleteCache.deleteCache;

public class AddPaymentFragment extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    TextView tvdetail;
    String menukey,istextclick,logintype,cid,pid,amount,date,pyid;
    LinearLayout llhide;
    Fragment fragment;
    EditText etcid,etpid,etamount,etdate;
    TextView tvcustid,tvprojectid;
    Button btnsubmit,btncust,btnproject,btnsetdate,btndeletepayment,btneditpayment;
    ArrayList<String> lcust=new ArrayList<>();
    ArrayList<String> lproject=new ArrayList<>();
    SpinnerDialog spinnerDialog1,spinnerDialog2;
    ProgressBar pbar;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    DatePickerDialog datePickerDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_payment,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = this.getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        istextclick=prefs.getString("fromtextclick", "0");

        etcid=(EditText)view.findViewById(R.id.etcid);
        etpid=(EditText)view.findViewById(R.id.etpid);
        etamount=(EditText)view.findViewById(R.id.etamount);
        etdate=(EditText)view.findViewById(R.id.etdate);

        tvcustid=(TextView)view.findViewById(R.id.tvcustid);
        tvprojectid=(TextView)view.findViewById(R.id.tvprojectid);
        tvdetail=(TextView)view.findViewById(R.id.tvdetail);

        btnsetdate = (Button) view.findViewById(R.id.btnsetdate);
        btnsubmit = (Button) view.findViewById(R.id.btnsubmit);
        btncust=(Button)view.findViewById(R.id.btncust);
        btnproject=(Button)view.findViewById(R.id.btnproject);
        btndeletepayment=(Button)view.findViewById(R.id.btndeletepayment);
        btneditpayment=(Button)view.findViewById(R.id.btneditpayment);
        pbar = (ProgressBar)view.findViewById(R.id.pbar);
        llhide = (LinearLayout) view.findViewById(R.id.llhide);

        getcust();

        if (istextclick.equals("1") ) {
            pbar.setVisibility(View.VISIBLE);
            llhide.setVisibility(View.VISIBLE);
            btnsubmit.setVisibility(View.GONE);
            tvdetail.setText("Edit Payment Detail");
            pyid = getArguments().getString("idkey");
            ShowDetails();
        }
        else
            llhide.setVisibility(View.GONE);

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
                                etdate.setText(year + "-" + month + "-" + day);
                                date = year + "-" + month + "-" + day;
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        btncust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog1.showSpinerDialog();
            }
        });

        btnproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog2.showSpinerDialog();
            }
        });

        spinnerDialog1=new SpinnerDialog(getActivity(),lcust,"select customer");
        spinnerDialog1.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                String[] separated = s.split(":");
                String cid = separated[0].trim();
                String cname = separated[1].trim();
                etcid.setText(cid);
                tvcustid.setText(cname);
                getproject();
            }
        });

        spinnerDialog2=new SpinnerDialog(getActivity(),lproject,"select project");
        spinnerDialog2.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                String[] separated = s.split(":");
                String pid = separated[0].trim();
                String pname = separated[1].trim();
                etpid.setText(pid);
                tvprojectid.setText(pname);
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                cid = etcid.getText().toString().trim();
                pid = etpid.getText().toString().trim();
                amount = etamount.getText().toString().trim();
                date = etdate.getText().toString().trim();
                deleteCache(getActivity());
                if (!cid.isEmpty() && !pid.isEmpty() && !amount.isEmpty() && !date.isEmpty()) {
                    pbar.setVisibility(View.VISIBLE);
                        FillDetails();
                } else {
                    Toast.makeText(getActivity(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        btneditpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.setVisibility(View.VISIBLE);
                InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                cid = etcid.getText().toString().trim();
                pid = etpid.getText().toString().trim();
                amount = etamount.getText().toString().trim();
                date = etdate.getText().toString().trim();
                deleteCache(getActivity());
                if (!cid.isEmpty() && !pid.isEmpty() && !amount.isEmpty() && !date.isEmpty()) {
                    EditDetails();
                } else {
                    Toast.makeText(getActivity(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        btndeletepayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "under construction", Toast.LENGTH_SHORT).show();
            }
        });


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
                        etpid.setText("");
                        etamount.setText("");
                        etcid.setText("");
                        etdate.setText("");
                        tvcustid.setText("");
                        tvprojectid.setText("");
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
        AddPaymentRequest addPaymentRequest = new AddPaymentRequest(cid, pid, amount, date, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(addPaymentRequest);
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
        EditPaymentRequest editPaymentRequest = new EditPaymentRequest(pyid,cid, pid, amount, date, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(editPaymentRequest);
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
                        JSONObject user = jsonResponse.getJSONObject("payment");
                        tvcustid.setText(user.getString("cname"));
                        etcid.setText(user.getString("cid"));
                        tvprojectid.setText(user.getString("pname"));
                        etpid.setText(user.getString("pid"));
                        etamount.setText(user.getString("amount"));
                        etdate.setText(user.getString("date"));
                        getproject();
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
        ShowPaymentRequest showPaymentRequest = new ShowPaymentRequest(pyid, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(showPaymentRequest);
    }

    public void getcust()
    {
        lcust.clear();
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
                        lcust.add(jsonObject.getString("lcust"));
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
        CustListRequest custListRequest = new CustListRequest(responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(custListRequest);
    }

    public void getproject()
    {
        cid=etcid.getText().toString();
        lproject.clear();
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
                        lproject.add(jsonObject.getString("lproject"));
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
        ProjectListRequest projectListRequest = new ProjectListRequest(cid,responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(projectListRequest);
    }

}
