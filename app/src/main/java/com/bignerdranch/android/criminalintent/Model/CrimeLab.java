package com.bignerdranch.android.criminalintent.Model;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * 模型层CrimeLab，包含Crimes和get方法
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mAppContext;
    private ArrayList<Crime> mCrimes;

    /**
     * 构造方法
     */
    public CrimeLab(Context appContext) {
        mAppContext = appContext;
        mCrimes= new ArrayList<Crime>();
    }

    /**
     * get方法可以返回CrimeLab类实例，如果没有实例，执行新建语句
     */
    public static CrimeLab get(Context c) {
        if (sCrimeLab==null) {
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    /**
     * getCrimes可以生成一个Crimes ArrayList
     */
    public ArrayList<Crime> getCrimes(){
        return mCrimes;
    }

    /**
     * getCrime方法可以获得List里边指定id的Crime
     */
    public Crime getCrime(UUID id){
        for (Crime c:mCrimes){
            if (c.getId().equals(id)) //遍历，按id查找
                return c;
        }
        return null;
    }

    /**
     *添加新的Crime的方法
     */
    public void addCrime (Crime c){
        mCrimes.add(c);
    }


}
