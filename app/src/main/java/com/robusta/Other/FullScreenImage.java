package com.robusta.Other;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.robusta.photoweather.R;

public class FullScreenImage extends Dialog {

    public ImageView fullScreenImage, closeButton;
    Bitmap bitmap;

    public FullScreenImage(Bitmap bitmap, @NonNull Context context) {
        super(context);
        this.bitmap = bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.full_screen_image_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        closeButton = findViewById(R.id.dialog_close_btn);
        closeButton.setOnClickListener(v -> FullScreenImage.this.dismiss());

        fullScreenImage = findViewById(R.id.full_image);
        fullScreenImage.setImageBitmap(bitmap);
    }
}
