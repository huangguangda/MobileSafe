# MobileSafe -[可参考另一版本](https://github.com/huangguangda/DashuMobile)
android手机卫士

## Github 欢迎 Star、Fork

### 如果喜欢，那就点个赞吧！❤️ 

## 手机安全卫士项目

### 一，项目结构

下面我要讲解得很清楚，毅力这东西不是每个人都有的，天天学基础也不好，直接上手做项目也是可以的。MobileSafe这个该项目分为 9 个功能模块，包括手机防盗、通讯卫士、软件管家、手机杀毒、缓存清理、进程管理、流量统计、高级工具、设置中心。手机安全卫士，欢迎页面进入到主界面。

> 建议做这个项目之前，你是否学会Java基础语法，如果还没学，请先去系统学习一下，否则是看不下的。如果学过那就好，请开始你的项目历程。

下面开始从splash布局，获取版本名称并展示，在到构建服务端json和网络数据请求，json解析过程，消息机制发送不同类型的信息，弹出对话框，以及使用xutils的说明，打包apk可以自行了解，提示下载更新跳转到主界面。

首先先去下载Code tree for GitHub插件，它能帮助你更好地查看项目结构与分支下载功能。**图片资源也是用插件帮忙下载，如有不懂自行百度** 来源 Chrome 网上应用店

借用Code tree for GitHub插件功能找到libs下的jar包，下载下来导入项目中。

```
xUtils-2.6.14.jar
```

xutils使用过程

