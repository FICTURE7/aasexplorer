package com.ficture7.aasexplorer;

import android.content.Context;
import android.content.Intent;

import com.ficture7.aasexplorer.view.activity.EditBookmarkListActivity;

public class Navigator {

    public void navigateToEditBookmarks(Context context) {
        Intent intent = new Intent(context, EditBookmarkListActivity.class);
        context.startActivity(intent);
    }
}
