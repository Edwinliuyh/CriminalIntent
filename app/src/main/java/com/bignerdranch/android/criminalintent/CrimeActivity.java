package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

/**
 * 用于托管CrimeFragment
 */
public class CrimeActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }
}
