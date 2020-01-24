package com.example.note.viewmodel.DaoTasks;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.note.AppDatabase;
import com.example.note.Note;
import com.example.note.NoteDao;

import java.util.List;

public final class NoteDatabaseTasks {

    private NoteDao mNoteDao;
    private LiveData<List<Note>> allNotes;


    public NoteDatabaseTasks(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        mNoteDao = database.noteDao();
        allNotes = mNoteDao.getAll();
    }

    public LiveData<List<Note>> getAll() {
        return allNotes;
    }



    public void updateNote(final Note note) {
        new UpdateNoteAsyncTask(mNoteDao).execute(note);
    }

    public void deleteNote(final Note note) {
        new DeleteNoteAsyncTask(mNoteDao).execute(note);
    }

    public void insertNote(final Note note) {
        new InsertNoteAsyncTask(mNoteDao).execute(note);
    }




    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao mNoteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.mNoteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mNoteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao mNoteDao;

        private DeleteNoteAsyncTask(NoteDao noteDAO) {
            this.mNoteDao = noteDAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mNoteDao.delete(notes[0]);
            return null;
        }
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao mNoteDao;

        private InsertNoteAsyncTask(NoteDao noteDAO) {
            this.mNoteDao = noteDAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mNoteDao.insert(notes[0]);
            return null;
        }
    }

}
