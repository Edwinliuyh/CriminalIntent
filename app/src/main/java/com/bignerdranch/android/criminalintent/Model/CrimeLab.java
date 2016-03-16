package com.bignerdranch.android.criminalintent.Model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * 模型层CrimeLab，包含Crimes和get方法
 */
public class CrimeLab {
    private static final String TAG ="CrimeLab";
    private static final String FILENAME = "crimes.json";
    private CriminalIntentJSONSerializer mSerializer;

    private static CrimeLab sCrimeLab;
    private Context mAppContext;
    private ArrayList<Crime> mCrimes;

    /**
     * CrimeLab的构造方法
     */
    public CrimeLab(Context appContext) {
        mAppContext = appContext;
        mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);//初始化Serializer
        try{
            mCrimes=mSerializer.loadCrimes(); //从文件加载crime数据
        }catch (Exception e){
            mCrimes = new ArrayList<Crime>(); //如加载失败，则新建一个空数组列表
            Log.e(TAG,"Error loading crimes:",e);
        }
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
     *增加Crime的方法
     */
    public void addCrime (Crime c){
        mCrimes.add(c);
    }

    /**
     * 删除Crime的方法
     */
    public void deleteCrime(Crime c){
        mCrimes.remove(c);
    }

    /**
     *在CrimeLab类中保存crime记录
     */
    public boolean saveCrimes(){
        try{
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG,"crimes saved to file");
            return true;
        }catch (Exception e){
            Log.e(TAG,"Error saving crimes:", e);
            return false;
        }
    }

}
