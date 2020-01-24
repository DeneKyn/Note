package com.example.note.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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


    private int sortStage ;

    private LiveData<List<Note>> allNotes;
    private LiveData<List<Tag>> allTags;

    private final NoteDatabaseTasks mNoteDatabaseTasks;
    private final TagDatabaseTasks mTagDatabaseTasks;
    public List<String> addedTag;


    public Note mCurrentNote;


    public MainViewModel(@NonNull Application application) {

        super(application);
        sortStage = 1;
        mNoteDatabaseTasks = new NoteDatabaseTasks(application);
        mTagDatabaseTasks = new TagDatabaseTasks(application);
        addedTag = new ArrayList<>();
        allNotes = mNoteDatabaseTasks.getAll();
        allTags = mTagDatabaseTasks.getAll();

    }

    public int getSortStage(){
        return sortStage;
    }

    public void setSortStage(int stage){
        this.sortStage = stage;
    }

    public int selectStorageStage(){
        this.sortStage +=1;
        if (sortStage > 4){sortStage = 1;}
        return sortStage;
    }

    public LiveData<List<Note>> getAll() {
        return allNotes;
    }


    public LiveData<List<Tag>> getAllTags() {
        return allTags;
    }

    public void addNote(Note note) {
        mNoteDatabaseTasks.insertNote(note);
    }

    public void addTag(Tag tag) {
        mTagDatabaseTasks.insertTag(tag);

    }

    public void removeTag(Tag tag) {
        mTagDatabaseTasks.deleteTag(tag);

    }

    public void removeNote(Note note) {
        mNoteDatabaseTasks.deleteNote(note);
    }

    public void updateNote(Note note) {
        mNoteDatabaseTasks.updateNote(note);
    }
}
