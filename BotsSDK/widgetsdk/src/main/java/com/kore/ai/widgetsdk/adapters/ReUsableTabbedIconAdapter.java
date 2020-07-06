package com.kore.ai.widgetsdk.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.kore.ai.widgetsdk.models.KoreHomeFragmentModel;

import java.util.ArrayList;

public class ReUsableTabbedIconAdapter extends FragmentStatePagerAdapter {
    String userId;
    ArrayList<KoreHomeFragmentModel> fragmentModelArrayList;
    private ViewPager viewPager;

    public ReUsableTabbedIconAdapter(ViewPager viewPager, FragmentManager fm, ArrayList<KoreHomeFragmentModel> fragmentModels) {
        super(fm);
        this.fragmentModelArrayList = fragmentModels;
        this.viewPager = viewPager;

    }

    @Override
    public Fragment getItem(int position) {
        return fragmentModelArrayList.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return fragmentModelArrayList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentModelArrayList.get(position).getTitle();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }
}