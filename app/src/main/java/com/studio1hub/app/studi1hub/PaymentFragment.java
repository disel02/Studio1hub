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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.studio1hub.app.studi1hub.Request.AdminAllPaymentRequest;
import com.studio1hub.app.studi1hub.Request.AdminAllProjectRequest;
import com.studio1hub.app.studi1hub.Request.AdminPaymentRequest;
import com.studio1hub.app.studi1hub.Request.ListAdapterClass;
import com.studio1hub.app.studi1hub.Request.subjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.studio1hub.app.studi1hub.Request.DeleteCache.deleteCache;

public class PaymentFragment extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    Animation fabOpen,fabClose, rotateForword,rotateBackward;
    FloatingActionButton fab,fab1,fab2;
    boolean isOpen=false;
    Fragment fragment=null;
    String login,isbtnclick,fromnav;
    TextView tvpyid,tvpyname,tvpyamount;
    ProgressBar pbar;
    ListView lvpayment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment,null);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editor = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE).edit();

        final FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction ft = fragmentManager.beginTransaction();

        fab = view.findViewById(R.id.fab);
        fab1 = view.findViewById(R.id.fab1);
        fab2 = view.findViewById(R.id.fab2);
        pbar = (ProgressBar)view.findViewById(R.id.pbar);

        tvpyid=(TextView)view.findViewById(R.id.tvpyid);
        tvpyname=(TextView)view.findViewById(R.id.tvpyname);
        tvpyamount=(TextView)view.findViewById(R.id.tvpyamount);

        lvpayment=(ListView)view.findViewById(R.id.lvpayment);

        fabOpen= AnimationUtils.loadAnimation(getActivity(),R.anim.fab_open);
        fabClose= AnimationUtils.loadAnimation(getActivity(),R.anim.fab_close);
        rotateForword= AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_forward);
        rotateBackward= AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_backword);

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        login = prefs.getString("logintype", null);
        isbtnclick= prefs.getString("isbtnclick", "0");
        fromnav= prefs.getString("fromnav", "0");

        try {
            getAllList();
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
        }

        lvpayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lid = ((TextView) view.findViewById(R.id.tvpyid)).getText().toString();
                editor.putString("fromtextclick", "1");
                editor.apply();
                    fragment = new AddPaymentFragment();
                    Bundle args = new Bundle();
                    args.putString("idkey", lid);
                    fragment.setArguments(args);
                    ft.replace(R.id.screen_area, fragment, "fragment three");
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
                editor.putString("fromtextclick", "0");
                editor.apply();
                final FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction t = fragmentManager.beginTransaction();
                fragment = new AddPaymentFragment();
                t.replace(R.id.screen_area, fragment,"fragment three");
                t.addToBackStack(null);
                t.commit();
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

    public void getAllList()
    {
        deleteCache(getActivity());
        editor.putString("listkey", "APyF");
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
                        subjects.Spyid = jsonObject.getString("pyID");
                        subjects.Spyname = jsonObject.getString("uFName");
                        subjects.Spyamount = jsonObject.getString("pyAmount");
                        subjectsList.add(subjects);
                    }
                    pbar.setVisibility(View.GONE);
                    if (subjectsList != null) {
                        ListAdapterClass adapter = new ListAdapterClass(subjectsList, context);
                        lvpayment.setAdapter(adapter);
                    }
                } catch (JSONException e1) {
                    pbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
                    e1.printStackTrace();
                }
            }
        };
        AdminPaymentRequest adminPaymentRequest = new AdminPaymentRequest(responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(adminPaymentRequest);
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
