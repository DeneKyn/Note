package com.example.note.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note.NoteComparators;
import com.example.note.Tag;
import com.example.note.viewmodel.MainViewModel;
import com.example.note.Note;
import com.example.note.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>/* implements Filterable*/ {

    public List<Note> mNotes;
    private List<Note> mAllNotes;
    private List<String> mSelectedTagId;
    private List<Tag> mTags = new ArrayList<Tag>();
    private OnNoteListener mOnNoteListener;


    public NoteAdapter(OnNoteListener onNoteListener) {
        mNotes = new ArrayList<Note>();
        mAllNotes = new ArrayList<Note>();
        mSelectedTagId = new ArrayList<String>();
        mOnNoteListener = onNoteListener;
    }

    public void setNotes(List<Note> notes, int sortStage) {
        this.mNotes = notes;
        mAllNotes = new ArrayList<Note>(notes);
        notifyDataSetChanged();
        applySort(sortStage);

    }

    public void applySort(int sortStage) {
        switch (sortStage) {
            case 1:
                sortByTitle(false);
                break;
            case 2:
                sortByTitle(true);
                break;
            case 3:
                sortByDate(false);
                break;
            case 4:
                sortByDate(true);
                break;
            default:

        }
    }

    public void sortByTitle(boolean isRevers)
    {
        mNotes.sort(NoteComparators.TITLE_COMPARATOR);
        if(isRevers){
            Collections.reverse(mNotes);
        }

        notifyDataSetChanged();
    }

    public void sortByDate(boolean isRevers)
    {
        mNotes.sort(NoteComparators.DATE_COMPARATOR);
        if(isRevers){
            Collections.reverse(mNotes);
        }
        notifyDataSetChanged();
    }

    public void setTags(List<Tag> tags){
        this.mTags = tags;
        notifyDataSetChanged();
    }

    public String getTag(String id) {
        for (Tag tag : mTags) {
            if (tag.mUniqueID.equals(id)) {
                return tag.mTag;
            }
        }
        return null;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note, parent, false);
        NoteViewHolder pvh = new NoteViewHolder(view, mOnNoteListener);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = mNotes.get(position);
        StringBuilder tagsString = new StringBuilder();
        List<String> tagsID = note.getTags();

        if (tagsID.size() > 0) {
            tagsString.append("tags: ");
            if (tagsID.size() > 3) {
                for (int i = 0; i < 3; ++i) {
                    String tag = getTag(tagsID.get(0));
                    tagsString.append(tag).append(", ");
                }
                tagsString.append("and ").append(tagsID.size() - 3).append(" more");
            } else {
                for (String id : tagsID) {
                    String tag = getTag(id);
                    tagsString.append(tag).append(", ");
                }
                if (tagsString.length() > 0) {
                    tagsString.delete(tagsString.length() - 2, tagsString.length());
                }
            }
        }


        holder.title.setText(note.getTitle());
        holder.body.setText(note.getBody());
        holder.tags.setText(tagsString);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        holder.data.setText(sdf.format(note.getDate()));
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView title;
        TextView body;
        TextView tags;
        TextView data;
        OnNoteListener mOnNoteListener;

        NoteViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            cv = itemView.findViewById(R.id.note);
            title = itemView.findViewById(R.id.titleNote);
            body = itemView.findViewById(R.id.bodyNote);
            tags = itemView.findViewById(R.id.tagsNote);
            data = itemView.findViewById(R.id.dateNote);
            mOnNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

    public void kek(int sortStage){
        mSelectedTagId.clear();
        for (final Tag tag : mTags) {
            if (tag.selected) {
                mSelectedTagId.add(tag.mUniqueID);
            }
        }

        if (mSelectedTagId.size() == 0){
            setNotes(mAllNotes, sortStage);
        }
        else{
            List<Note> filteredNoed = new ArrayList<>();
            for (final Note note : mAllNotes) {
                List<String> tagsId = new ArrayList<>(note.getTags());
                tagsId.retainAll(mSelectedTagId);
                if (tagsId.size() > 0) {
                    filteredNoed.add(note);
                }
            }
            mNotes = filteredNoed;
            notifyDataSetChanged();
            applySort(sortStage);
        }
    }



    public void lol(int sortStage){
        mSelectedTagId.clear();
        for (final Tag tag : mTags) {
            tag.selected = false;
        }
        setNotes(mAllNotes, sortStage);
    }


}
