package com.example.note;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Entity(tableName = "note_table")
public class Note {

    @NonNull
    @PrimaryKey
    public String mUniqueID;

    public String mTitle;

    public String mBody;

    @TypeConverters({TagsConverter.class})
    ArrayList<String> mTags;

    String mDate;

    public Note() {
        mTitle = "";
        mBody = "";
        mTags = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        mDate = sdf.format(Calendar.getInstance().getTime());
        mUniqueID = UUID.randomUUID().toString();
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public String getBody() {
        return mBody;
    }

    public String getDate() {
        return mDate;
    }

    public void addTag(String tag) {
        mTags.add(tag);
    }

    public void removeTag(String tag) {
        mTags.remove(tag);
    }

    public List<String> getTags() {
        return mTags;
    }

}
