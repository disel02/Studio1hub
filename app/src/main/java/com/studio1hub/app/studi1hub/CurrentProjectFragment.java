package com.studio1hub.app.studi1hub;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.studio1hub.app.studi1hub.Request.CountryRequest;
import com.studio1hub.app.studi1hub.Request.CurrentProjectRequest;
import com.studio1hub.app.studi1hub.Request.CurrentProjectRequest2;
import com.studio1hub.app.studi1hub.Request.ShowCurrentProjectRequest;
import com.studio1hub.app.studi1hub.Request.ShowEmpRequest;
import com.studio1hub.app.studi1hub.Request.VerifiedEmpCustRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.studio1hub.app.studi1hub.Request.DeleteCache.deleteCache;

public class CurrentProjectFragment extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    Button btnpick,btnsubmit;
    TextView tvproject;
    SpinnerDialog spinnerDialog;
    ArrayList<String> projects=new ArrayList<>();
    String pid,uid;
    ProgressBar pbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_project,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editor = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE).edit();

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        uid= prefs.getString("username", null);

        btnpick=(Button)view.findViewById(R.id.btnpick);
        tvproject=(TextView)view.findViewById(R.id.tvproject);
        btnsubmit = (Button)view.findViewById(R.id.btnsubmit);
        pbar = (ProgressBar)view.findViewById(R.id.pbar);

        ShowCurrentProject();
        getProjects();

        spinnerDialog=new SpinnerDialog(getActivity(),projects,"select project");
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                String[] separated = s.split(":");
                 pid = separated[0].trim();
                tvproject.setText(s);
            }
        });

        btnpick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialog.showSpinerDialog();
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.setVisibility(View.VISIBLE);
                setCurrentProject();
            }
        });

    }

    public void ShowCurrentProject() {
        final Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Boolean error = jsonResponse.getBoolean("error");

                    if (!error) {
                        pbar.setVisibility(View.GONE);
                        JSONObject user = jsonResponse.getJSONObject("project");
                        String tmp=user.getString("cProject");
                        String[] separated = tmp.split(":");
                        pid = separated[0].trim();
                        tvproject.setText(tmp);
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
        ShowCurrentProjectRequest showCurrentProjectRequest = new ShowCurrentProjectRequest(uid, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(showCurrentProjectRequest);
    }

    public void setCurrentProject() {
        deleteCache(getActivity());
        final Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Boolean error = jsonResponse.getBoolean("error");

                    if (!error) {
                        pbar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "successfully set", Toast.LENGTH_SHORT).show();
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
        CurrentProjectRequest2 currentProjectRequest2 = new CurrentProjectRequest2(uid,pid, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(currentProjectRequest2);
    }

    public void getProjects()
    {
        projects.clear();
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
                        projects.add(jsonObject.getString("projects"));
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
        CurrentProjectRequest currentProjectRequest = new CurrentProjectRequest(uid,responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(currentProjectRequest);
    }

}
