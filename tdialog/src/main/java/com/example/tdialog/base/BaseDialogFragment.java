package com.example.tdialog.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * DialogFragment的基類
 * 1.系統預設onCreateDialog方法返回一個Dialog物件,對其不做處理
 * 2.主要操作onCreateView方法
 * 因為DialogFragment繼承自Fragment,所以可以在onCreteView()方法返回xml佈局,
 * 該佈局在onActivityCreated()方法中,設置給系統之前創建的Dialog物件
 * //           @Override
 * //            public void onActivityCreated(Bundle savedInstanceState) {
 * //                super.onActivityCreated(savedInstanceState);
 * //
 * //                if (!mShowsDialog) {
 * //                return;
 * //                }
 * //
 * //                View view = getView();
 * //                if (view != null) {
 * //                if (view.getParent() != null) {
 * //                throw new IllegalStateException(
 * //                "DialogFragment can not be attached to a container view");
 * //                }
 * //                mDialog.setContentView(view);
 * //                }
 * //           }
 * 3.對應使用Dialog不同部分包括
 * a.xml佈局
 * b.寬高
 * c.位置
 * d.背景色
 * e.透明度
 * f.是否可以點擊空白處隱藏
 * 控制方法在onStart處理,
 * 4.暴露方法:介面中控制項處理和點擊事件處理
 * 5.監聽回檔,很多彈窗需要輸入資訊,然後將輸入的資訊通過回檔的方法返回
 * 6.當設備Configure屬性變化時,資料保存和恢復處理
 **/
public abstract class BaseDialogFragment extends DialogFragment {

    public static final String TAG = "TDialog";
    private static final float DEFAULT_DIMAMOUNT = 0.2F;

    protected abstract int getLayoutRes();

    protected abstract void bindView(View view);

    protected abstract View getDialogView();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        if (getLayoutRes() > 0) {
            view = inflater.inflate(getLayoutRes(), container, false);
        }
        if (getDialogView() != null) {
            view = getDialogView();
        }
        bindView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //去除Dialog默認頭部
        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(isCancelableOutside());
        if (dialog.getWindow() != null && getDialogAnimationRes() > 0) {
            dialog.getWindow().setWindowAnimations(getDialogAnimationRes());
        }
        if (getOnKeyListener() !=null){
            dialog.setOnKeyListener(getOnKeyListener());
        }
    }

    protected DialogInterface.OnKeyListener getOnKeyListener() {
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            //設置表單背景色透明
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //設置寬高
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            if (getDialogWidth() > 0) {
                layoutParams.width = getDialogWidth();
            } else {
                layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            }
            if (getDialogHeight() > 0) {
                layoutParams.height = getDialogHeight();
            } else {
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            }
            //透明度
            layoutParams.dimAmount = getDimAmount();
            //位置
            layoutParams.gravity = getGravity();
            window.setAttributes(layoutParams);
        }
    }

    //默認彈窗位置為中心
    public int getGravity() {
        return Gravity.CENTER;
    }

    //預設寬高為包裹內容
    public int getDialogHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    public int getDialogWidth() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    //默認透明度為0.2
    public float getDimAmount() {
        return DEFAULT_DIMAMOUNT;
    }

    public String getFragmentTag() {
        return TAG;
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, getFragmentTag());
    }

    protected boolean isCancelableOutside() {
        return true;
    }

    //獲取彈窗顯示動畫,子類實現
    protected int getDialogAnimationRes() {
        return 0;
    }

    //獲取設備螢幕寬度
    public static final int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    //獲取設備螢幕高度
    public static final int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}

