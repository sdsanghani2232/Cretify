package com.sd.certify.admin.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sd.certify.R;
import com.sd.certify.admin.fragments.Admin_Home_fragment;
import com.sd.certify.admin.fragments.Admin_Search_fragment;

public class Admin_Home_Page extends AppCompatActivity {

    BottomNavigationView bn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);
        bn = findViewById(R.id.bottm_nev);

        bn.setOnNavigationItemSelectedListener(item -> {

            int item_id = item.getItemId();

            if (item_id == R.id.home)
            {
                loadFragment(new Admin_Home_fragment(),false);
            }
            else if (item_id == R.id.search_files)
            {
                loadFragment(new Admin_Search_fragment(),false);
            }
            return true;
        });

        bn.setSelectedItemId(R.id.home);
    }

    private void loadFragment(Fragment fragment, boolean add) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if(add) ft.add(R.id.frame,fragment);
        else ft.replace(R.id.frame,fragment);

        ft.commit();
    }

}
