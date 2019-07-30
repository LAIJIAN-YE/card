package com.example.Wo_Ca_Lo.data;

/**
 * 英聽單字練習格式
 */
public class Phrase     {
    private  String Phrase;//單字
    private  String Phrase_ch;//單字中文
    private  int mediaPlayer;//音檔
    private  String save="";//設置給TextWatcher 裡的input 作暫存
    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public int getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(int mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
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


    public Phrase( String phrase, String phrase_ch, int mediaPlayer){
        this.Phrase_ch=phrase_ch;
        this.Phrase=phrase;
        this.mediaPlayer=mediaPlayer;

    }
}
