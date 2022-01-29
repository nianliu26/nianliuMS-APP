package com.dh.nianliums.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.dh.nianliums.MainActivity;
import com.dh.nianliums.Module.Employee;
import com.dh.nianliums.Module.StaticData;
import com.dh.nianliums.R;
import com.dh.nianliums.Util.HttpClientTool;
import com.dh.nianliums.Util.ImageUtil;
import com.dh.nianliums.Util.JSONhandle;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * 大致思想：
 *         注册页面每次只显示一个logo，一个提示标签，一个输入框，一个下一步按钮和一个上一步按钮
 *         在选择头像页面只需要一个提示信息，圆形图像显示框，一个选择按钮，和一个注册按钮
 *         头像选择在最后一步
 *         只有输入框内容符合要求才能点击下一步按钮
 *         点击下一步按钮，更换提示信息，并获取输入框内容
 *         切换下一步或者上一步都自动在输入框显示
 * 输入顺序：
 *      1、姓名
 *      2、电话号码
 *      3、性别
 *      4、家庭住址
 *      5、登录密码
 *      6、确认密码
 *      7、出生日期
 *      8、头像
 */
public class activity_logon extends Activity implements View.OnClickListener {

    private ImageView logonLogo;//app图标
    private TextView textLag;//输入框提示信息
    private EditText inputData;//数据输入框
    private TextView last;//上一步按钮
    private TextView next;//下一步按钮
    private ImageView userPhoto;//用户头像
    private LinearLayout ll_1;//输入框布局
    private LinearLayout ll_2;//按钮行
    private RelativeLayout dataLayout;//日期选择布局
    private RelativeLayout userLayout;//头像框选择布局
    private TextView checkData;//进入日期选择按钮
    private DatePicker choseData;//日期选择器
    private TextView dataEnter;//日期确定选择按钮
    private TextView erroPassword;//两次密码不一致提示

    private String[] page=new String[8];//保存输入的数据
    private int pageNumber=0;//保存当前在哪个页面
    private int datePageNumber=0;//日期页面显示哪一个
    private boolean dateFlage=false;//日期选择标志
    private boolean photoFlage=false;//头像选择标志

    //保存日期数据
    private int year;
    private int month;
    private int day;


    //图片操作可能会用到的变量
    private final int CAMERA = 55;
    private final int ALBUM = 56;
    private final int CAMERACUP = 57;
    private final int ALBUMCUP = 58;
    private final int CUPREQUEST = 102;

