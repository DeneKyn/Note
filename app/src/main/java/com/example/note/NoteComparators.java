package com.example.note;

import java.util.Comparator;

public final class NoteComparators {

    public final static Comparator<Note> DATE_COMPARATOR = new Comparator<Note>() {
        @Override
        public int compare(Note o1, Note o2) {
            return o1.mDate.compareTo(o2.mDate);
        }
    };

    public final static Comparator<Note> TITLE_COMPARATOR = new Comparator<Note>() {
        @Override
        public int compare(Note o1, Note o2) {
            return o1.mTitle.compareTo(o2.mTitle);
        }
    };

}
