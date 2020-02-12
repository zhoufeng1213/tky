package com.xxxx.tky.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxxx.tky.R;

public class ImageTextButton extends LinearLayout {
    private Context mContext;
    private ImageButton mIcon;
    private TextView mText;
    private TextView mText_state;
    private int mIconResourceId;

    public ImageTextButton(Context context) {
      this(context,null);
    }

    public ImageTextButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate( R.layout.image_text_button, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CarImageTextButton);

        mIcon = findViewById(R.id.button_icon);
        mIcon.setScaleType(ImageView.ScaleType.CENTER);
        mIcon.setClickable(false);
        mIconResourceId = typedArray.getResourceId(R.styleable.CarImageTextButton_icon, 0);
        mIcon.setImageResource(mIconResourceId);

        mText = findViewById(R.id.button_text);
        mText.setClickable(false);
        String text = typedArray.getString(R.styleable.CarImageTextButton_text);
        mText.setText(text);

        mText_state = findViewById(R.id.button_text_state);
        mText_state.setClickable(false);
        String text1 = typedArray.getString(R.styleable.CarImageTextButton_text_state);
        mText_state.setText(text1);

    }
}
