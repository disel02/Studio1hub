package com.studio1hub.app.studi1hub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.studio1hub.app.studi1hub.Request.AdminAllProjectRequest;
import com.studio1hub.app.studi1hub.Request.AdminProjectNavRequest;
import com.studio1hub.app.studi1hub.Request.AdminProjectRequest;
import com.studio1hub.app.studi1hub.Request.AdminUserRequest;
import com.studio1hub.app.studi1hub.Request.ListAdapterClass;
import com.studio1hub.app.studi1hub.Request.subjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.studio1hub.app.studi1hub.Request.DeleteCache.deleteCache;

public class AdminProjectFragment extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    Animation fabOpen,fabClose, rotateForword,rotateBackward;
    FloatingActionButton fab,fab1,fab2;
    boolean isOpen=false;
    Fragment fragment=null;
    String login,isbtnclick,fromnav,username,menukey,status2,tmp;
    TextView tvstatus;
    ListView lvproject;
    ProgressBar pbar;
    int status;
    public static final String[] STATUS = {"Received", "Assigned", "Req.Material", "Inprogress", "Wait Approval", "Correction","Complete","Cancel"};
    public static final String[] CUST_STATUS = {"Uploaded", "Uploaded", "Req.Material", "Inprogress", "Received", "Inprogress","Complete","Cancel"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_project,null);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction ft = fragmentManager.beginTransaction();

        editor = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE).edit();

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        login = prefs.getString("logintype", null);
        isbtnclick= prefs.getString("isbtnclick", "0");
        fromnav= prefs.getString("fromnav", "0");
        status= prefs.getInt("projectkey", 0);
        status2= String.valueOf(status);
        menukey = prefs.getString("menukey", null);

        lvproject=(ListView)view.findViewById(R.id.lvproject);
        fab = view.findViewById(R.id.fab);
        fab1 = view.findViewById(R.id.fab1);
        fab2 = view.findViewById(R.id.fab2);
        pbar = (ProgressBar)view.findViewById(R.id.pbar);

        tvstatus=(TextView)view.findViewById(R.id.tvstatus);
 
        lvproject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lid = ((TextView) view.findViewById(R.id.tvpid)).getText().toString();
                String isReceived = ((TextView) view.findViewById(R.id.tvstatus)).getText().toString();
                if (login.equals("employee"))
                {
                    fragment = new ViewProjectFragment();
                    Bundle args = new Bundle();
                    args.putString("idkey", lid);
                    fragment.setArguments(args);
                    ft.replace(R.id.screen_area, fragment, "fragment three");
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else if (login.equals("customer"))
                {
                    editor.putString("fromtextclick", "1");
                    editor.apply();
                    fragment = new AddProjectViaCustFragment();
                    Bundle args = new Bundle();
                    args.putString("idkey", lid);
                    fragment.setArguments(args);
                    ft.replace(R.id.screen_area, fragment, "fragment three");
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else {
                    editor.putString("fromtextclick", "1");
                    editor.apply();
                    fragment = new AddProjectFragment();
                    Bundle args = new Bundle();
                    args.putString("idkey", lid);
                    if (isReceived.equals("Received"))
                    {
                        args.putBoolean("isReceived", true);
                    }
                    else
                    {
                        args.putBoolean("isReceived", false);
                    }
                    fragment.setArguments(args);
                    ft.replace(R.id.screen_area, fragment, "fragment three");
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });


        fabOpen= AnimationUtils.loadAnimation(getActivity(),R.anim.fab_open);
        fabClose= AnimationUtils.loadAnimation(getActivity(),R.anim.fab_close);
        rotateForword= AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_forward);
        rotateBackward= AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_backword);

        if (login.equals("employee") || login.equals("customer") || isbtnclick.equals("1"))
        {
            username = prefs.getString("username", null);
            if (!fromnav.equals("1")) {
               getList();
            }else {
                editor.putString("fromnav", "0");
                editor.apply();
                tvstatus.setVisibility(View.GONE);
                lvproject.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                getListNav();
            }
        }
        else
        {
            getAllList();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                editor.putString("fromtextclick", "0");
                editor.apply();
                final FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction t = fragmentManager.beginTransaction();
                if (login.equals("customer"))
                {
                    fragment = new AddProjectViaCustFragment();
                    t.replace(R.id.screen_area, fragment,"fragment three");
                    t.addToBackStack(null);
                    t.commit();
                }
                else
                {
                    fragment = new AddProjectFragment();
                    t.replace(R.id.screen_area, fragment,"fragment three");
                    t.addToBackStack(null);
                    t.commit();
                }
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                Toast.makeText(getActivity(), "All Projects", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getList()
    {
        deleteCache(getActivity());
        editor.putString("listkey", "APF");
        editor.apply();
        final Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                List<subjects> subjectsList;
                Context context = getActivity();
                try {
                    jsonArray = new JSONArray(response);
                    JSONObject jsonObject;
                    subjects subjects;
                    subjectsList = new ArrayList<subjects>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        subjects = new subjects();
                        jsonObject = jsonArray.getJSONObject(i);
                        subjects.Spid = jsonObject.getString("pID");
                        subjects.Stitle = jsonObject.getString("pTitle");
                        if (login.equals("customer"))
                        {
                            subjects.Sstatus = CUST_STATUS[jsonObject.getInt("pStatus")];
                        }
                        else
                        {
                            subjects.Sstatus = STATUS[jsonObject.getInt("pStatus")];
                        }
                        subjectsList.add(subjects);
                    }
                    pbar.setVisibility(View.GONE);
                    if (subjectsList != null) {
                        ListAdapterClass adapter = new ListAdapterClass(subjectsList, context);
                        lvproject.setAdapter(adapter);
                    }
                } catch (JSONException e1) {
                    pbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
                    e1.printStackTrace();
                }
            }
        };
        AdminProjectRequest adminProjectRequest = new AdminProjectRequest(username, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(adminProjectRequest);
    }

    public void getListNav()
    {
        deleteCache(getActivity());
        editor.putString("listkey", "APNF");
        editor.apply();
        final Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                List<subjects> subjectsList;
                Context context = getActivity();
                try {
                    jsonArray = new JSONArray(response);
                    JSONObject jsonObject;
                    subjects subjects;
                    subjectsList = new ArrayList<subjects>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        subjects = new subjects();
                        jsonObject = jsonArray.getJSONObject(i);
                        subjects.Spid = jsonObject.getString("pID");
                        subjects.Stitle=jsonObject.getString("pTitle");
                        subjectsList.add(subjects);
                    }
                    pbar.setVisibility(View.GONE);
                    if (subjectsList != null) {
                        ListAdapterClass adapter = new ListAdapterClass(subjectsList, context);
                        lvproject.setAdapter(adapter);
                    }
                } catch (JSONException e1) {
                    pbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
                    e1.printStackTrace();
                }
            }
        };
        AdminProjectNavRequest adminProjectNavRequest = new AdminProjectNavRequest(username,status2, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(adminProjectNavRequest);
    }



    public void getAllList()
    {
        deleteCache(getActivity());
        editor.putString("listkey", "APF");
        editor.apply();
        final Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                List<subjects> subjectsList;
                Context context = getActivity();
                try {
                    jsonArray = new JSONArray(response);
                    JSONObject jsonObject;
                    subjects subjects;
                    subjectsList = new ArrayList<subjects>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        subjects = new subjects();
                        jsonObject = jsonArray.getJSONObject(i);
                        subjects.Spid = jsonObject.getString("pID");
                        subjects.Stitle = jsonObject.getString("pTitle");
                        if (login.equals("customer"))
                        {
                            subjects.Sstatus = CUST_STATUS[jsonObject.getInt("pStatus")];
                        }
                        else
                        {
                            subjects.Sstatus = STATUS[jsonObject.getInt("pStatus")];
                        }
                        subjectsList.add(subjects);
                    }
                    pbar.setVisibility(View.GONE);
                    if (subjectsList != null) {
                        ListAdapterClass adapter = new ListAdapterClass(subjectsList, context);
                        lvproject.setAdapter(adapter);
                    }
                } catch (JSONException e1) {
                    pbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
                    e1.printStackTrace();
                }
            }
        };
        AdminAllProjectRequest adminAllProjectRequest = new AdminAllProjectRequest(responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(adminAllProjectRequest);
    }

    public void animateFab()
    {
        if (isOpen) {
            fab.startAnimation(rotateForword);
            fab1.startAnimation(fabClose);
            fab2.startAnimation(fabClose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isOpen = false;
        }
        else
        {
            fab.startAnimation(rotateBackward);
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isOpen = true;
        }
    }

}
