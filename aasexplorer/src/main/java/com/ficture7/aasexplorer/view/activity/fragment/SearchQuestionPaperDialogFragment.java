package com.ficture7.aasexplorer.view.activity.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ficture7.aasexplorer.App;
import com.ficture7.aasexplorer.R;
import com.ficture7.aasexplorer.model.QuestionPaper;
import com.ficture7.aasexplorer.model.Subject;

import java.util.ArrayList;
import java.util.List;

public class SearchQuestionPaperDialogFragment extends DialogFragment {

    private static final String TAG = "SearchQuestionPaper";
    public static final String ARGUMENT_SUBJECT_ID = "ARGUMENT_SUBJECT_ID";

    private ArrayAdapter<String> yearSpinnerAdapter;
    private ArrayAdapter<String> seasonSpinnerAdapter;
    private ArrayAdapter<String> paperNumberSpinnerAdapter;
    private ArrayAdapter<String> paperVariantSpinnerAdapter;

    public static SearchQuestionPaperDialogFragment newInstance(int subjectId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARGUMENT_SUBJECT_ID, subjectId);

        SearchQuestionPaperDialogFragment searchDialog = new SearchQuestionPaperDialogFragment();
        searchDialog.setArguments(bundle);
        return searchDialog;
    }

    private Subject subject;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*
            Retrieve the subject ID/code from the arguments of the
            fragment.
         */
        int subjectId = getArguments().getInt(ARGUMENT_SUBJECT_ID, -1);
        if (subjectId == -1) {
            Log.e(TAG, "No ARGUMENT_SUBJECT_ID passed to SearchQuestionPaperDialogFragment.");
            return null;
        }

        /*
            Retrieve the subject instance from the 'global'
            explorer instance.
         */
        subject = App.getInstance().getExplorer().getALevel().getSubjects().get(subjectId);
        if (subject == null) {
            Log.e(TAG, "Subject instance with ARGUMENT_SUBJECT_ID passed to SearchQuestionPaperDialogFragment was not found.");
            return null;
        }

        /* Inflate the view of the fragment. */
        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_search_question_paper, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setTitle("Search")
                .setPositiveButton("Search", (dialogInterface, i) -> {
                    String yearString = ((Spinner)dialogView.findViewById(R.id.spinner_year)).getSelectedItem().toString();
                    String seasonString = ((Spinner)dialogView.findViewById(R.id.spinner_season)).getSelectedItem().toString();
                    String paperNumberString = ((Spinner)dialogView.findViewById(R.id.spinner_paper_number)).getSelectedItem().toString();
                    String paperVariantString = ((Spinner)dialogView.findViewById(R.id.spinner_paper_variant)).getSelectedItem().toString();

                    int year = Integer.parseInt(yearString);
                    int paperNumber = Integer.parseInt(paperNumberString);
                    int paperVariant = Integer.parseInt(paperVariantString);
                })
                .setNegativeButton("Cancel", null);

        yearSpinnerAdapter = initSpinner(dialogView, R.id.spinner_year);
        seasonSpinnerAdapter = initSpinner(dialogView, R.id.spinner_season);
        paperNumberSpinnerAdapter = initSpinner(dialogView, R.id.spinner_paper_number);
        paperVariantSpinnerAdapter = initSpinner(dialogView, R.id.spinner_paper_variant);

        initSpinnerValues();

        return builder.create();
    }

    private ArrayAdapter<String> initSpinner(View view, int resId) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = view.findViewById(resId);
        spinner.setAdapter(spinnerAdapter);
        return spinnerAdapter;
    }

    private void initSpinnerValues() {
        List<String> years = new ArrayList<>();
        List<String> seasons = new ArrayList<>();
        List<String> paperNumbers = new ArrayList<>();
        List<String> paperVariants = new ArrayList<>();

        for (QuestionPaper qp : subject.getResources().getQuestionPapers()) {
            String year = String.valueOf(qp.getSession().getYear());
            if (!years.contains(year))
                years.add(year);

            String season = "Unk";
            switch (qp.getSession().getSeason()) {
                case SUMMER:
                    season = "May/June";
                    break;
                case WINTER:
                    season = "Nov/Oct";
                    break;
            }

            if (!seasons.contains(season))
                seasons.add(season);

            String paperNumber = String.valueOf(qp.getNumber() / 10);
            if (!paperNumbers.contains(paperNumber))
                paperNumbers.add(paperNumber);

            String paperVariant = String.valueOf(qp.getNumber() % 10);
            if (!paperVariants.contains(paperVariant))
                paperVariants.add(paperVariant);
        }

        yearSpinnerAdapter.addAll(years);
        seasonSpinnerAdapter.addAll(seasons);
        paperNumberSpinnerAdapter.addAll(paperNumbers);
        paperVariantSpinnerAdapter.addAll(paperVariants);
    }
}
