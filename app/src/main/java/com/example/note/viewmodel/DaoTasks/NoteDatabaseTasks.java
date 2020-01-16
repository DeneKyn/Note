package com.example.note.viewmodel.DaoTasks;

import android.os.AsyncTask;

import com.example.note.Note;
import com.example.note.NoteDao;

import java.util.List;

public final class NoteDatabaseTasks {

    private final NoteDao mNoteDao;

    public NoteDatabaseTasks(NoteDao noteDao) {
        mNoteDao = noteDao;
    }

    public void getAllNote(final List<Note> notes, final List<Note> currentNotes) {
        new GetAllNoteAsyncTask(mNoteDao, notes, currentNotes).execute();
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

    private static class GetAllNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao mNoteDao;
        private List<Note> mNotes;
        private List<Note> mCurrentNotes;

        private GetAllNoteAsyncTask(NoteDao noteDao, List<Note> notes, List<Note> currentNotes) {
            mNoteDao = noteDao;
            mNotes = notes;
            mCurrentNotes = currentNotes;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mNotes.addAll(mNoteDao.getAll());
            mCurrentNotes.addAll(mNotes);
            return null;
        }
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
