package com.example.tryapplication;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tdialog.TDialog;
import com.example.tryapplication.Login.Check;
import com.example.tryapplication.Login.LoginActivity;
import com.example.tryapplication.data.Post;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 讓所有 運用 AppCompatActivity 繼承此類
 * 1.運用廣播 判斷是否有網路
 * 2.重複資料 統一寫在此類
 * 3.輸入格式檢查 姓名 電話等等
 * 4.上傳資料 以及下載
 * 5.firebase 登入
 */
public class SuperActivity extends AppCompatActivity  {
    public  final String TAG="SuperActivity";
    protected   static FirebaseAuth auth;
    protected   static FirebaseUser user;
    protected   static AuthUI authUI;
    protected   static  FirebaseAuth.AuthStateListener authListener;
    protected  TextWatcher Name_Watcher,Phone_Watcher,Email_Watcher,Password_Watcher,Birthday_Watcher;
    private     NetWorkChangeReceiver netWorkChangeReceiver;
    protected   TDialog tDialog;
    protected   AlertDialog.Builder alertDialog;
    /**
     * 上傳資料 成功 or 失敗 Callback 設置
     */
    public  interface  ApiCallbacj_Patch{
        void ErrorMsg_Patch(String msg);//要顯示的錯誤訊息
        void loadComlete_Patch(JSONObject msg);//api執行完成
    }

