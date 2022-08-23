package com.ssafy.utf.api.controller;

import com.ssafy.utf.api.service.LectureRoomService;
import com.ssafy.utf.api.service.LectureService;
import com.ssafy.utf.db.entity.lecture.Lecture;
import com.ssafy.utf.db.entity.statistics.RecentLecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/lecture")
@CrossOrigin(origins = "*")
public class LectureController {

    @Autowired
    LectureRoomService lectureRoomService;
    @Autowired
    LectureService lectureService;

    @GetMapping("/recent/{userId}")
    public ResponseEntity<List<RecentLecture>> getRecentlyLectures(@PathVariable long userId){ //최근 5개강의 조회
        List<RecentLecture> recentLecture = lectureRoomService.getRecentLectures(userId);
        return new ResponseEntity<List<RecentLecture>>(recentLecture, HttpStatus.OK);
    }

    @GetMapping("/{lectureRoomId}")
    public ResponseEntity<ArrayList<Lecture>> getAllLectures(@PathVariable long lectureRoomId){ //강의실 들어왔을 떄 모든 강의 조회
        ArrayList<Lecture> lectures = lectureService.getLectures(lectureRoomId);
        return new ResponseEntity<ArrayList<Lecture>>(lectures,HttpStatus.OK);
    }

    @GetMapping("/one/{lectureId}")
    public ResponseEntity<Lecture> getOneLecture(@PathVariable long lectureId){ //강의 한개 조회
        Lecture lecture = lectureService.getLecture(lectureId);
        return new ResponseEntity<Lecture>(lecture, HttpStatus.OK);
    }

    @GetMapping("/recentRecord")
    public ResponseEntity<Integer> getRecordLectureId(){
        int id = lectureService.getRecordLectureId();
        return new ResponseEntity<Integer>(id, HttpStatus.OK);
    }
}
