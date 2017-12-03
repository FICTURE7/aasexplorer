package com.ficture7.aasexplorer.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ficture7.aasexplorer.R;

public class LoaderView extends LinearLayout {

    private final ProgressView progressView;
    private final ErrorView errorView;

    public LoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_loader, this, true);

        progressView = findViewById(R.id.view_progress);
        errorView = findViewById(R.id.view_error);
    }

    public ErrorView getErrorView() {
        return errorView;
    }

    public ProgressView getProgressView() {
        return progressView;
    }
}

