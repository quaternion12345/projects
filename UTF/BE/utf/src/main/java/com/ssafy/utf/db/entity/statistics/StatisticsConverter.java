package com.ssafy.utf.db.entity.statistics;

import com.google.gson.Gson;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;

public class StatisticsConverter implements AttributeConverter<ArrayList<Integer>, String> {

    @Override
    public String convertToDatabaseColumn(ArrayList<Integer> attribute) {
        return new Gson().toJson(attribute);
    }

    @Override
    public ArrayList<Integer> convertToEntityAttribute(String dbData) {
        int len = dbData.length();
        String[] temp = dbData.substring(1,len-1).split(",");

        ArrayList<Integer> convert = new ArrayList<>();
        for(String s: temp){
            convert.add(Integer.parseInt(s));
        }
        return convert;
    }
}
