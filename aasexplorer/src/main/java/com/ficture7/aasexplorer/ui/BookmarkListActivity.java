package com.ficture7.aasexplorer.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ficture7.aasexplorer.App;
import com.ficture7.aasexplorer.AppExplorerLoader;
import com.ficture7.aasexplorer.Explorer;
import com.ficture7.aasexplorer.R;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.ui.view.LoaderView;

public class BookmarkListActivity extends ListActivity {

    private LoaderView loaderView;
    private SubjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new SubjectAdapter(this);

        setContentView(R.layout.activity_bookmarks);
        setListAdapter(adapter);

        loaderView = findViewById(R.id.view_loader);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Explorer explorer = App.getInstance().getExplorer();
        // Set the LoaderView of the AppExplorerLoader instance so it can push updates to
        // the activity.
        AppExplorerLoader loader = App.getInstance().getLoader();
        loader.setLoaderView(loaderView);

        // If the SubjectRepository is already loaded, we load the data directly
        // otherwise we hook a callback to the load async task.
        if (explorer.alevel().subjects().isLoaded()) {
            updateList(explorer.alevel().subjects());
        } else {
            // Add call back to something to add the subject to the adapter.
            loader.getLoadSubjectsAsyncTask().setLoadCallback(new AppExplorerLoader.LoadCallback() {
                @Override
                public void onLoad() {
                    updateList(explorer.alevel().subjects());
                }
            });
        }
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        Subject subject = adapter.getItem(position);
        if (subject != null) {
            Intent intent = new Intent(this, SubjectActivity.class);
            intent.putExtra("SUBJECT_ID", subject.id());

            startActivity(intent);
        }
    }

    // Adds the specified subjects to the list of the activity.
    private void updateList(Iterable<Subject> subjects) {
        adapter.clear();
        for (Subject subject : subjects) {
            adapter.add(subject);
        }
    }

    private class SubjectAdapter extends ArrayAdapter<Subject> {

        public SubjectAdapter(Context context) {
            super(context, R.layout.item_subject);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_subject, parent, false);

                holder = new ViewHolder();
                holder.nameLbl = convertView.findViewById(R.id.text_subject_name);
                holder.idLbl = convertView.findViewById(R.id.text_subject_id);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Subject subject = getItem(position);
            if (subject != null) {
                holder.nameLbl.setText(subject.name());
                holder.idLbl.setText(String.valueOf(subject.id()));
            }

            return convertView;
        }
    }

    private static class ViewHolder {
        public TextView nameLbl;
        public TextView idLbl;
    }
}
