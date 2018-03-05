package com.ficture7.aasexplorer;

import android.content.Context;
import android.content.Intent;

import com.ficture7.aasexplorer.view.activity.EditBookmarkListActivity;
import com.ficture7.aasexplorer.view.activity.QuestionPaperListActivity;
import com.ficture7.aasexplorer.view.activity.SubjectActivity;

public class Navigator {

    public void navigateToEditBookmarks(Context context) {
        Intent intent = new Intent(context, EditBookmarkListActivity.class);
        context.startActivity(intent);
    }

    public void navigateToSubject(Context context, int subjectId) {
        Intent intent = new Intent(context, SubjectActivity.class);
        intent.putExtra(App.Intents.EXTRA_SUBJECT_ID, subjectId);

        context.startActivity(intent);
    }

    public void navigateToQuestionPapers(Context context, int subjectId) {
        Intent intent = new Intent(context, QuestionPaperListActivity.class);
        intent.putExtra(App.Intents.EXTRA_SUBJECT_ID, subjectId);

        context.startActivity(intent);
    }
}
