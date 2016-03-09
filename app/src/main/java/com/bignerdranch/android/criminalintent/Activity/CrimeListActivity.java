package com.bignerdranch.android.criminalintent.Activity;

import android.app.Fragment;

import com.bignerdranch.android.criminalintent.Fragment.CrimeListFragment;

/**
 * 用于托管CrimeListFragment
 */
public class CrimeListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
