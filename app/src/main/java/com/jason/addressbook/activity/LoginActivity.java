package com.jason.addressbook.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jason.addressbook.Contact;
import com.jason.addressbook.R;
import com.jason.addressbook.http.HttpBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Jason on 2016/3/19.
 */
public class LoginActivity extends ActionBarActivity {
    private Button bt = null;
    private EditText et_password= null;
    private EditText et_phNum = null;
    private Map<String, String> param = null;
    private Contact user = new Contact();
    private String response;
    private MyHandler handler = new MyHandler();
    private UserMessageHander userMessageHander = new UserMessageHander();
    private String phonenumber;

    Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getContentView();
        initToolbar();


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reg_password = "[a-zA-Z][a-zA-Z0-9]{5,14}";
                String reg_phonenumber = "[0-9]{11}";
                phonenumber =et_phNum.getText().toString();
                boolean flag_phonenumber =phonenumber.matches(reg_phonenumber);
                boolean flag_password = et_password.getText().toString().matches(reg_password);
                if (flag_phonenumber) {
                    if (flag_password) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public  void run() {
                                param = new HashMap();
                                param.put("user_phonenumber", et_phNum.getText().toString());
                                String response =null;
                                response = HttpBase.readresponse(HttpBase.doHttpTask(HttpBase.LOGIN, param));
                                Message msg = new Message();
                                Bundle bundle = new Bundle();
                                bundle.putString("response",response);
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                        });
                        thread.start();
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.wrongtype_password, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, R.string.fill_correctpassword, Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.next);
        toolbar.setTitle(R.string.login);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        toolbar.setBackgroundColor(Color.parseColor("#00CED1"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);

            }
        });
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener(){
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()){

                case R.id.action_sign:
                    msg += R.string.sign;
            }
            if(!msg.equals("")) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,SignActivity.class);
                LoginActivity.this.startActivity(intent);
            }
            return true;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    private void getContentView()
    {
        bt = (Button) this.findViewById(R.id.bt_login);
        et_password = (EditText) this.findViewById(R.id.et_password_login);
        et_phNum = (EditText) this.findViewById(R.id.et_phNum_login);

    }

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

            if (result.equals("null"))
                Toast.makeText(LoginActivity.this, R.string.notfinduser, Toast.LENGTH_SHORT).show();
             else if (et_password.getText().toString().equals(result)) {
                Thread thread = new Thread(new Runnable() {
                    public synchronized void run() {
                        Map<String, String> param = new HashMap();
                        param.put("user_phonenumber", et_phNum.getText().toString());
                        response = HttpBase.readresponse(HttpBase.doHttpTask(HttpBase.GETSELFMSG, param));
                        Log.i("sum", "response="+response);
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("response",response);
                        msg.setData(bundle);
                        userMessageHander.sendMessage(msg);
                    }
                });
                thread.start();

            }

            else
                Toast.makeText(LoginActivity.this, R.string.wrong_password, Toast.LENGTH_SHORT).show();
        }
    }
    class UserMessageHander extends Handler {
        public UserMessageHander() {
        }

        public UserMessageHander(Looper L) {
            super(L);
        }
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            JSONObject json ;
            Bundle bundle = msg.getData();
            String result = bundle.getString("response");
            try {
                json= new JSONObject(result);
                SharedPreferences sharedPreferences = getSharedPreferences("user_inf",Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor =sharedPreferences.edit();
                editor.putBoolean("logtag",true);
                editor.putString("user_id", json.getString("user_id"));
                editor.putString("user_name",json.getString("user_name") );
                editor.putString("user_phonenumber",et_phNum.getText().toString());
                editor.commit();
            } catch (JSONException e) {

                e.printStackTrace();
            }

                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();

        }
    }

}
