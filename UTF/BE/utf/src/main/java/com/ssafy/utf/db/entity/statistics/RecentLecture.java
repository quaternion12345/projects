package com.ssafy.utf.db.entity.statistics;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecentLecture implements Comparable<RecentLecture>{

    private String title;
    private String startTime;
    private long lectureRoomId;
    private long lectureId;

    @Override
    public int compareTo(RecentLecture o) {
        return o.getStartTime().compareTo(this.startTime);
    }
}
