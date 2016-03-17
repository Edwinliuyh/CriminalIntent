package com.bignerdranch.android.criminalintent.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import com.bignerdranch.android.criminalintent.Fragment.CrimeCameraFragment;

/**
 * Created by dell on 2016/3/17.
 */
public class CrimeCameraActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment(){
        return new CrimeCameraFragment();
    }

    /**
     * 在onCreate，调用Window的命令
     * 隐藏标题、窗口全屏
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }


}
