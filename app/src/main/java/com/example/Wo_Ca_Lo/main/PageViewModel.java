package com.example.Wo_Ca_Lo.main;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;


import com.example.Wo_Ca_Lo.Adapter.LinearAdapter;
import com.example.Wo_Ca_Lo.data.FormatEnglish;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Activity 跟 Fragment 數據共享跟數據跟新
 */
public class PageViewModel extends ViewModel {

    private  static MutableLiveData<ArrayList<FormatEnglish>> In_Mutable;   //中級單字列表
    private  static MutableLiveData<HashMap<Integer,Boolean>> hashMapMutable; //所有按鈕設定值
    private  static MutableLiveData<ArrayList<FormatEnglish>> Pr_Mutable; //初級單字列表
    private  static MutableLiveData<Boolean> booleanLiveData; //我的最愛 跟新值
    private  static  MutableLiveData<LinearAdapter> Pr_Adapter; //初級單字 適配器
    private  static  MutableLiveData<LinearAdapter> In_Adapter;//中級單字 適配器
    private  static  MutableLiveData<LinearAdapter> Ml_Adapter; //我的最愛 適配器
    private  static  MutableLiveData<String[]>background;//背景樣式

    public  MutableLiveData<String[]> getBackground() {
        if(background==null){
            background=new MutableLiveData<>();
        }
        return background;
    }



    public  MutableLiveData<LinearAdapter> getMl_Adapter() {
        if(Ml_Adapter==null){
            Ml_Adapter=new MutableLiveData<>();
        }
        return Ml_Adapter;
    }


    public  MutableLiveData<LinearAdapter> getPr_Adapter() {
       if(Pr_Adapter==null){
           Pr_Adapter=new MutableLiveData<>();
       }
        return Pr_Adapter;
    }

    public  MutableLiveData<LinearAdapter> getIn_Adapter() {
        if(In_Adapter==null){
            In_Adapter=new MutableLiveData<>();
        }
        return In_Adapter;
    }


    //判斷我的最愛跟新時機
    public MutableLiveData<Boolean> getBooleanLiveData() {
        if(booleanLiveData==null){
            booleanLiveData=new MutableLiveData<>();
        }
        return booleanLiveData;
    }


    //基本單字資料
    public MutableLiveData<ArrayList<FormatEnglish>> getPr_Mutable() {
        if(Pr_Mutable==null){
            Pr_Mutable=new MutableLiveData<>();
        }
        return Pr_Mutable;
    }
    //中級單字資料
    public MutableLiveData<ArrayList<FormatEnglish>> getIn_Mutable() {
        if(In_Mutable==null){
            In_Mutable=new MutableLiveData<>();
        }
        return In_Mutable;
    }
    //傳遞check點即資料
    public MutableLiveData<HashMap<Integer, Boolean>> getHashMapMutable() {
        if(hashMapMutable==null){
            hashMapMutable=new MutableLiveData<>();
        }
        return hashMapMutable;
    }




}