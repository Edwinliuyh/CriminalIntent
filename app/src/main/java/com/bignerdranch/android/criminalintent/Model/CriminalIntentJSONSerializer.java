package com.bignerdranch.android.criminalintent.Model;

import android.content.Context;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * 负责创建和解析JSON数据的工作
 */
public class CriminalIntentJSONSerializer {
    private Context mContext;
    private String mFilename;

    /**
     * 构造JSONSerializer的方法
     */
    public CriminalIntentJSONSerializer(Context c, String f){
            mContext=c;
            mFilename=f;
        }

    /**
     *保存crimes记录到文件的方法
     */
    public void saveCrimes(ArrayList<Crime> crimes) throws JSONException, IOException {
            JSONArray array= new JSONArray(); //Build an array in JSON
            for(Crime c : crimes){
                array.put(c.toJSON());}  //对所有crime记录调用toJSON()方法，放入JSONArray
            //Write the file to disk
            Writer writer = null;
            try{
                OutputStream out=null;
                if ((Environment.getExternalStorageState()).equals(Environment.MEDIA_MOUNTED)){
                    out=new FileOutputStream(new File(Environment.getExternalStorageDirectory(), mFilename));
                }else{
                    out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
                }
                writer = new OutputStreamWriter(out);
                writer.write(array.toString()); //JSONArray转为字符串写入文件
            }finally{
                if(writer != null)
                    writer.close();
            }
        }

    /**
     *从文件中加载crimes记录的方法
     */
    public ArrayList<Crime> loadCrimes() throws IOException, JSONException{
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        BufferedReader reader=null;
        try{
            //IO流将字符串文件逐行转为 StringBuilder
            InputStream in=null;
            if ((Environment.getExternalStorageState()).equals(Environment.MEDIA_MOUNTED)){
                in =new FileInputStream(new File(Environment.getExternalStorageDirectory(), mFilename));
            }else{
                in = mContext.openFileInput(mFilename);
            }
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line=null;
            while((line=reader.readLine())!=null){
                //Line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            //用 JSONTokener 将字符串 转型为 JSON数组
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            //从JSON的数组 转化成 crimes
            for (int i = 0; i<array.length(); i++){
                crimes.add(new Crime(array.getJSONObject(i)));
            }
        }catch (FileNotFoundException e){
            //Ignore this; it happens when starting fresh
        }finally{
            if(reader !=null)
                reader.close();
        }
        return crimes;
    }
}
