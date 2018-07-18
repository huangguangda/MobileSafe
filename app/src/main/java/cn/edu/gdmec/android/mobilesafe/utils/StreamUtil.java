package cn.edu.gdmec.android.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 达叔小生 on 2018/7/16.
 * email:2397923107@qq.com
 * version: 1.0
 */

public class StreamUtil {
    /*
    * @param 流对象
    * @return 流转换成字符串 返回Null代表异常
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
