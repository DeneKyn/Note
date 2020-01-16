package com.example.note.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.note.App;
import com.example.note.Note;
import com.example.note.NoteComparators;
import com.example.note.NoteDao;
import com.example.note.Tag;
import com.example.note.TagDao;
import com.example.note.viewmodel.DaoTasks.NoteDatabaseTasks;
import com.example.note.viewmodel.DaoTasks.TagDatabaseTasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private List<Note> mNotes;
    private List<Note> mCurrentNotes;
    private List<Tag> mTags;
    private List<String> mSelectedTagsId;

    private int mLastTypeScreen = 0;
    private Note mLastUsedNote;

    private final NoteDatabaseTasks mNoteDatabaseTasks;
    private final TagDatabaseTasks mTagDatabaseTasks;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mNotes = new ArrayList<>();
        mCurrentNotes = new ArrayList<>();
        mTags = new ArrayList<>();
        mSelectedTagsId = new ArrayList<>();
        NoteDao noteDao = App.getInstance().getDatabase().noteDao();
        TagDao tagDao = App.getInstance().getDatabase().tagDao();
        mNoteDatabaseTasks = new NoteDatabaseTasks(noteDao);
        mTagDatabaseTasks = new TagDatabaseTasks(tagDao);
        mNoteDatabaseTasks.getAllNote(mNotes, mCurrentNotes);
        mTagDatabaseTasks.getAllTag(mTags);
    }

    public void setLastTypeScreen(final int typeScreen) {
        mLastTypeScreen = typeScreen;
    }

    public int getLastTypeScreen() {
        return mLastTypeScreen;
    }

    public void setLastUsedNote(final Note note) {
        mLastUsedNote = note;
    }

    public Note getLastUsedNote() {
        return mLastUsedNote;
    }

    public void updateSelectedTagsId() {
        mSelectedTagsId.clear();
        for (final Tag tag : mTags) {
            if (tag.selected) {
                mSelectedTagsId.add(tag.mUniqueID);
            }
        }
        updateCurrentNotes();
    }

    public void clearSelectedTagsId() {
        mSelectedTagsId.clear();
        for (final Tag tag : mTags) {
            tag.selected = false;
        }
        updateCurrentNotes();
    }

    public void updateCurrentNotes() {
        mCurrentNotes.clear();
        if (mSelectedTagsId.size() == 0) {
            mCurrentNotes.addAll(mNotes);
        } else {
            for (final Note note : mNotes) {
                List<String> tagsId = new ArrayList<>(note.getTags());
                tagsId.retainAll(mSelectedTagsId);
                if (tagsId.size() > 0) {
                    mCurrentNotes.add(note);
                }
            }
        }
    }

    public void sortNotes(boolean isDateSort) {
        if (isDateSort) {
            mNotes.sort(NoteComparators.DATE_COMPARATOR);
            mCurrentNotes.sort(NoteComparators.DATE_COMPARATOR);
        } else {
            mNotes.sort(NoteComparators.TITLE_COMPARATOR);
            mCurrentNotes.sort(NoteComparators.TITLE_COMPARATOR);
        }
    }

    public List<Note> getNotes() {
        return mCurrentNotes;
    }

    public List<Tag> getTags() {
        Collections.sort(mTags);
        return new ArrayList<>(mTags);
    }

    public String getTag(String id) {
        for (Tag tag : mTags) {
            if (tag.mUniqueID.equals(id)) {
                return tag.mTag;
            }
        }
        return null;
    }

    public void addNote(Note note) {
        mNotes.add(note);
        mNoteDatabaseTasks.insertNote(note);
    }

    public void addTag(Tag tag) {

        mTags.add(tag);
        mTagDatabaseTasks.insertTag(tag);

    }

    public void removeTag(Tag tag) {

        mTags.remove(tag);
        mSelectedTagsId.remove(tag);
        mTagDatabaseTasks.deleteTag(tag);

    }

    public void removeNote(Note note) {
        mNotes.remove(note);
        mCurrentNotes.remove(note);
        mNoteDatabaseTasks.deleteNote(note);
    }

    public void updateNote(Note note) {
        mNoteDatabaseTasks.updateNote(note);
    }

}
