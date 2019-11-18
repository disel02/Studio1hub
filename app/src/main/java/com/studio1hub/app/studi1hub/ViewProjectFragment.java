package com.studio1hub.app.studi1hub;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import com.studio1hub.app.studi1hub.Request.ShowProjectRequest2;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewProjectFragment extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    LinearLayout llhide;
    Fragment fragment;
    TextView tvpid,tvtitle,tvdiscription,tvcname;
    ProgressBar pbar;
    String pid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_project,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editor = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE).edit();

        SharedPreferences prefs = this.getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);

        tvpid=(TextView)view.findViewById(R.id.tvpid);
        tvtitle=(TextView)view.findViewById(R.id.tvtitle);
        tvdiscription=(TextView)view.findViewById(R.id.tvdiscription);
        tvcname=(TextView)view.findViewById(R.id.tvcname);
        pbar = (ProgressBar)view.findViewById(R.id.pbar);

        pid = getArguments().getString("idkey");

        ShowDetails();

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
                        tvpid.setText(user.getString("id"));
                        tvtitle.setText(user.getString("title"));
                        tvdiscription.setText(user.getString("discription"));
                        tvcname.setText(user.getString("cname"));
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
        ShowProjectRequest2 showProjectRequest2 = new ShowProjectRequest2(pid, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(showProjectRequest2);
    }

}
