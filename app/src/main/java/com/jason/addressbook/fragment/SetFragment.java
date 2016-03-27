package com.jason.addressbook.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jason.addressbook.activity.LoginActivity;
import com.jason.addressbook.R;

/**
 * Created by Jason on 2016/3/11.
 */
public class SetFragment extends Fragment {
    private boolean logintag = false;
    private   SharedPreferences sharedPreferences;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.set,container,false);

    }

    @Override
    public void onStart() {

        super.onStart();
        sharedPreferences = getActivity().getSharedPreferences("user_inf", Activity.MODE_PRIVATE);
        Button bt = (Button) getActivity().findViewById(R.id.bt_logout);
        if (sharedPreferences != null) {
            logintag = sharedPreferences.getBoolean("logtag", false);

        }
        if(logintag){

            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("logtag", false);
                    editor.commit();
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
            });
        }else{
            bt.setText("登录");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
            });
        }

    }

}
