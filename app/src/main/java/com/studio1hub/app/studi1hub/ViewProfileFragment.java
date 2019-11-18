package com.studio1hub.app.studi1hub;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.studio1hub.app.studi1hub.Request.ShowEmpRequest;
import com.studio1hub.app.studi1hub.Request.ShowEmpRequest2;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewProfileFragment extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    LinearLayout llhide;
    Button btnsubmit;
    Fragment fragment;
    TextView tvid,tvname,tvemail,tvphone,tvdob,tvaddress,tvcity,tvpincode,tvcountry;
    ProgressBar pbar;
    String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_profile,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editor = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE).edit();

        SharedPreferences prefs = this.getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        username = prefs.getString("username", null);

        btnsubmit=(Button)view.findViewById(R.id.btnsubmit);
        tvid=(TextView)view.findViewById(R.id.tvid);
        tvname=(TextView)view.findViewById(R.id.tvname);
        tvemail=(TextView)view.findViewById(R.id.tvemail);
        tvaddress=(TextView)view.findViewById(R.id.tvaddress);
        tvphone=(TextView)view.findViewById(R.id.tvphone);
        tvdob=(TextView)view.findViewById(R.id.tvdob);
        tvcity=(TextView)view.findViewById(R.id.tvcity);
        tvpincode=(TextView)view.findViewById(R.id.tvpincode);
        tvcountry=(TextView)view.findViewById(R.id.tvcountry);
        pbar = (ProgressBar) view.findViewById(R.id.pbar);

        ShowDetails();

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "under construction", Toast.LENGTH_SHORT).show();
            }
        });

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
                        tvid.setText(user.getString("id"));
                        String fname=user.getString("fname");
                        String mname=user.getString("mname");
                        String lname=user.getString("lname");
                        tvname.setText(fname+" "+mname+" "+lname);
                        tvemail.setText(user.getString("email"));
                        tvphone.setText(user.getString("phone"));
                        tvdob.setText(user.getString("dob"));
                        tvaddress.setText(user.getString("address"));
                        tvcity.setText(user.getString("city"));
                        tvpincode.setText(user.getString("pincode"));
                        tvcountry.setText(user.getString("country"));
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
        ShowEmpRequest2 showEmpRequest2 = new ShowEmpRequest2(username, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(showEmpRequest2);
    }

}
