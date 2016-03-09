package com.bignerdranch.android.criminalintent.Model;

import java.util.Date;
import java.util.UUID;

public class Crime {
	
	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean mSolved;

	/**
	 *  模型层Crime类
	 */
	public Crime(){
		mId=UUID.randomUUID();
		mDate = new Date();
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

}
