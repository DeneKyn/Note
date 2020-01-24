package com.example.note.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.note.Note;
import com.example.note.R;
import com.example.note.Tag;
import com.example.note.viewmodel.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NoteFragment extends Fragment {


    private List<Tag> allTags = new ArrayList<Tag>();
    private MainViewModel viewModel;
    private EditText titleView;
    private EditText bodyView;
    private TextView tagsView;
    private Note mNote;


    public NoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        setHasOptionsMenu(true);

                viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        bodyView = view.findViewById(R.id.bodyNoteAc);
        titleView = view.findViewById(R.id.titleNoteAc);
        tagsView = view.findViewById(R.id.tagsNoteAc);
        bodyView.setMovementMethod(new ScrollingMovementMethod());
        tagsView.setMovementMethod(new ScrollingMovementMethod());


        mNote = viewModel.mCurrentNote;
        if (mNote == null){
            mNote = new Note();
            viewModel.mCurrentNote = mNote;
        }
        else{
            setTags();
            bodyView.setText(mNote.getBody());
            titleView.setText(mNote.getTitle());
            tagsView.setText(setTags());
        }

        viewModel.getAllTags().observe(this, new Observer<List<Tag>>() {
            @Override
            public void onChanged(List<Tag> tags) {
                allTags = tags;
                setTags();
                tagsView.setText(setTags());
            }
        });
        return view;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_note, menu);
        boolean create = getArguments().getBoolean("NOTE_CREATE");
        if (create){
            menu.findItem(R.id.action_delete).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:

                NavHostFragment.findNavController(NoteFragment.this).navigate(R.id.action_noteFragment_to_tagsFragment);
                break;
            case R.id.action_delete:
                viewModel.removeNote(mNote);
                getActivity().onBackPressed();
                break;
            case R.id.action_save:
                saveChange();
                getActivity().onBackPressed();
                break;
            default:
                // Not one of ours. Perform default menu processing
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    public void saveChange() {

        mNote.setTitle(titleView.getText().toString());
        if (mNote.getTitle().isEmpty()){
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            mNote.setTitle(sdf.format(mNote.getDate()));
        }
        mNote.setBody(bodyView.getText().toString());
        if (mNote.getBody().isEmpty()){
            Toast.makeText(getActivity(), "Can't save note with empty body", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean create = getArguments().getBoolean("NOTE_CREATE");
        if(create){
            viewModel.addNote(mNote);
        }
        else{
        viewModel.updateNote(mNote);
        }
        viewModel.addedTag.clear();

    }

    public String setTags() {

        List<String> tagsId = mNote.getTags();
        StringBuilder tagsString = new StringBuilder();
        for (String id : tagsId) {
            String tag = getTag(id);
            tagsString.append(tag).append(", ");
        }
        if (tagsString.length() > 0) {
            tagsString.delete(tagsString.length() - 2, tagsString.length());
        }
        //mTags = tagsString.toString();
        return tagsString.toString();
    }

    public String getTag(String id) {
        for (Tag tag : allTags) {
            if (tag.mUniqueID.equals(id)) {
                return tag.mTag;
            }
        }
        return null;
    }

}
