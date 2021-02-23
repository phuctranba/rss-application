package com.e.rssapplication.LocalStorageActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.e.rssapplication.DataBase.EnumWebSite;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        EnumWebSite enumWebSite = EnumWebSite.TUOITRE;

        switch (position){
            case 0: enumWebSite = EnumWebSite.CAND; break;
            case 1: enumWebSite = EnumWebSite.HAITUH; break;
            case 2: enumWebSite = EnumWebSite.VNEXPRESS; break;
            case 3: enumWebSite = EnumWebSite.VTC; break;
            case 4: enumWebSite = EnumWebSite.THANHNIEN; break;
        }

        Bundle data = new Bundle();
        LocalNewsFragment fragment = new LocalNewsFragment();
        data.putSerializable("WEBSITE",enumWebSite);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
}
