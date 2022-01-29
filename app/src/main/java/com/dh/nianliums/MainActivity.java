package com.dh.nianliums;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.dh.nianliums.Util.ImageUtil;
import com.dh.nianliums.Util.PowerUitl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends Activity implements View.OnClickListener {

    private final int CAMERA = 55;
    private final int ALBUM = 56;
    private final int CAMERACUP = 57;
    private final int ALBUMCUP = 58;
    private final int CUPREQUEST = 102;

    Uri imageuri;

    private Button btn;
    private ImageView pic_iv;
    private String picPath;
    private Button compressBtn;
    private Bitmap bitmap;
    private Button cupBtn;
    private ImageView cv;
    private File outImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(this);
        pic_iv = (ImageView) findViewById(R.id.pic_iv);
        cv = (ImageView) findViewById(R.id.cv);

        compressBtn = (Button) findViewById(R.id.compressBtn);
        compressBtn.setOnClickListener(this);
        cupBtn = (Button) findViewById(R.id.cupBtn);
        cupBtn.setOnClickListener(this);


        //可能会用到的权限申请
        PowerUitl.requestPower(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击选择一个照片按钮按钮
            case R.id.btn:
                new AlertDialog.Builder(MainActivity.this).setItems(new String[]{"拍摄", "从相册选择"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //获得项目缓存路径
                                String sdPath = getExternalCacheDir().getPath();
                                //根据时间随机生成图片名
                                String name = new DateFormat().format("yyyyMMddhhmmss",
                                        Calendar.getInstance(Locale.CHINA)) + ".jpg";
                                picPath = sdPath + "/" + name;

                                File outImage = new File(picPath);

                                //如果是7.0以上 那么就把uir包装
                                if (Build.VERSION.SDK_INT >= 24) {
                                    imageuri = FileProvider.getUriForFile(MainActivity.this, "com.dh.nianliums.MainActivity", outImage);
                                } else {
                                    //否则就用老系统的默认模式
                                    imageuri = Uri.fromFile(outImage);
                                }
                                //启动相机
                                Intent intent = new Intent();
                                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
                                startActivityForResult(intent, CAMERA);


                                break;
                            case 1:
                                //启动相册
                                Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(albumIntent, ALBUM);
                                break;
                        }
                    }
                }).create().show();
                break;


            case R.id.compressBtn:
                Log.e("cc","开始压缩图片："+bitmap);
                if (bitmap != null) {
                    //使用工具类压缩图片
                    ImageUtil.compressBitmap(this.bitmap, 300, new ImageUtil.CompressCallback() {
                        @Override
                        public void finish(Bitmap bitmap) {
                            pic_iv.setImageBitmap(bitmap);
                            String path =  ImageUtil.bitmapToFile(MainActivity.this,bitmap);
                            Log.d("MainActivity", path);
                        }
                    });
                }
                break;


            case R.id.cupBtn:
                //点击剪裁按钮
                new AlertDialog.Builder(MainActivity.this).setItems(new String[]{"拍摄", "从相册选择"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                String sdPath = getExternalCacheDir().getPath();
                                String name = new DateFormat().format("yyyyMMddhhmmss",
                                        Calendar.getInstance(Locale.CHINA)) + ".jpg";
                                picPath = sdPath + "/" + name;

                                outImage = new File(picPath);


                                if (Build.VERSION.SDK_INT >= 24) {
                                    imageuri = FileProvider.getUriForFile(MainActivity.this, "com.dh.nianliums.MainActivity", outImage);
                                } else {
                                    imageuri = Uri.fromFile(outImage);
                                }

                                Intent intent = new Intent();
                                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);

                                startActivityForResult(intent, CAMERACUP);


                                break;
                            case 1:
                                Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(albumIntent, ALBUMCUP);
                                break;
                        }
                    }
                }).create().show();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA:
                //将生成的文件转成bitmap显示
                if (resultCode == RESULT_OK) {
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageuri));
                        pic_iv.setImageBitmap(bitmap);
                        cv.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case ALBUM:
                //Log.e("cc","从相册取到了图片..."+resultCode);
                //用到了工具类解析相册的回调数据 相册回调分4.4之前和之后 工具类已经封装好了 cv就是circleview
                if (resultCode == RESULT_OK) {
                    picPath = ImageUtil.getPath(MainActivity.this, data.getData());
                    //Log.e("cc","得到地址："+picPath);
                    bitmap = ImageUtil.fileToBitmap(picPath,200);
                    //if (bitmap!=null) Log.e("cc","得到图片...");
                    //else Log.e("cc","图片未转换...");
                    pic_iv.setImageBitmap(bitmap);
                    cv.setImageBitmap(bitmap);
                }
                break;
            case CAMERACUP:
                if (resultCode == RESULT_OK){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //如果是7.0剪裁图片 同理 需要把uri包装
                        Uri inputUri = FileProvider.getUriForFile(MainActivity.this, "com.dh.nianliums.MainActivity", outImage);//通过FileProvider创建一个content类型的Uri
                        startPhotoZoom(inputUri);//设置输入类型
                    } else {
                        Uri inputUri = Uri.fromFile(outImage);
                        startPhotoZoom(inputUri);
                    }
                }
                break;
            case ALBUMCUP:
                if (resultCode == RESULT_OK) {
                    picPath = ImageUtil.getPath(MainActivity.this, data.getData());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri inputUri = FileProvider.getUriForFile(MainActivity.this, "com.dh.nianliums.MainActivity", new File(picPath));//通过FileProvider创建一个content类型的Uri
                        startPhotoZoom(inputUri);//设置输入类型
                    } else {
                        Uri inputUri = Uri.fromFile(new File((picPath)));
                        startPhotoZoom(inputUri);
                    }
                }
                break;

            case CUPREQUEST:
                if (data == null){
                    return;
                }
                //将图片显示到组件上
                setImageToView (data);

                break;
        }
    }

    private void setImageToView(Intent data) {

        Bundle bundle = data.getExtras();
        if (bundle != null){
            bitmap = bundle.getParcelable("data");
            cv.setImageBitmap(bitmap);
            pic_iv.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this, "bundle空", Toast.LENGTH_SHORT).show();
        }
    }

    //裁剪
    private void startPhotoZoom(Uri uri) {
        //Log.e("cc","准备开始剪图片...");

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        //sdk>=24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            intent.putExtra("noFaceDetection", false);//去除默认的人脸识别，否则和剪裁匡重叠
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        }

        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        //返回数据 如果false 回调收不到
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式

        startActivityForResult(intent,CUPREQUEST);
    }

}