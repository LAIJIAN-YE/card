package com.example.tryapplication.data;


/**
 * 帳號上傳格式
 */
public class Post {
  private String phone;//電話
  private String gender_st;//性別
  private String email;//信箱
  private String birthday;//生日

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  private String uid;
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  private String name;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getbirthday() {
    return birthday;
  }

  public void setbirthday(String birthday) {
    this.birthday = birthday;
  }


  public String get_telephone_number() {
    return phone;
  }

  public void set_telephone_number(String phone) {
    this.phone = phone;
  }

  public String getgender_st() {
    return gender_st;
  }

  public void setgender_st(String gender_st) {
    this.gender_st = gender_st;
  }



  public Post(String phone, String gender_st,String email,String birthday,String name,String uid){
    this.gender_st=gender_st;
    this.email=email;
    this.birthday=birthday;
    this.phone=phone;
    this.name=name;
    this.uid=uid;
  }
}
