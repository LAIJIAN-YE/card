package com.example.tryapplication.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tryapplication.ActivityCollector;
import com.example.tryapplication.R;
import com.example.tryapplication.SuperActivity;
import com.example.tryapplication.data.Post;

import org.json.JSONObject;

/**
 *  檢測到 資料庫沒有資料就會跳到此
 *  快速登入 註冊
 */
public class FastRegisteredActivity extends SuperActivity implements View.OnClickListener , SuperActivity.ApiCallbacj_Patch {
    private Button post,cancle;
    private TextInputEditText name,phone,birthday;
    private TextView email;
   private TextInputLayout name_la,phone_la,birthday_la;
    private RadioGroup group;
    private RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_registered);
        ActivityCollector.addActivity(this);
        findview();
        setLisener();
        setview();
        Name_Check(name,name_la);
        Phone_Check(phone,phone_la);
        Birthday_Check(birthday,birthday_la);
    }

    private void findview(){
        birthday=findViewById(R.id.fast_birthday);
        post=findViewById(R.id.fast_post);
        cancle=findViewById(R.id.fast_cancel);
        name=findViewById(R.id.fast_name);
        email=findViewById(R.id.fast_email);
        birthday_la=findViewById(R.id.fast_birthday_la);
        phone_la=findViewById(R.id.fast_phone_la);
        name_la=findViewById(R.id.fast_name_la);
        phone=findViewById(R.id.fast_phone);

        group=findViewById(R.id.fast_gender);
        radioButton=findViewById(R.id.boy_re);
    }
    private  void setLisener(){
        post.setOnClickListener(this);
        cancle.setOnClickListener(this);
        //右邊圖標 按壓設置
        birthday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //方向
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() >= (birthday.getWidth() - birthday
                            .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        birthday.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(FastRegisteredActivity.this,R.drawable.ic_border), null);
                        VerifiCation_Birthday(birthday);

                        return true;
                    }
                }
                return false;
            }
        });
        //單選按鈕
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton =group.findViewById(checkedId);

            }
        });
    }
    private  void setview(){
        if(user!=null){
            email.setText(user.getEmail());
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        //註冊 name phone birthday TextChangedListener 監聽
        if(Name_Watcher!=null){
            name.addTextChangedListener(Name_Watcher);
        }
        if(Phone_Watcher!=null){
            phone.addTextChangedListener(Phone_Watcher);
        }
        if(Birthday_Watcher!=null){
            birthday.addTextChangedListener(Birthday_Watcher);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //解除 name phone birthday TextChangedListener 監聽
        if (Name_Watcher != null) {
            name.removeTextChangedListener(Name_Watcher);
        }
        if(Phone_Watcher!=null){
            phone.removeTextChangedListener(Phone_Watcher);
        }
        if(Birthday_Watcher!=null){
            birthday.removeTextChangedListener(Birthday_Watcher);
        }

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
            case R.id.fast_post:
                //確認資料格式 以及是否為空值
                if(!name_la.isErrorEnabled()&&!name.getText().toString().isEmpty()
                        &&!email.getText().toString().isEmpty()
                        &&!phone_la.isErrorEnabled()&&!phone.getText().toString().isEmpty()
                        &&!birthday_la.isErrorEnabled()&&!birthday.getText().toString().isEmpty()){
                    //上傳資料
                    Volley_Patch(FastRegisteredActivity.this,auth.getUid(),
                            email.getText().toString(),
                            name.getText().toString().trim(),
                            phone.getText().toString().trim(),
                            radioButton.getText().toString(),
                            birthday.getText().toString()
                    );


                }else{
                    Toast.makeText(FastRegisteredActivity.this,"格式錯誤",Toast.LENGTH_LONG).show();
                }
                break;
                //返回登入畫面
            case R.id.fast_cancel:
                i=new Intent(FastRegisteredActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
                break;


        }
    }
    //上傳失敗 取得錯誤資訊
    @Override
    public void ErrorMsg_Patch(String msg) {
        Toast.makeText(FastRegisteredActivity.this,msg+"",Toast.LENGTH_SHORT).show();
    }
    //上傳成功 彈出dialog
    @Override
    public void loadComlete_Patch(JSONObject msg) {
        alertDialog.show();
    }
}
