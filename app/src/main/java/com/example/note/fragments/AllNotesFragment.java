package com.example.note.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.note.Note;
import com.example.note.R;
import com.example.note.Tag;
import com.example.note.activity.MainActivity;
import com.example.note.adapter.NoteAdapter;
import com.example.note.viewmodel.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class AllNotesFragment extends Fragment implements NoteAdapter.OnNoteListener{

    private FloatingActionButton fab;
    private NoteAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private MainViewModel viewModel;
    private List<Tag> currentTags;


    public AllNotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_notes, container, false);
        setHasOptionsMenu(true);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.mCurrentNote = null;
                Bundle bundle = new Bundle();
                bundle.putBoolean("NOTE_CREATE", true);
                viewModel.addedTag.clear();
                Navigation.findNavController(view).navigate(R.id.action_allNotesFragment_to_noteFragment, bundle);
            }
        });


        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            mRecyclerView.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(getContext())));
        else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(Objects.requireNonNull(getContext()), 2));
        }

        mAdapter = new NoteAdapter(this);
        mRecyclerView.setAdapter(mAdapter);


        viewModel.getAllTags().observe(this, new Observer<List<Tag>>() {
            @Override
            public void onChanged(List<Tag> tags) {
                mAdapter.setTags(tags);
                currentTags = tags;

            }
        });

        viewModel.getAll().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                mAdapter.setNotes(notes, viewModel.getSortStage());
                mAdapter.kek(viewModel.getSortStage());
            }
        });



        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_note, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:

                mAdapter.applySort(viewModel.selectStorageStage());
                showSortToast();
                break;
            case R.id.action_tag:
                createSelectTagDialog().show();

                break;
            case R.id.action_clear:

                viewModel.setSortStage(1);
                mAdapter.applySort(viewModel.getSortStage());
                showSortToast();
                break;
            default:
                // Not one of ours. Perform default menu processing
                return super.onOptionsItemSelected(item);
        }



        return super.onOptionsItemSelected(item);
    }

    public void showSortToast(){
        String messageToat;
        switch (viewModel.getSortStage()) {
            case 1:
                messageToat = "Sort by Title. INCREASE";
                break;
            case 2:
                messageToat = "Sort by Title. DECREASE";
                break;
            case 3:
                messageToat = "Sort by Date. INCREASE";
                break;
            case 4:
                messageToat = "Sort by Date. DECREASE";
                break;
            default:
                messageToat = "ERROR";

        }

        Toast.makeText(getActivity(), messageToat, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        for(String id: viewModel.addedTag){
            viewModel.mCurrentNote.removeTag(id);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteClick(int position) {
        Note note =  mAdapter.mNotes.get(position);
        Bundle bundle = new Bundle();
        bundle.putBoolean("NOTE_CREATE", false);
        viewModel.mCurrentNote = note;
        viewModel.addedTag.clear();
        NavHostFragment.findNavController(AllNotesFragment.this)
                .navigate(R.id.action_allNotesFragment_to_noteFragment, bundle);


    }

    private Dialog createSelectTagDialog() {
        final List<Tag> tags = currentTags;
        final boolean[] checkedItems = new boolean[tags.size()];
        final String[] checkTags = new String[tags.size()];
        for (int i = 0; i < tags.size(); ++i) {
            final Tag tag = tags.get(i);
            checkedItems[i] = tag.selected;
            checkTags[i] = tag.mTag;
        }
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("Select tags")
                .setCancelable(false)

                .setMultiChoiceItems(checkTags, checkedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which, boolean isChecked) {
                                checkedItems[which] = isChecked;
                                tags.get(which).selected = isChecked;
                            }
                        })

                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                mAdapter.kek(viewModel.getSortStage());
                            }
                        })

                .setNegativeButton("Clear",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                mAdapter.lol(viewModel.getSortStage());
                            }
                        });
        return dialogBuilder.create();
    }
}



