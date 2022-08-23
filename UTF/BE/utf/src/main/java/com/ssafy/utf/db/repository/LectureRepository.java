package com.ssafy.utf.db.repository;

import com.ssafy.utf.db.entity.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    ArrayList<Lecture> findByLectureRoomIdOrderByStartTimeDesc(long lectureRoomId);


    @Transactional
    void deleteByLectureRoomId(long lectureRoomId);

    Lecture findByLectureId(long lectureId);

    @Query(value = "SELECT MAX(lecture_id) FROM lecture", nativeQuery = true)
    int getRecordLectureId();
}
