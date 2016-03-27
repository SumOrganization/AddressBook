package com.jason.addressbook.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jason.addressbook.Contact;
import com.jason.addressbook.R;
import com.jason.addressbook.tools.ContactManager;
import com.jason.addressbook.http.HttpBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jason on 2016/3/21.
 */
public class UserfriendActivity extends ActionBarActivity {

    public List<String> phoneContactsNumber = new ArrayList<String>();
    public List<Contact> myfriends =new ArrayList<>();
    Map<String, String> param = new HashMap<>();
    MyHandler handler = new MyHandler();
    Context mContext = null;
    private JSONArray friends_info;
    private String phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_friends);
        initToolbar();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        phonenumber = bundle.getString("phonenumber");
        ContactManager contactManager = new ContactManager(this);
        phoneContactsNumber = contactManager.getPhoneContactsNumber();

            Thread thread = new Thread(new Runnable() {
                @Override
                public synchronized void run() {

                    param.put("user_phonenumber", phonenumber);
                    String s = HttpBase.readresponse(HttpBase.doHttpTask(HttpBase.GETFRIENDLIST, param));
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("response",s);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                    }
            });
            thread.start();

    }
    public void  initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.next);
        toolbar.setTitle(R.string.back);
        toolbar.setTitle(R.string.managefriends);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        toolbar.setBackgroundColor(Color.parseColor("#00CED1"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(UserfriendActivity.this, MainActivity.class);
                UserfriendActivity.this.startActivity(intent);
                UserfriendActivity.this.finish();

            }
        });
    }
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener(){
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()){

                case R.id.action_addfriend:
                    msg += R.string.addfriend;
            }
            if(!msg.equals("")) {
                Intent intent = new Intent();
                intent.setClass(UserfriendActivity.this, AddfriendsActivity.class);

               intent.putParcelableArrayListExtra("MyfriendsList",(ArrayList<Contact>) myfriends);
                UserfriendActivity.this.startActivity(intent);
            }
            return true;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friendmessage, menu);
        return true;
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
            try {
                friends_info = new JSONArray(result);
                if(friends_info.length()==0)
                {
                    TextView tv = (TextView) UserfriendActivity.this.findViewById(R.id.tv_nofriend);

                    tv.setVisibility(View.VISIBLE);
                }
                else {
                    TextView tv = (TextView) UserfriendActivity.this.findViewById(R.id.tv_friendlist);
                    tv.setVisibility(View.VISIBLE);
                    ListView listView = (ListView) UserfriendActivity.this.findViewById(R.id.list_friends);
                    MyListAdapter adapter = new MyListAdapter(UserfriendActivity.this);
                    listView.setAdapter(adapter);

                    for(int i=0;i<friends_info.length();i++) {
                        try {
                            JSONObject friend_info = friends_info.getJSONObject(i);
                            Contact friend = new Contact();
                            friend.setId(friend_info.getString("friend_id"));
                            friend.setName(friend_info.getString("friend_name"));
                            friend.setPhoneNumber(friend_info.getString("friend_phone"));
                            myfriends.add(friend);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    class MyListAdapter extends BaseAdapter {

        public MyListAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
            //设置绘制数量
            return friends_info.length();
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.listitem_friend, null);
                holder = new ViewHolder();
                holder.tx_name = (TextView) convertView.findViewById(R.id.friend_name);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            try {
                JSONObject friend_info = friends_info.getJSONObject(position);

                holder.tx_name.setText(friend_info.getString("friend_name"));
                holder.tx_name.setOnClickListener(new MyButtonListener(friend_info));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return convertView;
        }
    }

    public static class ViewHolder{
        private TextView tx_name;

    }
    class MyButtonListener implements View.OnClickListener {
        private JSONObject friend_info;

        MyButtonListener(JSONObject jsonObject) {
            friend_info = jsonObject;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserfriendActivity.this,FriendMsgActivity.class);
            Bundle bundle = new Bundle();
            try {
                bundle.putString("phonenumber",friend_info.getString("friend_phone"));
                bundle.putString("name",friend_info.getString("friend_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            intent.putExtra("bundle", bundle);
            startActivity(intent);
        }
    }
}
