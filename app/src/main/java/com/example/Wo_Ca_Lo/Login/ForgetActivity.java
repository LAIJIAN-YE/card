package com.example.Wo_Ca_Lo.Login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.Wo_Ca_Lo.SuperActivity;
import com.example.Wo_Ca_Lo.ActivityCollector;
import com.example.Wo_Ca_Lo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * 忘記密碼 可以傳信到信箱重新設置
 */
public class ForgetActivity extends SuperActivity {
    private TextInputEditText email;
    private Button send,cancle;
    private TextInputLayout email_la;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        ActivityCollector.addActivity(this);//加入list 列表
        Email_Check(email,email_la);
        email=findViewById(R.id.for_email);//輸入email
        send=findViewById(R.id.send);//寄送email按鍵
        cancle=findViewById(R.id.for_cancle);
        email_la=findViewById(R.id.for_email_la);
        //返回登入畫面
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ForgetActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Email_Check(email,email_la);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email.getText().toString().isEmpty()&&!email_la.isErrorEnabled()){
                    //寄送email 加入監聽判斷這帳號是否已經註冊過 彈跳出dialog 告訴使用者是否成功
                    auth.sendPasswordResetEmail(email.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //寄送成功 彈出dialog 顯示成功 必且跳回登入畫面
                                    if (task.isSuccessful()){
                                        new AlertDialog.Builder(ForgetActivity.this)
                                                .setTitle("")
                                                .setMessage("寄送成功")
                                                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent=new Intent(ForgetActivity.this,LoginActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                })
                                                .show();

                                    }else{
                                        //失敗 彈出dialog 顯示失敗
                                        new AlertDialog.Builder(ForgetActivity.this)
                                                .setTitle("")
                                                .setMessage("寄送失敗")
                                                .setPositiveButton("確定", null)
                                                .show();
                                    }
                                }
                            });
                }else {
                    Toast.makeText(ForgetActivity.this,"格式有錯誤",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //刪除list 列表
        ActivityCollector.removeActivity(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //email 註冊監聽
        if(Email_Watcher!=null){
            email.addTextChangedListener(Email_Watcher);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //解除監聽
        if(Email_Watcher!=null){
            email.removeTextChangedListener(Email_Watcher);
        }
    }
}
