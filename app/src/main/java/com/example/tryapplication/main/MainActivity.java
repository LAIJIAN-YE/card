package com.example.tryapplication.main;
import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tdialog.TDialog;
import com.example.tdialog.base.BindViewHolder;
import com.example.tdialog.listener.OnBindViewListener;
import com.example.tdialog.listener.OnViewClickListener;
import com.example.tryapplication.ActivityCollector;
import com.example.tryapplication.FragmentViewpage.Dialog_Image;
import com.example.tryapplication.Login.LoginActivity;
import com.example.tryapplication.PhraseActivity;
import com.example.tryapplication.R;
import com.example.tryapplication.SuperActivity;
import com.example.tryapplication.data.ExampleList_in;
import com.example.tryapplication.data.ExampleList_pr;
import com.example.tryapplication.data.FormatEnglish;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 1.主頁面 包含3個 fragment
 * 2.左拉 視窗
 * 3.搜尋
 *
 */
public class MainActivity extends SuperActivity implements NavigationView.OnNavigationItemSelectedListener, SuperActivity.ApiCallback_Get , SuperActivity.ApiCallbacj_Patch {
    private PageViewModel pageViewModel;
    private  int AllSize;
    private SharedPreferences sharedPreferences;
    private HashMap<Integer,Boolean> Check;
    private String SAVE="max";//預設sharedPreferences儲存路徑
    private static  final  int CAMER=500;
    private static  final  int Recode=777;
    private static  final  int PHOT=100;
    private ArrayList<FormatEnglish> Pr_list,In_list;
    private ViewPager viewPager;
    private TabLayout tabs;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private View header;
    private DrawerLayout drawerLayout;
    private TextView HE_name,HE_email;
    private Button HE_add;
    private Uri imageUri;
    private ImageView HE_Picture;
    private TextInputEditText birthday,name,phone;
    private TextInputLayout birthday_la,name_la,phone_la;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private  Dialog_Image dialog_image;
    private  EditText textsize;
    private  String  S_T_C,S_B_C;
    private  String radiogender="男";//預設值
    private boolean bg_check=false;
    private String[] Callbackmsg;
    private  TextView email;
    private  Boolean volley_check=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._main);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        findView();
        setListener();
        setView();
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);
        Pr_list=new ExampleList_pr().englishes();
        In_list=new ExampleList_in().englishes();
        extract();
        pageViewModel= ViewModelProviders.of(this).get(PageViewModel.class);
        pageViewModel.getHashMapMutable().postValue(Check);
        pageViewModel.getIn_Mutable().postValue(In_list);
        pageViewModel.getPr_Mutable().postValue(Pr_list);
        dialog_image.show(getSupportFragmentManager(),"");
        ActivityCollector.addActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     *  設置在 Tablelayout上的選單
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            /**
             * 搜尋
             */
            case R.id.menu_search:
                android.support.v7.widget.SearchView  searchView=(android.support.v7.widget.SearchView) item.getActionView();
                searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
                searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
