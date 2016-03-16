package com.bignerdranch.android.criminalintent.Activity;


import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.bignerdranch.android.criminalintent.Fragment.CrimeFragment;
import com.bignerdranch.android.criminalintent.Model.Crime;
import com.bignerdranch.android.criminalintent.Model.CrimeLab;
import com.bignerdranch.android.criminalintent.R;

import java.util.ArrayList;
import java.util.UUID;

/**
 * 创建和管理ViewPager
 */
public class CrimePagerActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private ArrayList<Crime> mCrimes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将ViewPaper设为Activity内容视图
        mViewPager=new ViewPager(this);
        mViewPager.setId(R.id.viewPaper);
        setContentView(mViewPager);
        //将adapter与CrimeFragment绑定
        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fm= getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) { //给ViewPaper设置Adapter
            @Override
            public Fragment getItem(int position) { //返回CrimeFragment实例
                Crime crime=mCrimes.get(position); //获取指定位置的Crime实例
                return CrimeFragment.newInstance(crime.getId());//根据CrimeId生成Fragment
            }
            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        //根据传入的crimeId，查找应该显示的CrimeFragment页面
        UUID crimeId= (UUID)getIntent()
                .getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);//获得传入intent的键值对
        for (int i =0; i<mCrimes.size();i++){
            if (mCrimes.get(i).getId().equals(crimeId)){ //搜索比对crimeId
                mViewPager.setCurrentItem(i);//ViewPaper显示初始页面
                break;
            }
        }
        //左右滑动的监听器
        mViewPager.addOnPageChangeListener(new  ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrollStateChanged(int state) {
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                Crime crime=mCrimes.get(position);
                if (crime.getTitle()!=null){
                    setTitle(crime.getTitle());
                }
            }

        });
    }
}
