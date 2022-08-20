package com.robusta.Other;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.robusta.photoweather.R;

public class MyLoadingProgressDialog extends AlertDialog {

    public MyLoadingProgressDialog(Context context) {
        super(context);
        MyLoadingProgressDialog.this.setCancelable(true);

        View holder = View.inflate(MyLoadingProgressDialog.this.getContext(), R.layout.loading_popup, null);

        MyLoadingProgressDialog.this.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        MyLoadingProgressDialog.this.getWindow().setGravity(Gravity.CENTER);
        MyLoadingProgressDialog.this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        MyLoadingProgressDialog.this.setView(holder);

    }


}