//                //如果輸入字體變化 把字體傳到linearAdapter
                        if(pageViewModel.getPr_Adapter().getValue()!=null){
                            pageViewModel.getPr_Adapter().getValue().getFilter().filter(s);
                        }
                         if (pageViewModel.getMl_Adapter().getValue()!=null){
                             pageViewModel.getMl_Adapter().getValue().getFilter().filter(s);
                         }
                         if (pageViewModel.getIn_Adapter().getValue()!=null){
                             pageViewModel.getIn_Adapter().getValue().getFilter().filter(s);
                         }

                        return true;
                    }
                });

                break;
            /**
             * 彈出diallog
             *設定字體大小 顏色 跟背景顏色
             */
            case R.id.menu_settings:
                new TDialog.Builder(getSupportFragmentManager())
                        .setLayoutRes(R.layout.dialog_layout)
                        .setScreenWidthAspect(MainActivity.this,0.6f)
                        .setScreenHeightAspect(MainActivity.this,0.4f)
                        .addOnClickListener(R.id.confirm,R.id.cancel)
                        .setOnBindViewListener(new OnBindViewListener() {
                            @Override
                            public void bindView(BindViewHolder viewHolder) {
                                Spinner textcolor=viewHolder.getView(R.id.ed_textcolor);
                                Spinner backgroundcolor=viewHolder.getView(R.id.ed_backgrundcolor);
                                 textsize=viewHolder.getView(R.id.ed_textsize);
                                textsize.setOnTouchListener(new View.OnTouchListener() {
                                    //設定EditText 右邊 圖案 按壓彈跳出日期設定
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        final int DRAWABLE_RIGHT = 2;
                                        if (event.getAction() == MotionEvent.ACTION_UP) {
                                            if (event.getX() >= (textsize.getWidth() - textsize
                                                    .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                                textsize.setCompoundDrawablesWithIntrinsicBounds(null, null, MainActivity.this.getResources().getDrawable(R.drawable.ic_arrow_drop_down), null);
                                                showListPopulWindow();
                                                return true;
                                            }
                                        }
                                        return false;
                                    }
                                });
                                //下拉選單 設定視配器
                                ArrayAdapter<CharSequence> nAdapter = ArrayAdapter.createFromResource(
                                        MainActivity.this, R.array.ed_textcolor, android.R.layout.simple_list_item_1 );
                                ArrayAdapter<CharSequence> nAdapter2 = ArrayAdapter.createFromResource(
                                        MainActivity.this, R.array.ed_backgrundcolor, android.R.layout.simple_list_item_1 );
                                textcolor.setAdapter(nAdapter);
                                backgroundcolor.setAdapter(nAdapter2);
                                textcolor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                         S_T_C=MainActivity.this.getResources().getStringArray(R.array.ed_textcolor)[position];
                                        Toast.makeText(MainActivity.this,""+S_T_C,Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                                backgroundcolor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                         S_B_C=MainActivity.this.getResources().getStringArray(R.array.ed_backgrundcolor)[position];

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                            }
                        })
                        .setOnViewClickListener(new OnViewClickListener() {
                            @Override
                            public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
                                switch (view.getId()){
                                    case R.id.confirm:

                                        String background[]=new String[3];
                                        //預設值
                                        background[0]="20";
                                        background[1]="黑色";
                                        background[2]="白色";
                                        //如果長度不為0 而且 不為null 字體是可輸入
                                        if(!textsize.getText().toString().isEmpty()&&textsize.getText().toString()!=null){
                                            background[0]=(textsize.getText().toString());

                                        }
                                        //設置字體顏色
                                        if(S_T_C!=null){
                                            background[1]=S_T_C;
                                        }


                                        //設置背景顏色
                                        if(S_B_C!=null){
                                            background[2]=S_B_C;
                                        }
                                        Log.d(TAG, "onViewClick: "+S_T_C+S_B_C+textsize.getText().toString());
                                            //跟新
                                        if (!bg_check){
                                            pageViewModel.getBackground().postValue(background);
                                        }


                                        tDialog.dismiss();
                                        break;
                                    case R.id.cancel:
                                        tDialog.dismiss();
                                        break;
                                }
                            }
                        })
                        .create()
                        .show();


                break;
            /**
             * 英聽練習 返回 答題數 跟答對題目數量
             */
            case R.id.menu_primary_sing:
                Intent i=new Intent(MainActivity.this, PhraseActivity.class);
                startActivityForResult(i,Recode);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 找到 View
     */
    private void findView(){
        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);

        //標題bar
        toolbar = findViewById(R.id.toolbar);
        //左邊拉出來的頁面
        navigationView = findViewById(R.id.main_nav);
        drawerLayout = findViewById(R.id.DrawerLayout_main);
        header=navigationView.inflateHeaderView(R.layout.nav_header_3);
        HE_Picture=header.findViewById(R.id.header_imageView);
        HE_name=header.findViewById(R.id.header_name);
        HE_add=header.findViewById(R.id.header_add);
        HE_email=header.findViewById(R.id.header_email);

    }
    private void setListener(){
        navigationView.setNavigationItemSelectedListener(this);
        /**
         *  設定類似 bottomsheetdialog 樣式 從最下方彈跳出來 拍照 跟圖像的調用
         */
        HE_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TDialog.Builder(getSupportFragmentManager())
                        .setLayoutRes(R.layout.dialog_photo)
                        .setScreenWidthAspect(MainActivity.this, 1.0f)
                        .setGravity(Gravity.BOTTOM)
                        .setDialogAnimationRes(R.style.animate_dialog)
                        .addOnClickListener(R.id.dialog_cancel, R.id.dialog_album, R.id.dialog_camera)
                        .setOnViewClickListener(new OnViewClickListener() {
                            @Override
                            public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
                                switch (view.getId()) {
                                    case R.id.dialog_camera:
                                        //放到緩存目錄下
                                        File outputimage=new File(getExternalCacheDir(),"output_image.jpg");
                                        try {

                                            if (outputimage.exists()){
                                                //離開時刪除
                                                outputimage.delete();
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        //使用內容提供器 否則大於android7.0後會抱錯
                                        if(Build.VERSION.SDK_INT>=24){
                                            imageUri= FileProvider.getUriForFile(MainActivity.this,"com.example.tryapplication.fileprovider",outputimage);
                                        }else{
                                            //真實路徑
                                            imageUri=Uri.fromFile(outputimage);
                                        }
                                        Intent i=new Intent("android.media.action.IMAGE_CAPTURE");
                                        i.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                                        startActivityForResult(i,CAMER);
                                        break;

                                    case R.id.dialog_album:
                                        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                                        }else {
                                            openAlbum();
                                        }
                                        break;
                                    case R.id.dialog_cancel:

                                        tDialog.dismiss();
                                        break;
                                }
                            }
                        })
                        .create()
                        .show();

            }


        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i==2){
                    //跟新我的最愛ui介面
                    pageViewModel.getBooleanLiveData().setValue(true);
                }
                Log.d(TAG, "onPageSelected: "+i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    /**
     * 設定View的參數
     */
    private void setView(){
        //用使用者uid從資料庫找資料
        if (auth!=null){
            //用uid當作sharedPreferencesy儲存路徑
            SAVE=auth.getUid();
            Volley_Get(auth.getUid());
        }
        //存放會員資料
        //0.email 1.name 2.phone 3.gender 4.birthday 5.英聽練習答對題數 6.總數
        Callbackmsg=new String[7];

        drawerLayout.closeDrawer(GravityCompat.START);
        //設定toolbar
        setSupportActionBar(toolbar);
        //設定drawer 到toolbar 以及開關閉
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //設定點即地按鍵在左上
        toggle.syncState();
        //DialogFragment+Viewpager+Fragment
         dialog_image=new Dialog_Image();
    }

    /**
     * 1.hashmap 設定 有sharedPreferences 存取的話 則提取否則 用預設值
     * 2.圖片的提取
     * 3.答題數目的提取
     */
    private  void extract (){
        AllSize=(new ExampleList_in().englishes().size()+ new ExampleList_pr().englishes().size());
        sharedPreferences=getSharedPreferences(SAVE,MainActivity.MODE_PRIVATE);
        Check=new HashMap<>();
        if(!sharedPreferences.getString("image","").equals("")){
            getBitmapFromSharedPreferences(HE_Picture);
        }
        //答題數 跟答對數
        if(Callbackmsg!=null){
            Callbackmsg[5]=sharedPreferences.getString("call5","0");
            Callbackmsg[6]=sharedPreferences.getString("call6","0");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                //取出資料 如果沒有資料則用預設 給每個都設定false
                if(sharedPreferences.getBoolean("check",false)){
                    for(int i=0;i<AllSize;i++){
                        Check.put(i,sharedPreferences.getBoolean(""+i,false));
                        Log.d(TAG, "onStart: "+i+  sharedPreferences.getBoolean(""+i,false));
                    }
                }else{
                    for(int i=0;i<AllSize;i++){
                        if (Check.get(i) == null) {
                            Check.put(i, false);
                        }
                    }

                }
               // 設置 標籤 讓 私藏單字 再點選值可以用 標籤值去對應
                for(int i=0;i<Pr_list.size();i++){
                    Pr_list.get(i).setNumber(i);
                }

                for(int i=0;i<In_list.size();i++){
                    In_list.get(i).setNumber((i+Pr_list.size()));
                }
            }
        }).start();

    }
    /**
     * 保存 fragment 的hasmap資料 跟圖片檔
     */
    @Override
    protected void onStop() {
        super.onStop();
        sharedPreferences=getSharedPreferences(SAVE,MainActivity.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("check",true).commit();
        for(int i=0;i<Check.size();i++){
            sharedPreferences.edit()

                    .putBoolean(""+i,Check.get(i))
                    .commit();
            Log.d(TAG, "onStop: "+i+"/"+Check.get(i));
        }
        Log.d(TAG, "onStop: "+HE_Picture.getDrawable());
        if(HE_Picture.getDrawable()!=null){
            saveBitmapToSharedPreferences(HE_Picture.getDrawable());
        }
        if(Callbackmsg[5]!=null){
            sharedPreferences.edit().putString("call5",Callbackmsg[5])
                    .commit();
        }
        if(Callbackmsg[6]!=null){
            sharedPreferences.edit().putString("call6",Callbackmsg[6])
                    .commit();
        }
    }

    /**
     * 左拉選單 Item設置
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.action_home:
                dialog_image.show(getSupportFragmentManager(),"");

                break;
            case R.id.action_singlog:
                /**
                 * 設置 會員資料  並且可以修改部分內容
                 */

                new TDialog.Builder(getSupportFragmentManager())
                        .setLayoutRes(R.layout.sing_dialog)
                        .setDimAmount(0.8f)

                        .setScreenHeightAspect(MainActivity.this,0.95f)
                        .addOnClickListener(R.id.main_close,R.id.main_post,R.id.main_clear)
                        .setOnBindViewListener(new OnBindViewListener() {
                            @Override
                            public void bindView(BindViewHolder viewHolder) {
                                name= viewHolder.getView(R.id.main_name);
                                 birthday= viewHolder.getView(R.id.main_birthday);
                                 birthday_la=viewHolder.getView(R.id.main_birthday_la);
                                 email=viewHolder.getView(R.id.main_email);
                                 name_la=viewHolder.getView(R.id.main_name_la);
                                 phone= viewHolder.getView(R.id.main_phone);
                                 phone_la=viewHolder.getView(R.id.main_phone_la);
                                RadioGroup radioGroup=viewHolder.getView(R.id.main_gender);
                                 TextView difficult=viewHolder.getView(R.id.main_difficult);
                                 TextView quantity=viewHolder.getView(R.id.main_quantity);
                                RadioButton boy=viewHolder.getView(R.id.main_boy);
                                RadioButton gril=viewHolder.getView(R.id.main_girl);

                              //透過json拿到的值 復值
                               if(Callbackmsg[0]!=null){
                                   email.setText(Callbackmsg[0]);
                               }
                                if(Callbackmsg[1]!=null){
                                    name.setText(Callbackmsg[1]);
                                }
                                if(Callbackmsg[2]!=null){
                                    phone.setText(Callbackmsg[2]);
                                }
                                if(Callbackmsg[3]!=null){
                                    if(Callbackmsg[3].equals("男")){
                                        boy.setChecked(true);
                                        radiogender="男";
                                    }else{
                                        gril.setChecked(true);
                                        radiogender="女";
                                    }
                                }
                                if(Callbackmsg[4]!=null){
                                    birthday.setText(Callbackmsg[4]);
                                }
                                if (Callbackmsg[5]!=null){
                                    difficult.setText(Callbackmsg[5]);
                                }
                                if (Callbackmsg[6]!=null){
                                    quantity.setText(Callbackmsg[6]);
                                }
                                //樣式檢查
                                Phone_Check(phone,phone_la);
                                Name_Check(name,name_la);
                                Birthday_Check(birthday,birthday_la);
                                if(Birthday_Watcher!=null){
                                    birthday.addTextChangedListener(Birthday_Watcher);
                                }
                                if(Name_Watcher!=null){
                                    name.addTextChangedListener(Name_Watcher);
                                }
                                if(Phone_Watcher!=null){
                                    phone.addTextChangedListener(Phone_Watcher);
                                }
                                //設置右邊圖案按鈕
                                birthday.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {

                                        final int DRAWABLE_RIGHT = 2;
                                        if (event.getAction() == MotionEvent.ACTION_UP) {
                                            if (event.getX() >= (birthday.getWidth() - birthday
                                                    .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                                birthday.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(MainActivity.this,R.drawable.ic_border), null);
                                                VerifiCation_Birthday(birthday);
                                                Log.d(TAG, "onTouch: "+"df");
                                                return true;
                                            }
                                        }
                                        return false;
                                    }
                                });

                                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                        RadioButton button=(group.findViewById(checkedId));
                                        radiogender=button.getText().toString();

                                    }
                                });

                            }
                        })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if(Birthday_Watcher!=null){
                                    birthday.removeTextChangedListener(Birthday_Watcher);
                                }
                                if(Name_Watcher!=null){
                                    name.removeTextChangedListener(Name_Watcher);
                                }
                                if(Phone_Watcher!=null){
                                    phone.removeTextChangedListener(Phone_Watcher);
                                }
                                if(auth!=null&&volley_check==true){
                                    Volley_Get(auth.getUid());
                                    volley_check=false;
                                }
                            }
                        })
                        .setCancelableOutside(false)
                        .setOnViewClickListener(new OnViewClickListener() {
                            @Override
                            public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
                                switch (view.getId()){
                                    case R.id.main_close:
                                        tDialog.dismiss();
                                        break;
                                        case R.id.main_clear:
                                             birthday=viewHolder.getView(R.id.main_birthday);
                                            birthday.setText("");
                                             phone=viewHolder.getView(R.id.main_phone);
                                            phone.setText("");
                                             name=viewHolder.getView(R.id.main_name);
                                             String a=name.getText().toString();
                                            name.setText("");
                                            break;
                                    case R.id.main_post:
                                       if(!name_la.isErrorEnabled()&&!name.getText().toString().isEmpty()
                                            &&!email.getText().toString().isEmpty()
                                            &&!phone_la.isErrorEnabled()&&!phone.getText().toString().isEmpty()
                                            &&!birthday_la.isErrorEnabled()&&!birthday.getText().toString().isEmpty()
                                            &&auth!=null){
                                        Volley_Patch(MainActivity.this,auth.getUid(),email.getText().toString()
                                                ,name.getText().toString(),phone.getText().toString()
                                                ,radiogender,birthday.getText().toString());
                                        volley_check=true;
                                       }else {
                                           Toast.makeText(MainActivity.this,"請檢查格式",Toast.LENGTH_SHORT).show();
                                       }

                                        break;

                                }
                            }
                        })
                        .setScreenWidthAspect(MainActivity.this, 0.95f)
                        .create()
                        .show();
                break;
            case R.id.action_sign_out:
                ActivityCollector.finishAll();
                Intent i=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                break;
        }

        return false;
    }

    /**
     * 後退鍵設置
     */
    @Override
    public void onBackPressed() {
        //案返回鍵如果右邊選單有展開 則關閉選單
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void openAlbum() {
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");//過濾檔案
        startActivityForResult(intent,PHOT);//打開相簿
    }

    /**
     *  Activity返回值 這邊針對 相機 相簿 跟英聽練習的返回值
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CAMER:
                if(resultCode==RESULT_OK){
                    try {
                        //轉成 bitmap
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        HE_Picture.setImageBitmap(bitmap);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case PHOT:
                if(resultCode==RESULT_OK){
                        handleImageOnKitKat(data);
                }
                break;
            case Recode:
                if(resultCode==RESULT_OK){
                    //取得英聽練習答對題目跟總量
                    if(data.getIntExtra("Nb_Correct",-1)>0){
                        Callbackmsg[5]=(Integer.valueOf(Callbackmsg[5]==null?Callbackmsg[5]="0":Callbackmsg[5])+data.getIntExtra("Nb_Correct",-1))+"";
                    }
                    if(data.getIntExtra("all",-1)>0){
                        Callbackmsg[6]=(Integer.valueOf(Callbackmsg[6]==null?Callbackmsg[6]="0":Callbackmsg[6])+data.getIntExtra("all",-1))+"";
                    }
                    Log.d(TAG, "onActivityResult: "+data.getIntExtra("Nb_Correct",-1));
                    Log.d(TAG, "onActivityResult: "+data.getIntExtra("all",-1));
                }
                break;
        }

    }

    /**
     * 處理相簿路徑格式
     * @param data
     */
    private void handleImageOnKitKat(Intent data) {
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是document類型的uri 則通過document id處理
            String docID=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                //解析出數字格式id
                String id=docID.split(":")[1];
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else  if("com.android.prividers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docID));
                imagePath=getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content 類型的Uri 則使用普通方式處理
            imagePath=getImagePath(uri,null);
            //如果是file類型的Uri直接獲取圖票路徑即可
        }else  if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        //根據圖片路徑顯示圖片
        displayImage(imagePath);
    }

    /**
     * 根據路徑設置圖片
     * @param imagePath
     */
    private void displayImage(String imagePath) {
        if(imagePath!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            HE_Picture.setImageBitmap(bitmap);

        }else {
            Toast.makeText(this,"找不到圖片",Toast.LENGTH_LONG).show();
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path=null;
        //通過Uri和selection來獲取真實的圖片路徑
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if (cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            }
            cursor.close();
        }
        return path;
    }

    /**
     * 系統權限
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this,"沒有訪問權限",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /**
     * EditText下拉列表數據設置
     */
    private void showListPopulWindow() {
        final String[] list = {"16", "20", "24","32","40","48"};//要添充的數據
        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(MainActivity.this);
        listPopupWindow.setAdapter(new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1, list));//用android的基本樣式
        listPopupWindow.setAnchorView(textsize);//設置EditText 為基準
        listPopupWindow.setModal(true);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                textsize.setText(list[i]);//把選到的內容顯示到  EditText上
                listPopupWindow.dismiss();//如果有選擇 其他隱藏起來
            }
        });
        listPopupWindow.show();//把ListPopWindow展示出来
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }


    /**
     * 將Bitmap轉換成字串保存至SharedPreferences
     *
     *
     */
    private void saveBitmapToSharedPreferences(Drawable image){
        Bitmap bitmap=((BitmapDrawable)image).getBitmap();
        //第一步:將Bitmap壓縮至位元組陣列輸出流ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        //第二步:利用Base64將位元組陣列輸出流中的資料轉換成字串String
        byte[] byteArray=byteArrayOutputStream.toByteArray();
        String imageString=new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //第三步:將String保存至SharedPreferences
        SharedPreferences sharedPreferences=getSharedPreferences(SAVE, MainActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("image", imageString);
        editor.commit();
    }


    /**
     * 從SharedPreferences中取出Bitmap
     */
    private void getBitmapFromSharedPreferences(ImageView imageView){
        SharedPreferences sharedPreferences=getSharedPreferences(SAVE, MainActivity.MODE_PRIVATE);
        //第一步:取出字串形式的Bitmap
        String imageString=sharedPreferences.getString("image", "");
        //第二步:利用Base64將字串轉換為ByteArrayInputStream
        byte[] byteArray=Base64.decode(imageString, Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArray);
        //第三步:利用ByteArrayInputStream生成Bitmap
        Bitmap bitmap=BitmapFactory.decodeStream(byteArrayInputStream);
        imageView.setImageBitmap(bitmap);
    }

    /**
     * SuperActivity Callback 用json格式調用資料庫 上傳資料
     * 錯誤時 回傳資料
     * @param msg
     */

    @Override
    public void ErrorMsg_Patch(String msg) {
        Toast.makeText(MainActivity.this,""+msg,Toast.LENGTH_SHORT).show();
    }
    /**
     * SuperActivity Callback 用json格式調用資料庫 上傳資料
     * 正確時 回傳資料
     * @param msg
     */
    @Override
    public void loadComlete_Patch(JSONObject msg) {
        Toast.makeText(MainActivity.this,"上傳成功",Toast.LENGTH_SHORT).show();
    }
    /**
     * SuperActivity Callback 用json格式調用資料庫 取得資料
     * 錯誤時 回傳資料
     * @param msg
     */
    @Override
    public void ErrorMsg_Get(String msg) {
        Toast.makeText(MainActivity.this,"取得失敗"+msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * SuperActivity Callback 用json格式調用資料庫
     * 正確時 回傳資料
     * @param msg
     */
    @Override
    public void loadComplete_Get(JSONObject msg) {


        try {
            Callbackmsg[0]=msg.getString("email");
            Callbackmsg[1]=msg.getString("name");
            Callbackmsg[2]=msg.getString("phone");
            Callbackmsg[3]=msg.getString("gender");
            Callbackmsg[4]=msg.getString("birthday");
            Log.d(TAG, "loadComplete_Get: "+Callbackmsg[2]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HE_email.setText(Callbackmsg[0]);
        HE_name.setText(Callbackmsg[1]);
    }
}