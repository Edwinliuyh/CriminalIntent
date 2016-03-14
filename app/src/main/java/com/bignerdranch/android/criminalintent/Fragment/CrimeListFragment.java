package com.bignerdranch.android.criminalintent.Fragment;

import android.annotation.TargetApi;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private Button addCrimeButton;
    private boolean mSubtitleVisible;
    private static final String TAG="CrimeListFragment";
    private static final int REQUEST_CRIME=1;

    /**
     * 生成视图
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//显示选项菜单
        getActivity().setTitle(R.string.crimes_title);//允许fragment处理activity事务。这里，使用它调用Activity.setTitle(int)
        mCrimes= CrimeLab.get(getActivity()).getCrimes();//获得模型层
        CrimeAdapter adapter= new CrimeAdapter(mCrimes);
        setListAdapter(adapter);//给内置的View设置adapter
        setRetainInstance(true);//设置保留，以免旋转销毁
        mSubtitleVisible=false;//变量初始化
    }

    /**
     * 根据变量mSubtitleVisible的值设置子标题
     */
    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB){
            if (mSubtitleVisible){
                getActivity().getActionBar().setSubtitle(R.string.subtitle);
            }
        }
        return v;
    }

    /**
     * 用于列表的空视图
     */
    @Override
    public void onStart() {
        super.onStart();
        View emptyView =getActivity().getLayoutInflater().inflate(R.layout.fragment_crime_list,null);
        ((ViewGroup)this.getListView().getParent()).addView(emptyView);
        this.getListView().setEmptyView(emptyView);
        Button addCrimeButton = (Button) emptyView.findViewById(R.id.add_crime);
        addCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crime crime= new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent i = new Intent(getActivity(),CrimePagerActivity.class);
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getId());
                startActivityForResult(i,0);
            }
        });
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
     * 实例化生成选项菜单
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu (menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible && showSubtitle !=null){ //基于mSubtitleVisible变量的值，正确显示菜单项标题
            showSubtitle.setTitle(R.string.hide_subtitle);
        }
    }

    /**
     *响应菜单项选择事件
     */
    @TargetApi(11)
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime= new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent i = new Intent(getActivity(),CrimePagerActivity.class);
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getId());
                startActivityForResult(i,0);
                return true;
            case R.id.menu_item_show_subtitle:
                if (getActivity().getActionBar().getSubtitle()==null){ //如果操作栏上没有显示子标题，则应设置显示子标题
                    getActivity().getActionBar().setSubtitle(R.string.subtitle);
                    mSubtitleVisible=true;
                    item.setTitle(R.string.hide_subtitle);//菜单项显示为隐藏
                }else{
                    getActivity().getActionBar().setSubtitle(null);//子标题为null
                    mSubtitleVisible=false;
                    item.setTitle(R.string.show_subtitle);//菜单项切换为显示
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
