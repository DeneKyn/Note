package com.example.note;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagsConverter {

    private static final String SEPARATOR = "~/~";

    @TypeConverter
    public String formTags(List<String> tags) {
        return tags.stream().collect(Collectors.joining(SEPARATOR));
    }

    @TypeConverter
    public ArrayList<String> toTags(String data) {
        ArrayList<String> tags = new ArrayList<>(Arrays.asList(data.split(SEPARATOR)));
        if (tags.size() == 1 && tags.get(0).equals("")) {
            return new ArrayList<>();
        }
        return tags;
    }

}
