package com.example.note.viewmodel.DaoTasks;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.note.AppDatabase;
import com.example.note.Note;
import com.example.note.NoteDao;
import com.example.note.Tag;
import com.example.note.TagDao;

import java.util.List;

public class TagDatabaseTasks {

    private final TagDao mTagDao;
    private LiveData<List<Tag>> allTags;

    public TagDatabaseTasks(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        mTagDao = database.tagDao();
        allTags = mTagDao.getAll();
    }

    public LiveData<List<Tag>> getAll() {
        return allTags;
    }


    public void insertTag(final Tag tag) {
        new InsertTagAsyncTask(mTagDao).execute(tag);
    }

    public void deleteTag(final Tag tag) {
        new TagDatabaseTasks.DeleteTagAsyncTask(mTagDao).execute(tag);
    }



    private static class InsertTagAsyncTask extends AsyncTask<Tag, Void, Void> {
        private TagDao mTagDao;

        private InsertTagAsyncTask(TagDao tagDao) {
            this.mTagDao = tagDao;
        }

        @Override
        protected Void doInBackground(Tag... tags) {
            mTagDao.insert(tags[0]);
            return null;
        }
    }

    private static class DeleteTagAsyncTask extends AsyncTask<Tag, Void, Void> {
        private TagDao mTagDao;

        private DeleteTagAsyncTask(TagDao tagDAO) {
            this.mTagDao = tagDAO;
        }

        @Override
        protected Void doInBackground(Tag... tags) {
            mTagDao.delete(tags[0]);
            return null;
        }
    }


}
