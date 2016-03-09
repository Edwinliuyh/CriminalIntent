package com.bignerdranch.android.criminalintent.Activity;

import android.app.Fragment;

import com.bignerdranch.android.criminalintent.Fragment.CrimeFragment;

/**
 * 用于托管CrimeFragment
 */
public class CrimeActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }
}
