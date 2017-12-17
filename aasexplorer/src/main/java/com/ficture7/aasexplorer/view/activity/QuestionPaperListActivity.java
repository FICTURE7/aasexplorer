package com.ficture7.aasexplorer.view.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ficture7.aasexplorer.App;
import com.ficture7.aasexplorer.Explorer;
import com.ficture7.aasexplorer.R;
import com.ficture7.aasexplorer.client.Client;
import com.ficture7.aasexplorer.model.QuestionPaper;
import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.Subject;

import java.util.Comparator;

public class QuestionPaperListActivity extends ListActivity {

    private Subject subject;
    private QuestionPaperAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new QuestionPaperAdapter(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra(App.Intents.SUBJECT_ID, -1);
        if (id != -1) {
            Explorer explorer = App.getInstance().getExplorer();
            subject = explorer.alevel().subjects().get(id);
        }

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(String.valueOf(subject.id()));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setContentView(R.layout.activity_question_papers);
        setListAdapter(adapter);

        for (QuestionPaper qp : subject.resources().questionPapers()) {
            adapter.add(qp);
        }

        adapter.sort(new Comparator<QuestionPaper>() {

            @Override
            public int compare(QuestionPaper a, QuestionPaper b) {
                return sum(b) - sum(a);
            }

            private int sum(QuestionPaper qp) {
                int season = 0;
                switch (qp.session().season()) {
                    case SUMMER:
                        season = 1;
                        break;
                    case WINTER:
                        season = 2;
                        break;
                }

                int number = qp.number();
                if (number < 10) {
                    number = number * 10;
                }

                return (qp.session().year() * 1000) + (season * 100) + number;
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        QuestionPaper qp = adapter.getItem(position);
        if (qp != null) {
            /*
                If we have more than 1 source for the same resource we
                give the user the option of choosing where to download it.
                Otherwise we download it from the first source.
             */
            if (qp.sources().size() > 1) {
                createDownloadDialog(qp).show();
            } else {
                download(qp.sources().iterator().next());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Dialog createDownloadDialog(QuestionPaper questionPaper) {
        final ResourceSource[] sources = new ResourceSource[questionPaper.sources().size()];
        String[] clientNames = new String[questionPaper.sources().size()];
        int index = 0;
        for (ResourceSource source : questionPaper.sources()) {
            sources[index] = source;
            clientNames[index] = source.client().name();
            index++;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Download")
               .setItems(clientNames, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int which) {
                       download(sources[which]);
                   }
               });
        return builder.create();
    }

    private void download(ResourceSource source) {
        Uri uri = Uri.parse(source.uri().toString());
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(source.name());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDescription("Downloading resource from " + source.client().name());

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        Toast.makeText(QuestionPaperListActivity.this, "Downloading " + source.name() + "...", Toast.LENGTH_LONG).show();
    }

    public class QuestionPaperAdapter extends ArrayAdapter<QuestionPaper> {

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
                holder.seasonLbl = convertView.findViewById(R.id.text_question_paper_season);
                holder.yearLbl = convertView.findViewById(R.id.text_question_paper_year);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            QuestionPaper questionPaper = getItem(position);
            if (questionPaper != null) {
                String name = "Paper " + questionPaper.number();
                String season = "Unk";
                switch (questionPaper.session().season()) {
                    case SUMMER:
                        season = "May/June";
                        break;
                    case WINTER:
                        season = "Nov/Oct";
                        break;
                }
                String year = String.valueOf(questionPaper.session().year());

                holder.nameLbl.setText(name);
                holder.seasonLbl.setText(season);
                holder.yearLbl.setText(year);
            }

            return convertView;
        }
    }


    private static class ViewHolder {
        public TextView nameLbl;
        public TextView seasonLbl;
        public TextView yearLbl;
    }
}
