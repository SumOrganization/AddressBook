package com.jason.addressbook.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jason.addressbook.Contact;
import com.jason.addressbook.R;
import com.jason.addressbook.http.HttpBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Jason on 2016/3/19.
 */
public class SignActivity extends ActionBarActivity {
    private Button bt = null;
    private EditText tx_name = null;
    private EditText tx_phNum = null;
    private EditText tx_password = null;
    private Contact newuser = new Contact();
    private String response;
    Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);
        final MyHandler handler = new MyHandler();
        getContentView();
        initToolbar();
       bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newuser.setName(tx_name.getText().toString());
                newuser.setPhoneNumber(tx_phNum.getText().toString());
                newuser.setPassword(tx_password.getText().toString());

                Thread thread = new Thread(new Runnable() {
                    public synchronized void run() {
                        Map<String, String> param = new HashMap();

                        try {
                            JSONObject  myJSONObject = new JSONObject();
                            myJSONObject.put("user_name", newuser.getName());
                            myJSONObject.put("user_phonenumber",  newuser.getPhoneNumber());
                            myJSONObject.put("user_password", newuser.getPassword());
                            myJSONObject.put("id","SIGN");
                            param.put("user_msg",myJSONObject.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        InputStream inputStream = HttpBase.doHttpTask(HttpBase.SIGN, param);
                        response = HttpBase.readresponse(inputStream);
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("response",response);
                        msg.setData(bundle);
                        handler.sendMessage(msg);

                    }
                });
                thread.start();
            }
        });
    }
    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.next);
        toolbar.setTitle(R.string.sign);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#00CED1"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SignActivity.this, LoginActivity.class);
                SignActivity.this.startActivity(intent);

            }
        });


    }
    private void getContentView()
    {
        bt = (Button) this.findViewById(R.id.bt_sign);
        tx_name = (EditText) this.findViewById(R.id.et_name);
        tx_phNum = (EditText) this.findViewById(R.id.et_phNum);
        tx_password = (EditText) this.findViewById(R.id.et_password);

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
            Toast toast = Toast.makeText(SignActivity.this,bundle.getString("response"),Toast.LENGTH_SHORT);
            toast.show();


        }
    }
}