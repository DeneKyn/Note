package com.example.note.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note.viewmodel.MainViewModel;
import com.example.note.Note;
import com.example.note.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> mNotes;
    private OnNoteListener mOnNoteListener;
    private MainViewModel mViewModel;

    public NoteAdapter(List<Note> notes, OnNoteListener onNoteListener, MainViewModel viewModel) {
        mNotes = notes;
        mOnNoteListener = onNoteListener;
        mViewModel = viewModel;
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
                    String tag = mViewModel.getTag(tagsID.get(0));
                    tagsString.append(tag).append(", ");
                }
                tagsString.append("and ").append(tagsID.size() - 3).append(" more");
            } else {
                for (String id : tagsID) {
                    String tag = mViewModel.getTag(id);
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
        holder.data.setText(note.getDate());
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

}
