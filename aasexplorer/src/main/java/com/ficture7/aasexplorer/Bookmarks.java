package com.ficture7.aasexplorer;

import android.content.Context;
import android.content.SharedPreferences;

import com.ficture7.aasexplorer.model.Subject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Bookmarks {

    private final SharedPreferences prefs;
    // List of bookmarked subject IDs.
    private final List<Integer> subjectIds;

    public Bookmarks(Context ctx) {
        subjectIds = new ArrayList<>();

        prefs = ctx.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        Set<String> values = prefs.getStringSet("bookmarks", null);

        if (values != null) {
            for (String subjectId : values) {
                subjectIds.add(Integer.parseInt(subjectId));
            }
        }
    }

    public void add(Subject subject) {
        subjectIds.add(subject.getId());
    }

    public void remove(Subject subject) {
        subjectIds.remove(Integer.valueOf(subject.getId()));
    }

    public boolean contains(Subject subject) {
        return subjectIds.contains(subject.getId());
    }

    public void save() {
        Set<String> subjectIdString = new HashSet<>(subjectIds.size());
        for (Integer id : subjectIds) {
            subjectIdString.add(String.valueOf(id));
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("bookmarks", subjectIdString);
        editor.apply();
    }
}
