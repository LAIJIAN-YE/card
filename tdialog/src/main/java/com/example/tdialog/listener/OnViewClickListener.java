package com.example.tdialog.listener;

import android.view.View;

import com.example.tdialog.TDialog;
import com.example.tdialog.base.BindViewHolder;


public interface OnViewClickListener {
    void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog);
}
