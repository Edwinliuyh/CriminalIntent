package com.bignerdranch.android.criminalintent.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bignerdranch.android.criminalintent.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by dell on 2016/3/17.
 */
public class CrimeCameraFragment extends Fragment{
    private static final String TAG = "CrimeCameraFragment";
    public static final String EXTRA_PHOTO_FILENAME=
            "com.bignerdranch.android.criminalintent.photo_filename";
    private Camera mCamera ;
    private SurfaceView mSurfaceView;
    private View mProgressContainer;

    /**
     * 在onCreateView，引入布局获得控件
     * 处理Surface的生命周期
     */
    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_crime_camera, parent, false);

        Button takePictureButton=(Button) v
                .findViewById(R.id.crime_camera_takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (mCamera !=null){
                    mCamera.takePicture(mShutterCallback, null, mJpegCallback);
                }
            }
        });

        mSurfaceView=(SurfaceView) v.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//相机预览
        //SurfaceHolder的Callback接口监听Surface生命周期中的事件，控制Surface与其客户端协同工作。
        holder.addCallback(new SurfaceHolder.Callback(){
            //在surfaceCreated，告诉相机使用此surface作为预览区
            public void surfaceCreated(SurfaceHolder holder){
                try{
                    if(mCamera!=null){
                        mCamera.setPreviewDisplay(holder);
                    }
                }catch(IOException exception){
                    Log.e(TAG, "Error setting up preview display", exception);
                }
            }

            //在surfaceDestroyed，surface不再出现，所以停止预览，
            public void surfaceDestroyed(SurfaceHolder holder){
                if (mCamera!=null){
                    mCamera.stopPreview();
                }
            }

            //在surfaceChanged，根据surface变化，更新预览区域
            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
                if (mCamera==null) return;
                Camera.Parameters parameters = mCamera.getParameters();
                Camera.Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), w, h);//找出最佳预览尺寸
                parameters.setPreviewSize(s.width, s.height);//设置相机预览的尺寸
                s = getBestSupportedSize(parameters.getSupportedPictureSizes(), w,h);
                parameters.setPictureSize(s.width, s.height);//设置要创建的图片尺寸
                mCamera.setParameters(parameters);
                try{
                    mCamera.startPreview();
                }catch(Exception e){
                    Log.e(TAG, "Could not start preview", e);
                    mCamera.release();
                    mCamera=null;
                }
            }
        });

        mProgressContainer= v.findViewById(R.id.crime_camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);//进度条容器不可见

        return v;
    }

    /**
     * 在onResume，打开相机
     */
    @SuppressWarnings("deprecation")
    @TargetApi(9)
    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.GINGERBREAD){
            mCamera= Camera.open(0);//Api9或以上
        }else{
            mCamera=Camera.open();//Api1以上
        }
    }

    /**
     * 在onPause，释放相机资源
     */
    @Override
    public void onPause(){
        super.onPause();
        if(mCamera!=null){
            mCamera.release();
            mCamera=null;
        }
    }

    /**
     * 找出设备支持的最佳尺寸
     */
    @SuppressWarnings("deprecation")
    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int width, int height){
        Camera.Size bestSize = sizes.get(0);
        int largestArea =bestSize.width*bestSize.height;
        for (Camera.Size s : sizes){
            int area = s.width*s.height;
            if (area>largestArea){
                largestArea=area;
                bestSize=s;
            }
        }
        return bestSize;
    }

    /**
     * 在ShutterCallback，进度条容器设为可见
     */
    @SuppressWarnings("deprecation")
    private Camera.ShutterCallback mShutterCallback= new Camera.ShutterCallback(){
        public void onShutter(){
            mProgressContainer.setVisibility(View.VISIBLE);
        }
    };

    /**
     * 在PictureCallback，命名并保存已拍摄的JPEG图片文件
     */
    @SuppressWarnings("deprecation")
    private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback(){
        public void onPictureTaken(byte[] data, Camera camera){
            //随机生成文件名
            String filename = UUID.randomUUID().toString() + ".jpg";
            //保存jpeg数据到手机内存
            FileOutputStream os=null;
            boolean success = true;
            try{
                os= getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                os.write(data);
            }catch(Exception e){
                Log.e(TAG,"Error writing to file"+filename, e);
                success = false;
            }finally{
                try{
                    if (os!=null)
                        os.close();
                }catch(Exception e){
                    Log.e(TAG, "Error closing file"+filename, e);
                    success=false;
                }
            }
            //设置文件名到结果的intent
            if(success){
                Intent i =new Intent();
                i.putExtra(EXTRA_PHOTO_FILENAME, filename);
                getActivity().setResult(Activity.RESULT_OK,i);
            }else{
                getActivity().setResult(Activity.RESULT_CANCELED);
            }
            getActivity().finish();
        }
    };
}
