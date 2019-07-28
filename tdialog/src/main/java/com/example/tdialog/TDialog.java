package com.example.tdialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.example.tdialog.base.BaseDialogFragment;
import com.example.tdialog.base.BindViewHolder;
import com.example.tdialog.base.TController;
import com.example.tdialog.listener.OnBindViewListener;
import com.example.tdialog.listener.OnViewClickListener;


/**
 * 1.0.0版本: 彈窗實現基本功能
 * OnBindViewListener
 * 1.1.0版本: 添加點擊事件封裝回檔方法
 * addOnClickListener()
 * setOnViewClickListener()
 * 1.2.0版本:
 * 分離出列表彈窗TListDialog
 * 解決彈窗按Home鍵時出現的bug
 * 1.3.0版本:
 * 處理setCancelable()方法,禁止彈窗點擊取消
 * 彈窗內容直接傳入View: setDialogView()
 * 1.3.1版本:
 * 添加彈窗隱藏時回檔監聽方法:setOnDismissListener()
 *
 * @author Timmy
 * @time 2018/1/4 16:28
 * @GitHub https://github.com/Timmy-zzh/TDialog
 **/
public class TDialog extends BaseDialogFragment {

    private static final String KEY_TCONTROLLER = "TController";
    protected TController tController;

    public TDialog() {
        tController = new TController();
    }

    /**
     * 當設備旋轉時,會重新調用onCreate,進行資料恢復
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            tController = (TController) savedInstanceState.getSerializable(KEY_TCONTROLLER);

        }
    }

    /**
     * 進行資料保存
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_TCONTROLLER, tController);
        super.onSaveInstanceState(outState);
    }

    /**
     * 彈窗消失時回檔方法
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        DialogInterface.OnDismissListener onDismissListener = tController.getOnDismissListener();
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    /**
     * 獲取彈窗xml佈局介面
     */
    @Override
    protected int getLayoutRes() {
        return tController.getLayoutRes();
    }

    @Override
    protected View getDialogView() {
        return tController.getDialogView();
    }

    @Override
    protected void bindView(View view) {
        //控制項點擊事件處理
        BindViewHolder viewHolder = new BindViewHolder(view, this);
        if (tController.getIds() != null && tController.getIds().length > 0) {
            for (int id : tController.getIds()) {
                viewHolder.addOnClickListener(id);
            }
        }
        //回檔方法獲取到佈局,進行處理
        if (tController.getOnBindViewListener() != null) {
            tController.getOnBindViewListener().bindView(viewHolder);
        }
    }

    @Override
    public int getGravity() {
        return tController.getGravity();
    }

    @Override
    public float getDimAmount() {
        return tController.getDimAmount();
    }

    @Override
    public int getDialogHeight() {
        return tController.getHeight();
    }

    @Override
    public int getDialogWidth() {
        return tController.getWidth();
    }

    @Override
    public String getFragmentTag() {
        return tController.getTag();
    }

    public OnViewClickListener getOnViewClickListener() {
        return tController.getOnViewClickListener();
    }

    @Override
    protected boolean isCancelableOutside() {
        return tController.isCancelableOutside();
    }

    @Override
    protected int getDialogAnimationRes() {
        return tController.getDialogAnimationRes();
    }

    @Override
    protected DialogInterface.OnKeyListener getOnKeyListener() {
        return tController.getOnKeyListener();
    }

    public TDialog show() {
        Log.d(TAG, "show");
        try {

            FragmentTransaction ft = tController.getFragmentManager().beginTransaction();
            ft.add(this, tController.getTag());
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            Log.e("TDialog", e.toString());
        }
        return this;
    }

    /*********************************************************************
     * 使用Builder模式實現
     *
     */
    public static class Builder {

        TController.TParams params;

        public Builder(FragmentManager fragmentManager) {
            params = new TController.TParams();
            params.mFragmentManager = fragmentManager;


        }

        /**
         * 傳入彈窗xmL佈局文件
         *
         * @param layoutRes
         * @return
         */
        public Builder setLayoutRes(@LayoutRes int layoutRes) {
            params.mLayoutRes = layoutRes;
            return this;
        }

        /**
         * 直接傳入控制項
         *
         * @param view
         * @return
         */
        public Builder setDialogView(View view) {
            params.mDialogView = view;
            return this;
        }

        /**
         * 設置彈窗寬度(單位:圖元)
         *
         * @param widthPx
         * @return
         */
        public Builder setWidth(int widthPx) {
            params.mWidth = widthPx;
            return this;
        }

        /**
         * 設置彈窗高度(px)
         *
         * @param heightPx
         * @return
         */
        public Builder setHeight(int heightPx) {
            params.mHeight = heightPx;
            return this;
        }

        /**
         * 設置彈窗寬度是螢幕寬度的比例 0 -1
         */
        public Builder setScreenWidthAspect(Context context, float widthAspect) {
            params.mWidth = (int) (getScreenWidth(context) * widthAspect);
            return this;
        }

        /**
         * 設置彈窗高度是螢幕高度的比例 0 -1
         */
        public Builder setScreenHeightAspect(Context context, float heightAspect) {
            params.mHeight = (int) (getScreenHeight(context) * heightAspect);
            return this;
        }

        /**
         * 設置彈窗在螢幕中顯示的位置
         *
         * @param gravity
         * @return
         */
        public Builder setGravity(int gravity) {
            params.mGravity = gravity;
            return this;
        }

        /**
         * 設置彈窗在彈窗區域外是否可以取消
         *
         * @param cancel
         * @return
         */
        public Builder setCancelableOutside(boolean cancel) {
            params.mIsCancelableOutside = cancel;
            return this;
        }

        /**
         * 彈窗dismiss時監聽回檔方法
         *
         * @param dismissListener
         * @return
         */
        public Builder setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
            params.mOnDismissListener = dismissListener;
            return this;
        }


        /**
         * 設置彈窗背景透明度(0-1f)
         *
         * @param dim
         * @return
         */
        public Builder setDimAmount(float dim) {
            params.mDimAmount = dim;
            return this;
        }

        public Builder setTag(String tag) {
            params.mTag = tag;
            return this;
        }

        /**
         * 通過回檔拿到彈窗佈局控制項物件
         *
         * @param listener
         * @return
         */
        public Builder setOnBindViewListener(OnBindViewListener listener) {
            params.bindViewListener = listener;
            return this;
        }

        /**
         * 添加彈窗控制項的點擊事件
         *
         * @param ids 傳入需要點擊的控制項id
         * @return
         */
        public Builder addOnClickListener(int... ids) {
            params.ids = ids;
            return this;
        }

        /**
         * 彈窗控制項點擊回檔
         *
         * @param listener
         * @return
         */
        public Builder setOnViewClickListener(OnViewClickListener listener) {
            params.mOnViewClickListener = listener;
            return this;
        }

        /**
         * 設置彈窗動畫
         *
         * @param animationRes
         * @return
         */
        public Builder setDialogAnimationRes(int animationRes) {
            params.mDialogAnimationRes = animationRes;
            return this;
        }

        /**
         * 監聽彈窗後，返回鍵點擊事件
         */
        public Builder setOnKeyListener(DialogInterface.OnKeyListener keyListener) {
            params.mKeyListener = keyListener;
            return this;
        }

        /**
         * 真正創建TDialog物件實例
         *
         * @return
         */
        public TDialog create() {
            TDialog dialog = new TDialog();
            Log.d(TAG, "create");
            //將資料從Buidler的DjParams中傳遞到DjDialog中
            params.apply(dialog.tController);
            return dialog;
        }
    }
}

