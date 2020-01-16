package com.example.note.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.note.activity.MainActivity;
import com.example.note.viewmodel.MainViewModel;
import com.example.note.Note;
import com.example.note.R;
import com.example.note.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagsFragment extends Fragment {

    ListView listView;
    MainViewModel viewModel;
    MainActivity activity;
    ArrayAdapter<String> adapter;
    List<String> tagsString;
    List<Tag> mTags;
    private Note mNote;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_tags, container, false);
        activity = (MainActivity) getActivity();
        listView = view.findViewById(R.id.listView);
        viewModel = activity.getMainViewModel();
        mTags = viewModel.getTags();
        tagsString = getTagsStringList(mTags);
        adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_multiple_choice, tagsString);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SparseBooleanArray sp =listView.getCheckedItemPositions();
                changeBoolValue(position, sp.get(position));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Tag tag = viewModel.getTags().get(position);
                viewModel.removeTag(tag);
                tagsString.remove(tag.mTag);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        setItemsChecked();

        setTextWatcher((EditText) view.findViewById(R.id.findTags));

        return view;
    }

    void changeBoolValue(int position, boolean value) {
        if (value) {
            mNote.addTag(mTags.get(position).mUniqueID);
        } else {
            mNote.removeTag(mTags.get(position).mUniqueID);
        }
    }

    public void updateListView() {
        tagsString.clear();
        mTags = viewModel.getTags();
        tagsString.addAll(getTagsStringList(mTags));
        adapter.notifyDataSetChanged();
    }

    public void setNote(Note note) {
        mNote = note;
    }

    private void setItemsChecked() {
        for (int i = 0; i < mTags.size(); ++i) {
            boolean isChecked = false;
            if (mNote.getTags().contains(mTags.get(i).mUniqueID)) {
                isChecked = true;
            }
            listView.setItemChecked(i, isChecked);
        }
    }

    private void setTextWatcher(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(before > count){
                    initList();
                    searchItem(s.toString());
                } else {
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initList() {
        mTags = viewModel.getTags();
        tagsString.clear();
        tagsString.addAll(getTagsStringList(mTags));
    }

    private void searchItem(String textToSearch){
        ArrayList<Tag> tmp = new ArrayList<>();
        for(Tag item : mTags){
            if(item.mTag.contains(textToSearch) == false){
                tmp.add(item);
            }
        }
        mTags.removeAll(tmp);
        tagsString.clear();
        tagsString.addAll(getTagsStringList(mTags));
        setItemsChecked();
        adapter.notifyDataSetChanged();
    }


    private List<String> getTagsStringList(List<Tag> tags) {
        List<String> tagsString = new ArrayList<>();
        for (Tag tag : tags) {
            tagsString.add(tag.mTag);
        }
        return tagsString;
    }
}
