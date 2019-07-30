package com.example.Wo_Ca_Lo.Login;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Wo_Ca_Lo.SuperActivity;
import com.example.Wo_Ca_Lo.ActivityCollector;
import com.example.Wo_Ca_Lo.R;
import com.example.Wo_Ca_Lo.main.MainActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * 登入主畫面
 */
public class LoginActivity extends SuperActivity implements View.OnClickListener , SuperActivity.ApiCallback_Get {
    public  final String TAG="LoginActivity";
    private Button del,login,fastlogin;
    private List<AuthUI.IdpConfig> providesr;
    private static final int MY_REQUEST_CORE = 7117; //返回碼
    private String SAVEUID[]=new String[100]; //Uid 儲存陣列
    private CheckBox rememberPass;
    private SharedPreferences sharedPreferences;
    private TextView password_fg,account;
    private TextInputEditText password,email;
    private TextInputLayout password_la,email_la;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findview();
        setListener();
        setview();
        Log.d(TAG, "onCreate: ");
        //加入到list列表
        ActivityCollector.addActivity(this);

    }
    private  void findview(){
        rememberPass=findViewById(R.id.log_check);//記住帳密
        email=findViewById(R.id.login_input_email);
        email_la=findViewById(R.id.login_input_layout_email);
        del=findViewById(R.id.log_del);//清除按鍵
        login=findViewById(R.id.log_login);//登入案件
        password_fg=findViewById(R.id.log_password_fg);//忘記密碼
        account=findViewById(R.id.log_account);//帳密註冊
        fastlogin=findViewById(R.id.log_fastlogin);//快速登入
        password=findViewById(R.id.login_input_password);
        password_la=findViewById(R.id.login_input_layout_password);

    }
    private  void  setListener(){
        //設置點擊事件
        login.setOnClickListener(this);
        fastlogin.setOnClickListener(this);
        del.setOnClickListener(this);
        account.setOnClickListener(this);
        password_fg.setOnClickListener(this);
    }
    private  void setview(){
        //用包名創建 sharedPreferences
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

        account.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//給帳號註冊設置底線
        password_fg.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//給忘記密碼設置底線
        //把需要用到的項目儲存到list
        providesr = Arrays.asList(

                new AuthUI.IdpConfig.FacebookBuilder().build(),//fb
                new AuthUI.IdpConfig.GoogleBuilder().build()//google

        );

        //預設為false
        boolean isRemember= sharedPreferences.getBoolean("remember_password",false);
        if(isRemember){
            //設定 email 跟password 設定為上次儲存資料
            String Save_email =sharedPreferences.getString("email","");
            String Save_password =sharedPreferences.getString("password","");
            email.setText(Save_email);
            password.setText(Save_password);
            rememberPass.setChecked(true);
        }
        //設定變數完預設登出
        authUI=AuthUI.getInstance();
        auth= FirebaseAuth.getInstance();
        authUI.signOut(this);
        auth.signOut();

        Email_Check(email,email_la);
        Password_Check(password,password_la);





    }
    @Override
    protected void onStart() {
        super.onStart();
        //密碼 跟 信箱 設置 TextChangedListener 監聽
        if(Password_Watcher!=null){
            password.addTextChangedListener(Password_Watcher);
        }
        if(Email_Watcher!=null){
            email.addTextChangedListener(Email_Watcher);
        }
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //密碼 跟 信箱 解除 TextChangedListener 監聽
        if(Password_Watcher!=null){
            password.removeTextChangedListener(Password_Watcher);
        }
        if(Email_Watcher!=null){
            email.removeTextChangedListener(Email_Watcher);
        }

        Log.d(TAG, "onStop: ");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清除Activity
        ActivityCollector.removeActivity(this);
        Log.d(TAG, "onDestroy: ");
        Log.d("List", ""+ ActivityCollector.activities.size());
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.log_del:

                email.setText("");
                password.setText("");
                break;
            case R.id.log_login:
                    //判斷email 跟 password 不為null 跟格式無錯誤
                if (!password_la.isErrorEnabled() &&!email_la.isErrorEnabled()&&!password.getText().toString().isEmpty()&&!email.getText().toString().isEmpty()) {
                    //加載圖樣
                    tDialog.show();
                    //判斷有沒有勾選 有勾選 把email 跟password 儲存
                    final String S_email = email.getText().toString().trim();//讀取email輸入的內容 並去空白

                    final String S_password = password.getText().toString().trim();//讀取password輸入的內容 並去空白
                    Log.d(TAG, "onClick: "+email.getText()+"/"+password.getText());
                    if(rememberPass.isChecked()){
                        sharedPreferences.edit()
                                .putBoolean("remember_password",true)
                                .putString("email",S_email)
                                .putString("password",S_password)
                                .apply();

                    }else{
                        //否則把boolean設為false
                        sharedPreferences.edit()
                                .putBoolean("remember_password",false)
                                .apply();
                    }

                    //把email跟password 送到Firebase 比對
                    auth.signInWithEmailAndPassword(S_email, S_password)
                            //監聽 返回值 判斷登入有沒有成功
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                            //登入不成功彈出DiaLog提示使用者
                                    if (!task.isSuccessful()) {//判斷是否 註冊成功 密碼需大於6碼
                                        Log.d("onComplete", "登入失敗");
                                        // register(email, password);
                                        new AlertDialog.Builder(LoginActivity.this) //註冊失敗 new dialog 通知
                                                .setTitle("登入問題")//設定主題
                                                .setMessage("無此帳號，請註冊")//設定內容
                                                .setPositiveButton("確定", null)
                                                .show(); //把dialog 顯示出來

                                                tDialog.dismiss();
                                    } else {
                                        user=auth.getCurrentUser();    //取得使用者資訊
                                        if(user.isEmailVerified()) { //判斷這個使用者是否通過email 認證
                                            //通過的話 往主程式 並finish掉目前頁面
                                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                            tDialog.dismiss();
                                            startActivity(i);
                                            finish();
                                        }else{
                                            //沒通過的話 在頁面上顯示 請認證信箱
                                            Toast.makeText(LoginActivity.this,"請驗證信箱",Toast.LENGTH_LONG).show();
                                            tDialog.dismiss();
                                        }
                                    }
                                }

                            });

                }else{
                    Toast.makeText(LoginActivity.this,"格式錯誤",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.log_fastlogin:

              showSignInOptions();
                break;
            case R.id.log_password_fg:
                i=new Intent(LoginActivity.this,ForgetActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.log_account:
                i=new Intent(LoginActivity.this, RegisteredActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }

    /**
     * 快速登入模組創建
     */
    private void showSignInOptions() {
        startActivityForResult(
                authUI.createSignInIntentBuilder()
                        .setAvailableProviders(providesr)//AuthUi list列表
                        .setIsSmartLockEnabled(false)
                        .setTheme(R.style.LoginTheme)
                        .setTosAndPrivacyPolicyUrls(
                                "https://policies.google.com/terms?hl=zh-TW/服務條款",
                                "https://policies.google.com/privacy?hl=zh-TW/隱私權政策"
                        )

                        .build(), MY_REQUEST_CORE//設定回傳碼
        );
    }


    /**
     * 對返回值做判斷
     * @param requestCode 傳出去的值
     * @param resultCode  返回設定值
     * @param data  資料
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //重新回到MainActivity的回傳碼 驗證
        if (requestCode == MY_REQUEST_CORE) {

            IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                //傳uid 作為目錄
                if (auth!=null){

                    Volley_Get(auth.getUid());
                }
            } else {
                Toast.makeText(LoginActivity.this,"登入失敗",Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * 抓不到資料 或著資料錯誤 跳轉到 註冊頁面
     * @param msg
     */
    @Override
    public void ErrorMsg_Get(String msg) {
        if(tDialog!=null){
            tDialog.dismiss();
        }
        Log.d(TAG, "showMsg: "+msg);
        Log.d("成功跳轉", "FastRegisteredActivity");

        Intent intent = new Intent(LoginActivity.this, FastRegisteredActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 資訊正確 後 確認信箱是否有驗證過
     * @param msg
     */
    @Override
    public void loadComplete_Get(JSONObject msg) {
        if(tDialog!=null){
            tDialog.dismiss();
        }
        if(user.isEmailVerified()){
            Log.d("成功跳轉", "MainActivity");

            startActivity(new Intent(LoginActivity.this, MainActivity.class));

            finish();
        }else {
            Toast.makeText(LoginActivity.this, "請驗證信箱", Toast.LENGTH_LONG).show();

        }
    }
}
