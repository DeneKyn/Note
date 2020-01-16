package com.example.note.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.note.viewmodel.MainViewModel;
import com.example.note.Note;
import com.example.note.adapter.NoteAdapter;
import com.example.note.R;
import com.example.note.Tag;
import com.example.note.fragments.AllNotesFragment;
import com.example.note.fragments.NoteFragment;
import com.example.note.fragments.TagsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteListener {

    private MainViewModel mMainViewModel;

    private MenuItem removeItem;
    private MenuItem selectItem;
    private MenuItem sortItem;
    private MenuItem addItem;

    private int mTypeScreen = 0;
    private boolean isDateSort = true;

    private Note mCurrentNote;

    private AlertDialog mAddTagAlertDialog;
    private AlertDialog mSelectTagAlertDialog;

    private AllNotesFragment allNotesFragment;
    private NoteFragment noteFragment;
    private TagsFragment tagsFragment;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        allNotesFragment = new AllNotesFragment();

        mMainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mTypeScreen = mMainViewModel.getLastTypeScreen();
        mCurrentNote = mMainViewModel.getLastUsedNote();


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectItem.setVisible(false);
                sortItem.setVisible(false);
                fab.setVisibility(View.INVISIBLE);
                addNewNote();
            }
        });


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, allNotesFragment)
                .commit();

        createAddTagDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (noteFragment != null) {
            //noteFragment.saveChange();
            mMainViewModel.updateNote(mCurrentNote);
        }
        mMainViewModel.setLastTypeScreen(mTypeScreen);
        mMainViewModel.setLastUsedNote(mCurrentNote);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @ Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_note, menu);
        removeItem = menu.findItem(R.id.action_remove);
        selectItem = menu.findItem(R.id.action_select);
        sortItem = menu.findItem(R.id.action_sort);
        addItem = menu.findItem(R.id.action_add);
        addItem.setVisible(false);
        restoreFragmentState();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            isDateSort = !isDateSort;
            mMainViewModel.sortNotes(isDateSort);
            allNotesFragment.updateRecyclerView();
        } else if (item.getItemId() == R.id.action_select) {
            createSelectTagDialog();
            mSelectTagAlertDialog.show();
        } else if (item.getItemId() == R.id.action_add) {
            addAction();
        } else {
            onBackPressed();
            removeNote(mCurrentNote);
        }

        return true;
    }

    @Override
    public void onNoteClick(int position) {
        openNoteFragment(mMainViewModel.getNotes().get(position), false, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
    }

    @Override
    public void onBackPressed() {
        if (mTypeScreen == 1) {
            noteFragment.saveChange();
            if (mCurrentNote.getBody().trim().length() == 0) {
                removeNote(mCurrentNote);
            }
            openAllNotesFragment(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        } else if (mTypeScreen == 2) {
            openNoteFragment(mCurrentNote, false, FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        } else {
            super.onBackPressed();
        }
    }

    public MainViewModel getMainViewModel() {
        return mMainViewModel;
    }

    private void openAllNotesFragment(int transactionType) {
        this.setTitle("Notes");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, allNotesFragment)
                .setTransition(transactionType)
                .commit();
        mTypeScreen = 0;
        removeItem.setVisible(false);
        selectItem.setVisible(true);
        addItem.setVisible(false);
        sortItem.setVisible(true);
        fab.setVisibility(View.VISIBLE);
        mMainViewModel.updateNote(mCurrentNote);
        mMainViewModel.sortNotes(isDateSort);
        mMainViewModel.updateCurrentNotes();
        allNotesFragment.updateRecyclerView();
    }

    private void openNoteFragment(Note note, boolean isNew, int transactionType) {
        noteFragment = new NoteFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, noteFragment)
                .setTransition(transactionType)
                .commit();
        mCurrentNote = note;
        noteFragment.setViewModel(mMainViewModel);
        noteFragment.setNote(mCurrentNote);
        mTypeScreen = 1;
        removeItem.setVisible(!isNew);
        selectItem.setVisible(false);
        sortItem.setVisible(false);
        addItem.setVisible(true);
        fab.setVisibility(View.INVISIBLE);
    }

    private void openTagFragment(int transactionType) {
        tagsFragment = new TagsFragment();
        tagsFragment.setNote(mCurrentNote);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, tagsFragment)
                .setTransition(transactionType)
                .commit();
        mTypeScreen = 2;
        removeItem.setVisible(false);
        selectItem.setVisible(false);
        sortItem.setVisible(false);
        addItem.setVisible(true);
    }

    private void addAction() {
          if (mTypeScreen == 1) {
            openTagFragment(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            noteFragment.saveChange();
        } else if (mTypeScreen == 2) {
            mAddTagAlertDialog.show();
        }
    }

    private void restoreFragmentState() {
        if (mCurrentNote != null && mTypeScreen != 0) {
            if (mTypeScreen == 1) {
                openNoteFragment(mCurrentNote, false, FragmentTransaction.TRANSIT_NONE);
            } else if (mTypeScreen == 2) {
                openTagFragment(FragmentTransaction.TRANSIT_NONE);
            }
        }
    }

    private void addNewNote() {
        Note note = new Note();
        mMainViewModel.addNote(note);
        openNoteFragment(note, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

    }

    private void removeNote(Note note) {
        mMainViewModel.removeNote(note);
        allNotesFragment.updateRecyclerView();
    }

    private void createAddTagDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View myAlert = li.inflate(R.layout.my_alert, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setView(myAlert);

        final EditText userInput = myAlert.findViewById(R.id.input_text);
        userInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);

        dialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mMainViewModel.addTag(new Tag(userInput.getText().toString()));
                                tagsFragment.updateListView();
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

    private void createSelectTagDialog() {
        final List<Tag> tags = mMainViewModel.getTags();
        final boolean[] checkedItems = new boolean[tags.size()];
        final String[] checkTags = new String[tags.size()];
        for (int i = 0; i < tags.size(); ++i) {
            final Tag tag = tags.get(i);
            checkedItems[i] = tag.selected;
            checkTags[i] = tag.mTag;
        }
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
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
                                mMainViewModel.updateSelectedTagsId();
                                allNotesFragment.updateRecyclerView();
                            }
                        })

                .setNegativeButton("Clear",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                mMainViewModel.clearSelectedTagsId();
                                allNotesFragment.updateRecyclerView();
                            }
                        });
        mSelectTagAlertDialog = dialogBuilder.create();
    }

}
