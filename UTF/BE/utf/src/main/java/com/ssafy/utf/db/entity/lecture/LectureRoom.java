package com.ssafy.utf.db.entity.lecture;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
public class LectureRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long lectureRoomId;
    long userId;
    String title;
    int subject;
    String lectureRoomCode;
    boolean isActive;
}
