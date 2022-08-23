package com.ssafy.utf.db.repository;

import com.ssafy.utf.db.entity.lecture.LectureRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface LectureRoomRepository extends JpaRepository<LectureRoom, Integer> {
    LectureRoom findByLectureRoomCode(String lectureRoomCode);

    List<LectureRoom> findAllByUserId(long userId);

    @Modifying
    @Transactional
    void deleteByLectureRoomId(long lectureRoomId);

    @Query(value = "SELECT lecture_room_id FROM lecture_room WHERE lecture_room_code = :lectureRoomCode", nativeQuery = true)
    long findLectureRoomIdByLectureRoomCode(@Param("lectureRoomCode") String lectureRoomCode);
}
