package com.example.note;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class, Tag.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract NoteDao noteDao();

    public abstract TagDao tagDao();

}
