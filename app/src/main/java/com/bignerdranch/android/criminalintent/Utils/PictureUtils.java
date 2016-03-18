package com.bignerdranch.android.criminalintent.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;


public class PictureUtils {
    /**
     * 将图片缩放到设备屏幕的显示尺寸
     */
    @SuppressWarnings("deprecation")
    public static BitmapDrawable getScaledDrawable(Activity a, String path){
        //获得屏幕大小
        Display display=a.getWindowManager().getDefaultDisplay();
        float destWidth=display.getWidth();
        float destHeight=display.getHeight();

        //获得BitmapFactory.Options，将对应路径的文件绑定
        BitmapFactory.Options options= new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path, options);
        //通过options获得图片文件的宽和高
        float srcWidth= options.outWidth;
        float srcHeight= options.outHeight;
        //如果图片大于屏幕大小，则剪裁
        int inSampleSize = 1;
        if (srcHeight>destHeight||srcWidth>destWidth){
            if(srcWidth> srcHeight){ //如果图片宽大于高
                inSampleSize =Math.round(srcHeight / destHeight);// 求出图片高/屏幕高
            }else{
                inSampleSize=Math.round(srcWidth/destWidth);// 求出图片宽/屏幕宽
            }
        }

        options=new BitmapFactory.Options();
        options.inSampleSize= inSampleSize;//原始相片会缩小为 原尺寸*inSampleSize的倒数

        Bitmap bitmap=BitmapFactory.decodeFile(path, options);//按options生成bitmap
        return new BitmapDrawable(a.getResources(), bitmap);//返回缩小了的图片
    }

    /**
     * 清理工作
     */
    public static void cleanImageView(ImageView imageView){
        if(!(imageView.getDrawable() instanceof BitmapDrawable))
            return;//如果没有实例，退出

        //主动调用recycle()方法释放内存
        BitmapDrawable b = (BitmapDrawable) imageView.getDrawable();
        b.getBitmap().recycle();
        imageView.setImageDrawable(null);
    }
}
