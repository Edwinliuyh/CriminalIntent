package com.bignerdranch.android.criminalintent.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;

import com.bignerdranch.android.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 修改时间的对话框
 */
public class TimePickerFragment extends DialogFragment {
    public static final String EXTRA_TIME =
            "com.bignerdranch.android.criminalintent.time";
    private Date mDate;

    /**
     * 用CrimeFragment传递给TimePickerFragment的数据，创建PickerFragment实例（Bundle）
     */
    public static TimePickerFragment newInstance(Date date){
        Bundle args= new Bundle();
        args.putSerializable(EXTRA_TIME,date);
        TimePickerFragment fragment=new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    };

    /**
     *在onCreateDialog，初始化picker的数据，监听picker的变化
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //从args获取Date对象，使用Calendar提取Date信息
        mDate = (Date) getArguments().getSerializable(EXTRA_TIME);
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(mDate);
        final int year= calendar.get(Calendar.YEAR);
        final int month= calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour= calendar.get(Calendar.HOUR_OF_DAY);
        int min= calendar.get(Calendar.MINUTE);
        final int sec = calendar.get(Calendar.SECOND);
        //获取布局和对话框控件，用Date信息初始化DatePicker，加监听器
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_time,null);
        TimePicker timePicker =(TimePicker)v.findViewById(R.id.dialog_time_timepicker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(min);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mDate = new GregorianCalendar(year, month, day, hourOfDay, minute, sec).getTime();
                getArguments().putSerializable(EXTRA_TIME,mDate);//监听日期变化，并保存在Args
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(
                        android.R.string.ok,
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                sendResult(Activity.RESULT_OK);} //启用sendResult方法
                        })
                .create();
    }

    /**
     * sendResult方法，回调目标fragment的onActivityResult
     */
    private void sendResult(int resultCode){
        if (getTargetFragment()==null)
            return;
        Intent i= new Intent();
        i.putExtra(EXTRA_TIME,mDate);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,i);
    }
}
