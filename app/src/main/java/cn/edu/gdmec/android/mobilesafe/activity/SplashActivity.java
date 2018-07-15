package cn.edu.gdmec.android.mobilesafe.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.edu.gdmec.android.mobilesafe.R;
import cn.edu.gdmec.android.mobilesafe.utils.StreamUtil;

/**
 * Created by 达叔小生 on 2018/7/16.
 * email:2397923107@qq.com
 * version: 1.0
 */

public class SplashActivity extends Activity {

    private TextView tv_version_name;
    private int mLocalVersionCode;

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

            private String tag;

            public void run(){
                //发送请求获取数据，参数则为请求json的链接地址
                //http://ipconfig/update.json 测试阶段不是最优
                //仅限于模拟器访问电脑tomcat

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
                    }

                } catch (MalformedURLException e) {
                    //URL异常
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
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