    /**
     * 存取資料 成功 or 失敗 Callback 設置
     */
    public interface ApiCallback_Get {
        void ErrorMsg_Get(String msg); //要顯示的錯誤訊息
        void loadComplete_Get(JSONObject msg); //api執行完成
    }
    public  ApiCallbacj_Patch callbacj_patch;
    public  ApiCallback_Get callback_get;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       ActivityCollector.addActivity(this);
       //上傳成功彈出 確認後 寄送郵件 跟回到登入主畫面
        alertDialog= new AlertDialog.Builder(SuperActivity.this)
                .setTitle("上傳成功")
                .setMessage("請查看信箱認證")
                .setCancelable(false)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //發送信件
                        CheckEmail();
                        ActivityCollector.finishAll();
                        Intent intent =new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
        //加載圖示
        tDialog = new TDialog.Builder(getSupportFragmentManager())
                .setLayoutRes(R.layout.dialog_loading)
                .setHeight(300)
                .setWidth(300)
                .setCancelableOutside(false)
                .create();
        Log.d(TAG, "onCreate: ");

    }

    @Override
    protected void onStart() {
        super.onStart();

        //判斷當前是否有登入firebase
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                user = auth.getCurrentUser();//取得使用者資訊
                if (user!=null) {

                    Log.d("onAuthStateChanged", "登入:"+ user.getUid());

                }else{
                    Log.d("onAuthStateChanged", "已登出");
                }
            }
        };
        if(auth!=null){
            auth.addAuthStateListener(authListener);
        }

        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //onResume 註冊廣播 並且根據網路有變化時發送廣播
        //因為onResume 狀態為可見 視窗 可以讓 呼叫使用的Activity 限制在棧頂 才可以使用
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netWorkChangeReceiver=new NetWorkChangeReceiver();
        registerReceiver(netWorkChangeReceiver,intentFilter);
        Log.d(TAG, "onResume: ");

    }

    @Override
    protected void onPause() {
        super.onPause();
        // onPause 解除廣播
        if(netWorkChangeReceiver!=null){
            unregisterReceiver(netWorkChangeReceiver);
            netWorkChangeReceiver=null;

        }

        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();

        //解除authListener 監聽
        if(authListener!=null&&auth!=null){
            auth.removeAuthStateListener(authListener);
        }

        Log.d(TAG, "onStop: ");
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityCollector.removeActivity(this);
        Log.d(TAG, "onDestroy: ");
        Log.d("List", ""+ ActivityCollector.activities.size());

    }

    /**
     * 判斷該會員是否有驗證過信箱
     * 沒有則發送信件驗證
     */

    protected   void CheckEmail() {

        //判斷是否有驗證過
        if (!user.isEmailVerified()) {
            //如果沒有則發送信件
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SuperActivity.this, "Email 發送成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SuperActivity.this, "Email 發送" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    /**
     * 輸入密碼時 針對格式檢查
     * 不通過則彈跳 格式有問題
     * @param password
     * @param password_la
     */
    protected  void Password_Check(final TextInputEditText password, final TextInputLayout password_la){
        Password_Watcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //用check類裡的規則格式 檢查格式
                if(!password.getText().toString().isEmpty()&&Check.isPassword(password.getText().toString())){
                    password_la.setErrorEnabled(false);
                }else {
                    password_la.setError("密碼格式不正確");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

    }

    /**
     * 輸入信箱 格式檢測
     * 有問題則彈跳出 格式問題
     * @param email
     * @param email_la
     */
    protected void Email_Check(final TextInputEditText email,final TextInputLayout email_la){

        Email_Watcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //用check類的規則格式 檢查格式
                if(!email.getText().toString().isEmpty()&&Check.isValidEmail(email.getText().toString())){
                    email_la.setErrorEnabled(false);
                }else {
                    email_la.setError("email格式不正確");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    /**
     *  輸入姓名 判斷姓名不得少於2 以及 不為空值
     *  有問題彈跳出 格式問題
     * @param name
     * @param name_la
     */
    protected void Name_Check (final TextInputEditText name, final TextInputLayout name_la){

        Name_Watcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //檢查格式
                if(!name.getText().toString().isEmpty()&&name.getText().length()>1){
                    name_la.setErrorEnabled(false);
                }else {

                    name_la.setError("姓名不低於2個字");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    /**
     * 輸入手機號碼 格式檢測
     * 有問題則彈跳出 格式問題
     * @param phone
     * @param phone_la
     */
    protected  void Phone_Check(final TextInputEditText phone, final TextInputLayout phone_la){

        Phone_Watcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //用check類的規則格式 檢查格式
                    if(!phone.getText().toString().isEmpty()&& Check.isMobile(phone.getText().toString())){
                        phone_la.setErrorEnabled(false);
                    }else{
                        phone_la.setError("格式錯誤");
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

    }

    /**
     *輸入生日 格式檢測 西元年 xxxx/xx/xx
     *有問題則彈跳出 格式問題
     * @param birthday
     * @param birthday_la
     */
    protected  void Birthday_Check(final TextInputEditText birthday,final TextInputLayout birthday_la){
        Birthday_Watcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //用check類的規則格式 檢查格式
                if( !birthday.getText().toString().isEmpty()&&Check.isDAY(birthday.getText().toString())){
                    birthday_la.setErrorEnabled(false);
                }else{
                    birthday_la.setError("格式錯誤");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

    }

    /**
     *選擇生日 dialog 設定在birthday 輸入旁邊 的按鈕
     * @param textView
     */
    protected void VerifiCation_Birthday(final TextInputEditText textView) {
                //取的目前時間的年分 月份 幾號
                Calendar calender = Calendar.getInstance();
                int day = calender.get(Calendar.DAY_OF_MONTH);
                int month = calender.get(Calendar.MONTH)+1;
                int year = calender.get(Calendar.YEAR);
                //創建生日選取的介面
                DatePickerDialog datePickerDialog = new DatePickerDialog(SuperActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //把所選到 年 月 日期 設定到 TextInputEditText
                        String i = year + "/" + (month>9?month:"0"+month)  + "/" + (dayOfMonth>9?dayOfMonth:"0"+dayOfMonth);
                        textView.setText(i);
                    }
                }, year, month,day);//設定datePickerDialog 初始 年 月 日 取用現在時間
                datePickerDialog.show();//顯示出來


    }
    /**
     *  用volley把資料上傳到firebase資料庫
     */

    protected  void Volley_Patch(final Context context, String url, String email, String name, String phone, String gender, String birthday){
        if (tDialog!=null){
            tDialog.show();
        }
        callbacj_patch= (ApiCallbacj_Patch) SuperActivity.this;
        Map<String,String> params=new HashMap<String,String>();
        params.put("email",email);
        params.put("name",name);
        params.put("phone",phone);
        params.put("gender",gender);
        params.put("birthday",birthday);
        //資料庫路徑
        String uri="https://oeie-af5e8.firebaseio.com/"+url+".json";
        //volley 是異部處理
        //5個變數 第一個 決定讀寫 第二個 路徑 第三個 資料已hashmap鍵值對 第4個 成功後資料回傳 第5個 錯誤的資料回傳
        JsonObjectRequest PATCH_Request=new JsonObjectRequest(Request.Method.PATCH,uri,new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                        Log.d(TAG, "onResponse: "+response);
                        callbacj_patch.loadComlete_Patch(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (tDialog!=null){
                    tDialog.dismiss();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error : " + error.toString());
                        if (tDialog!=null){
                            tDialog.dismiss();
                        }
                        callbacj_patch.ErrorMsg_Patch(error.toString());
                    }
                });
        Volley.newRequestQueue(this).add(PATCH_Request);

    }
    /**
     * 用volley從firebase資料庫 取得json格式資料
     */

    protected  void Volley_Get(String url){
        if (tDialog!=null){
            tDialog.show();
        }
        //創建實例
        callback_get = (ApiCallback_Get) SuperActivity.this;
        //資料庫路徑
        String uri="https://oeie-af5e8.firebaseio.com/"+url+".json";
        //volley 是異部處理
        //5個變數 第一個 決定讀寫 第二個 路徑 第三個 為空值  第4個 成功後資料回傳 第5個 錯誤的資料回傳
        JsonObjectRequest PATCH_Request=new JsonObjectRequest(Request.Method.GET,uri,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    callback_get.loadComplete_Get(response);

                    Log.d(TAG, "onResponse: "+response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (tDialog!=null){
                    tDialog.dismiss();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        callback_get.ErrorMsg_Get(error.toString());


                        Log.e(TAG, "error : " + error.toString());
                        if (tDialog!=null){
                            tDialog.dismiss();
                        }
                    }
                });
        Volley.newRequestQueue(this).add(PATCH_Request);



    }


    /**
     * 繼承廣播 判斷網路狀態
     */
    class  NetWorkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            //接收到網路變化時 判斷當前手機的網路狀態
            ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null&&networkInfo.isConnected()) {
                //wifi狀態
                if (networkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                   // Toast.makeText(context, "wifi", Toast.LENGTH_LONG).show();
                //行動數據
                } else if (networkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                 //   Toast.makeText(context, "網路", Toast.LENGTH_LONG).show();
                }
            }else {
                //沒有網路 彈跳出 dialog 返回 登入畫面
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("無法連接網路")
                        .setMessage("請重新登入")
                        .setCancelable(false)
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCollector.finishAll();
                                Intent i = new Intent(context, LoginActivity.class);
                                context.startActivity(i);
                            }
                        });
                builder.show();
                Toast.makeText(context, "沒有網路", Toast.LENGTH_LONG).show();
            }
        }
    }
}
