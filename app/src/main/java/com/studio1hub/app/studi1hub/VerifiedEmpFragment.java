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
import com.studio1hub.app.studi1hub.Request.VerifiedUserRequest;
import com.studio1hub.app.studi1hub.Request.subjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.studio1hub.app.studi1hub.Request.DeleteCache.deleteCache;

public class VerifiedEmpFragment extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    boolean isOpen=false;
    Fragment fragment=null;
    String menukey,type;
    ListView lvuser;
    ProgressBar pbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_verified_emp,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        lvuser=(ListView)view.findViewById(R.id.lvuser);
        pbar = (ProgressBar)view.findViewById(R.id.pbar);

        final FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction ft = fragmentManager.beginTransaction();

        lvuser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lid = ((TextView) view.findViewById(R.id.tvid)).getText().toString();
                editor.putString("fromtextclick", "2");
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

    }

    public void getList()
    {
        deleteCache(getActivity());
        editor.putString("listkey", "VEF");
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
                        subjects.Sverified = jsonObject.getString("uVerified");
                        subjectsList.add(subjects);
                    }
                    pbar.setVisibility(View.GONE);
                    if (subjectsList != null) {
                        ListAdapterClass adapter = new ListAdapterClass(subjectsList, context);
                        lvuser.setAdapter(adapter);
                    }
                } catch (JSONException e1) {
                    pbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
                    e1.printStackTrace();
                }
            }
        };
        VerifiedUserRequest verifiedUserRequest = new VerifiedUserRequest(type, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(verifiedUserRequest);
    }

}
