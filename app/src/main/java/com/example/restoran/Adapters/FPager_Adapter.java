package com.example.restoran.Adapters;

import android.content.Context;
import android.content.pm.SigningInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.restoran.Fragements.SginUpFragment;
import com.example.restoran.Fragements.SigninFragment;
import com.example.restoran.LoginRegister;

public class FPager_Adapter extends FragmentPagerAdapter {

    Context context;
    SginUpFragment sup;
    SigninFragment sin;
    public FPager_Adapter(@NonNull FragmentManager fm) {
        super(fm);

    }
    public void setContext(Context context,LoginRegister aci){
        this.context = context;
        sup = new SginUpFragment();
        sin = new SigninFragment();
        sup.setAcitivity(aci);
        sin.setAcitivity(aci);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position==0){
            return sin;
        }
        return sup;
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
