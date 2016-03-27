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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jason.addressbook.Contact;
import com.jason.addressbook.R;
import com.jason.addressbook.http.HttpTool;
import com.jason.addressbook.tools.ContactManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 2016/3/23.
 */
public class AddfriendsActivity extends ActionBarActivity{
    private List<String> phoneContactsNumber = new ArrayList<String>();
    private List<String> contantsSignedPhonenumber =new ArrayList<>();
    public List<Contact> myfriend_now =new ArrayList<>();
    public List<Contact> notmyfriend = new ArrayList<>();
 private MyHandler handler = new MyHandler();
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriends);
        initToolbar();
        Intent intent = this.getIntent();
        myfriend_now =intent.getParcelableArrayListExtra("MyfriendsList");
        ContactManager contactManager = new ContactManager(this);
        phoneContactsNumber = contactManager.getPhoneContactsNumber();

        for(final String phonenumber:phoneContactsNumber)
        {
            Thread thread = new Thread(new Runnable() {
                @Override
                public synchronized void run() {
                        String number = phonenumber.replaceAll(" ", "");
                        String reg_password = "[1][0-9]{10}";
                        if(number.matches(reg_password)){
                           String s = HttpTool.judgefriend(number);
                            if (s.equals("true")) {
                                contantsSignedPhonenumber.add(phonenumber);
                                Message msg = new Message();
                                Bundle bundle = new Bundle();
                                bundle.putString("phonenumber",phonenumber);
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                        }
                }
            });
            thread.start();
        }

    }
    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.next);
        toolbar.setTitle(R.string.back);
        toolbar.setTitle(R.string.managefriends);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#00CED1"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(AddfriendsActivity.this, MainActivity.class);
                AddfriendsActivity.this.startActivity(intent);
                AddfriendsActivity.this.finish();
            }
        });
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
            String phonenumber = bundle.getString("phonenumber");

            if(contantsSignedPhonenumber.isEmpty()) {
                TextView tv = (TextView) AddfriendsActivity.this.findViewById(R.id.tv_nonewfriend);
               tv.setVisibility(View.VISIBLE);
            }
            else{
                TextView tv = (TextView) AddfriendsActivity.this.findViewById(R.id.tv_newfriendlist);
                tv.setVisibility(View.VISIBLE);
                ListView listView = (ListView) AddfriendsActivity.this.findViewById(R.id.list_newfriends);
                boolean flag =true;
                for(Contact friend:myfriend_now) {
                    if(phonenumber.equals(friend.getPhoneNumber())) {
                        flag =false;
                    }
                    if(flag){
                        Contact contact = new Contact();
                        contact.setPhoneNumber(phonenumber);
                        notmyfriend.add(contact);
                    }
                }
                MyListAdapter adapter = new MyListAdapter(AddfriendsActivity.this);
                listView.setAdapter(adapter);
            }
        }
    }
    class MyListAdapter extends BaseAdapter {

        public MyListAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
            return notmyfriend.size();
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
                        R.layout.listitem_newfriend, null);
                holder = new ViewHolder();
                holder.tx_number = (TextView) convertView.findViewById(R.id.newfriend_phonenumber);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
                holder.tx_number.setText(notmyfriend.get(position).getPhoneNumber());
            return convertView;
        }
    }

    public static class ViewHolder{
        private TextView tx_number;
    }
}
