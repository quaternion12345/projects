package com.ssafy.utf.db.repository;

import com.ssafy.utf.db.entity.statistics.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, String> {
    @Query(value = "SELECT understand FROM statistics WHERE video_id = :lectureId", nativeQuery = true)
    String findUnderstandByLectureId(long lectureId);

    @Query(value = "SELECT neutral FROM statistics WHERE video_id = :lectureId", nativeQuery = true)
    String findNeutralByLectureId(long lectureId);

    @Query(value = "SELECT not_understand FROM statistics WHERE video_id = :lectureId", nativeQuery = true)
    String findNotUnderstandByLectureId(long lectureId);

    @Query(value = "SELECT focus FROM statistics WHERE video_id = :lectureId", nativeQuery = true)
    String findFocusByLectureId(long lectureId);

    @Query(value = "SELECT not_focus FROM statistics WHERE video_id = :lectureId", nativeQuery = true)
    String findNotFocusByLectureId(long lectureId);
}
