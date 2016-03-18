package com.bignerdranch.android.criminalintent.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dell on 2016/3/18.
 */
public class Photo {
    private static final String JSON_FILENAME="filename";
    private String mFilename;

    /**
     * 根据给定的文件名创建一个Photo对象
     */
    public Photo(String filename){
        mFilename=filename;
    }

    /**
     * 根据给定的JSON创建一个Photo对象
     */
    public Photo(JSONObject json) throws JSONException{
        mFilename=json.getString(JSON_FILENAME);
    }

    /**
     * 将Photo对象转为JSON
     * @return
     * @throws JSONException
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject json= new JSONObject();
        json.put(JSON_FILENAME, mFilename);
        return json;
    }

    /**
     * Filenam的get方法
     */
    public String getFilename(){
        return mFilename;
    }
}
