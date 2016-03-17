package com.bignerdranch.android.criminalintent.Fragment;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.bignerdranch.android.criminalintent.Activity.CrimeListActivity;
import com.bignerdranch.android.criminalintent.Activity.CrimePagerActivity;
import com.bignerdranch.android.criminalintent.Model.Crime;
import com.bignerdranch.android.criminalintent.Model.CrimeLab;
import com.bignerdranch.android.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class CrimeFragment extends Fragment {
	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private Button mTimeButton;
	private CheckBox mSolvedCheckBox;
	public static final String EXTRA_CRIME_ID=
			"com.bignerdranch.android.criminalintent.crime_id";
	private static final String DIALOG_DATE="date";
	private static final String DIALOG_TIME="time";
	private static final int REQUEST_DATE=0;
	private static final int REQUEST_TIME=1;

	/**
	 * 将crimeId放入返回的CrimeFragment实例（Bundle）
	 */
	public static CrimeFragment newInstance(UUID crimeId){
		Bundle args =new Bundle();
		args.putSerializable(EXTRA_CRIME_ID,crimeId);
		CrimeFragment fragment=new CrimeFragment();
		fragment.setArguments(args);
		return fragment;
	};

	/**
	 * 在OnCreate，获得Bundle的crimeId，并通过crimeId寻找到对应的Crime
     */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UUID crimeId=(UUID)getArguments()
				.getSerializable(EXTRA_CRIME_ID);
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
		setHasOptionsMenu(true);//开启右上角选项菜单
	}

	/**
	 * 在onPause()方法中调用保存数据的方法
	 */
	@Override
	public void onPause(){
		super.onPause();
		CrimeLab.get(getActivity()).saveCrimes();
	}

	/**
	 *在onCreateView，生成视图、获得控件、加监听器
     */
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
							 Bundle savedInstanceState) {
		View v= inflater.inflate(R.layout.fragment_crime, parent, false);
		if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB){
			if (NavUtils.getParentActivityName(getActivity())!=null){ ////检查元数据中是否指定了父activity
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);//启用向上导航按钮
			}
		}
		
		mTitleField = (EditText) v.findViewById (R.id.crime_title);
		mTitleField.setText(mCrime.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				//
			}

			@Override
			public void onTextChanged(CharSequence c, int start, int before,
					int count) {
				mCrime.setTitle(c.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {
				//
			}
		});

		mDateButton=(Button)v.findViewById(R.id.crim_date);
		updateDate();
		mDateButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				FragmentManager fm =getActivity().getSupportFragmentManager();
				DatePickerFragment dialog = DatePickerFragment
						.newInstance(mCrime.getDate());//从CrimeFragment传Date数据给dialog
				dialog.setTargetFragment( CrimeFragment.this, REQUEST_DATE);//设置dialog的回传目标
				dialog.show(fm, DIALOG_DATE);//显示对话框
			}
		});

		mTimeButton=(Button)v.findViewById(R.id.crim_time);
		updateTime();
		mTimeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm=getActivity()
						.getSupportFragmentManager();
				TimePickerFragment dialog= TimePickerFragment
						.newInstance(mCrime.getDate());//从CrimeFragment传Date数据给dialog
				dialog.setTargetFragment(CrimeFragment.this,REQUEST_TIME);//设置dialog的回传目标
				dialog.show(fm,DIALOG_TIME);

			}
		});

		mSolvedCheckBox=(CheckBox)v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.isSolved());
		mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mCrime.setSolved(isChecked);
			}
		});
		
		return v;
		
	}

	/**
	 * 响应Picker对话框，更新Crime的数据，更新按钮的文本
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode!= Activity.RESULT_OK) return;
		if(requestCode==REQUEST_DATE){
			Date date = (Date)data
					.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			updateDate();
		}
		if(requestCode==REQUEST_TIME){
			Date date = (Date)data
					.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
			mCrime.setDate(date);
			updateTime();
		}

	}



	/**
	 * 实例化生成右上角选项菜单
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu (menu, inflater);
		inflater.inflate(R.menu.crime_list_item_context, menu);
	}

	/**
	 * 事件响应
	 * 响应图标Home键，向上导航的方法
	 * 右上角选项菜单项的选择事件响应
	 */
	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
			case android.R.id.home:
				//如果元数据中指定了父activity，导航至父activity界面
				if (NavUtils.getParentActivityName(getActivity())!=null){
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
			case R.id.menu_item_delete_crime:
				CrimeLab.get(getActivity()).deleteCrime(mCrime);//删除crime
				Intent i = new Intent(getActivity(),CrimeListActivity.class);
				startActivity(i);
				return true;
			default:
					return super.onOptionsItemSelected(item);
			}
		}

	//??
	public void returnResult(){
		getActivity().setResult(Activity.RESULT_OK,null);
	}

	//下面是更新日期和时间按钮文字的方法
	public void updateDate(){
		mDateButton.setText(DateFormat.format("yyyy年MM月dd日 EEEE",mCrime.getDate()));
	}
	public void updateTime(){
		mTimeButton.setText(DateFormat.format("kk:mm",mCrime.getDate()));
	};


}
