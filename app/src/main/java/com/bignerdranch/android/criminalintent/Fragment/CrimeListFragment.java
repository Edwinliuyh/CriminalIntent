package com.bignerdranch.android.criminalintent.Fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.bignerdranch.android.criminalintent.Activity.CrimePagerActivity;
import com.bignerdranch.android.criminalintent.Model.Crime;
import com.bignerdranch.android.criminalintent.Model.CrimeLab;
import com.bignerdranch.android.criminalintent.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Crimes的控制层
 */
public class CrimeListFragment extends ListFragment {
    private ArrayList<Crime> mCrimes;
    private static final String TAG="CrimeListFragment";
    private static final int REQUEST_CRIME=1;

    /**
     * 生成视图
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.crimes_title);//允许fragment处理activity事务。这里，使用它调用Activity.setTitle(int)
        mCrimes= CrimeLab.get(getActivity()).getCrimes();//获得模型层
        CrimeAdapter adapter= new CrimeAdapter(mCrimes);
        setListAdapter(adapter);//给内置的View设置adapter
    }

    /**
     *设置点击事件
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime c = ((CrimeAdapter) getListAdapter()).getItem(position);//通过点击的位置获得Crime
        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID,c.getId());//附加Crime对象的信息
        startActivityForResult(i,REQUEST_CRIME);//启动CrimeActivity
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_CRIME){
            //handle result
        }
    }

    /**
     *
     */
    private class CrimeAdapter extends ArrayAdapter<Crime>{
        public CrimeAdapter(ArrayList<Crime> crimes){
            super(getActivity(),0,crimes);//在构造方法中调用父类的构造方法，将adapter与crimes绑定
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null){ //如果无convertView，用list_item_crime布局文件填充
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_crime,null);
            }
            Crime c=getItem(position); //获得Crime对象
            TextView titleTextView=
                    (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
            TextView dateTextView=
                    (TextView)convertView.findViewById(R.id.crime_list_item_dateText);
            CheckBox solvedCheckBox=
                    (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            titleTextView.setText(c.getTitle());
            dateTextView.setText(c.getDate().toString());
            solvedCheckBox.setChecked(c.isSolved());

            return convertView;
        }
    }

    /**
     * 恢复视图时，用adpter通知模型层数据变更，重新加载
     */
    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }
}
