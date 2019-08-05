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
import com.example.Wo_Ca_Lo.R;
import com.example.Wo_Ca_Lo.data.ExampleList_in;
import com.example.Wo_Ca_Lo.data.FormatEnglish;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * 中級單字 Fragment介面
 */
public class In_Fragment extends Fragment {
    private RecyclerView recyclerView;
    private  ArrayList<FormatEnglish> In_list;
    private LinearAdapter In_Adapter;
    private static  final  String TAG="In_Fragment";
    private PageViewModel pageViewModel;

    private HashMap<Integer,Boolean> Check;
    private MyAsyncTask  myAsyncTask;
    private int Pr_size,In_list_size;

    public static In_Fragment newInstance() {
        In_Fragment fragment = new In_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.in_fragment, container, false);

        recyclerView=root.findViewById(R.id.main_2_RV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //從mainActivity 接受資料
        pageViewModel.getIn_Mutable().observe(getActivity(), new Observer<ArrayList<FormatEnglish>>() {
            @Override
            public void onChanged(@Nullable ArrayList<FormatEnglish> formatEnglishes) {
                In_list_size=formatEnglishes.size();

                myAsyncTask=new MyAsyncTask();
                myAsyncTask.execute();

            }
        });

        //字體顏色 大小 背景 顏色改變時 刷新介面
        pageViewModel.getBackground().observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(@Nullable String[] strings) {


                if(myAsyncTask!=null){
                    myAsyncTask.cancel(true);
                    myAsyncTask=null;
                }

                myAsyncTask=new MyAsyncTask();
                myAsyncTask.execute();
            }
        });
        return root;
    }
    private  class MyAsyncTask extends AsyncTask{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            In_list=new ExampleList_in().englishes();
            Pr_size=pageViewModel.getPr_Mutable().getValue().size();
            for(int i=0;i<In_list.size();i++){
                In_list.get(i).setNumber((i+Pr_size));
            }
            //從MainActivity取得hashmap
            Check=pageViewModel.getHashMapMutable().getValue();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            In_Adapter=new LinearAdapter(getActivity(),In_list);
            //傳出適配器 給mainActivty 做搜尋
            pageViewModel.getIn_Adapter().postValue(In_Adapter);
            //從linearAdapter callback 取得 checkBox 跟 onBindViewHolder 的postion 跟 FormatEnglish List
            In_Adapter.set_onchangeListner(new LinearAdapter.SetonChangeListner() {
                @Override
                public void onchage(final CheckBox checkBox, final int position, ArrayList<FormatEnglish> list) {

                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            //如果不為空 設定按鈕改變 並把值 傳送出去
                            if(Check.get(position)!=null) {
                                Check.put(In_list.get(position).getNumber() , isChecked);
                                pageViewModel.getHashMapMutable().setValue(Check);
                            }

                        }
                    });
                    //比搜尋時size大的 position 不設置check 會有異位錯誤發生
                    if (In_list.size()>=(position+1)&&In_list.get(position)!=null){
                        checkBox.setChecked(Check.get((In_list.get(position).getNumber())));
                    }
                    //如果hashmap在其他地方有有跟改則回調 跟新按鈕值
                    pageViewModel.getHashMapMutable().observe(In_Fragment.this, new Observer<HashMap<Integer, Boolean>>() {
                        @Override

                        public void onChanged(@Nullable HashMap<Integer, Boolean> integerBooleanHashMap) {
                            //比搜尋時size大的 position 不設置check 會有異位錯誤發生
                            if (In_list.size()>=(position+1)){
                                checkBox.setChecked(integerBooleanHashMap.get((In_list.get(position).getNumber())));
                            }
                        }
                    });


                }
            });
            if(recyclerView.getAdapter()!=null){
                //刷新當前介面
                recyclerView.setAdapter(null);
            }

            recyclerView.swapAdapter(In_Adapter,false);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(myAsyncTask!=null){
            myAsyncTask.cancel(true);
            myAsyncTask=null;
        }
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