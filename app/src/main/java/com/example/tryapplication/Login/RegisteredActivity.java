package com.example.tryapplication.Login;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.example.tryapplication.ActivityCollector;
import com.example.tryapplication.R;
import com.example.tryapplication.SuperActivity;
import com.example.tryapplication.data.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import org.json.JSONObject;

/**
 * 註冊會員
 */
public class RegisteredActivity extends SuperActivity implements View.OnClickListener , SuperActivity.ApiCallbacj_Patch {
    private Button post,cancle;
    private TextInputEditText name,email,password,phone;
   private  TextInputEditText birthday;
    private RadioGroup group;
    private  RadioButton radioButton;
    private TextInputLayout name_la,password_la,phone_la,birthday_la,email_la;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        ActivityCollector.addActivity(this);
        findview();
        setLisener();
        Email_Check(email,email_la);
       Name_Check(name,name_la);
       Password_Check(password,password_la);
       Phone_Check(phone,phone_la);
       Birthday_Check(birthday,birthday_la);
    }
    private void findview(){
        email_la=findViewById(R.id.reg_email_la);
        name_la=findViewById(R.id.reg_name_la);
        password_la=findViewById(R.id.reg_password_la);
        phone_la=findViewById(R.id.reg_phone_la);
        birthday_la=findViewById(R.id.reg_birthday_la);
        post=findViewById(R.id.reg_post);
        cancle=findViewById(R.id.reg_cancel);
        name=findViewById(R.id.reg_name);
        email=findViewById(R.id.reg_email);
        password=findViewById(R.id.reg_password);
        phone=findViewById(R.id.reg_phone);
        birthday=findViewById(R.id.reg_birthday);
        group=findViewById(R.id.reg_gender);
        radioButton=findViewById(R.id.boy);
    }
    private  void setLisener(){
        post.setOnClickListener(this);
        cancle.setOnClickListener(this);
        //設置 birthday右邊圖形按壓效果
        birthday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() >= (birthday.getWidth() - birthday
                            .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        birthday.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(RegisteredActivity.this,R.drawable.ic_border), null);
                        VerifiCation_Birthday(birthday);
                        Log.d(TAG, "onTouch: "+"df");
                        return true;
                    }
                }
                return false;
            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                 radioButton =group.findViewById(checkedId);

            }
        });

    }

    /**
     * firebase 帳號創建 以及 上傳資料 到資料庫
     * @param email_s
     * @param password
     */
    private void createUser(String email_s, String password) {
        //註冊監聽
        auth.createUserWithEmailAndPassword(email_s, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {

                                       //傳送給Super裡的volley 上傳到firebase資料庫
                                      Volley_Patch(RegisteredActivity.this,auth.getUid(),
                                            email.getText().toString(),
                                            name.getText().toString().trim(),
                                            phone.getText().toString().trim(),
                                            radioButton.getText().toString(),
                                            birthday.getText().toString());

                                    }else {
                                         new AlertDialog.Builder(RegisteredActivity.this)
                                        .setTitle("註冊失敗")
                                        .setMessage("請重新註冊")
                                        .setCancelable(false)
                                        .setPositiveButton("確定", null)
                                        .show();

                            }

                            }
                        });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.reg_post:
                //排除格是錯誤 以及 輸入攔為空值
                if(!name_la.isErrorEnabled()&&!name.getText().toString().isEmpty()
                        &&!password_la.isErrorEnabled()&&!password.getText().toString().isEmpty()
                        && !email_la.isErrorEnabled()&&!email.getText().toString().isEmpty()
                        &&!phone_la.isErrorEnabled()&&!phone.getText().toString().isEmpty()
                        &&!birthday_la.isErrorEnabled()&&!birthday.getText().toString().isEmpty()){
                        String e_mail=email.getText().toString().trim();
                        String p_assword=password.getText().toString().trim();
                     Log.d(TAG, "onClick: "+e_mail+"/"+p_assword);

                        createUser(e_mail,p_assword);

                }else{
                    Toast.makeText(RegisteredActivity.this,"格式錯誤",Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.reg_cancel:
                //註冊頁面
                i=new Intent(RegisteredActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //添加監聽
        if(Birthday_Watcher!=null){
            birthday.addTextChangedListener(Birthday_Watcher);
        }
        if(Name_Watcher!=null){
            name.addTextChangedListener(Name_Watcher);
        }
       if(Phone_Watcher!=null){
           phone.addTextChangedListener(Phone_Watcher);
       }
       if(Email_Watcher!=null){
           email.addTextChangedListener(Email_Watcher);
       }
       if(Password_Watcher!=null){
           password.addTextChangedListener(Password_Watcher);
       }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //解除監聽
        if (Name_Watcher != null) {
            name.removeTextChangedListener(Name_Watcher);
        }
        if(Phone_Watcher!=null){
            phone.removeTextChangedListener(Phone_Watcher);
        }
        if(Email_Watcher!=null){
            email.removeTextChangedListener(Email_Watcher);
        }
        if(Password_Watcher!=null){
            password.removeTextChangedListener(Password_Watcher);
        }
        if(Birthday_Watcher!=null){
            birthday.removeTextChangedListener(Birthday_Watcher);
        }
    }

    /**
     * 資料上傳失敗
     * @param msg
     */
    @Override
    public void ErrorMsg_Patch(String msg) {
        Toast.makeText(RegisteredActivity.this,""+msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 資料上傳成功彈出 dialog 發送郵件
     * @param msg
     */
    @Override
    public void loadComlete_Patch(JSONObject msg) {
        Log.d(TAG, "loadComlete_Patch: ");
        alertDialog.show();
    }
}
