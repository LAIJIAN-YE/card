package com.example.tryapplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 *  管理所有Activity列表
 */
public class ActivityCollector {

    public  static List<Activity> activities=new ArrayList<>();
    //增加
    public  static  void  addActivity(Activity activity){
        activities.add(activity);
    }
    //刪除
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    //刪除列表內所有
    public  static  void finishAll(){
        for(Activity activity:activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }

    }

}
