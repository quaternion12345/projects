package com.ssafy.utf.api.service;

import com.ssafy.utf.db.entity.statistics.Emotion;
import com.ssafy.utf.db.entity.statistics.Statistics;

import java.util.ArrayList;

public interface StatisticsService {
    int getUnderstanding(Emotion e);

    void insertLecture(Statistics st);

    ArrayList<Integer> getRecordedUnderstand(long lectureId);
    ArrayList<Integer> getRecordedNeutral(long lectureId);
    ArrayList<Integer> getRecordedNotUnderstand(long lectureId);

    ArrayList<Integer> getRecordedFocus(long lectureId);

    ArrayList<Integer> getRecordedNotFocus(long lectureId);
}
