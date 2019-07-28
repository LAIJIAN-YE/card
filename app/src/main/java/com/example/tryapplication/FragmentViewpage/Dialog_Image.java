package com.example.tryapplication.FragmentViewpage;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.tryapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * dialogFragment +FragmentPagerAdapter +Fragment
 * 創照換頁效果 主要用於介紹 軟體使用
 */
public class Dialog_Image extends DialogFragment {
    private TextView now,max;
    private View view;
    private ImageView right,left,close;
    private ViewPager viewPager;
    private int page=0;
    private static final String TAG="Dialog_Image";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //設定背景透明色
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         view=inflater.inflate(R.layout.viewpage,container);
         right=view.findViewById(R.id.pager_right);
         left=view.findViewById(R.id.pager_left);
        max=view.findViewById(R.id.num_max);
        now=view.findViewById(R.id.num_now);
         viewPager=view.findViewById(R.id.main_viewpage);
         close=view.findViewById(R.id.dialog_close);
        viewPager.setAdapter(new Viewpager(getChildFragmentManager(),fragments()));


        setView();

        return view;
    }

    private void setView() {
        //初始值
        now.setText("1");
        max.setText(""+fragments().size());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                //利用監聽viewPager 設定頁碼
                now.setText("" + (i+1));
                Log.d(TAG, "onPageSelected: " + i);
                page=i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        //往左邊翻頁按鈕
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page!=0){
                    viewPager.setCurrentItem(--page);
                }

            }
        });
        //往右邊翻頁按鈕
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page!=fragments().size()){
                    viewPager.setCurrentItem(++page);
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    private ArrayList<Fragment> fragments(){
        ArrayList<Fragment>fragments =new ArrayList<>();
        fragments.add(PhotoFragment.newInstance(R.drawable.home1));
        fragments.add(PhotoFragment.newInstance(R.drawable.home2));
        fragments.add(PhotoFragment.newInstance(R.drawable.home3));
        fragments.add(PhotoFragment.newInstance(R.drawable.home4));
        fragments.add(PhotoFragment.newInstance(R.drawable.home5));
        return  fragments;
    }

    /**
     * 管理 Fragment  的視配器
     */
 public class Viewpager extends FragmentPagerAdapter {

     private static final String TAG = "SectionsPagerAdapter";


     private ArrayList<Fragment> fragments;

     public Viewpager(FragmentManager fm, ArrayList<Fragment> fragments) {
         super(fm);
         this.fragments = fragments;


     }

     @Override
     public Fragment getItem(int position) {

         return fragments.get(position);
     }

     @Nullable


     @Override
     public int getCount() {

         return fragments.size();
         }
     }
}