package com.ficture7.aasexplorer.view.activity;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.ficture7.aasexplorer.App;
import com.ficture7.aasexplorer.Explorer;
import com.ficture7.aasexplorer.R;
import com.ficture7.aasexplorer.model.Subject;

public class EditBookmarkListActivity extends ListActivity {

    private SubjectBookmarkAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new SubjectBookmarkAdapter(this);

        setContentView(R.layout.activity_edit_bookmarks);
        setListAdapter(adapter);

        // Display UP button in action bar.
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Explorer explorer = App.getInstance().getExplorer();
        updateList(explorer.getALevel().getSubjects());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_bookmarks, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setQueryHint(getString(R.string.hint_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
            public boolean onQueryTextSubmit(String query) {
                searchAndUpdate(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newQuery) {
                searchAndUpdate(newQuery);
                return false;
            }

            private void searchAndUpdate(String query) {
                adapter.clear();
                Iterable<Subject> subjects = App.getInstance().getExplorer().getALevel().getSubjects();
                for (Subject subject : subjects) {
                    String id = String.valueOf(subject.getId());
                    String name = subject.getName().toLowerCase();

                    if (id.contains(query) || name.contains(query)) {
                        adapter.add(subject);
                    }
                }
            }
        });
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save the bookmarks once the user is done editing them.
        App.getInstance().getBookmarks().save();
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

    // Adds the specified subjects to the list of the activity.
    private void updateList(Iterable<Subject> subjects) {
        adapter.clear();
        for (Subject subject : subjects) {
            adapter.add(subject);
        }
    }

    private class SubjectBookmarkAdapter extends ArrayAdapter<Subject> {

        public SubjectBookmarkAdapter(Context context) {
            super(context, R.layout.item_subject_bookmark);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_subject_bookmark, parent, false);

                holder = new ViewHolder();
                holder.nameLbl = convertView.findViewById(R.id.text_subject_name);
                holder.idLbl = convertView.findViewById(R.id.text_subject_id);
                holder.bookmarkCkb = convertView.findViewById(R.id.check_subject_bookmark);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Subject subject = getItem(position);
            boolean bookmarked = App.getInstance().getBookmarks().contains(subject);

            if (subject != null) {
                holder.nameLbl.setText(subject.getName());
                holder.idLbl.setText(String.valueOf(subject.getId()));

                holder.bookmarkCkb.setOnCheckedChangeListener(null);
                holder.bookmarkCkb.setChecked(bookmarked);
                holder.bookmarkCkb.setOnCheckedChangeListener((compoundButton, checked) -> {
                    if (checked) {
                        App.getInstance().getBookmarks().add(subject);
                    } else {
                        App.getInstance().getBookmarks().remove(subject);
                    }
                });
            }

            return convertView;
        }
    }

    private static class ViewHolder {
        public TextView nameLbl;
        public TextView idLbl;
        public CheckBox bookmarkCkb;
    }
}