    Uri imageuri;
    Uri uritempFile;
    private String picPath;
    private File outImage;
    private Bitmap bitmap;






    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_logon);

        initView();

    }


    /**
     * 进行一些初始化：
     * 1、控件初始化
     * 2、显示app的logo
     * 3、显示输入框、下一步按钮
     * 4、隐藏上一步按钮、头像框、头像框标签
     * 5、将下一步按钮设置为不可使用
     */
    private void initView(){

        //初始化控件
        logonLogo=(ImageView) activity_logon.this.findViewById(R.id.logon_logo);
        textLag=(TextView)activity_logon.this.findViewById(R.id.text_lag);
        inputData=(EditText)activity_logon.this.findViewById(R.id.input_data);
        last=(TextView)activity_logon.this.findViewById(R.id.last);
        next=(TextView)activity_logon.this.findViewById(R.id.next_and_enter);
        userPhoto=(ImageView)activity_logon.this.findViewById(R.id.user_photo);
        ll_1=(LinearLayout)activity_logon.this.findViewById(R.id.ll_1);
        ll_2=(LinearLayout)activity_logon.this.findViewById(R.id.ll_2);
        dataLayout=(RelativeLayout)activity_logon.this.findViewById(R.id.data_layout);
        userLayout=(RelativeLayout)activity_logon.this.findViewById(R.id.user_layout);
        checkData=(TextView) activity_logon.this.findViewById(R.id.check_data);
        choseData=(DatePicker)activity_logon.this.findViewById(R.id.chose_data);
        dataEnter=(TextView)activity_logon.this.findViewById(R.id.data_enter);
        erroPassword=(TextView)findViewById(R.id.erro_password);



        //显示
        logonLogo.setVisibility(View.VISIBLE);//显示logo
        ll_1.setVisibility(View.VISIBLE);//显示输入行
        ll_2.setVisibility(View.VISIBLE);//显示按钮行

        textLag.setText("姓名");
        inputData.setInputType(InputType.TYPE_CLASS_TEXT);

        //隐藏
        last.setVisibility(View.GONE);//隐藏上一步按钮
        userLayout.setVisibility(View.GONE);//隐藏头像选择
        dataLayout.setVisibility(View.GONE);//隐藏日期选择
        checkData.setVisibility(View.GONE);//隐藏进入日期选择按钮

        //下一步不可使用
        next.setEnabled(false);
        next.setBackgroundResource(R.drawable.shape_unable);

        inputData.addTextChangedListener(textWatcher);//添加监听

        next.setOnClickListener(this);
        last.setOnClickListener(this);
        userPhoto.setOnClickListener(this);
        checkData.setOnClickListener(this);
        choseData.setOnClickListener(this);
        dataEnter.setOnClickListener(this);


        //初始化日期选择器
        choseData.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                activity_logon.this.year=year;
                activity_logon.this.month=monthOfYear;
                activity_logon.this.day=dayOfMonth;
                show(year,monthOfYear,dayOfMonth);
            }
        });

    }


    private  void show(int year,int month,int day){
        String str=year+"年"+(month+1)+"月"+day+"日";
        Log.e("date",str);

    }


    @Override
    public void onClick(View v) {


        doClick(v);

        clickEnd();


    }


    /**
     * 对点击事件进行响应
     * @param v 点击的控件
     */
    private void doClick(View v){
        switch (v.getId()){

            case R.id.next_and_enter:
                //点击下一步
                if (pageNumber<6){
                    //前6步正常输入，将输入框的内容保存
                    page[pageNumber]=inputData.getText().toString();
                } else if (pageNumber==6){
                    //第7步，选择出生日期，将保存的日期数据格式化放入page[6]，日期需要YYYY-MM-DD的格式
                    //此时出生日期选择完成，将保存的日期数据放入page[6]即可
                    page[pageNumber]=year+"-"+month+"-"+day;
                    Log.e("date","日期选择成功..."+page[pageNumber]+"");


                }else if (pageNumber==7){
                    //TODO 第8步，为确认步，这一步下一步按钮将会变成“确认”键，点击后将会提交头像文件，并且接收将服务器返回的文件保存位置放到page[7]，再将page构造成json文件发送给服务器

                    /**
                     * 1、将bitmap发送给服务器，接收其保存地址
                     * 2、将保存地址放到page中
                     * 3、将注册标识符与page构建成一个json文件
                     * 4、将这个json文件发送给服务器
                     */
                    //Log.e("photo",bitmap.toString()+"");
                    new Thread(){
                        @Override
                        public void run() {
                            JSONObject imgSend=JSONhandle.imageToJSON(ImageUtil.bitmapToBase64(bitmap),page[0]);
                            //Log.e("aa","准备连接服务器...");
                            HttpClientTool tool=new HttpClientTool();
                            JSONObject response= null;
                            try {
                                //Log.e("aa","正在连接服务器...");
                                String req = tool.sendJSON(imgSend,StaticData.WEB_PATH);//发送请求，并接收响应
                                response = new JSONObject(req);//将响应的数据转换为JSON对象
                                if (response.optString("handleFlag").equals("image")){
                                    //Log.e("dd","提取响应信息");
                                    page[pageNumber] = "img/"+response.optString("imagePath");//提取响应的信息

                                }
                                Log.e("path",page[pageNumber]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                /**
                                 * 输入顺序：
                                 *       1、姓名
                                 *       2、电话号码
                                 *       3、性别
                                 *       4、家庭住址
                                 *       5、登录密码
                                 *       6、确认密码
                                 *       7、出生日期
                                 *       8、头像
                                 */

                                Employee employee=new Employee();
                                employee.setNickname(page[0]);
                                employee.setPhone(page[1]);
                                employee.setSex(page[2]);
                                employee.setAddress(page[3]);
                                employee.setPassword(page[4]);
                                employee.setBirthday(page[6]);
                                employee.setPhoto(page[7]);


                                JSONObject logonJSON=JSONhandle.structureJSON(employee, StaticData.LOGON_FLAG);
                                String req = tool.sendJSON(logonJSON,StaticData.WEB_PATH);
                                response=new JSONObject(req);
                                if (response.optString("handleFlag").equals("consequence")){
                                    //TODO 服务器返回
                                }


                            } catch (Exception e){

                            }

                        }
                    }.start();



                }

                //防越界处理
                if (pageNumber<7) pageNumber++;

                break;

            case R.id.last:
                //点击上一步
                pageNumber--;

                break;

            case R.id.user_photo:
                //点击选择图片
                //点击选择图片调用系统相册，并进行剪裁，然后自动压缩

                //点击剪裁按钮
                new AlertDialog.Builder(activity_logon.this).setItems(new String[]{"拍摄", "从相册选择"}, new DialogInterface.OnClickListener() {
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
                                    imageuri = FileProvider.getUriForFile(activity_logon.this, "com.dh.nianliums.MainActivity", outImage);
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

            case R.id.check_data:
                //点击进入日期选择
                datePageNumber++;

                break;

            case R.id.data_enter:
                //确定选择日期
                //关闭日期选择布局，显示logo、进入日期选择按钮、按钮行
                datePageNumber--;
                checkData.setText("已选择出生日期："+year+"-"+month+"-"+day);
                //日期选择成功，下一步可用
                dateFlage=true;



                break;


        }
    }


    /**
     * 有点击事件结束则进行该方法
     * 1、根据点击之后页面，对界面进行对应的处理
     * 2、将pageNumber加1，以达到页面转换的效果
     */
    private void clickEnd(){
        switch (pageNumber){
            case 0:
                //姓名输入界面应该：
                next.setText("下一步");//按钮为“下一步按钮”
                textLag.setText("姓名");
                inputData.setInputType(InputType.TYPE_CLASS_TEXT);
                inputData.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});

                //隐藏
                last.setVisibility(View.GONE);//隐藏上一步按钮
                userLayout.setVisibility(View.GONE);//隐藏头像选择
                dataLayout.setVisibility(View.GONE);//隐藏日期选择
                checkData.setVisibility(View.GONE);//隐藏进入日期选择按钮

                //显示
                logonLogo.setVisibility(View.VISIBLE);//显示logo
                ll_1.setVisibility(View.VISIBLE);//显示输入行
                ll_2.setVisibility(View.VISIBLE);//显示按钮行


                break;

            case 1:
                //电话号码输入界面，应该：
                next.setText("下一步");//按钮为“下一步按钮”
                textLag.setText("电话号码");
                inputData.setInputType(InputType.TYPE_CLASS_NUMBER);
                inputData.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});

                //隐藏
                userLayout.setVisibility(View.GONE);//隐藏头像选择
                dataLayout.setVisibility(View.GONE);//隐藏日期选择
                checkData.setVisibility(View.GONE);//隐藏进入日期选择按钮

                //显示
                logonLogo.setVisibility(View.VISIBLE);//显示logo
                ll_1.setVisibility(View.VISIBLE);//显示输入行
                ll_2.setVisibility(View.VISIBLE);//显示按钮行
                last.setVisibility(View.VISIBLE);//显示上一步按钮

                break;

            case 2:
                //性别输入界面
                next.setText("下一步");//按钮为“下一步按钮”
                textLag.setText("性别");
                inputData.setInputType(InputType.TYPE_CLASS_TEXT);
                inputData.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});//限制输入一个字符

                //隐藏
                userLayout.setVisibility(View.GONE);//隐藏头像选择
                dataLayout.setVisibility(View.GONE);//隐藏日期选择
                checkData.setVisibility(View.GONE);//隐藏进入日期选择按钮

                //显示
                logonLogo.setVisibility(View.VISIBLE);//显示logo
                ll_1.setVisibility(View.VISIBLE);//显示输入行
                ll_2.setVisibility(View.VISIBLE);//显示按钮行
                last.setVisibility(View.VISIBLE);//显示上一步按钮

                break;

            case 3:
                //家庭住址
                next.setText("下一步");//按钮为“下一步按钮”
                textLag.setText("家庭住址");
                inputData.setInputType(InputType.TYPE_CLASS_TEXT);
                inputData.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});//限制输入一个字符

                //隐藏
                userLayout.setVisibility(View.GONE);//隐藏头像选择
                dataLayout.setVisibility(View.GONE);//隐藏日期选择
                checkData.setVisibility(View.GONE);//隐藏进入日期选择按钮

                //显示
                logonLogo.setVisibility(View.VISIBLE);//显示logo
                ll_1.setVisibility(View.VISIBLE);//显示输入行
                ll_2.setVisibility(View.VISIBLE);//显示按钮行
                last.setVisibility(View.VISIBLE);//显示上一步按钮

                break;

            case 4:
                //登录密码
                next.setText("下一步");//按钮为“下一步按钮”
                textLag.setText("登录密码");
                inputData.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                inputData.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});//限制输入一个字符

                //隐藏
                userLayout.setVisibility(View.GONE);//隐藏头像选择
                dataLayout.setVisibility(View.GONE);//隐藏日期选择
                checkData.setVisibility(View.GONE);//隐藏进入日期选择按钮

                //显示
                logonLogo.setVisibility(View.VISIBLE);//显示logo
                ll_1.setVisibility(View.VISIBLE);//显示输入行
                ll_2.setVisibility(View.VISIBLE);//显示按钮行
                last.setVisibility(View.VISIBLE);//显示上一步按钮


                break;

            case 5:
                //确认密码
                textLag.setText("确认密码");
                next.setText("下一步");//按钮为“下一步按钮”
                inputData.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                inputData.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});//限制输入一个字符

                //隐藏
                userLayout.setVisibility(View.GONE);//隐藏头像选择
                dataLayout.setVisibility(View.GONE);//隐藏日期选择
                checkData.setVisibility(View.GONE);//隐藏进入日期选择按钮

                //显示
                logonLogo.setVisibility(View.VISIBLE);//显示logo
                ll_1.setVisibility(View.VISIBLE);//显示输入行
                ll_2.setVisibility(View.VISIBLE);//显示按钮行
                last.setVisibility(View.VISIBLE);//显示上一步按钮

                break;

            case 6:
                //出生日期
                if (datePageNumber==0){
                    next.setText("下一步");//按钮为“下一步按钮”

                    //隐藏
                    ll_1.setVisibility(View.GONE);//隐藏输入行
                    dataLayout.setVisibility(View.GONE);//隐藏日期选择页面

                    //显示
                    ll_2.setVisibility(View.VISIBLE);//显示按钮行
                    last.setVisibility(View.VISIBLE);//显示上一步按钮
                    logonLogo.setVisibility(View.VISIBLE);//显示logo
                    checkData.setVisibility(View.VISIBLE);//显示进入日期选择按钮
                } else if (datePageNumber==1){

                    //关闭logo、进入日期选择按钮、按钮行，显示日期选择布局
                    //隐藏
                    logonLogo.setVisibility(View.GONE);
                    checkData.setVisibility(View.GONE);
                    ll_2.setVisibility(View.GONE);
                    userLayout.setVisibility(View.GONE);
                    //显示
                    dataLayout.setVisibility(View.VISIBLE);
                }


                break;

            case 7:
                //头像选择
                next.setText("确定");//下一步按钮变为“确定”按钮
                //隐藏
                logonLogo.setVisibility(View.GONE);
                ll_1.setVisibility(View.GONE);
                checkData.setVisibility(View.GONE);

                //显示
                userLayout.setVisibility(View.VISIBLE);//显示日期选择
                ll_2.setVisibility(View.VISIBLE);

                break;
        }
        inputData.setText(page[pageNumber]);
    }




    private TextWatcher textWatcher = new TextWatcher(){

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {



            if(inputData.getEditableText().length()>0 && pageNumber!=4 && pageNumber!=5 && pageNumber!=1
                    || inputData.getEditableText().length()>8 && pageNumber==4
                    || inputData.getEditableText().length()==11 && pageNumber==1
                    || pageNumber==5 && inputData.getEditableText().length()>8 && page[pageNumber-1]!=null && inputData.getEditableText().toString().equals(page[pageNumber-1])){

                next.setEnabled(true);
                next.setBackgroundResource(R.drawable.selector_click);
            }else {
                next.setEnabled(false);
                next.setBackgroundResource(R.drawable.shape_unable);
            }

            //两次密码不一致时不能点击下一步，并显示提示信息
            if (pageNumber==5 && page[pageNumber-1]!=null && !inputData.getEditableText().toString().equals(page[pageNumber-1])){
                erroPassword.setVisibility(View.VISIBLE);
            }else erroPassword.setVisibility(View.GONE);


            //日期选择成功，下一步可用
            if (pageNumber==6 && dateFlage){
                next.setEnabled(true);
                next.setBackgroundResource(R.drawable.selector_click);
            }

            //头像选择成功，确定可用
            if (pageNumber==7 && photoFlage){
                next.setEnabled(true);
                next.setBackgroundResource(R.drawable.selector_click);
            }

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case CAMERACUP:
                if (resultCode == RESULT_OK){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //如果是7.0剪裁图片 同理 需要把uri包装
                        Uri inputUri = FileProvider.getUriForFile(activity_logon.this, "com.dh.nianliums.MainActivity", outImage);//通过FileProvider创建一个content类型的Uri
                        startPhotoZoom(inputUri);//设置输入类型
                    } else {
                        Uri inputUri = Uri.fromFile(outImage);
                        startPhotoZoom(inputUri);
                    }
                }
                break;
            case ALBUMCUP:
                if (resultCode == RESULT_OK) {
                    picPath = ImageUtil.getPath(activity_logon.this, data.getData());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri inputUri = FileProvider.getUriForFile(activity_logon.this, "com.dh.nianliums.MainActivity", new File(picPath));//通过FileProvider创建一个content类型的Uri
                        startPhotoZoom(inputUri);//设置输入类型
                    } else {
                        Uri inputUri = Uri.fromFile(new File((picPath)));
                        startPhotoZoom(inputUri);
                    }
                }
                break;

            case CUPREQUEST:
                if (data == null){
                    Log.e("data","是空的");
                    return;
                }
                Log.e("data","不是空的");
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //将图片显示到组件上
                setImageToView ();

                break;
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
        /**
         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
         * 故只保存图片Uri，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         */
        //intent.putExtra("return-data", true);

        //裁剪后的图片Uri路径，uritempFile为Uri类变量
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式
        Log.e("cut","开始剪裁");

        startActivityForResult(intent,CUPREQUEST);
    }


    private void setImageToView() {

            if (bitmap != null) {
                //使用工具类压缩图片
                bitmap=ImageUtil.compressBitmap(this.bitmap, 300, new ImageUtil.CompressCallback() {
                    @Override
                    public void finish(Bitmap bitmap) {
                        String path =  ImageUtil.bitmapToFile(activity_logon.this,bitmap);
                        Log.d("MainActivity", path);
                    }
                });
                userPhoto.setImageBitmap(bitmap);
                next.setEnabled(true);
                next.setBackgroundResource(R.drawable.selector_click);
            }else {
            Toast.makeText(this, "bitmap空", Toast.LENGTH_SHORT).show();
            next.setEnabled(false);
            next.setBackgroundResource(R.drawable.shape_unable);
        }
    }
}
