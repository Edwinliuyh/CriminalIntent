package com.bignerdranch.android.criminalintent.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

public class Crime {
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE="title";
	private static final String JSON_SOLVED = "solved";
	private static final String JSON_DATE="date";
	private static final String JSON_PHOTO="photo";

	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean mSolved;
	private Photo mPhoto;

	/**
	 *  构造Crime的方法
	 */
	public Crime(){
		mId=UUID.randomUUID();
		mDate = new Date();
	}

	/**
	 * 接受JSONObject对象的构造Crime的方法
     */
	public Crime(JSONObject json) throws JSONException{
		mId=UUID.fromString(json.getString(JSON_ID));
		if (json.has(JSON_TITLE)){
			mTitle=json.getString(JSON_TITLE);
		}
		mSolved = json.getBoolean(JSON_SOLVED);
		mDate=new Date(json.getLong(JSON_DATE));
		if (json.has(JSON_PHOTO))
			mPhoto= new Photo(json.getJSONObject(JSON_PHOTO));
	}

	/**
	 * ListView调用adapter的getView(...)依赖于toString()方法。它首先生成布
	 局视图，然后找到指定位置的Crime对象并对其调用toString()方法，最后得到字符串信息并传
	 递给TextView。
	 */
	@Override
	public String toString() {
		return mTitle;
	}

	/**
	 *将Crime对象数据转换为可写入JSON文件的JSONObject对象数据。
     */
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_SOLVED,mSolved);
		json.put(JSON_DATE, mDate.getTime());
		return json;
	}

	//下面是所有数据域的get方法和set方法
	public UUID getId() {
		return mId;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public boolean isSolved() {
		return mSolved;
	}

	public void setSolved(boolean solved) {
		mSolved = solved;
	}

	public Photo getPhoto(){
		return mPhoto;
	}

	public void setPhoto(Photo p) {
		mPhoto = p;
	}

}
