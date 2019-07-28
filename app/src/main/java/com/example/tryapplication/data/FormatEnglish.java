package com.example.tryapplication.data;

/**
 * 中級 跟初級 資料格式
 */
public class FormatEnglish  {

    private   String Singleword,Singleword_ch; //片語 片語中文
    private   String Phrase;//單字
    private  String Phrase_kk; //單字kk
    private  boolean checkBox;//收藏
    private  String Phrase_ch;//單字中文
    private  int mediaPlayer;//音檔
    private  int number;
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


    public String getPhrase_kk() {
        return Phrase_kk;
    }

    public void setPhrase_kk(String phrase_kk) {
        Phrase_kk = phrase_kk;
    }
    public boolean getboolean() {
        return checkBox;
    }

    public void setboolean(boolean checkBox) {
        this.checkBox = checkBox;
    }


    public int getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(int mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public String getSingleword() {
        return Singleword;
    }

    public void setSingleword(String singleword) {
        Singleword = singleword;
    }

    public String getSingleword_ch() {
        return Singleword_ch;
    }

    public void setSingleword_ch(String singleword_ch) {
        Singleword_ch = singleword_ch;
    }

    public String getPhrase() {
        return Phrase;
    }

    public void setPhrase(String phrase) {
        Phrase = phrase;
    }

    public String getPhrase_ch() {
        return Phrase_ch;
    }

    public void setPhrase_ch(String phrase_ch) {
        Phrase_ch = phrase_ch;
    }



    public FormatEnglish(String Singleword,String Singleword_ch,String phrase,String phrase_ch,String Phrase_kk,boolean checkBox,int mediaPlayer){
        this.Phrase_ch=phrase_ch;
        this.Phrase=phrase;
        this.Singleword=Singleword;
        this.Singleword_ch=Singleword_ch;
        this.mediaPlayer=mediaPlayer;
        this.checkBox=checkBox;
        this.Phrase_kk=Phrase_kk;
    }
}
