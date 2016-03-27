package com.jason.addressbook.http;

import android.util.Log;

import com.jason.addressbook.Contact;

import org.apache.http.client.HttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Created by Jason on 2016/3/17.
 */

/**
 * 通过GET方法获取页面信息
 * 参数为对应页面的URL
 */
public class HttpBase {

    public static final String LOGIN = "LOGIN";
    public static final String SIGN = "SIGN";
    public static final String GETSELFMSG = "GETSELFMSG";
    public static final String JUDGEFRIEND = "JUDGEFRIEND";
    public static final String GETFRIENDLIST = "GETFRIENDLIST";
    public static final String GETFRIENDMSG = "GETFRIENDMSG";
    public static final String MODUSERMSG ="MODUSERMSG";

    public static InputStream doHttpTask(String TaskId, Map<String, String> param) {
        InputStream content = null;
        switch (TaskId) {
            case LOGIN: {
                String contact_phonenumber = param.get("user_phonenumber");
                content = login(contact_phonenumber);
                break;
            }
            case SIGN: {
                String user_msg = param.get("user_msg");

                content = sign(user_msg);
                break;
            }
            case GETSELFMSG: {
                String user_phonenumber = param.get("user_phonenumber");
                content = getselfmsg(user_phonenumber);
                break;
            }
            case JUDGEFRIEND:{
                String contact_phonenumber = param.get("contact_phonenumber");
                content = judgefriend(contact_phonenumber);
                break;
            }
            case GETFRIENDLIST:{
                String user_phonenumber = param.get("user_phonenumber");
                content = getfriendlist(user_phonenumber);
                break;
            }
            case GETFRIENDMSG:{
                String friend_phonenumber = param.get("friend_phonenumber");
                content = getfriendmsg(friend_phonenumber);
                break;
            }
            case MODUSERMSG:{
                String user_msg = param.get("user_msg");
                content = modusermsg(user_msg);
                break;
            }
            default:
                break;
        }
        return content;
    }

    private static InputStream sign(String s) {

        //定义输出流变量
        InputStream content = null;

         content = postmsg_json(s);


        return content;
    }
    private static InputStream postmsg_json(String s) {

        //定义输出流变量
        InputStream content = null;


        try {
            URL url = new URL("http://www.sopenapps.com/myservlet/");
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(30000);
            urlConnection.setReadTimeout(30000);

            HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;

            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestProperty("Content-type", "application/json");
            httpUrlConnection.setRequestMethod("POST");

            OutputStream outStrm = httpUrlConnection.getOutputStream();
            ObjectOutputStream objOutputStrm = new ObjectOutputStream(outStrm);

            objOutputStrm.writeObject(s);
            objOutputStrm.flush();
            objOutputStrm.close();
            content = httpUrlConnection.getInputStream();

        } catch (Exception e) {

        }
        return content;
    }

    private static InputStream modusermsg(String user_msg){
        InputStream content = null;
        content =  postmsg_json(user_msg);
        return content;
    }
    private static InputStream login(String user_phonenumber) {
        InputStream content = null;
        try {
            URL url = new URL("http://www.sopenapps.com/myservlet/?" + "id=" + LOGIN + "&user_phonenumber=" + user_phonenumber);
            content = Getmsgfromweb(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    private static InputStream getselfmsg(String user_phonenumber) {
        InputStream content = null;
        try {
            URL url = new URL("http://www.sopenapps.com/myservlet/?" + "id=" + GETSELFMSG+"&user_phonenumber="+ user_phonenumber);
            content = Getmsgfromweb(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
    public static InputStream getfriendmsg(String friend_phonenumber){
        InputStream content = null;
        try {
            URL url = new URL("http://www.sopenapps.com/myservlet/?" + "id=" + GETFRIENDMSG+"&friend_phonenumber="+ friend_phonenumber);
            content = Getmsgfromweb(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  content;
    }
    public static InputStream judgefriend(String contact_phonenumber){
        InputStream content = null;
        try {
            URL url = new URL("http://www.sopenapps.com/myservlet/?"+"id="+JUDGEFRIEND+"&contact_phonenumber="+ contact_phonenumber);
            content = Getmsgfromweb(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }
    public static  InputStream getfriendlist(String user_phonenumber ){
        InputStream content = null;
        try {
            URL url = new URL("http://www.sopenapps.com/myservlet/?"+"id="+GETFRIENDLIST+"&user_phonenumber="+user_phonenumber);
            content = Getmsgfromweb(url);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return  content;
    }
    private static InputStream Getmsgfromweb(URL url)throws IOException{
              InputStream content = null;

            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(30000);
            urlConnection.setReadTimeout(30000);

            HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;

            httpUrlConnection.setDoOutput(false);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setRequestMethod("GET");
            content = httpUrlConnection.getInputStream();


        return content;
    }

    public static String readresponse(InputStream inputStream){
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        boolean firstLine = true;
        String line = null;

        try {
            while((line = bufferedReader.readLine())!=null){
                if(!firstLine)
                    stringBuilder.append(System.getProperty("line.separator"));
                else
                    firstLine = false;
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


}
