package com.ssafy.utf.api.service;

import com.ssafy.utf.api.request.LectureRoomRegistReq;
import com.ssafy.utf.api.request.RecordingReq;
import com.ssafy.utf.db.entity.lecture.Lecture;
import com.ssafy.utf.db.entity.lecture.LectureRoom;
import com.ssafy.utf.db.entity.statistics.RecentLecture;

import java.util.ArrayList;
import java.util.List;

public interface LectureRoomService {
    LectureRoom registLectureRoom(LectureRoomRegistReq lectureRoomRegistReq) throws InterruptedException;

    List<LectureRoom> getLectureRoomList(long userId);

    void deleteLectureRoom(long lectureRoomId);

    List<RecentLecture> getRecentLectures(long userId);


    Lecture registRecordVideo(RecordingReq recordingReq) throws Exception;

    long getLectureRoomId(String lectureRoomCode);
}
