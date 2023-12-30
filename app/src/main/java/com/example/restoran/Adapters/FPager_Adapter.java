package com.example.restoran.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.restoran.Fragements.SginUpFragment;
import com.example.restoran.Fragements.SigninFragment;

public class FPager_Adapter extends FragmentPagerAdapter {

    public FPager_Adapter(@NonNull FragmentManager fm) {
        super(fm);

    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position==0){
            return new SigninFragment();
        }
        return new SginUpFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0){
            return "Sign In";
        }
        return "Sign Up";
    }

    @Override
    public int getCount() {
        return 2;
    }
}
