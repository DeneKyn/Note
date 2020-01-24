package com.example.note;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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



        @TypeConverter
        public static Date toDate(Long dateLong){
            return dateLong == null ? null: new Date(dateLong);
        }

        @TypeConverter
        public static Long fromDate(Date date){
            return date == null ? null : date.getTime();
        }


}
