package com.studio1hub.app.studi1hub;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.studio1hub.app.studi1hub.Request.AddProjectRequest;
import com.studio1hub.app.studi1hub.Request.CustListRequest;
import com.studio1hub.app.studi1hub.Request.EditProjectRequest;
import com.studio1hub.app.studi1hub.Request.EmpListRequest;
import com.studio1hub.app.studi1hub.Request.ShowProjectRecievedRequest;
import com.studio1hub.app.studi1hub.Request.ShowProjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.studio1hub.app.studi1hub.Request.DeleteCache.deleteCache;

public class AddProjectFragment extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    TextView tvdetail,tvid,tvempid,tvcustid,tvemp,tvcust;
    Button btnsubmit,btncust,btnemp;
    String istextclick,title,discription,cid,eid,pid,menukey,cost;
    EditText ettitle,etdiscription,etcid,eteid,etcost;
    ProgressBar pbar;
    ArrayList<String> lcust=new ArrayList<>();
    ArrayList<String> lemp=new ArrayList<>();
    SpinnerDialog spinnerDialog1,spinnerDialog2;
    Boolean isReceived;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_project,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = this.getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        istextclick=prefs.getString("fromtextclick", "0");
        menukey = prefs.getString("menukey", null);

        tvdetail=(TextView)view.findViewById(R.id.tvdetail);
        btnsubmit=(Button)view.findViewById(R.id.btnsubmit);
        btncust=(Button)view.findViewById(R.id.btncust);
        btnemp=(Button)view.findViewById(R.id.btnemp);
        tvid=(TextView)view.findViewById(R.id.tvid);
        pbar = (ProgressBar)view.findViewById(R.id.pbar);
        tvempid=(TextView)view.findViewById(R.id.tvempid);
        tvcustid=(TextView)view.findViewById(R.id.tvcustid);
        tvemp=(TextView)view.findViewById(R.id.tvemp);
        tvcust=(TextView)view.findViewById(R.id.tvcust);

        ettitle=(EditText)view.findViewById(R.id.ettitle);
        etdiscription=(EditText)view.findViewById(R.id.etdiscription);
        etcid=(EditText)view.findViewById(R.id.etcid);
        eteid=(EditText)view.findViewById(R.id.eteid);
        etcost=(EditText)view.findViewById(R.id.etcost);

        getcust();
        getemp();

        if (istextclick.equals("1") )
        {
            pbar.setVisibility(View.VISIBLE);
            pid = getArguments().getString("idkey");
            if (menukey.equals("employee"))
            {
                tvempid.setVisibility(View.GONE);
                eteid.setVisibility(View.GONE);
                btnemp.setVisibility(View.GONE);
                tvemp.setVisibility(View.GONE);
                ShowDetails();
            }
            else if (menukey.equals("customer"))
            {
                tvcustid.setVisibility(View.GONE);
                etcid.setVisibility(View.GONE);
                btncust.setVisibility(View.GONE);
                tvcust.setVisibility(View.GONE);
                ShowDetails();
            }
            else
            {
                isReceived = getArguments().getBoolean("isReceived");
                if (isReceived)
                {
                    ShowDetails2();
                }
                else
                {
                    ShowDetails();
                }
            }

            tvdetail.setText("Project id: ");
            btnsubmit.setText("edit");
            tvid.setText("P-"+pid);
            tvid.setVisibility(View.VISIBLE);
        }

        btncust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog1.showSpinerDialog();
            }
        });

        btnemp.setOnClickListener(new View.OnClickListener() {
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
            }
        });

        spinnerDialog2=new SpinnerDialog(getActivity(),lemp,"select employee");
        spinnerDialog2.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                String[] separated = s.split(":");
                String eid = separated[0].trim();
                String ename = separated[1].trim();
                eteid.setText(eid);
                tvempid.setText(ename);
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.setVisibility(View.VISIBLE);
                InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                title = ettitle.getText().toString().trim();
                discription = etdiscription.getText().toString().trim();
                cid = etcid.getText().toString().trim();
                eid = eteid.getText().toString().trim();
                cost = etcost.getText().toString().trim();
                deleteCache(getActivity());
                if (!title.isEmpty() && !discription.isEmpty() && !cid.isEmpty() && !cost.isEmpty() && !eid.isEmpty()) {
                    if (istextclick.equals("1")) {
                        EditDetails();
                    } else
                        FillDetails();
                } else {
                    pbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
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
                        ettitle.setText("");
                        etdiscription.setText("");
                        etcid.setText("");
                        eteid.setText("");
                        etcost.setText("");
                        tvcustid.setText("");
                        tvempid.setText("");
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
        AddProjectRequest addProjectRequest = new AddProjectRequest(title, discription, cid, eid,cost, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(addProjectRequest);
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
        EditProjectRequest editProjectRequest = new EditProjectRequest(title, discription, cid, eid, pid,cost,responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(editProjectRequest);
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
                        JSONObject user = jsonResponse.getJSONObject("project");
                        ettitle.setText(user.getString("title"));
                        etdiscription.setText(user.getString("discription"));
                        etcid.setText(user.getString("cid"));
                        eteid.setText(user.getString("eid"));
                        etcost.setText(user.getString("cost"));
                        tvcustid.setText(user.getString("cname"));
                        tvempid.setText(user.getString("ename"));
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
        ShowProjectRequest showProjectRequest = new ShowProjectRequest(pid, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(showProjectRequest);
    }

    public void ShowDetails2() {
        final Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Boolean error = jsonResponse.getBoolean("error");

                    if (!error) {
                        pbar.setVisibility(View.GONE);
                        JSONObject user = jsonResponse.getJSONObject("project");
                        ettitle.setText(user.getString("title"));
                        etdiscription.setText(user.getString("discription"));
                        etcid.setText(user.getString("cid"));
                        tvcustid.setText(user.getString("cname"));
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
        ShowProjectRecievedRequest showProjectRecievedRequest = new ShowProjectRecievedRequest(pid, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(showProjectRecievedRequest);
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

    public void getemp()
    {
        lemp.clear();
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
                        lemp.add(jsonObject.getString("lemp"));
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
        EmpListRequest empListRequest = new EmpListRequest(responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(empListRequest);
    }


}
