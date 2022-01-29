package com.dh.nianliums.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.dh.nianliums.Module.Employee;
import com.dh.nianliums.Module.StaticData;
import com.dh.nianliums.R;
import com.dh.nianliums.Util.HttpClientTool;
import com.dh.nianliums.Util.JSONhandle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;

/**
 * activity_login：
 *     (1)实时检测输入框中的内容是否合法
 *     (2)点击登录按钮之后，获取到输入框中的内容，并将该内容交给json类封装成json文件，再由socket类发送到服务器中进行进一步判断
 *     (3)根据解析服务器返回的内容，判断是否跳转页面以及跳转到哪个页面
 *
 *
 *
 * 实现步骤：
 * 1、实例化控件对象，并进行初始化绑定
 * 2、实现登录功能：
 *      获取输入框内容；
 *      将输入框内容交给json类进行打包；
 *      根据解析的服务器反馈信息进行判断；
 *      跳转，以及不跳转输出信息；
 */
public class activity_login extends Activity {

    //实例化控件对象
    private EditText inputName;
    private EditText inputPassword;

    private ImageView loginButton;

    private TextView forgotButton;
    private TextView toLogonButton;

    //保存输入框内容
    private String name;
    private String password;

    //登录状态,"0"表示默认，"1"表示验证成功可以登录，"-1"表示验证失败
    private String vertify="0";

    StaticData staticData=new StaticData();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        //调用initView()函数进行初始化
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView(){

        //对几个控件进行初始化
        inputName=findViewById(R.id.input_name);
        inputPassword=findViewById(R.id.input_password);
        loginButton=findViewById(R.id.login_button);
        forgotButton=findViewById(R.id.forgot_button);
        toLogonButton=findViewById(R.id.to_logon_button);

        //将登录按钮默认设置为不可用
        loginButton.setEnabled(false);
        loginButton.setBackgroundResource(R.drawable.shape_unable);

        //监听
        inputName.addTextChangedListener(textWatcher);
        inputPassword.addTextChangedListener(textWatcher);
    }


    /**
     * 登录功能：获取输入框内容；将输入框内容交给json类进行打包；根据解析的服务器反馈信息进行判断；跳转，以及不跳转输出信息；
     * @param v  触发词事件的控件
     */
    public void login(View v){

        //Log.e("aa","已点击");

        name = inputName.getEditableText().toString();
        password = inputPassword.getEditableText().toString();

        //将name和password交给json类封装并发送给服务器
        Employee employee=new Employee();
        employee.setName(name);
        employee.setPassword(password);

        JSONhandle jsoNhandle=new JSONhandle();
        JSONObject jsonObject = jsoNhandle.structureJSON(employee,StaticData.LOGIN_FLAG);
        new Thread(){
            @Override
            public void run() {
                //Log.e("aa","准备连接服务器...");
                HttpClientTool tool=new HttpClientTool();
                //服务器应该返回验证状态，vertify:1,表示通过，-1表示没有通过
                JSONObject response= null;
                try {
                    //Log.e("aa","正在连接服务器...");
                    String req = tool.sendJSON(jsonObject,StaticData.WEB_PATH);//发送请求，并接收响应
                    response = new JSONObject(req);//将响应的数据转换为JSON对象
                    vertify = response.optString("vertify");//提取响应的信息
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //Log.e("bb","vertify="+vertify);
                //根据返回的数据进行判断是否可以登录
                if (vertify.equals("1")){
                    //TODO 跳转到实际页面，即登录成功
                    Log.e("bb","登录成功...");

                } else if (vertify.equals("-1")){

                    Looper.prepare();
                    Toast.makeText(activity_login.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                    Log.e("bb","登录失败...");
                    Looper.loop();
                }
                vertify="0";


            }
        }.start();
    }



    public void toLogon(View v){
        Intent intent=new Intent(activity_login.this,activity_logon.class);
        startActivity(intent);
        finish();
    }



    /**
     * 监听输入框内容是否符合要求
     * 符合要求则将登录按钮设置为可用状态
     */
    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean login1=false,login2=false;
            if(inputName.getEditableText().length()>0) login1=true;
            else login1=false;

            if (inputPassword.getEditableText().length()>=8) login2=true;
            else login2=false;

            loginButton.setEnabled(login1 && login2);
            if (login1 && login2) loginButton.setBackgroundResource(R.drawable.selector_click);
            else loginButton.setBackgroundResource(R.drawable.shape_unable);
        }
    };
}
