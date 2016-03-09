package com.bignerdranch.android.criminalintent.Activity;

import android.app.Fragment;

import com.bignerdranch.android.criminalintent.Fragment.CrimeFragment;

import java.util.UUID;

/**
 * 用于托管CrimeFragment
 */
public class CrimeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        UUID crimeId=(UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }
}
