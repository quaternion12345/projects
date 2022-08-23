package com.ssafy.utf.api.service;

import com.ssafy.utf.db.entity.lecture.Lecture;

import java.util.ArrayList;

public interface LectureService {
    ArrayList<Lecture> getLectures(long lectureRoomId);

    Lecture getLecture(long lectureId);

    int getRecordLectureId();
}
