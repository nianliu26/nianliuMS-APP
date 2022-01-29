package com.dh.nianliums.Util;


import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 调用此类中的方法的时候需要创建一个线程
 * 实现和服务器的http连接，将json对象发送到服务器，以及接收服务器的json对象
 *     (1)发送数据
 *         通过String.valueOf()方法将json对象转换为字符串
 *         通过设置URL对象，该URL对象需要对应到服务器具体响应类的映射中
 *         使用URL对象的openConnection()方法获取一个HttpURLConnection对象
 *         设置HttpURLConnection对象的属性
 *         使用HttpURLConnection对象的getOutputStream()方法获得一个输出流对象
 *             注：可以通过BufferedOutputStream的构造方法将获取到的输出流对象转换为字节流对象
 *         通过字符/字节流对象的write方法将json数据写入服务器
 *         关闭输出流
 *     (2)接收数据：
 *         直接使用发送数据时创建的HttpURLConnection对象的getInputStream获取输入流对象
 *             注：也可使用BufferedInputStream的构造方法将获取到的输入流对象转换为字节流对象
 *         通过read方法读取输入流的数据
 *         将读取到的字符串通过JSONObject.fromObject()方法转换为JSONObject并返回
 */
public class HttpClientTool {

    //TODO 实际使用时请更改此地址
    String serverUrl="http://192.168.101.184:8080/";

    /***
     * 发送请求，并且将响应内容返回
     * @param json json数据
     * @param u 服务器具体地址
     * @return 返回响应结果
     */
    public String sendJSON(JSONObject json,String u){

        StringBuffer response=null;
        //Log.e("aa","准备连接到："+serverUrl+u);

        try {

            //转换json数据
            String content=String.valueOf(json);
            //创建连接
            URL url = new URL(serverUrl+u);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            //Log.e("aa","连接成功，准备发送数据...");

            //设置连接
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("User-Agent", "Fiddler");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Charset", "UTF-8");

            //获取输出流
            OutputStream os=new BufferedOutputStream(connection.getOutputStream());
            os.write(content.getBytes());
            os.close();

            //Log.e("aa","数据已发送");

            //接收响应
            if (connection.getResponseCode()!=200) return null;
            BufferedReader is=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            response=new StringBuffer();
            String line=null;
            while ((line=is.readLine())!=null){
                response.append(line);
            }

            //Log.e("bb","接收到数据："+response);

        } catch (Exception e){
            e.printStackTrace();
            Log.e("bb","网络错误..."+e.getMessage().toString());
            return null;
        }


        return response.toString();
    }


}
