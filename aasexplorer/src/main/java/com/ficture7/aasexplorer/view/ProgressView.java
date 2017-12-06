package com.ficture7.aasexplorer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ficture7.aasexplorer.R;

public class ProgressView extends LinearLayout {

    private final TextView messageTv;

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_progress, this, true);

        messageTv = findViewById(R.id.text_progress_message);

        TypedArray res = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ProgressView, 0, 0);
        String message = null;

        try {
            message = res.getString(R.styleable.ProgressView_pv_message);
        } finally {
            res.recycle();
        }

        setMessage(message);
    }

    public CharSequence getMessage() {
        return messageTv.getText();
    }

    public void setMessage(CharSequence message) {
        messageTv.setText(message);
    }
}
