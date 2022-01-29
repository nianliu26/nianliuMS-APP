package com.dh.nianliums.Util;


import android.util.Log;

import com.dh.nianliums.Module.StaticData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

/**
 * JSON文件处理类，负责将字符串内容构造成JSON对象，以及解析服务器发送过来的JSON对象
 *     (1)构造json对象：
 *         创造一个空的JSONObject对象,通过jsonobject.put(k,v)将数据放入对象中
 *         通过JSONObject.toJSONString(JSONObject)将JSON对象转换成字符串
 *         通过StringEntity 的构造方法设置JSON字符串的编码格式为utf-8,然后返回
 *     (2)解析json对象：
 *         使用Gson库对其进行解析，解析的数据放到Module包中对应的对象之中，然后返回
 */
public class JSONhandle {

    private Gson gson=new Gson();


    //构建一个json格式的字符串用于发送

    /**
     * 通过反射获取对象的所有属性以及get方法
     * 再将获取到的数据一一put到JSONObject中
     * @param object 一个实体对象
     * @param handleFlag 标识一个操作标识符，指定此操作的标识
     * @return 返回一个JSONObject对象
     */
    public static JSONObject structureJSON(Object object,int handleFlag){

        JSONObject jsonObject=new JSONObject();

        String name=null;
        String methodName=null;

        Field[] fields=object.getClass().getDeclaredFields();//获取该类的所有属性
        //找到所有属性的命名，并获取其所有get方法的值，再全部放入jsonObject中
        try {
            jsonObject.put("handleFlag",handleFlag);

            for (Field field:fields){
                name=field.getName();
                methodName ="get"+name.substring(0,1).toUpperCase().concat(name.substring(1));//将属性首字母大写第一个

                Method method=object.getClass().getMethod(methodName);
                jsonObject.put(name,method.invoke(object));//构造json对象

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Log.e("封装JSON","封装好的数据："+jsonObject.toString());

        return jsonObject;
    }

    /**
     * 重载方法
     * @param object 一个实体
     * @param strings 实体属性对应的要赋的值
     * @param handleFlag 请求标志位
     * @return
     */
    public static JSONObject structureJSON(Object object,String[] strings,int handleFlag){
        JSONObject jsonObject=new JSONObject();

        String name=null;

        Field[] fields=object.getClass().getDeclaredFields();//获取该类的所有属性

        try {
            jsonObject.put("handleFlag",handleFlag);
            for (int i=0;i<strings.length;i++){
                name=fields[i+1].getName();
                jsonObject.put(name,strings[i]);
            }
        } catch (Exception e){
            e.getMessage();
        }



        return jsonObject;
    }




    /**
     * 使用Gson库，解析json对象
     * @param jsonObject 一个json对象
     * @param c 一个需要转换的示例的类名
     * @return 返回一个实体对象
     */
    private Object analysisJSON(JSONObject jsonObject,Class c){

        Object o=gson.fromJson(String.valueOf(jsonObject),c);

        return o;
    }



    public static final JSONObject imageToJSON(String image,String imageName){
        JSONObject imageJSON=new JSONObject();
        try {
            imageJSON.put("handleFlag", StaticData.getImageFlag());
            imageJSON.put("image",image);
            imageJSON.put("imageName",imageName);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


        return imageJSON;
    }
}
