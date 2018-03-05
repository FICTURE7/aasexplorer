package com.ficture7.aasexplorer.view.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.ficture7.aasexplorer.Explorer;
import com.ficture7.aasexplorer.R;
import com.ficture7.aasexplorer.model.QuestionPaper;
import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.view.activity.fragment.SearchQuestionPaperDialogFragment;

import java.util.Comparator;

public class QuestionPaperListActivity extends ListActivity {

    private Subject subject;
    private QuestionPaperAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new QuestionPaperAdapter(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra(App.Intents.EXTRA_SUBJECT_ID, -1);
        if (id != -1) {
            Explorer explorer = App.getInstance().getExplorer();
            subject = explorer.getALevel().getSubjects().get(id);
        }

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(String.valueOf(subject.getId()));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setContentView(R.layout.activity_question_papers);
        setListAdapter(adapter);

        updateList(subject.getResources().getQuestionPapers());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_question_papers, menu);
        return super.onCreateOptionsMenu(menu);
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
            if (qp.getSources().size() > 1) {
                createDownloadDialog(qp).show();
            } else {
                download(qp.getSources().iterator().next());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_search:
                SearchQuestionPaperDialogFragment.newInstance(subject.getId()).show(getFragmentManager(), null);
                return false;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateList(Iterable<QuestionPaper> questionPapers) {
        adapter.clear();
        for (QuestionPaper qp : questionPapers) {
            adapter.add(qp);
        }

        adapter.sort(new Comparator<QuestionPaper>() {

            @Override
            public int compare(QuestionPaper a, QuestionPaper b) {
                return sum(b) - sum(a);
            }

            private int sum(QuestionPaper qp) {
                int season = 0;
                switch (qp.getSession().getSeason()) {
                    case SUMMER:
                        season = 1;
                        break;
                    case WINTER:
                        season = 2;
                        break;
                }

                int number = qp.getNumber();
                if (number < 10) {
                    number = number * 10;
                }

                return (qp.getSession().getYear() * 1000) + (season * 100) + number;
            }
        });
    }

    private Dialog createDownloadDialog(QuestionPaper questionPaper) {
        final ResourceSource[] sources = new ResourceSource[questionPaper.getSources().size()];
        String[] clientNames = new String[questionPaper.getSources().size()];
        int index = 0;
        for (ResourceSource source : questionPaper.getSources()) {
            sources[index] = source;
            clientNames[index] = source.getClient().getName();
            index++;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Download")
               .setItems(clientNames, (dialogInterface, which) -> download(sources[which]));
        return builder.create();
    }

    private void download(ResourceSource source) {
        Uri uri = Uri.parse(source.getURI().toString());
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(source.getName());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDescription("Downloading resource from " + source.getClient().getName());

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        Toast.makeText(QuestionPaperListActivity.this, "Downloading " + source.getName() + "...", Toast.LENGTH_LONG).show();
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
                String name = "Paper " + questionPaper.getNumber();
                String season = "Unk";
                switch (questionPaper.getSession().getSeason()) {
                    case SUMMER:
                        season = "May/June";
                        break;
                    case WINTER:
                        season = "Nov/Oct";
                        break;
                }
                String year = String.valueOf(questionPaper.getSession().getYear());

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
