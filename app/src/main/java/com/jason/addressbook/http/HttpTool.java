package com.jason.addressbook.http;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jason on 2016/3/26.
 */
public class HttpTool {
    public static String getMyInf(String phonenumber){
        String response = null;
        Map<String, String> param = new HashMap();
        param.put("user_phonenumber", phonenumber);
        response = HttpBase.readresponse(HttpBase.doHttpTask(HttpBase.GETSELFMSG, param));
        return  response;
    }
    public static String modMyInf(String id,String name,String phonenumber,String sex){
        String response = null;
        Map<String, String> param = new HashMap();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",id);
            jsonObject.put("user_name", name);
            jsonObject.put("user_phonenumber", phonenumber);
            jsonObject.put("user_sex", sex);
            jsonObject.put("id", "MODUSERMSG");
            String jsonString = jsonObject.toString();
            param.put("user_msg", jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        response = HttpBase.readresponse(HttpBase.doHttpTask(HttpBase.MODUSERMSG, param));

        return  response;
    }
    public static String getMyFriend(String phonenumber){
        String response = null;
        Map<String, String> param = new HashMap<>();
        param.put("friend_phonenumber", phonenumber);
        response = HttpBase.readresponse(HttpBase.doHttpTask(HttpBase.GETFRIENDMSG, param));

        return  response;
    }
    public static String judgefriend(String phonenumber){
        String response = null;
        Map<String, String> param = new HashMap<>();
        param.put("contact_phonenumber", phonenumber);
        response = HttpBase.readresponse(HttpBase.doHttpTask(HttpBase.JUDGEFRIEND, param));
        return  response;
    }
}
