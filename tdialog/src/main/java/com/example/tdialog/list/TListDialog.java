package com.example.tdialog.list;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.tdialog.R;
import com.example.tdialog.TDialog;
import com.example.tdialog.base.TBaseAdapter;
import com.example.tdialog.base.TController;
import com.example.tdialog.listener.OnBindViewListener;
import com.example.tdialog.listener.OnViewClickListener;


/**
 * 列表彈窗  與TDialog實現分開處理
 *
 * @author Timmy
 * @time 2018/1/11 14:38
 **/
public class TListDialog extends TDialog {


    @Override
    protected void bindView(View view) {
        super.bindView(view);
        if (tController.getAdapter() != null) {//有設置列表
            //列表
            RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
            if (recyclerView == null) {
                throw new IllegalArgumentException("自訂列表xml佈局,請設置RecyclerView的控制項id為recycler_view");
            }
            tController.getAdapter().setTDialog(this);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(),tController.getOrientation(),false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(tController.getAdapter());
            tController.getAdapter().notifyDataSetChanged();
            if (tController.getAdapterItemClickListener() != null) {
                tController.getAdapter().setOnAdapterItemClickListener(tController.getAdapterItemClickListener());
            }
        }else{
            Log.d("TDialog","清單彈窗需要先調用setAdapter()方法!");
        }
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

        //各種setXXX()方法設置資料
        public TListDialog.Builder setLayoutRes(@LayoutRes int layoutRes) {
            params.mLayoutRes = layoutRes;
            return this;
        }

        //設置自訂列表佈局和方向
        public TListDialog.Builder setListLayoutRes(@LayoutRes int layoutRes, int orientation) {
            params.listLayoutRes = layoutRes;
            params.orientation = orientation;
            return this;
        }

        /**
         * 設置彈窗寬度是螢幕寬度的比例 0 -1
         */
        public TListDialog.Builder setScreenWidthAspect(Activity activity, float widthAspect) {
            params.mWidth = (int) (getScreenWidth(activity) * widthAspect);
            return this;
        }

        public TListDialog.Builder setWidth(int widthPx) {
            params.mWidth = widthPx;
            return this;
        }

        /**
         * 設置螢幕高度比例 0 -1
         */
        public TListDialog.Builder setScreenHeightAspect(Activity activity, float heightAspect) {
            params.mHeight = (int) (getScreenHeight(activity) * heightAspect);
            return this;
        }

        public TListDialog.Builder setHeight(int heightPx) {
            params.mHeight = heightPx;
            return this;
        }

        public TListDialog.Builder setGravity(int gravity) {
            params.mGravity = gravity;
            return this;
        }

        public TListDialog.Builder setCancelOutside(boolean cancel) {
            params.mIsCancelableOutside = cancel;
            return this;
        }

        public TListDialog.Builder setDimAmount(float dim) {
            params.mDimAmount = dim;
            return this;
        }

        public TListDialog.Builder setTag(String tag) {
            params.mTag = tag;
            return this;
        }

        public TListDialog.Builder setOnBindViewListener(OnBindViewListener listener) {
            params.bindViewListener = listener;
            return this;
        }

        public TListDialog.Builder addOnClickListener(int... ids) {
            params.ids = ids;
            return this;
        }

        public TListDialog.Builder setOnViewClickListener(OnViewClickListener listener) {
            params.mOnViewClickListener = listener;
            return this;
        }

        //清單資料,需要傳入資料和Adapter,和item點擊數據
        public <A extends TBaseAdapter> TListDialog.Builder setAdapter(A adapter) {
            params.adapter = adapter;
            return this;
        }

        public TListDialog.Builder setOnAdapterItemClickListener(TBaseAdapter.OnAdapterItemClickListener listener) {
            params.adapterItemClickListener = listener;
            return this;
        }

        public TListDialog.Builder setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
            params.mOnDismissListener = dismissListener;
            return this;
        }

        public TListDialog create() {
            TListDialog dialog = new TListDialog();
            //將資料從Buidler的DjParams中傳遞到DjDialog中
            params.apply(dialog.tController);
            return dialog;
        }
    }
}

