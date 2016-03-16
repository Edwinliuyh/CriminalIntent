package com.bignerdranch.android.criminalintent.Activity;

import android.app.Fragment;

import com.bignerdranch.android.criminalintent.Fragment.CrimeListFragment;

/**
 * 托管CrimeListFragment的Activity
 */
public class CrimeListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
