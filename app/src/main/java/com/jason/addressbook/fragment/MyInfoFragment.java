package com.jason.addressbook.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.jason.addressbook.NetBroadcastReceiver.netEventHandler;

import com.jason.addressbook.R;
import com.jason.addressbook.http.HttpBase;
import com.jason.addressbook.http.HttpTool;
import com.jason.addressbook.tools.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jason on 2016/3/11.
 */
public class MyInfoFragment extends Fragment implements netEventHandler{
    private EditText et_name = null ;
    private EditText et_phnum = null;
    private  TextView hint_login = null;
    private  View view = null;
    private Button bt_acc = null;
    private  Spinner sp_sex = null;
    private boolean logintag;
    private String response = null;
    private  String contact_phonenumber;
    private  String user_name;
    private MyHandler handler = new MyHandler();
    private  JSONObject json;
    private String user_sex = "男";
    private  String user_id =null;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_inf", Activity.MODE_PRIVATE);
       if (sharedPreferences != null) {
            logintag = sharedPreferences.getBoolean("logtag", false);
            contact_phonenumber = sharedPreferences.getString("user_phonenumber", null);
            user_name = sharedPreferences.getString("user_name",null);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.myinformation,container,false);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        getWidget();

        if(logintag) {

            ConnectivityManager con=(ConnectivityManager)getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
            boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
            boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
            if(wifi|internet){
                et_phnum.addTextChangedListener(textWatcher);
                et_name.addTextChangedListener(textWatcher);

                Thread thread = new Thread(new Runnable() {
                    public synchronized void run() {
                        response = HttpTool.getMyInf(contact_phonenumber);
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("response", response);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                });
                thread.start();
            }else{

                 et_name.setText(user_name);
                 et_phnum.setText(contact_phonenumber);
            }


        }

    }


    private void  getWidget(){

        et_name = (EditText) getActivity().findViewById(R.id.et_name);
        et_phnum = (EditText) getActivity().findViewById(R.id.et_phNum);
        sp_sex = (Spinner) getActivity().findViewById(R.id.sp_sex);
        hint_login = (TextView) getActivity().findViewById(R.id.hint_login);
        bt_acc = (Button) getActivity().findViewById(R.id.bt_acc);

        if(logintag)
        hint_login.setVisibility(View.GONE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_spinner_item);
        String sex[] = getResources().getStringArray(R.array.sex);//资源文件
        for (int i = 0; i < sex.length; i++) {
            adapter.add(sex[i]);
        }
        adapter.setDropDownViewResource(R.layout.mdropdown);
        sp_sex.setAdapter(adapter);
       sp_sex.setOnItemSelectedListener(new MyItemSelectListenner());


    }

    @Override
    public void onNetChange() {
        et_phnum.addTextChangedListener(textWatcher);
        et_name.addTextChangedListener(textWatcher);
        if (NetUtil.getNetworkState(getActivity()) == NetUtil.NETWORN_NONE) {
           Toast.makeText(getActivity(),"111111111111111111",Toast.LENGTH_SHORT);
        }else {
            Toast.makeText(getActivity(), "222222222222222", Toast.LENGTH_SHORT);
        }
    }

    class MyItemSelectListenner implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(user_sex.equals(sp_sex.getSelectedItem().toString())){
                    bt_acc.setClickable(false);
                }else{
                    bt_acc.setClickable(true);
                }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    class MyClickListenner implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {


                    String name = et_name.getText().toString();
                    String phonenumber = et_phnum.getText().toString();
                    String sex = sp_sex.getSelectedItem().toString();

                    String s = HttpTool.modMyInf(user_id,name,phonenumber,sex);
                }
            });
            thread.start();

        }
    }
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {

            try {
                if(!(et_name.getText().toString().equals(json.getString("user_name")))){
                    bt_acc.isClickable();
                    bt_acc.setOnClickListener(new MyClickListenner());

                }else if(!(et_phnum.getText().toString().equals(contact_phonenumber))){
                    bt_acc.isClickable();
                    bt_acc.setOnClickListener(new MyClickListenner());
                }else{
                    bt_acc.setClickable(false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {



        }
    };

    class MyHandler extends Handler {
        public MyHandler() {
        }

        public MyHandler(Looper L) {
            super(L);
        }
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            Bundle bundle = msg.getData();
            String result = bundle.getString("response");
            try {
                 json= new JSONObject(result);
                 et_name.setText(json.getString("user_name"));
                 et_phnum.setText(contact_phonenumber);
                 user_sex = json.optString("user_sex","男");
                 user_id = json.getString("user_id");
                 if(json.getString("user_sex").equals("女")){
                    sp_sex.setSelection(2);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
