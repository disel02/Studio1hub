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
import android.widget.Toast;

public class AdminMenuFragment extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_menu,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editor = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE).edit();

        view.findViewById(R.id.btnemp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("menukey", "employee");
                editor.apply();
                final FragmentManager fragmentManager = getFragmentManager();
                fragment = new AdminEmpFragment();
                FragmentTransaction t = fragmentManager.beginTransaction();
                t.replace(R.id.screen_area, fragment,"fragment two");
                t.addToBackStack(null);
                t.commit();
            }
        });

        view.findViewById(R.id.btncust).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("menukey", "customer");
                editor.apply();
                final FragmentManager fragmentManager = getFragmentManager();
                fragment = new AdminEmpFragment();
                FragmentTransaction t = fragmentManager.beginTransaction();
                t.replace(R.id.screen_area, fragment,"fragment two");
                t.addToBackStack(null);
                t.commit();
            }
        });

        view.findViewById(R.id.btnproject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("menukey", "project");
                editor.putString("isbtnclick", "0");
                editor.apply();
                final FragmentManager fragmentManager = getFragmentManager();
                fragment = new AdminProjectFragment();
                FragmentTransaction t = fragmentManager.beginTransaction();
                t.replace(R.id.screen_area, fragment,"fragment two");
                t.addToBackStack(null);
                t.commit();
            }
        });

        view.findViewById(R.id.btnpayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentManager fragmentManager = getFragmentManager();
                fragment = new PaymentFragment();
                FragmentTransaction t = fragmentManager.beginTransaction();
                t.replace(R.id.screen_area, fragment,"fragment two");
                t.addToBackStack(null);
                t.commit();
            }
        });
    }
}
