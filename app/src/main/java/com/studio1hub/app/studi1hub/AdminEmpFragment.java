package com.studio1hub.app.studi1hub;

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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.studio1hub.app.studi1hub.Request.AdminUserRequest;
import com.studio1hub.app.studi1hub.Request.ListAdapterClass;
import com.studio1hub.app.studi1hub.Request.subjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.studio1hub.app.studi1hub.Request.DeleteCache.deleteCache;

public class AdminEmpFragment extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    Animation fabOpen,fabClose, rotateForword,rotateBackward;
    FloatingActionButton fab,fab1,fab2;
    boolean isOpen=false;
    Fragment fragment=null;
    String menukey,type;
    ListView lvuser;
    ProgressBar pbar;
    ShimmerFrameLayout mShimmerViewContainer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_emp,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmerAnimation();

        editor = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE).edit();

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        menukey = prefs.getString("menukey", null);

        if (menukey.equals("employee"))
        {
            type="E";
            getList();
        }
        else if (menukey.equals("customer"))
        {
            type="C";
            getList();
        }

        editor.putString("fromtextclick", "0");
        editor.apply();

        fab = view.findViewById(R.id.fab);
        fab1 = view.findViewById(R.id.fab1);
        fab2 = view.findViewById(R.id.fab2);
        lvuser=(ListView)view.findViewById(R.id.lvuser);
        pbar = (ProgressBar)view.findViewById(R.id.pbar);

        fabOpen= AnimationUtils.loadAnimation(getActivity(),R.anim.fab_open);
        fabClose= AnimationUtils.loadAnimation(getActivity(),R.anim.fab_close);
        rotateForword= AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_forward);
        rotateBackward= AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_backword);

        final FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction ft = fragmentManager.beginTransaction();

        lvuser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lid = ((TextView) view.findViewById(R.id.tvid)).getText().toString();
                editor.putString("fromtextclick", "1");
                editor.apply();
                fragment = new AddEmpFragment();
                Bundle args = new Bundle();
                args.putString("idkey", lid);
                fragment.setArguments(args);
                ft.replace(R.id.screen_area, fragment,"fragment three");
                ft.addToBackStack(null);
                ft.commit();
            }
        });

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
                editor.putString("fillformkey", "employee");
                editor.apply();
                fragment = new AddEmpFragment();
                ft.replace(R.id.screen_area, fragment,"fragment three");
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                Toast.makeText(getActivity(), "All Employees", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getList()
    {
        deleteCache(getActivity());
        editor.putString("listkey", "AEF");
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
                        subjects.Sid = jsonObject.getString("uID");
                        subjects.Sname = jsonObject.getString("uFName");
                        subjects.Scproject = jsonObject.getString("pTitle");
                        subjectsList.add(subjects);
                    }
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    pbar.setVisibility(View.GONE);
                    if (subjectsList != null) {
                        ListAdapterClass adapter = new ListAdapterClass(subjectsList, context);
                        lvuser.setAdapter(adapter);
                    }
                } catch (JSONException e1) {
                    pbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Empty"+e1, Toast.LENGTH_SHORT).show();
                    e1.printStackTrace();
                }
            }
        };
        AdminUserRequest adminUserRequest = new AdminUserRequest(type, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(adminUserRequest);
    }

    public void animateFab()
    {
        if (isOpen) {
            fab.startAnimation(rotateBackward);
            fab1.startAnimation(fabClose);
            fab2.startAnimation(fabClose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isOpen = false;
        }
        else
        {
            fab.startAnimation(rotateForword);
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isOpen = true;
        }
    }

}
