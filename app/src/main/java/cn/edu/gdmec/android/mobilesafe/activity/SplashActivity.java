package cn.edu.gdmec.android.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.edu.gdmec.android.mobilesafe.R;
import cn.edu.gdmec.android.mobilesafe.utils.StreamUtil;
import cn.edu.gdmec.android.mobilesafe.utils.ToastUtil;

/**
 * Created by 达叔小生 on 2018/7/16.
 * email:2397923107@qq.com
 * version: 1.0
 */

public class SplashActivity extends Activity {
    protected static final String tag = "SplashActivity";
    /*
    * 更新新版本的状态码
    * */
    private static final int UPDATE_VERSION = 100;
    /*
    * 进入应用程序主界面的状态码
    * */
    private static final int ENTER_HOME = 101;
    /*
    * url地址出错的状态码
    * */
    private static final int URL_ERROR = 102;
    private static final int IO_ERROR = 103;
    private static final int JSON_ERROR = 104;

    private TextView tv_version_name;
    private int mLocalVersionCode;

    //成员变量
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_VERSION:
                    //弹出对话框，提示用户更新
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    //进入应用程序主界面，activity跳转过程
                    enterHome();
                    break;
                case URL_ERROR:
                    //弹出对话框，提示用户更新
                    //Toast.makeText(context,text,duration).show();
                    //ToastUtil.show(getApplicationContext(),"url异常");
                    ToastUtil.show(SplashActivity.this,"url异常");
                    enterHome();
                    break;
                case IO_ERROR:
                    //弹出对话框，提示用户更新
                    //Toast.makeText(context,text,duration).show();
                    ToastUtil.show(SplashActivity.this,"读取异常");
                    enterHome();
                    break;
                case JSON_ERROR:
                    //弹出对话框，提示用户更新
                    ToastUtil.show(SplashActivity.this,"json异常");
                    enterHome();
                    break;
            }
        }
    };
    private String mVersionDes;
    private String mDownloadUrl;

    /*
    * 弹出对话框，提升用户更新
    * */
    private void showUpdateDialog() {
        //对话框，是依赖于activity存在的
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置左上角图标
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("版本更新");
        //设置描述内容
        builder.setMessage(mVersionDes);
        //api 积极按钮，立即更新
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载apk,apk链接地址,downloadUrl
                downloadApk();
            }
        });
        //api
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消对话框，进入主界面
                enterHome();
                dialog.dismiss();
            }
        });
        //点击取消监听
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //即使用户点击取消，也需要让其进入应用程序主界面
                enterHome();
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void downloadApk() {
        //apk下载链接地址，放置apk的所在路径

        //1.判断sd卡是否可用，是否挂在上
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //2.获取sd路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator+"mobilesafe.apk";
            //3.发生请求，获取apk,并且放置到指定路径
            HttpUtils httpUtils = new HttpUtils();
            //4.发送请求，传递参数（下载地址，下载应用放置位置）
            httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载成功 (下载过后的放置在sd卡中的apk)
                    Log.i(tag,"下载成功");
                    File file = responseInfo.result;
                    //提示用户安装
                    installApk(file);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.i(tag,"下载失败");
                    //下载失败
                }

                //刚刚开始下载方法
                @Override
                public void onStart() {
                    Log.i(tag,"刚刚开始下载");
                    super.onStart();
                }

                //下载过程中的方法
                /*
                * 下载apk总大小
                * 当前的下载位置
                * 是否正在下载
                * */
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    Log.i(tag,"下载过程中");
                    super.onLoading(total, current, isUploading);
                }
            });

        }
    }

    /*
    * 要去安装apk，你要告诉我apk放在哪啦
    * @param file 安装文件
    * */
    private void installApk(File file) {
        //系统应用界面,源码,安装apk入口
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
		/*//文件作为数据源
		intent.setData(Uri.fromFile(file));
		//设置安装的类型
		intent.setType("application/vnd.android.package-archive");*/
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
//		startActivity(intent);
        startActivityForResult(intent, 0);
    }

    //开启一个activity后，返回结果调用的方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
     * 进入应用程序的主界面
     * */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        //在开启一个新的界面后，将导航界面关闭（导航界面只可见一次）

        finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除掉当前activity头title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);


        //初始化UI
        initUI();
        //初始化数据
        initData();
    }

    /*
    * 获取数据方法
    * */
    private void initData() {
        //1.应用版本名称
        tv_version_name.setText(getVersionName());
        //检测（本地版本号和服务器版本号对比）是否有更新，如果有更新，提示用户下载
        //2.member(成员变量),获取本地的版本号
        mLocalVersionCode = getVersionCode();
        //3.获取服务器的版本号(客户端发送请求，服务端给响应(json,xml))
        //http://www.xxx.com/update.json?key=value 返回200请求成功，流的方式将数据读取下来
        //json中内容包含：
        /*
        * 更新版本的版本名称
        * 新版本的描述信息
        * 服务器版本号
        * 新版本apk下载地址
        * {
        *   versionName:"2.0",
        *   versionDes:"2.0版本发布了，狂拽炫酷吊炸天，快来下载啊",
        *   versionCode:"2",
        *   downloadUrl:"http://www.xxx.com/xxx.apk"
        * }
        * */
        /*
        * HiJson 2.1.2_jdk64
        *
        * */
        //获取服务端的数据，是要发请求，发请求是个耗时的操作,丢到子线程去
        checkVersion();
    }

    /*
    * 检测版本号
    * */
    private void checkVersion() {
        new Thread(){

            public void run(){
                //发送请求获取数据，参数则为请求json的链接地址
                //http://ipconfig/update.json 测试阶段不是最优
                //仅限于模拟器访问电脑tomcat
                //Message message = new Message();

                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    //1.封装url地址
                    URL url = new URL("http://ipconfig/update.json");
                    //2.开启一个链接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //3.设置常见请求参数（请求头）

                    //请求超时 连接超时时间 跟服务器连接
                    connection.setConnectTimeout(2000);
                    //读取超时 读取时候突然断了
                    connection.setReadTimeout(2000);

                    //默认就是get请求方式
                    /*connection.setRequestMethod("POST");*/

                    //4.获取成功响应码
                    if (connection.getResponseCode() == 200){
                        //5.以流的形式，将数据获取下来
                        InputStream is=connection.getInputStream();
                        //6.将流转换字符串(工具类封装)
                        String json = StreamUtil.streamToString(is);
                        Log.i(tag,json);
                        //7.json解析

                        JSONObject jsonObject = new JSONObject(json);

                        String versionName = jsonObject.getString("versionName");
                        mVersionDes = jsonObject.getString("versionDes");
                        String versionCode = jsonObject.getString("versionCode");
                        mDownloadUrl = jsonObject.getString("downloadUrl");

                        //debug
                        Log.i(tag,versionName);
                        Log.i(tag, mVersionDes);
                        Log.i(tag,versionCode);
                        Log.i(tag, mDownloadUrl);

                        //8.比对版本号（服务器的版本号>本地版本号，提示用户更新）
                        if (mLocalVersionCode<Integer.parseInt(versionCode)) {
                            //提示用户更新，弹出对话框（UI），消息机制
                            //可视化，子线程不能操作UI
                            msg.what=UPDATE_VERSION;
                        }else{
                            //进入应用程序主界面
                            msg.what=ENTER_HOME;
                        }
                    }


                } catch (MalformedURLException e) {
                    //URL异常
                    e.printStackTrace();
                    msg.what = URL_ERROR;
                } catch (IOException e) {
                    //IO异常
                    e.printStackTrace();
                    msg.what = IO_ERROR;
                }catch (JSONException e) {
                    //Json解析异常
                    e.printStackTrace();
                    msg.what = JSON_ERROR;
                }finally {
                    //分析msg.what=UPDATE_VERSION;
                    //不分析msg.what=ENTER_HOME;
                    //指定睡眠时间，请求网络的时长超时4秒则不做处理
                    //请求网络的时长小于4秒，强制让其睡眠4秒
                    long endTime = System.currentTimeMillis();
                    if (endTime-startTime<3000){
                        try {
                            Thread.sleep(3000-(endTime-startTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                }
            };
        }.start();

        //开启线程
       /* new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });*/

    }

    /*
    *  初始化UI方法
    * */
    private void initUI() {
        tv_version_name = findViewById(R.id.tv_version_name);
    }


    /*
    * 获取版本名称：清单文件中
    * @return 应用版本名称 放回null代表异常
    * */
    public String getVersionName() {
        //1.包管理者对象packageManager
        PackageManager pm = getPackageManager();
        //2.从包的管理者对象中，获取指定包名大的基本信息(版本名称，版本号);
        //传0代表获取基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
            //3.获取版本名称
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    * 版本号：int
    * @return
    * 非0则代表获取成功
    * */
    public int getVersionCode() {
        //1.包管理者对象packageManager
        PackageManager pm = getPackageManager();
        //2.从包的管理者对象中，获取指定包名大的基本信息(版本名称，版本号);
        //传0代表获取基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
            //3.获取版本名称
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
