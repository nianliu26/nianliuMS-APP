package com.dh.nianliums.Util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PowerUitl {

/*
     READ_CALENDAR;//日历
     WRITE_CALENDAR;//日历
     CAMERA;//相机
     READ_CONTACTS;//联系人
     WRITE_CONTACTS;//联系人
     GET_ACCOUNTS;//
    位置 ACCESS_FINE_LOCATION
    位置 ACCESS_COARSE_LOCATION
    麦克风 RECORD_AUDIO
    电话 READ_PHONE_STATE
    电话 CALL_PHONE
    电话 READ_CALL_LOG
    电话 WRITE_CALL_LOG
    电话 ADD_VOICEMAIL
    电话 USE_SIP
    电话 PROCESS_OUTGOING_CALLS
    传感器 BODY_SENSORS
    短信 SEND_SMS
    短信 RECEIVE_SMS
    短信 READ_SMS
    短信 RECEIVE_WAP_PUSH
    短信 RECEIVE_MMS
    存储 READ_EXTERNAL_STORAGE
    存储 WRITE_EXTERNAL_STORAGE*/









    public static final void requestPower(Activity activity,String POWER_CODE) {
        //Log.e("权限方法","activity:"+activity);


        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(activity,
                POWER_CODE)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    POWER_CODE)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
                    //Log.e("权限工具：","已经获得了权限...");
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,}, 1);
                //Log.e("权限工具：","权限申请成功...");
            }
        }
    }


}
