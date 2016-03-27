package com.jason.addressbook.tools;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import com.jason.addressbook.R;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Jason on 2016/3/11.
 */
public class ContactManager {
    /**获取库Phon表字段**/
    private static final String[] PHONES_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Contacts.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID };
    /**联系人显示名称**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**电话号码**/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**头像ID**/
    private static final int PHONES_PHOTO_ID_INDEX = 2;

    /**联系人的ID**/
    private static final int PHONES_CONTACT_ID_INDEX = 3;

    /**手机联系人名称**/
    private ArrayList<String> PhoneContactsName = new ArrayList<String>();

    /**手机联系人头像**/
    private ArrayList<String> PhoneContactsNumber = new ArrayList<String>();

    /**手机联系人头像**/
    private ArrayList<Bitmap> PhoneContactsPhoto = new ArrayList<Bitmap>();
    /**Sim卡联系人名称**/
    private ArrayList<String> SimContactsName = new ArrayList<String>();

    /**Sim卡联系人头像**/
    private ArrayList<String> SimContactsNumber = new ArrayList<String>();

    /**Sim卡联系人头像**/
    private ArrayList<Bitmap> SimContactsPhonto = new ArrayList<Bitmap>();
    Context mContext;
    ContactManager(){

    }
    public ContactManager(Context context){
        this.mContext = context;

    }
    public ArrayList<String> getPhoneContactsName(){
        getPhoneContacts();
        return PhoneContactsName;
    }
    public ArrayList<String> getPhoneContactsNumber(){
        getPhoneContacts();
        return PhoneContactsNumber;
    }
    public ArrayList<Bitmap> getPhoneContactsPhonto(){
        getPhoneContacts();
        return PhoneContactsPhoto;
    }
    public ArrayList<String> getSimContactsName(){
        getSIMContacts();
        return SimContactsName;
    }
    public ArrayList<String> getSimContactsNumber(){
        getSIMContacts();
        return SimContactsNumber;
    }
    public ArrayList<Bitmap> getSimContactsPhonto(){
        getSIMContacts();
        return SimContactsPhonto;
    }

    /**得到手机通讯录联系人信息**/
    private void getPhoneContacts() {
        if(PhoneContactsName.isEmpty()) {
            ContentResolver resolver = mContext.getContentResolver();

            // 获取手机联系人
            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);


            if (phoneCursor != null) {
                while (phoneCursor.moveToNext()) {

                    //得到手机号码
                    String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                    //当手机号码为空的或者为空字段 跳过当前循环
                    if (TextUtils.isEmpty(phoneNumber))
                        continue;

                    //得到联系人名称
                    String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                    //得到联系人ID
                    Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

                    //得到联系人头像ID
                    Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

                    //得到联系人头像Bitamp
                    Bitmap contactPhoto = null;

                    //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                    if (photoid > 0) {
                        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
                        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
                        contactPhoto = BitmapFactory.decodeStream(input);
                    } else {
                        contactPhoto = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.contact_photo);
                    }

                    PhoneContactsName.add(contactName);
                    PhoneContactsNumber.add(phoneNumber);
                    PhoneContactsPhoto.add(contactPhoto);
                }

                phoneCursor.close();
            }
        }else{

        };
    }

    /**得到手机SIM卡联系人人信息**/
    private void getSIMContacts() {
        if(SimContactsName.isEmpty()) {
            ContentResolver resolver = mContext.getContentResolver();
            // 获取Sims卡联系人
            Uri uri = Uri.parse("content://icc/adn");
            Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
                    null);

            if (phoneCursor != null) {
                while (phoneCursor.moveToNext()) {

                    // 得到手机号码
                    String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                    // 当手机号码为空的或者为空字段 跳过当前循环
                    if (TextUtils.isEmpty(phoneNumber))
                        continue;
                    // 得到联系人名称
                    String contactName = phoneCursor
                            .getString(PHONES_DISPLAY_NAME_INDEX);

                    //Sim卡中没有联系人头像

                    SimContactsName.add(contactName);
                    SimContactsNumber.add(phoneNumber);
                }

                phoneCursor.close();
            }
        }else{}
    }
}
