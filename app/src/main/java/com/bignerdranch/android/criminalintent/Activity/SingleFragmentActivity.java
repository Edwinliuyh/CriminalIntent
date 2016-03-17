package com.bignerdranch.android.criminalintent.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.bignerdranch.android.criminalintent.R;

/**
 * 抽象类，通用的托管单一Fragment的Activity模板
 */
public abstract class SingleFragmentActivity extends FragmentActivity {

    /**
     * 抽象类的子类必须要实现抽象类中未完成的方法
     */
    protected abstract Fragment createFragment();

    /**
     * 在OnCreat，生成空布局的fragment作为Container
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment =fm.findFragmentById(R.id.fragmentContainer);

        if(fragment==null){
            fragment=createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }


}