1. 导入xutils的jar包
2. 添加xutils需要使用的权限
```
<uses-permission android:name="android.permission.INTERNET" /> 
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```
3. 获取HttpUtils对象
4. 调用download(下载链接地址,下载后放置文件的路径,下载过程中方法的回调
```
onStart()
onloading()
onSuccesd()
onFail()
```

## 包结构

![包结构图](http://images.cnblogs.com/cnblogs_com/dashucoding/1247529/o_QQ%E6%88%AA%E5%9B%BE20180718225655.png)

#### 创建activity_splash.xml

```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@drawable/launch_bg">
    <TextView
        android:id="@+id/tv_version_name"
        android:layout_centerInParent="true"
        android:text="版本名称："
        android:shadowDx="1"
        android:shadowDy="1"
        android:shadowColor="#f00"
        android:shadowRadius="1"
        android:textSize="16sp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
    <ProgressBar
        android:layout_below="@+id/tv_version_name"
        android:layout_centerHorizontal="true"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
</RelativeLayout>
```

> 说明：android:background="@drawable/launch_bg"为添加背景图片，android:shadowDx,android:shadowDy,android:shadowColor,android:shadowRadius为设置阴影效果。

#### 创建SplashActivity类

先进行初始化控件，显示版本号：

```
public class SplashActivity extends Activity {
 private TextView tv_version_name;
 private int mLocalVersionCode;
 @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除掉当前activity头title，请求窗口。。自动提示导入，Window.  no title,没有头部
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        //初始化UI，这里定义一个方法
        initUI();
        //初始化数据，这里定义一个方法
        initData();
    }
    
    //...加入方法,显示红色自动导入
    private void initUI(){
    }
    
    //...加入方法,显示红色自动导入
    private void initData(){
      //中有getVersionCode()获取版本名称的方法
    }
    
    //分开说明：说明@1
}    
```

#### 说明@1

```
/*
 *  初始化UI方法
 * */
private void initUI() {
  tv_version_name = findViewById(R.id.tv_version_name);
}
```

```
/*
 * 获取数据方法
 * */
private void initData() {
 //1.显示应用版本名称 getVersionName()为定义获得版本名称的方法。
 tv_version_name.setText(getVersionName());
}
```

```
/*
 * 获取版本名称：清单文件中
 * @return 应用版本名称 放回null代表异常
 * */
public String getVersionName(){
 //版本名称为字符串，所以返回字符串对象
 //1.获取包管理者，创建包管理者对象packageManager
 PackageManager pm = getPackageManager();
 //2.包管理者获取包信息，从包管理者对象中，获取指定的包名 0代表获取基本信息
 try {
       PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
       //3.返回包信息的版本名称，获取版本名称
       return packageInfo.versionName;
   } catch (PackageManager.NameNotFoundException e) {
       e.printStackTrace();
   }
   //否则返回null,空字符串
   return null;
}
```

> 说明什么是版本名称？minSdkVersion，targetSdkVersion，versionCode，versionName是什么？ 

- minSdkVersion指明应用程序运行所需的最小API level。
- targetSdkVersion标明应用程序目标API Level的一个整数。
- versionCode主要是用于版本升级所用，第一个版本定义为1，以后递增，这样只要判断该值就能确定是否需要升级，该值不显示给用户。
- android:versionName:这个是我们常说明的版本号,该值是个字符串，可以显示给用户。

```
versionCode 1
versionName "1.0"
//
那么1.0和1.0.0有何区别
在软件修改中，第三位代表修复了一些小bug而已，第二位代表修复了部分功能，第一位代表软件代码彻底大改动，几乎所有功能以及布局大变。
```

#### 到了这里，就可以获取版本名称并且展示了。

## 版本更新流程

启动app，从服务器获取最新的版本信息，检验是否有更新，弹出升级对话框，用户选择是否更新，下载安装包，安装apk，如果没有更新则跳转主界面，取消下载更新也同样跳转到主界面。

是否更新，获取当前versionCode与服务器的versionCode进行比较，如果服务器的大，那么有更新。

那么就在获取数据方法那添加代码吧！

```
    /*
    * 获取数据方法
    * */
    //步骤：获取本地的版本号，与服务器的版本号进行比较。什么是服务器，可以理解为相当于一台电脑。
    //分别获取本地版本号和服务器版本号
    private void initData() {
        tv_version_name.setText(getVersionName());
        //检测（本地版本号和服务器版本号对比）是否有更新，如果有更新，提示用户下载
        //2.member(成员变量) mLocalVersionCode
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
        //获取服务端的数据，是要发请求，发请求是个耗时的操作,丢到子线程去
        //检测版本号的方法
        checkVersion();
    }
```

> 这里是要服务器支持的，你可以下载tomcat在本地自己试试看。

```
/*
 * 检测版本号
 * */
    private void checkVersion() {
        new Thread(){

            public void run(){
                //发送请求获取数据，参数则为请求json的链接地址
                //http://ipconfig/update.json 测试阶段不是最优
                //仅限于模拟器访问电脑tomcat
                //... Message.obtain()为将msg返还---public void handleMessage(Message msg) {}
                Message msg = Message.obtain();
                //...
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
                        //6.将流转换字符串(工具类封装)--->----@2 这里另外创建一个工具类为StreamUtil，下面有描述
                        String json = StreamUtil.streamToString(is);
                        Log.i(tag,json);
                        //7.json解析
                        //创建JSON对象， new JSONObject
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
                        //Integer.parseInt是因为上面的是字符串类型，所有要进行转换
                        if (mLocalVersionCode<Integer.parseInt(versionCode)) {
                            //提示用户更新，弹出对话框（UI），消息机制
                            //可视化，子线程不能操作UI----@3 UPDATE_VERSION为更新新版本的状态码
                            //这些状态码是用来返回消息机制的
                            msg.what=UPDATE_VERSION;
                        }else{
                            //进入应用程序主界面----- 进入应用程序主界面的状态码
                            msg.what=ENTER_HOME;
                        }
                    }


                } catch (MalformedURLException e) {
                    //URL异常----url地址出错的状态码
                    e.printStackTrace();
                    msg.what = URL_ERROR;
                } catch (IOException e) {
                    //IO异常-----同上
                    e.printStackTrace();
                    msg.what = IO_ERROR;
                }catch (JSONException e) {
                    //Json解析异常-----同上
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
    }
```

在SplashActivity中创建成员变量

```
private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what){
            //状态码，用来弹出对话框，或进入主界面，或弹出消息，用来分类消息
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
                    
                    //在这里创建一个工具类---ToastUtil
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
```

#### @2@3

创建一个ToastUtil类：

```
public class ToastUtil {
    //打印吐司
    /*
     * 上下文环境，吐司内容，可以传递吐司时间 0为短，1为长
     */
    public static void show(Context ctx,String msg){
        Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show();
    }
}
```

创建StreamUtil类：

```
public class StreamUtil {
    /*
    * @param 流对象
    * @return 流转换成字符串 返回Null代表异常，参数输入流，工具类输出
    * */
    public static String streamToString(InputStream is) {
        //1.在读取的过程中，将读取的内容存储 缓存中，然后一次性的转换成字符串返回
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //2.读流操作，读到没有为止（循环）
        byte[] buffer = new byte[1024];
        //3.记录读取内容的临时变量
        int temp = -1;
        try {
            while ((temp=is.read(buffer))!=-1){
                bos.write(buffer,0,temp);
            }
            //返回读取的数据
            //返回字符串，输出流
            return bos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
```

在这里工具类包utils下的两个工具类创建完毕，ToastUtil和StreamUtil

在private Handler mHandler = new Handler(){}中定义了一些方法：

```
showUpdateDialog();提示用户更新
enterHome();进入应用程序主界面
```

```
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
                //下载apk,apk链接地址,downloadUrl  ----定义了下载的方法@4
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
        //注意这步一定要添加，否则没效果
        builder.show();
    }
```

#### @4

```
onSuccess onFailure onStart onLoading
```

```
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
                    //返回下载responseInfo结果，下面定义一个方法提示用户安装
                    File file = responseInfo.result;
                    //提示用户安装 -- 参数 安装文件
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
```

```
//这部分看源码
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
      //----@5
      startActivityForResult(intent, 0);
    }
```

```
//开启一个activity后，返回结果调用的方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }
```

```
/*
 * 进入应用程序的主界面
 * */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        //在开启一个新的界面后，将导航界面关闭（导航界面只可见一次）

        finish();
    }
```

## 1.0 展示图
ok,大功告成，虚拟机运行效果展示：[启动页](http://images.cnblogs.com/cnblogs_com/dashucoding/1247529/o_QQ%E6%88%AA%E5%9B%BE20180718213323.png)  --> 到主界面 [跳转](http://images.cnblogs.com/cnblogs_com/dashucoding/1247529/o_QQ%E6%88%AA%E5%9B%BE20180718213335.png)

这个过程中会有显示json解析失败，主要是因为没有解决服务器问题，这方面可自行下载tomcat，在本地自己尝试一下，把自己的update.json放入到D:\apache-tomcat-xx\webapps\ROOT 该目录下，apk也是同样的道理，不过这方面没资料有点，希望有需要的朋友自行百度或Google查阅。
