package com.ficture7.aasexplorer.view.activity;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ficture7.aasexplorer.App;
import com.ficture7.aasexplorer.AppExplorerLoader;
import com.ficture7.aasexplorer.Explorer;
import com.ficture7.aasexplorer.R;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.view.LoaderView;

public class SubjectActivity extends ListActivity {

    private Subject subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int id = intent.getIntExtra(App.Intents.EXTRA_SUBJECT_ID, -1);
        if (id != -1) {
            Explorer explorer = App.getInstance().getExplorer();
            Subject subject = explorer.getALevel().getSubjects().get(id);

            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setTitle(subject.getName());
                actionBar.setSubtitle(String.valueOf(subject.getId()));
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            this.subject = subject;
        }

        AppExplorerLoader loader = App.getInstance().getLoader();
        if (!subject.getResources().isLoaded()) {
            loader.getLoadResourcesAsyncTask(subject).execute();
        }

        ArrayAdapter<String> entries = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        entries.add(getString(R.string.title_question_papers));
        entries.add(getString(R.string.title_marking_schemes));

        setContentView(R.layout.activity_subject);
        setListAdapter(entries);

        LoaderView loaderView = findViewById(R.id.view_loader);
        loader.setLoaderView(loaderView);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        switch(position) {
            case 0:
                App.getInstance().getNavigator().navigateToQuestionPapers(this, subject.getId());
                break;

            case 1:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
