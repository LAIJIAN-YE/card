package com.example.Wo_Ca_Lo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.Wo_Ca_Lo.Adapter.phraseAdapter;
import com.example.Wo_Ca_Lo.data.ExampleList_in;
import com.example.Wo_Ca_Lo.data.ExampleList_pr;
import com.example.Wo_Ca_Lo.data.FormatEnglish;
import com.example.Wo_Ca_Lo.data.Phrase;
import com.example.tdialog.TDialog;
import com.example.tdialog.base.BindViewHolder;
import com.example.tdialog.listener.OnBindViewListener;
import com.example.tdialog.listener.OnViewClickListener;


import java.util.ArrayList;

/**
 *  聽力練習頁面
 */
public class PhraseActivity extends SuperActivity {
    private RecyclerView mrecyclerView;
    public ArrayList<Phrase> exampleList;
    private com.example.Wo_Ca_Lo.Adapter.phraseAdapter phraseAdapter;
    private Button log_confrim,log_cancel;
    private String Sdifficult,Squantity;
    private String tag[]=new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrase);
        findview();
        setview();
        setListener();
    ActivityCollector.addActivity(this);

    }
    private void findview(){
        mrecyclerView=findViewById(R.id.log_recy);
        log_cancel=findViewById(R.id.log_cancel);
        log_confrim=findViewById(R.id.log_confrim);
    }
    private  void setListener(){
        new TDialog.Builder(getSupportFragmentManager())
                .setLayoutRes(R.layout.phrase_dialog)
                .setCancelableOutside(false)
                .setScreenWidthAspect(PhraseActivity.this,0.5f)
                .setScreenHeightAspect(PhraseActivity.this,0.34f)
                .addOnClickListener(R.id.phd_confirm,R.id.phd_cancel)
                .setOnViewClickListener(new OnViewClickListener() {
                    @Override
                    public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
                        switch (view.getId()){
                            case R.id.phd_confirm:
                                //取得dialog 上面的資訊 做判斷  看要取得那個list 以及數量
                                if(Sdifficult!=null&&!Sdifficult.isEmpty()&&Squantity!=null&&!Squantity.isEmpty()){
                                    if (Sdifficult.equals("初級")&&Squantity.equals("十")){
                                        setdata("初級",10);
                                    }else if (Sdifficult.equals("初級")&&Squantity.equals("二十")){
                                        setdata("初級",20);
                                    }else if (Sdifficult.equals("初級")&&Squantity.equals("三十")){
                                        setdata("初級",30);
                                    }else if (Sdifficult.equals("中級")&&Squantity.equals("十")){
                                        setdata("中級",10);
                                    }else if (Sdifficult.equals("中級")&&Squantity.equals("二十")){
                                        setdata("中級",20);
                                    }else  if(Sdifficult.equals("中級")&&Squantity.equals("三十")){
                                        setdata("中級",30);
                                    }
                                }
                                tDialog.dismiss();
                                break;
                            case R.id.phd_cancel:
                                tDialog.dismiss();
                                finish();
                                break;

                        }
                    }
                })
                .setOnBindViewListener(new OnBindViewListener() {
                    @Override
                    public void bindView(BindViewHolder viewHolder) {
                        final Spinner difficult=viewHolder.getView(R.id.text_difficult);
                        Spinner quantity=viewHolder.getView(R.id.text_quantity);
                        ArrayAdapter<CharSequence> nAdapter_di = ArrayAdapter.createFromResource(
                                PhraseActivity.this, R.array.arr_difficult, android.R.layout.simple_dropdown_item_1line );

                        ArrayAdapter<CharSequence> nAdapter_qu = ArrayAdapter.createFromResource(
                                PhraseActivity.this, R.array.arr_quantity, android.R.layout.simple_dropdown_item_1line );
                        difficult.setAdapter(nAdapter_di);
                        quantity.setAdapter(nAdapter_qu);
                        difficult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Sdifficult=PhraseActivity.this.getResources().getStringArray(R.array.arr_difficult)[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Squantity=PhraseActivity.this.getResources().getStringArray(R.array.arr_quantity)[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                })
                .create()
                .show();

        //按鍵 確認時
        log_confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Nb_Correct=0;
                String aa="";
             //   比對 exampleList  跟輸入的 英文 把對的答案顯示
                for(int i=0;i<exampleList.size();i++) {
                    if (tag[i] != null) {
                        //     Log.d("exampleList",exampleList.get(i).getPhrase()+"/"+tag[i]);
                        if (exampleList.get(i).getPhrase().equals(tag[i])) {
                            aa =aa + "答對" + (i + 1)+ tag[i]+"\n";
                            Log.d("tag", tag[i] + "答對" + i);
                            Nb_Correct++;
                        }
                    }
                }


                Toast.makeText(PhraseActivity.this,aa+"",Toast.LENGTH_LONG).show();
                Intent i=new Intent();
                i.putExtra("Nb_Correct",Nb_Correct);
                i.putExtra("all",exampleList.size());
                setResult(RESULT_OK,i);
                finish();
            }
        });
        //返回 把頁面 關掉
        log_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });


    }
    private void setview(){

    }
        //把 dialog 所選擇的 級別 跟題目長度 做判斷 把難度 初級 或 中級
    private void setdata(String name,int lenght) {

        exampleList=new ArrayList<>();
        ArrayList<FormatEnglish> io=new ArrayList<>();

        if(name.equals("初級")){
            io=new ExampleList_pr().englishes();
        }else if(name.equals("中級")){
            io=new ExampleList_in().englishes();
        }

        //用陣列 長度為傳入的長度
        int[] arr=new int[lenght];
        //作亂數 把exampleList 傳入的數打亂
        for(int p=0;p<lenght;p++){
            int ramdom=(int)(Math.random()*io.size());//取exampleList 的長度 做整數 避免 超出
            arr[p]=ramdom;
            //做雙重迴圈 把重複的亂數 剃除
            for (int y=0;y<p;y++){
                if (arr[y]==arr[p]){
                    p--;
                    break;

                }
            }

        }
        //把亂數 做為取exampleList
        for(int i=0;i<lenght;i++){
            Log.d("arr",arr[i]+"");
            exampleList.add(new Phrase(io.get(arr[i]).getPhrase(),io.get(arr[i]).getPhrase_ch(),io.get(arr[i]).getMediaPlayer()));
        }
        //創建Adapter
        phraseAdapter=new phraseAdapter(PhraseActivity.this,exampleList);
        //設定流線布局
        mrecyclerView.setLayoutManager(new LinearLayoutManager(PhraseActivity.this));
        //把布局設定上去
        mrecyclerView.setAdapter(phraseAdapter);
        //回調 接口裡的
        //把EdiText 輸入的給tag陣列 作為 最後比對答案
        phraseAdapter.setTextchangeListner(new phraseAdapter.TextchangeListner() {
            @Override
            public void ontextchange(String s, int position ) {
                tag[position]=s;
                //  tai.add(position,s);
                Log.d("tag_on",tag[position]+"/"+position);

            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
