package com.bignerdranch.android.criminalintent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * 通用的托管单一Fragment的Activity模板
 * 抽象类，抽象类的子类必须要实现抽象类中未完成的方法createFragment()
 */
public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm=getFragmentManager();
        android.app.Fragment fragment =fm.findFragmentById(R.id.fragmentContainer);

        if(fragment==null){
            fragment=createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }


}
