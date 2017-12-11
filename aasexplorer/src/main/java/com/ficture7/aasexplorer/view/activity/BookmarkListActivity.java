package com.ficture7.aasexplorer.view.activity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ficture7.aasexplorer.App;
import com.ficture7.aasexplorer.AppExplorerLoader;
import com.ficture7.aasexplorer.Explorer;
import com.ficture7.aasexplorer.R;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.view.LoaderView;

// This is also the MainActivity.
public class BookmarkListActivity extends ListActivity {

    private LoaderView loaderView;
    private SubjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new SubjectAdapter(this);

        // Set title here otherwise Android will use the title in manifest.
        setTitle(R.string.title_bookmarks);
        setContentView(R.layout.activity_bookmarks);
        setListAdapter(adapter);

        loaderView = findViewById(R.id.view_loader);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bookmarks, menu);
        return true;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_add:
                if (App.getInstance().getExplorer().alevel().subjects().isLoaded()) {
                    App.getInstance().getNavigator().navigateToEditBookmarks(this);
                    return true;
                } else {
                    Toast.makeText(this, R.string.message_subjects_not_loaded_yet, Toast.LENGTH_LONG).show();
                }
                return false;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        Subject subject = adapter.getItem(position);
        if (subject != null) {
            Intent intent = new Intent(this, SubjectActivity.class);
            intent.putExtra(App.Intents.SUBJECT_ID, subject.id());

            startActivity(intent);
        }
    }

    // Adds the specified subjects to the list of the activity
    // filtering out subjects which are not bookmarked.
    private void updateList(Iterable<Subject> subjects) {
        adapter.clear();
        for (Subject subject : subjects) {
            if (App.getInstance().getBookmarks().contains(subject)) {
                adapter.add(subject);
            }
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
