package com.example.Wo_Ca_Lo.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.Wo_Ca_Lo.data.Phrase;
import com.example.Wo_Ca_Lo.R;

import java.util.List;

/**
 * 英聽單字 視配器
 */

public class phraseAdapter extends RecyclerView.Adapter<phraseAdapter.LinearViewHilder>{
    private Context mContext;
    private List<Phrase> exampleList;
    private TextchangeListner mtextchangeListner;
    //建構子  傳入 調用的 Context 跟設定好 exampleList
    public phraseAdapter(Context context, List<Phrase> exampleList){
        this.mContext=context;
        this.exampleList=exampleList;
    }
    //接口 給PhraseActivity 回調
    public interface TextchangeListner
    {
        void ontextchange(String s, int position);

    }
    public void setTextchangeListner(TextchangeListner textchangeListner) {
        this.mtextchangeListner = textchangeListner;
    }
    //引進布局管理器
    @Override
    public phraseAdapter.LinearViewHilder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView=(LayoutInflater.from(mContext).inflate(R.layout.activity_phrase_adapter,viewGroup,false));
        return new LinearViewHilder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final phraseAdapter.LinearViewHilder viewHolder, final int postion) {
        //把PhraseActivity設定好的 exampleList 依序postion 從0開始設定
        final Phrase phrase=exampleList.get(postion);
        viewHolder.ph_number.setText(postion+1+":"); //列表的號碼
        if (viewHolder.input.getTag() instanceof TextWatcher) { //判斷取到的tag跟TextWatcher 是否有繼承關西
            //tag裡面有 TextWatcher 則先刪除 避免 列表在重製 導致 混亂
            viewHolder.input.removeTextChangedListener((TextWatcher) viewHolder.input.getTag());
        }

        viewHolder.input.setText(phrase.getSave() + "");//把原先輸入的設定上去
        //偵測 input裡面輸入的改變
        TextWatcher textWatcher=new TextWatcher() {
            //修改前
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            //正在改變中的字
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                   if (mtextchangeListner!=null) {
                       mtextchangeListner.ontextchange(viewHolder.input.getText().toString(),postion);
                   }
            }
            //修改後
            @Override
            public void afterTextChanged(Editable s) {

                phrase.setSave(viewHolder.input.getText().toString());//把input 的值儲存到save
            }
        };
            //點即一下 把原先的字體改成單字的中文
        viewHolder.TPhrase_ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.TPhrase_ch.setText(phrase.getPhrase_ch());
            }
        });

        viewHolder.radio_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把音檔的 int 數 傳過來 在這邊啟動 音檔
                MediaPlayer.create(mContext,phrase.getMediaPlayer()).start();
            }
        });
        viewHolder.input.addTextChangedListener(textWatcher);//設置監聽
        viewHolder.input.setTag(textWatcher);//設置tag 儲存位址

    }
    //RecyclerView 列表的數目 用exampleList 的大小決定
    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    //管理布局介面的類
    class LinearViewHilder extends RecyclerView.ViewHolder {
        public TextView TPhrase_ch,ph_number;
        private Button radio_bt;
        private EditText input;

        public LinearViewHilder(View itemView) {

            super(itemView);
            ph_number=itemView.findViewById(R.id.ph_number);//列表號碼
            input=itemView.findViewById(R.id.ph_input);//單字輸入
            radio_bt=itemView.findViewById(R.id.ph_radio_bt);//音檔
            TPhrase_ch=itemView.findViewById(R.id.ph_Phrase_ch);//中文


        }

    }
}
