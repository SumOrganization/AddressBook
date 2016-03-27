package com.jason.addressbook.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import com.astuetz.PagerSlidingTabStrip;
import com.jason.addressbook.R;
import com.jason.addressbook.fragment.ContactsFragment;
import com.jason.addressbook.fragment.MyInfoFragment;
import com.jason.addressbook.fragment.SetFragment;

public class MainActivity extends ActionBarActivity {
    ;
    private ContactsFragment contactsFragment = new ContactsFragment();
    private MyInfoFragment myInfoFragment = new MyInfoFragment();
    private SetFragment setFragment = new SetFragment();
    private static final int TAB_INDEX_COUNT = 3;

    private static final int TAB_INDEX_ONE = 0;
    private static final int TAB_INDEX_TWO = 1;
    private static final int TAB_INDEX_THREE = 2;

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private  String user_phonenumber;
    private  String user_id;
    private  String user_name;
    private boolean logintag;
    private Toolbar toolbar= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUserMsg();
        initViewPAger();
        initToolbar();
        ConnectivityManager con=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if(wifi|internet){

        }else{
            Toast.makeText(this,
                    "亲，网络连了么？", Toast.LENGTH_LONG)
                    .show();
        }
    }
    private void initUserMsg() {
        SharedPreferences sharedPreferences =getSharedPreferences("user_inf", Activity.MODE_PRIVATE);
        if (sharedPreferences != null) {
            logintag =sharedPreferences.getBoolean("logtag", false);
            user_id = sharedPreferences.getString("user_id", null);
            user_name = sharedPreferences.getString("user_name", null);
            user_phonenumber = sharedPreferences.getString("user_phonenumber", null);

        }

    }
    private void initViewPAger(){
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mViewPagerAdapter);
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

    }
    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.next);
        if(logintag)
            toolbar.setTitle(user_name);
        else
            toolbar.setTitle(R.string.logintosync);

        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#00CED1"));
        if(!logintag) {

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,LoginActivity.class);
                    MainActivity.this.startActivity(intent);
                }
            });
        }else {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, UserfriendActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("phonenumber",user_phonenumber);
                    intent.putExtra("bundle",bundle);
                    MainActivity.this.startActivity(intent);
                }
            });
        }
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {

            switch (position) {
                case TAB_INDEX_ONE:
                   return contactsFragment;
                case TAB_INDEX_TWO:
                    return myInfoFragment;
                case TAB_INDEX_THREE:
                    return setFragment;

            }
            throw new IllegalStateException("No fragment at position " + position);
        }

        @Override
        public int getCount() {
            return TAB_INDEX_COUNT;
        }


        public CharSequence getPageTitle(int position) {
            String tabLabel = null;
            switch (position) {
                case TAB_INDEX_ONE:
                    tabLabel = getString(R.string.tab_1);
                    break;
                case TAB_INDEX_TWO:
                    tabLabel = getString(R.string.tab_2);
                    break;
                case TAB_INDEX_THREE:
                    tabLabel = getString(R.string.tab_3);
                    break;
            }
            return tabLabel;
        }
    }



}










