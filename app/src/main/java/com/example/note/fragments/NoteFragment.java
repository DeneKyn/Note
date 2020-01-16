package com.example.note.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.note.activity.MainActivity;
import com.example.note.viewmodel.MainViewModel;
import com.example.note.Note;
import com.example.note.R;

import java.util.List;

public class NoteFragment extends Fragment {

    private EditText titleView;
    private EditText bodyView;
    private TextView tagsView;
    private MainActivity activity;
    private MainViewModel viewModel;
    private Note mNote;
    private String mTags;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        activity = (MainActivity) getActivity();
        activity.setTitle(mNote.getDate());
        bodyView = view.findViewById(R.id.bodyNoteAc);
        titleView = view.findViewById(R.id.titleNoteAc);
        tagsView = view.findViewById(R.id.tagsNoteAc);
        bodyView.setMovementMethod(new ScrollingMovementMethod());
        tagsView.setMovementMethod(new ScrollingMovementMethod());
        bodyView.setText(mNote.getBody());
        titleView.setText(mNote.getTitle());
        tagsView.setText(mTags);
        mNote.setTitle(mNote.mTitle);

        return view;
    }

    public void saveChange() {

            mNote.setTitle(titleView.getText().toString().trim().length() == 0 ? mNote.getDate() : titleView.getText().toString());
            mNote.setBody(bodyView.getText().toString());

    }

    public void setViewModel(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setNote(Note note) {
        mNote = note;
        List<String> tagsId = mNote.getTags();
        StringBuilder tagsString = new StringBuilder();
        for (String id : tagsId) {
            String tag = viewModel.getTag(id);
            tagsString.append(tag).append(", ");
        }
        if (tagsString.length() > 0) {
            tagsString.delete(tagsString.length() - 2, tagsString.length());
        }
        mTags = tagsString.toString();
    }

}
