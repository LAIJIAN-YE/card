package com.example.Wo_Ca_Lo.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Wo_Ca_Lo.data.FormatEnglish;
import com.example.Wo_Ca_Lo.R;

import java.util.ArrayList;
import java.util.List;

/**
 *自訂義 視配器用於 初級單字 中級單字 跟我的最愛
 * 1.含有搜尋功能 過濾輸入單字選項
 * 2.字體 大小 顏色 跟背景 都可以改動
 * 3.調用 checkbox 給MainActivity Callback
 */
public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.LinearViewHilder> implements Filterable {
    private Context mContext;
    private ArrayList<FormatEnglish> exampleList;
    private ArrayList<FormatEnglish> exampleListFull;
    private   static String text_allsize; //宣告成靜態 防止頁面再重新調用時 會初始化
    private   static String text_color;
    private   static String bakgrundcolor;
    private  SetonChangeListner msetonchangeListner;
    private  MediaPlayer mediaPlayer;
    private  static  final String  TAG ="LinearAdapter";
    //接口 讓Activity 調用 onBindViewHolder 裡的 checkBox 跟 position 跟 list
    public interface SetonChangeListner{
        void onchage(CheckBox checkBox, int position, ArrayList<FormatEnglish> list);
    }
    //從外調用接口
    public  void set_onchangeListner (SetonChangeListner setonchangeListner){
        this.msetonchangeListner=setonchangeListner;
    }

    public  void setText_color(String text_color) {
        this.text_color = text_color;
    }

    public  void setBakgrundcolor(String bakgrundcolor) {
        this.bakgrundcolor = bakgrundcolor;
    }

    public  void setText_allsize(String text_allsize) { this.text_allsize = text_allsize; }
    //建構子  傳入 調用的 Context 跟設定好 exampleList
    public  LinearAdapter(Context context,ArrayList<FormatEnglish> exampleList){

        this.mContext=context;
        this.exampleList=exampleList;
        //創一個新的 在做搜尋時用
        exampleListFull=new ArrayList<>(exampleList);
    }
    @Override
    public LinearAdapter.LinearViewHilder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //引進布局管理器
        View itemView=(LayoutInflater.from(mContext).inflate(R.layout.activity_linear_adapter,viewGroup,false));
        return new LinearViewHilder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(final LinearAdapter.LinearViewHilder viewHolder, final int postion) {
            //透過postion 取得相對應的 formatEnglish資料
        final FormatEnglish formatEnglish=exampleList.get(postion);
        viewHolder.TPhrase.setText(formatEnglish.getPhrase());//英文單字
        viewHolder.li_num.setText(postion+1+":");//號碼
        viewHolder.TPhrase_ch.setText(formatEnglish.getPhrase_ch());//英文單字中文
        viewHolder.Singleword.setText(formatEnglish.getSingleword());//片語
        viewHolder.Singleword_ch.setText(formatEnglish.getSingleword_ch());//片語中文
        viewHolder.Phrase_kk.setText(formatEnglish.getPhrase_kk());//英文單字kk
        viewHolder.radio_bt.setOnClickListener(new View.OnClickListener() { //音檔
            @Override
            public void onClick(View v) {
                //把音檔的 int 數 傳過來 在這邊啟動 音檔
                mediaPlayer.create(mContext,formatEnglish.getMediaPlayer()).start();
               if(mediaPlayer!=null){
                   if(!mediaPlayer.isPlaying()){
                       mediaPlayer.stop();
                       mediaPlayer.release();
                   }

               }
            }
        });

        //不是null則把資料 給MainActivity 做回調
        if(msetonchangeListner!=null){
            msetonchangeListner.onchage(viewHolder.checkBox,postion,exampleList);
        }

    }

    //RecyclerView 列表的數目 用exampleList 的大小決定
    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    @Override
    public Filter getFilter() {

        return exampleFilter;
    }
    private  Filter exampleFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //新建 filteredList 如果輸入為0 或空 則顯示全部的列表
            List<FormatEnglish> filteredList=new ArrayList<>();
            if (constraint==null||constraint.length()==0){
                filteredList.addAll(exampleListFull);//加入全部的列表
            }else{
                //以據輸入的值 在列表中找到相關的詞 並把帶有的列表 顯示出來
                String filterPattern=constraint.toString().toLowerCase().trim();
                for(FormatEnglish item:exampleListFull){
                    if(item.getPhrase().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }

                }

            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
           //取得過濾後的值 並跟新
            exampleList.clear();
            exampleList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
    class LinearViewHilder extends RecyclerView.ViewHolder    {
        private TextView TPhrase, TPhrase_ch, Singleword, Singleword_ch, Phrase_kk,li_num;
        private Button radio_bt;
        private CheckBox checkBox;
        private LinearLayout linear;
        private int size;
        public void setSize(int size) {
            this.size = size;
        }

        public LinearViewHilder(View itemView) {
            super(itemView);
            li_num=itemView.findViewById(R.id.li_num);//號碼
            radio_bt = itemView.findViewById(R.id.radio_bt);//音檔
            TPhrase = itemView.findViewById(R.id.Phrase);//英文單字
            TPhrase_ch = itemView.findViewById(R.id.Phrase_ch);//單字中文
            Singleword = itemView.findViewById(R.id.Singleword);//片語
            Singleword_ch = itemView.findViewById(R.id.Singleword_ch);//片語中文
            checkBox = itemView.findViewById(R.id.checkBox);//加入收藏
            Phrase_kk = itemView.findViewById(R.id.Phrase_kk);//單字kk
            linear = itemView.findViewById(R.id.linear);//LinearLayout 背景
            setsize();//設定字型大小
            setbagroundcolor();//設定背景顏色
            setcolor();//設定字體顏色
        }
            //判斷輸入 不為空 而且為數字
        private void setsize() {
            if (text_allsize != null && isNumeric(text_allsize)) {

                int text_size = Integer.parseInt(text_allsize);
                //做等比例放大 or縮小 片語 跟單字 字體大小不一
                int i = ((int) ((text_size) - 20));
                Log.d(TAG, "setsize: "+i);
                TPhrase.setTextSize(text_size);
                TPhrase_ch.setTextSize(text_size);
                Phrase_kk.setTextSize(text_size);
                Singleword.setTextSize((16 + i));
                Singleword_ch.setTextSize((16 + i));
            }
        }

        private void setcolor() {
            //不為空 則依據輸入的值 把字體改顏色
            if(text_color!=null) {

                if (text_color.trim().equals("黑色")) {
                    TPhrase.setTextColor(Color.BLACK);
                    TPhrase_ch.setTextColor(Color.BLACK);
                    Phrase_kk.setTextColor(Color.BLACK);
                    Singleword.setTextColor(Color.BLACK);
                    Singleword_ch.setTextColor(Color.BLACK);
                    li_num.setTextColor(Color.BLACK);
                } else if (text_color.trim().equals("白色")) {
                    TPhrase.setTextColor(Color.WHITE);
                    TPhrase_ch.setTextColor(Color.WHITE);
                    Phrase_kk.setTextColor(Color.WHITE);
                    Singleword.setTextColor(Color.WHITE);
                    Singleword_ch.setTextColor(Color.WHITE);
                    li_num.setTextColor(Color.WHITE);
                } else if (text_color.trim().equals("藍色")) {
                    TPhrase.setTextColor(Color.BLUE);
                    TPhrase_ch.setTextColor(Color.BLUE);
                    Phrase_kk.setTextColor(Color.BLUE);
                    Singleword.setTextColor(Color.BLUE);
                    Singleword_ch.setTextColor(Color.BLUE);
                    li_num.setTextColor(Color.BLUE);
                } else if (text_color.trim().equals("紅色")) {
                    TPhrase.setTextColor(Color.RED);
                    TPhrase_ch.setTextColor(Color.RED);
                    Phrase_kk.setTextColor(Color.RED);
                    Singleword.setTextColor(Color.RED);
                    Singleword_ch.setTextColor(Color.RED);
                    li_num.setTextColor(Color.RED);
                }
            }
        }

        private void setbagroundcolor() {
            //不為空 把layout 的背景 設置 黑or白
            if(bakgrundcolor!=null) {

                if (bakgrundcolor.trim().equals("黑色")) {

                   linear.setBackgroundResource(R.drawable.linear_black);
                } else {

                    linear.setBackgroundResource(R.drawable.linear_white);

                }
            }
        }

        //判斷是否為數字
        public  boolean isNumeric(String str) {
            for (int i = str.length(); --i >= 0; ) {
                int chr = str.charAt(i);
                //char48 到57 對應 0到9
                if (chr < 48 || chr > 57){
                    return false;
                }

            }
            return true;
        }
    }

}
