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
import com.example.Wo_Ca_Lo.data.ExampleList_pr;
import com.example.Wo_Ca_Lo.data.FormatEnglish;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * 初級單字 Fragmnet 介面
 */
public class Pr_Fragment extends Fragment {
    private  static  final  String TAG="Pr_Fragment";
    private RecyclerView recyclerView;
    private PageViewModel pageViewModel;
    private LinearAdapter Pr_Adapter;
    private ArrayList<FormatEnglish> Pr_list;
    private HashMap<Integer,Boolean> Check;
    private MyAsyncTask myAsyncTask;
    private int Pr_list_size;

    public static Pr_Fragment newInstance() {
        Pr_Fragment fragment = new Pr_Fragment();

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
        View root = inflater.inflate(R.layout.pr_fragment, container, false);
        recyclerView=root.findViewById(R.id.main_1_Rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pageViewModel.getPr_Mutable().observe(getActivity(), new Observer<ArrayList<FormatEnglish>>() {
            @Override
            public void onChanged(@Nullable ArrayList<FormatEnglish> formatEnglishes) {
                Pr_list_size=formatEnglishes.size();
                myAsyncTask=new MyAsyncTask();
                myAsyncTask.execute();
            }
        });
        pageViewModel.getBackground().observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(@Nullable String[] strings) {
                Log.d(TAG, "onChanged: "+strings[0]+strings[1]+strings[2]);
                if(Pr_Adapter!=null){
                    Pr_Adapter.setText_allsize(strings[0]);
                    Pr_Adapter.setText_color(strings[1]);
                    Pr_Adapter.setBakgrundcolor(strings[2]);

                    if(myAsyncTask!=null){
                        myAsyncTask.cancel(true);
                        myAsyncTask=null;
                    }

                    myAsyncTask=new MyAsyncTask();

                    myAsyncTask.execute();
                }


            }
        });

        return root;
    }


    private  class MyAsyncTask extends AsyncTask<Void,Void,String[]>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Check=pageViewModel.getHashMapMutable().getValue();
            Pr_list=new ExampleList_pr().englishes();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<Pr_list.size();i++){
                        Pr_list.get(i).setNumber(i);

                    }
                }
            }).start();
        }

        @Override
        protected void onPostExecute(String [] strings) {
            super.onPostExecute(strings);

            Pr_Adapter=new LinearAdapter(getActivity(),Pr_list);

            //傳出適配器 給mainActivty 做搜尋
            pageViewModel.getPr_Adapter().postValue(Pr_Adapter);
            //從linearAdapter callback 取得 checkBox 跟 onBindViewHolder 的postion 跟 FormatEnglish List
            Pr_Adapter.set_onchangeListner(new LinearAdapter.SetonChangeListner() {
                @Override
                public void onchage(final CheckBox checkBox, final int position, ArrayList<FormatEnglish> list) {

                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            //如果不為空 設定按鈕改變 並把值 傳送出去
                            if(Check.get(position)!=null) {
                                Check.put((Pr_list.get(position).getNumber()) , isChecked);
                                Log.d(TAG, "onCheckedChanged: "+Pr_list.get(position).getNumber());
                                pageViewModel.getHashMapMutable().setValue(Check);
                            }
                            Log.d(TAG, "onCheckedChanged: "+(position)+"/"+isChecked);
                        }
                    });
                    //比搜尋時size大的 position 不設置check 會有異位錯誤發生
                    if (Pr_list.size()==Pr_list_size&&Pr_list.get(position)!=null){
                        checkBox.setChecked(Check.get((Pr_list.get(position).getNumber())));
                    }
                    //如果hashmap在其他地方有有跟改則回調 跟新按鈕值
                    pageViewModel.getHashMapMutable().observe(Pr_Fragment.this, new Observer<HashMap<Integer, Boolean>>() {
                        @Override
                        public void onChanged(@Nullable HashMap<Integer, Boolean> integerBooleanHashMap) {
                            //比搜尋時size大的 position 不設置check 會有異位錯誤發生
                            if (Pr_list.size()>=(position+1)){
                                 checkBox.setChecked(integerBooleanHashMap.get((Pr_list.get(position).getNumber())));


                            }

                        }
                    });

                }
            });

            if (recyclerView.getAdapter()!=null){
                //刷新當前介面
                recyclerView.setAdapter(null);
            }


            recyclerView.swapAdapter(Pr_Adapter,false);
        }

        @Override
        protected String[] doInBackground(Void... voids) {
            return new String[0];
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
        myAsyncTask.cancel(true);
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(myAsyncTask!=null){
            myAsyncTask.cancel(true);
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