package com.ssafy.utf.api.service;

import com.ssafy.utf.db.entity.lecture.Lecture;
import com.ssafy.utf.db.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LectureServiceImpl implements LectureService {

    @Autowired
    LectureRepository lectureRepository;

    @Override
    public ArrayList<Lecture> getLectures(long lectureRoomId) {
        return lectureRepository.findByLectureRoomIdOrderByStartTimeDesc(lectureRoomId);
    }

    @Override
    public Lecture getLecture(long lectureId) {
        return lectureRepository.findByLectureId(lectureId);
    }

    @Override
    public int getRecordLectureId() {
        return lectureRepository.getRecordLectureId();
    }
}
