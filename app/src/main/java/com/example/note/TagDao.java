package com.example.note;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TagDao {

    @Query("SELECT * FROM tags_table ORDER BY mTag")
    LiveData<List<Tag>> getAll();

    @Query("SELECT * FROM tags_table WHERE mTag = :tag")
    Tag getByName(String tag);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Tag tag);

    @Delete
    void delete(Tag Tag);

}
