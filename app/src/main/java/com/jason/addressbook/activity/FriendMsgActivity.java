package com.jason.addressbook.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jason.addressbook.R;
import com.jason.addressbook.http.HttpTool;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Jason on 2016/3/24.
 */
public class FriendMsgActivity extends ActionBarActivity {
    private  MyHandler handler = new MyHandler();
    private  String phonenumber;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendmsg);

        Intent intent = getIntent();
        Bundle bundle =  intent.getBundleExtra("bundle");
        phonenumber =bundle.getString("phonenumber");
        name= bundle.getString("name");
        initToolbar();
        Thread thread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                String s = HttpTool.getMyFriend(phonenumber);
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("response",s);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        });
        thread.start();
    }
    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.next);
        toolbar.setTitle(name+"的个人资料");
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#00CED1"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(FriendMsgActivity.this, UserfriendActivity.class);
                FriendMsgActivity.this.startActivity(intent);
                FriendMsgActivity.this.finish();

            }
        });
    }
    class MyHandler extends Handler{
        public MyHandler() {
            super();
        }

        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String response = bundle.getString("response");
            TextView tv_name = (TextView) FriendMsgActivity.this.findViewById(R.id.tv_friend_name);
            TextView tv_phonenumber = (TextView) FriendMsgActivity.this.findViewById(R.id.tv_friend_phNum);
            TextView tv_sex = (TextView) FriendMsgActivity.this.findViewById(R.id.tv_friend_sex);
            Log.i("sum",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String name = jsonObject.getString("friend_name");
                String sex =  jsonObject.optString("friend_sex", "");
                tv_name.setText(name);
                tv_phonenumber.setText(phonenumber);
                if(sex.equals("")){
                    tv_sex.setText("无");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
