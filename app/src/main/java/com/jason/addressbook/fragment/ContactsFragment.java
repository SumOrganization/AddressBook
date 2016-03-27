package com.jason.addressbook.fragment;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jason.addressbook.R;
import com.jason.addressbook.tools.ContactManager;
import com.jason.addressbook.tools.DatabaseTool;
import com.jason.addressbook.tools.RankName;

import java.io.ByteArrayOutputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Jason on 2016/3/11.
 */
public class ContactsFragment extends ListFragment {
    Context mContext;
    /**手机联系人名称**/
    private List<String> phoneContactsName = new ArrayList<String>();

    /**手机联系人号码**/
    private List<String> phoneContactsNumber = new ArrayList<String>();

    /**手机联系人头像**/
    private List<Bitmap> phoneContactsPhoto = new ArrayList<Bitmap>();

    private RankName rankName = null;
    private MyListAdapter myAdapter = null;
    private ListView list;
    SQLiteDatabase sqLiteDatabase;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
            prepareDataBase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts,container,false);


        rankName = new RankName(phoneContactsName);
        phoneContactsName =  rankName.getRankedName(phoneContactsName);

        myAdapter = new MyListAdapter(getActivity());
        list = (ListView) view.findViewById(android.R.id.list);
        list.setAdapter(myAdapter);
        return view;
    }
    private void prepareDataBase(){
        ContactManager contactManager = new ContactManager(getActivity());
        phoneContactsName = contactManager.getPhoneContactsName();
        phoneContactsPhoto = contactManager.getPhoneContactsPhonto();
        phoneContactsNumber = contactManager.getPhoneContactsNumber();

    }


    @Override
    public void onResume() {
        super.onResume();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                //调用系统方法拨打电话
                Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri
                        .parse("tel:" + phoneContactsNumber.get(position)));
                startActivity(dialIntent);
            }
        });
    }

    class MyListAdapter extends BaseAdapter {
        public MyListAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
            //设置绘制数量
            return phoneContactsName.size()-1;
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
                        R.layout.list_item, null);
                holder = new ViewHolder();
                holder.im_photo = (ImageView) convertView.findViewById(R.id.color_image);
                holder.tx_name = (TextView) convertView.findViewById(R.id.color_title);
                convertView.setTag(holder);
                //  text = (TextView) convertView.findViewById(R.id.color_text);
            }else {
                holder = (ViewHolder)convertView.getTag();

            }
            //绘制联系人名称
            holder.tx_name.setText(phoneContactsName.get(position));
            //绘制联系人号码
            // text.setText(mContactsNumber.get(position));
            //绘制联系人头像
            holder.im_photo.setImageBitmap(phoneContactsPhoto.get(position));
            return convertView;


        }
    }

    public static class ViewHolder{
        public TextView tx_name;
        public TextView tx_number;
        public ImageView im_photo;
    }
}

