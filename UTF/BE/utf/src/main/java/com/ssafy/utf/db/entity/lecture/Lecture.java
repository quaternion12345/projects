package com.ssafy.utf.db.entity.lecture;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long lectureId;
    private long lectureRoomId;
    private String videoUrl;
    private String startTime;
    private String endTime;
    private String chatRecord;

}
