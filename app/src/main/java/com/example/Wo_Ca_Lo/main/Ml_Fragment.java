package com.example.Wo_Ca_Lo.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.Wo_Ca_Lo.Adapter.LinearAdapter;
import com.example.Wo_Ca_Lo.Login.Check;
import com.example.Wo_Ca_Lo.R;
import com.example.Wo_Ca_Lo.data.ExampleList_in;
import com.example.Wo_Ca_Lo.data.ExampleList_pr;
import com.example.Wo_Ca_Lo.data.FormatEnglish;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * 我的最愛單字 Fragment介面
 */
public class Ml_Fragment extends Fragment {
    private  static  final  String TAG="Ml_Fragment";
    private RecyclerView recyclerView;
    private LinearAdapter Ml_Adapter;
    private PageViewModel pageViewModel;
    private ArrayList<FormatEnglish> Ml_list;
    private HashMap<Integer,Boolean> Check;
    private ArrayList<FormatEnglish> Pr_list,In_list;
    private mAsyncTask UpadateUI;
    private int Pr_size,In_size;
    public static Ml_Fragment newInstance() {
        Ml_Fragment fragment = new Ml_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        Ml_list=new ArrayList<>();;

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ml_fragment, container, false);
        recyclerView=root.findViewById(R.id.main_3_RV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        UpadateUI=new mAsyncTask();
        UpadateUI.execute();
        Log.d(TAG, "onCreateView: ");
        //背景改變時刷新介面
        pageViewModel.getBackground().observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(@Nullable String[] strings) {


                if(UpadateUI!=null){
                    UpadateUI.cancel(true);
                    UpadateUI=null;
                }

                UpadateUI=new mAsyncTask();
                UpadateUI.execute();
            }
        });
        //當viewpager按回我的最愛做刷新頁面
        pageViewModel.getBooleanLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {

                    if(UpadateUI!=null){
                        UpadateUI.cancel(true);
                        UpadateUI=null;
                    }

                UpadateUI=new mAsyncTask();
                    UpadateUI.execute();

            }
        });

        return root;
    }



    public    class  mAsyncTask extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //清空 列表
            Ml_list.clear();
            //從mainActivity取得hashmap
            Check=pageViewModel.getHashMapMutable().getValue();
            //從mainActivity取得初級單字列表
            Pr_list=pageViewModel.getPr_Mutable().getValue();
            //從mainActivity取得中級單字列表
            In_list=pageViewModel.getIn_Mutable().getValue();
            Pr_size=Pr_list.size();
            In_size=In_list.size();
            Log.d(TAG, "onPreExecute: "+Ml_list.size()+"/"+In_list.size()+"/"+Pr_size);
        }
        @Override
        protected Object doInBackground(Object[] objects) {

            //取得hashmap 中的 boolean 為ture則加入到列表
            for(int i=0;i<Check.size();i++){
                Log.d(TAG, "doInBackground: "+ +i+"/"+Check.get(i));
                if (i < Pr_list.size()) {
                    if(Check.get(i).booleanValue()){
                        Log.d(TAG, "Pr_Check: "+i+Check.get(i).booleanValue());
                        Ml_list.add(Pr_list.get(i));
                        Log.d(TAG, "doInBackground: "+Ml_list.size()+"/"+In_list.size()+"/"+Pr_size);
                    }
                }else{


                    if(Check.get(i).booleanValue()){
                        Log.d(TAG, "In_Check: "+i+Check.get(i).booleanValue());

                        Ml_list.add(In_list.get(i-Pr_list.size()));
                        Log.d(TAG, "doInBackground: "+Ml_list.size()+"/"+In_list.size()+"/"+Pr_size);
                    }
                }

            }
            Log.d(TAG, "doInBackground: ");
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {

            Ml_Adapter = new LinearAdapter(getActivity(), Ml_list);
            pageViewModel.getMl_Adapter().postValue(Ml_Adapter);
            //從linearAdapter callback 取得 checkBox 跟 onBindViewHolder 的postion 跟 FormatEnglish List
            Ml_Adapter.set_onchangeListner(new LinearAdapter.SetonChangeListner() {
                @Override
                public void onchage(final CheckBox checkBox, final int position, ArrayList<FormatEnglish> list) {

                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if(Check.get(Ml_list.get(position).getNumber())!=null){
                                //取得列表中 對應其他list的數字
                                Check.put(Ml_list.get(position).getNumber(), isChecked);
                                //設定hashmap
                                pageViewModel.getHashMapMutable().setValue(Check);
                            }

                            //按鈕為false 跟新介面
                            if(isChecked==false){

                                if(UpadateUI!=null){
                                    UpadateUI.cancel(true);
                                    UpadateUI=null;
                                }

                                UpadateUI=new mAsyncTask();
                                UpadateUI.execute();
                            }
                        }

                    });

                    //從Ml_list取得對應Check的數值 交給Check中取得設定值
                    checkBox.setChecked(Check.get(Ml_list.get(position).getNumber()));
                 //   checkBox.setChecked(Check.get(position));
                }
            });
            if(recyclerView.getAdapter()!=null){
                //刷新當前介面
                recyclerView.setAdapter(null);
            }

            //設定recyclerView 列表
            recyclerView.swapAdapter(Ml_Adapter,false);

            super.onPostExecute(o);
        }

      
    }
    
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        if(UpadateUI!=null){
            UpadateUI.cancel(true);
            UpadateUI=null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
    }
}