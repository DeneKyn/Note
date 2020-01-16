package com.example.note.viewmodel.DaoTasks;

import android.os.AsyncTask;

import com.example.note.Note;
import com.example.note.NoteDao;
import com.example.note.Tag;
import com.example.note.TagDao;

import java.util.List;

public class TagDatabaseTasks {

    private final TagDao mTagDao;

    public TagDatabaseTasks(final TagDao tagDao) {
        mTagDao = tagDao;
    }

    public void getAllTag(final List<Tag> tags) {
        new GetAllTagAsyncTask(mTagDao ,tags).execute();
    }

    public void insertTag(final Tag tag) {
        new InsertTagAsyncTask(mTagDao).execute(tag);
    }

    public void deleteTag(final Tag tag) {
        new TagDatabaseTasks.DeleteTagAsyncTask(mTagDao).execute(tag);
    }

    private static class GetAllTagAsyncTask extends AsyncTask<Tag, Void, Void> {
        private TagDao mTagDao;
        private List<Tag> mTags;

        private GetAllTagAsyncTask(TagDao tagDao, List<Tag> tag) {
            mTagDao = tagDao;
            mTags = tag;
        }

        @Override
        protected Void doInBackground(Tag... tags) {

            mTags.addAll(mTagDao.getAll());
            return null;
        }
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
