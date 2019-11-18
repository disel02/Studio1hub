package com.studio1hub.app.studi1hub;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.studio1hub.app.studi1hub.Request.AdminAllPaymentRequest;
import com.studio1hub.app.studi1hub.Request.ListAdapterClass;
import com.studio1hub.app.studi1hub.Request.ProjectPaymentMasterRequest;
import com.studio1hub.app.studi1hub.Request.subjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.studio1hub.app.studi1hub.Request.DeleteCache.deleteCache;

public class ProjectPaymentMasterFragment extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    Fragment fragment=null;
    String login,isbtnclick,fromnav,payment_method,cid;
    TextView tvpyid,tvpyname;
    ProgressBar pbar;
    ListView lvpayment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project_payment_master,null);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction ft = fragmentManager.beginTransaction();

        editor = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE).edit();

        pbar = (ProgressBar)view.findViewById(R.id.pbar);

        lvpayment=(ListView)view.findViewById(R.id.lvpayment);

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        login = prefs.getString("logintype", null);
        isbtnclick= prefs.getString("isbtnclick", "0");
        fromnav= prefs.getString("fromnav", "0");
        payment_method= prefs.getString("paymentkey", "0");

        cid = getArguments().getString("idkey");
        getAllList();

        lvpayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lid = ((TextView) view.findViewById(R.id.tvpyid)).getText().toString();
                editor.putString("fromtextclick", "1");
                editor.apply();
                fragment = new AddProjectFragment();
                Bundle args = new Bundle();
                args.putString("idkey", lid);
                fragment.setArguments(args);
                ft.replace(R.id.screen_area, fragment, "fragment three");
                ft.addToBackStack(null);
                ft.commit();
            }
        });


    }

    public void getAllList()
    {
        deleteCache(getActivity());
        editor.putString("listkey", "APymF");
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
                        subjects.Spyid = jsonObject.getString("pID");
                        subjects.Spyname = jsonObject.getString("pTitle");
                        subjects.Spypaid = jsonObject.getString("Paid");
                      //  subjects.Spyremain = jsonObject.getString("Remain");
                      //  subjects.Spytotal = jsonObject.getString("Total");

                        String ltotal = jsonObject.getString("Total");
                        subjects.Spytotal = ltotal;
                        String lremain = jsonObject.getString("Remain");
                        if (lremain.equals("null"))
                        {
                            subjects.Spyremain = ltotal;
                        }
                        else
                            subjects.Spyremain = lremain;

                        subjectsList.add(subjects);
                    }
                    pbar.setVisibility(View.GONE);
                    if (subjectsList != null) {
                        ListAdapterClass adapter = new ListAdapterClass(subjectsList, context);
                        lvpayment.setAdapter(adapter);
                    }
                } catch (JSONException e1) {
                    pbar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Empty !", Toast.LENGTH_SHORT).show();
                    e1.printStackTrace();
                }
            }
        };
        ProjectPaymentMasterRequest projectPaymentMasterRequest = new ProjectPaymentMasterRequest(cid,responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(projectPaymentMasterRequest);
    }

}
