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
import com.studio1hub.app.studi1hub.Request.AddProjectViaCustRequest;
import com.studio1hub.app.studi1hub.Request.EditProjectViaCustRequest;
import com.studio1hub.app.studi1hub.Request.ShowProjectViaCustRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static com.studio1hub.app.studi1hub.Request.DeleteCache.deleteCache;

public class AddProjectViaCustFragment extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    Button btnsubmit;
    String istextclick,title,discription,pid,menukey,cid;
    EditText ettitle,etdiscription;
    ProgressBar pbar;
    TextView tvid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_project_via_cust,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = this.getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        editor = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE).edit();
        istextclick=prefs.getString("fromtextclick", "0");
        menukey = prefs.getString("menukey", null);
        cid = prefs.getString("username", null);

        btnsubmit=(Button)view.findViewById(R.id.btnsubmit);
        pbar = (ProgressBar)view.findViewById(R.id.pbar);

        ettitle=(EditText)view.findViewById(R.id.ettitle);
        etdiscription=(EditText)view.findViewById(R.id.etdiscription);
        tvid=(TextView)view.findViewById(R.id.tvid);

        if (istextclick.equals("1") )
        {
           btnsubmit.setText("Edit");
            pid = getArguments().getString("idkey");
           tvid.setText(pid);
           ShowDetails();
            editor.putString("fromtextclick", "0");
            editor.apply();
        }

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.setVisibility(View.VISIBLE);
                InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                title = ettitle.getText().toString().trim();
                discription = etdiscription.getText().toString().trim();
                deleteCache(getActivity());
                if (!title.isEmpty() && !discription.isEmpty()) {
                    if (istextclick.equals("1")) {
                        EditDetails();
                    } else
                        FillDetails();
                } else {
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
        AddProjectViaCustRequest addProjectRequest = new AddProjectViaCustRequest(title, discription,cid, responseListner);
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
        EditProjectViaCustRequest editProjectRequest = new EditProjectViaCustRequest(title, discription, pid,responseListner);
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
        ShowProjectViaCustRequest showProjectRequest = new ShowProjectViaCustRequest(pid, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(showProjectRequest);
    }

}
