package com.ficture7.aasexplorer.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
        updateList(explorer.alevel().subjects());
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
                holder.nameLbl.setText(subject.name());
                holder.idLbl.setText(String.valueOf(subject.id()));

                holder.bookmarkCkb.setOnCheckedChangeListener(null);
                holder.bookmarkCkb.setChecked(bookmarked);
                holder.bookmarkCkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        if (checked) {
                            App.getInstance().getBookmarks().add(subject);
                        } else {
                            App.getInstance().getBookmarks().remove(subject);
                        }
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
