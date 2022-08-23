package com.ssafy.utf.db.entity.statistics;

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
public class Emotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long emotionId;
    private String name;
    private String emotion;
    private Double score;
}
