package com.ficture7.aasexplorer.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ficture7.aasexplorer.R;

public class ErrorView extends LinearLayout {

    private final TextView titleTv;
    private final TextView subtitleTv;
    private final Button retryBtn;

    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_error, this, true);

        titleTv = findViewById(R.id.text_error_title);
        subtitleTv = findViewById(R.id.text_error_subtitle);
        retryBtn = findViewById(R.id.btn_error_retry);

        TypedArray res = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ErrorView, 0, 0);
        String title = null;
        String subtitle = null;

        try {
            title = res.getString(R.styleable.ErrorView_title);
            subtitle = res.getString(R.styleable.ErrorView_subtitle);
        } finally {
            res.recycle();
        }

        setTitle(title);
        setSubtitle(subtitle);
    }

    public CharSequence getTitle() {
        return titleTv.getText();
    }

    public void setTitle(CharSequence title) {
        titleTv.setText(title);
    }

    public CharSequence getSubtitle() {
        return subtitleTv.getText();
    }

    public void setSubtitle(CharSequence subtitle) {
        subtitleTv.setText(subtitle);
    }
}
