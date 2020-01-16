package com.example.note;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "tags_table",
        indices = {
                @Index(value = "mTag", unique = true)
        })
public class Tag implements Comparable<Tag> {

    @NonNull
    @PrimaryKey
    public String mUniqueID;

    public String mTag;

    public boolean selected;

    public Tag(String tag) {
        mTag = tag;
        mUniqueID = UUID.randomUUID().toString();
    }


    @Override
    public int compareTo(Tag o) {
        return mTag.compareTo(o.mTag);
    }
}
