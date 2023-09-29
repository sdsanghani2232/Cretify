package com.sd.certify.user.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sd.certify.R;
import com.sd.certify.admin.fragments.Admin_Home_fragment;
import com.sd.certify.admin.fragments.Admin_Search_fragment;
import com.sd.certify.admin.fragments.Event_fragment;
import com.sd.certify.admin.fragments.Profile_fragment;
import com.sd.certify.user.fragments.User_Home_fragment;
import com.sd.certify.user.fragments.User_Profile_fragment;
import com.sd.certify.user.fragments.User_notification_fragment;

public class User_Home_page extends AppCompatActivity {

    BottomNavigationView bn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uer_home_page);
        bn = findViewById(R.id.user_bottom_nev);

        bn.setOnNavigationItemSelectedListener(item -> {

            int item_id = item.getItemId();

            if (item_id == R.id.user_home)
            {
                loadFragment(new User_Home_fragment(),false);
            }
            else if (item_id == R.id.notification)
            {
                loadFragment(new User_notification_fragment(),false);
            }
            else if(item_id == R.id.user_profile)
            {
                loadFragment(new User_Profile_fragment(),false);
            }
            return true;
        });

        bn.setSelectedItemId(R.id.user_home);
    }

    private void loadFragment(Fragment fragment, boolean add) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if(add) ft.add(R.id.frame,fragment);
        else ft.replace(R.id.frame,fragment);

        ft.commit();
    }
}