package com.example.Wo_Ca_Lo.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;


import com.example.Wo_Ca_Lo.R;

import java.util.ArrayList;

/**
 * 主畫面 設置的 FragmentPager
 * Fragment不用時 隱藏起來 不調用 本身的刷新功能
 */
public class  SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG="SectionsPagerAdapter";
    private FragmentManager fragmentManager;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2,R.string.tab_text_3};
    private final Context mContext;
    private  ArrayList<Fragment> fragments=new ArrayList<>();

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        this.fragmentManager = fm;
        //加入集合
        fragments.add(Pr_Fragment.newInstance());
        fragments.add(In_Fragment.newInstance());
        fragments.add(Ml_Fragment.newInstance());

    }

    @Override
    public Fragment getItem(int position) {

        return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {

        return TAB_TITLES.length;
    }

    @Override

    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //隱藏 fragment
            Fragment fragment = fragments.get(position);

            fragmentManager.beginTransaction().hide(fragment).commit();

    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
            //顯示 framgment
            Fragment fragment = (Fragment) super.instantiateItem(container, position);

            this.fragmentManager.beginTransaction().show(fragment).commit();

            return fragment;


    }


}