package com.ficture7.aasexplorer.view.activity;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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
    private ArrayAdapter<String> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int id = intent.getIntExtra("SUBJECT_ID", -1);
        if (id != -1) {
            Explorer explorer = App.getInstance().getExplorer();
            Subject subject = explorer.alevel().subjects().get(id);

            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setTitle(subject.name());
                actionBar.setSubtitle(String.valueOf(subject.id()));
            }

            this.subject = subject;
        }

        AppExplorerLoader loader = App.getInstance().getLoader();
        if (!subject.resources().isLoaded()) {
            loader.getLoadResourcesAsyncTask(subject).execute();
        }

        entries = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        entries.add("Question Paper");
        entries.add("Marking Schemes");

        setContentView(R.layout.activity_subject);
        setListAdapter(entries);

        LoaderView loaderView = findViewById(R.id.view_loader);
        loader.setLoaderView(loaderView);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        switch(position) {
            case 0:
                Intent intent = new Intent(this, QuestionPaperListActivity.class);
                intent.putExtra("SUBJECT_ID", subject.id());

                startActivity(intent);
                break;
            case 1:
                break;
        }
    }
}
