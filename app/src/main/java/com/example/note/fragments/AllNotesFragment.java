package com.example.note.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.note.activity.MainActivity;
import com.example.note.viewmodel.MainViewModel;
import com.example.note.adapter.NoteAdapter;
import com.example.note.R;


public class AllNotesFragment extends Fragment {

    private NoteAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_all_notes, container, false);

        MainActivity activity = (MainActivity) getActivity();
        MainViewModel viewModel = activity.getMainViewModel();

        mRecyclerView = view.findViewById(R.id.recyclerView);
        configureRecyclerView(mRecyclerView, activity);

        mAdapter = new NoteAdapter(viewModel.getNotes(), activity, viewModel);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public void updateRecyclerView() {
        mAdapter.notifyDataSetChanged();
    }

    private void configureRecyclerView(RecyclerView recyclerView, Activity activity) {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(activity, 3));
        }
    }

}
