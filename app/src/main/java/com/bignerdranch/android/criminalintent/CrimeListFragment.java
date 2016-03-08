package com.bignerdranch.android.criminalintent;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Crimes的控制层
 */
public class CrimeListFragment extends ListFragment {
    private ArrayList<Crime> mCrimes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.crimes_title);//允许fragment处理activity事务。这里，使用它调用Activity.setTitle(int)
        mCrimes=CrimeLab.get(getActivity()).getCrimes();//获得模型层

        ArrayAdapter<Crime> adapter=
                new ArrayAdapter<Crime>(getActivity(),
                        android.R.layout.simple_list_item_1,
                        mCrimes);
        setListAdapter(adapter);//给内置的View设置adapter
    }
}
