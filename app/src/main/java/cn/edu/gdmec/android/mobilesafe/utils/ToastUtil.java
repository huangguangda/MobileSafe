package cn.edu.gdmec.android.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 达叔小生 on 2018/7/17.
 * email:2397923107@qq.com
 * version: 1.0
 */

public class ToastUtil {
    //打印吐司
    /*
     * 上下文环境，吐司内容，可以传递吐司时间 0为短，1为长
     */
    public static void show(Context ctx,String msg){
        Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show();
    }
}
