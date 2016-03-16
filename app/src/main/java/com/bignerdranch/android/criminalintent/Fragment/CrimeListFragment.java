package com.bignerdranch.android.criminalintent.Fragment;

import android.annotation.TargetApi;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
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

public class CrimeListFragment extends ListFragment {
    private ArrayList<Crime> mCrimes;
    private Button addCrimeButton;
    private boolean mSubtitleVisible;
    private static final String TAG="CrimeListFragment";
    private static final int REQUEST_CRIME=1;

    /**
     * 在onCreate，数据模型的初始化
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
     * 在onCreateView
     * 根据变量mSubtitleVisible的值，设置子标题
     * 为上下文菜单、上下文操作登记ListView，并处理兼容性问题
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

        ListView listView = (ListView) v.findViewById(android.R.id.list);
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB){
            registerForContextMenu(listView);//使用浮动的上下文菜单，登记ListView
        }else{
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);//自动使用上下文操作，listview可以多选
            //设置MultiChoiceModeListener监听器
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener(){
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked){
                //Required, but not used in this implementation
                }
                //ActionMode的回调，填充上下文操作栏的布局
                public boolean onCreateActionMode(ActionMode mode, Menu menu){
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.crime_list_item_context, menu);
                    return true;
                }
                public boolean onPrepareActionMode(ActionMode mode, Menu menu){
                    return false;
                //Required, but not used in this implementation
                }
                //点击事件的响应
                public boolean onActionItemClicked(ActionMode mode, MenuItem item){
                    switch (item.getItemId()){
                        case R.id.menu_item_delete_crime:
                            CrimeAdapter adapter =(CrimeAdapter) getListAdapter();
                            CrimeLab crimeLab= CrimeLab.get(getActivity());
                            for (int i = adapter.getCount()-1; i>=0; i--){
                                if (getListView().isItemChecked(i)){
                                    crimeLab.deleteCrime(adapter.getItem(i));//所有选中的item(i)对应的crime都要删除
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();//adapter通知数据变更
                            return true;
                        default:
                            return false;
                    }
                }
                public void onDestroyActionMode(ActionMode mode){
                //Required, but not used in this implementation
                }
            });
        }
        return v;
    }

    /**
     * 在onStart，增加用于列表的空视图
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
     *点击List的子项，跳转去响应的CrimePager页面
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime c = ((CrimeAdapter) getListAdapter()).getItem(position);//通过点击的位置获得Crime
        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID,c.getId());//附加Crime对象的信息
        startActivityForResult(i,REQUEST_CRIME);//启动CrimeActivity
    }

    //??
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_CRIME){
            //handle result
        }
    }

    /**
     * 实例化生成右上角选项菜单
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
     *右上角选项菜单项的选择事件响应
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
     * 实例化生成上下文菜单
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
    }

    /**
     *监听上下文菜单项选择事件
     */
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position=info.position; //获取选中子项在数据集的位置信息
        CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
        Crime crime = adapter.getItem(position);//根据位置获取对应的crime对象
        switch(item.getItemId()){
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(crime);//删除crime
                adapter.notifyDataSetChanged();//更新显示的数据
                return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     *内部类CrimeAdapter + convertView
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
     * onRsume时，用adpter通知变更，重新加载模型层数据
     */
    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }
}
