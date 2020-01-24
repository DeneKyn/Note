package com.example.note.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.note.Note;
import com.example.note.R;
import com.example.note.Tag;
import com.example.note.activity.MainActivity;
import com.example.note.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class TagsFragment extends Fragment {

    private MainViewModel viewModel;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<Tag> mTags;
    private Note mNote;

    private AlertDialog mAddTagAlertDialog;

    public TagsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tags, container, false);
        setHasOptionsMenu(true);
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        mNote = viewModel.mCurrentNote;
        listView = view.findViewById(R.id.listView);
        mTags = new ArrayList<>();




        viewModel.getAllTags().observe(this, new Observer<List<Tag>>() {
            @Override
            public void onChanged(@Nullable List<Tag> tags) {
                if (tags != null){
                mTags = tags;
                //tagsString = getTagsStringList(tags);
                adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, getTagsStringList(tags));
                listView.setAdapter(adapter);
                setItemsChecked();
                }
                adapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SparseBooleanArray sp =listView.getCheckedItemPositions();
                changeBoolValue(position, sp.get(position));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Tag tag = viewModel.getAllTags().getValue().get(position);
                viewModel.removeTag(tag);
                String deleteId = tag.mUniqueID;
                List<Note> notes = viewModel.getAll().getValue();
                for (Note  note : notes){
                    if (note.getTags().contains(deleteId)){
                        note.getTags().remove(deleteId);
                        viewModel.updateNote(note);
                    }
                }
                return false;
            }
        });

        createAddTagDialog();


        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tag, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                mAddTagAlertDialog.show();
                break;
            default:
                // Not one of ours. Perform default menu processing
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private List<String> getTagsStringList(List<Tag> tags) {
        List<String> tagsString = new ArrayList<>();
        for (Tag tag : tags) {
            tagsString.add(tag.mTag);
        }
        return tagsString;
    }

    private void changeBoolValue(int position, boolean value) {
        if (value) {

            String id = mTags.get(position).mUniqueID;
            mNote.addTag(id);

            viewModel.addedTag.add(id);
        } else {
            mNote.removeTag(mTags.get(position).mUniqueID);
        }
    }

    private void setItemsChecked() {
        for (int i = 0; i < mTags.size(); ++i) {
            boolean isChecked = false;
            if (mNote.getTags().contains(mTags.get(i).mUniqueID)) {
                isChecked = true;
            }
            listView.setItemChecked(i, isChecked);
        }
    }


    private void createAddTagDialog() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View myAlert = li.inflate(R.layout.my_alert, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setView(myAlert);

        final EditText userInput = myAlert.findViewById(R.id.input_text);

        dialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.addTag(new Tag(userInput.getText().toString()));
                                userInput.setText("");
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                                userInput.setText("");
                            }
                        });

        mAddTagAlertDialog = dialogBuilder.create();
    }
}
