package com.ficture7.aasexplorer.ui;

import android.app.DownloadManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ficture7.aasexplorer.App;
import com.ficture7.aasexplorer.Explorer;
import com.ficture7.aasexplorer.R;
import com.ficture7.aasexplorer.model.QuestionPaper;
import com.ficture7.aasexplorer.model.Subject;

public class QuestionPaperListActivity extends ListActivity {

    private Subject subject;
    private QuestionPaperAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new QuestionPaperAdapter(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra("SUBJECT_ID", -1);
        if (id != -1) {
            Explorer explorer = App.getInstance().getExplorer();
            subject = explorer.alevel().subjects().get(id);
        }

        setContentView(R.layout.activity_question_papers);
        setListAdapter(adapter);

        for (QuestionPaper qp : subject.resources().questionPapers()) {
            adapter.add(qp);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        QuestionPaper qp = adapter.getItem(position);
        if (qp != null) {
            Uri uri = Uri.parse(qp.sources().iterator().next().uri().toString());
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(qp.name());

            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        }
    }

    public class QuestionPaperAdapter extends ArrayAdapter<QuestionPaper>{

        public QuestionPaperAdapter(Context context) {
            super(context, R.layout.item_question_paper);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_question_paper, parent, false);

                holder = new ViewHolder();
                holder.nameLbl = convertView.findViewById(R.id.text_question_paper_name);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            QuestionPaper questionPaper = getItem(position);
            if (questionPaper != null) {
                holder.nameLbl.setText(questionPaper.name());
            }

            return convertView;
        }
    }


    private static class ViewHolder {
        public TextView nameLbl;
    }
}
