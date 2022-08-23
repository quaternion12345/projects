package com.ssafy.utf.db.entity.statistics;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long statistics_id;
    private int videoId;
    private String time;
    @Convert(converter = StatisticsConverter.class)
    private ArrayList<Integer> notUnderstand;
    @Convert(converter = StatisticsConverter.class)
    private ArrayList<Integer> neutral;
    @Convert(converter = StatisticsConverter.class)
    private ArrayList<Integer> understand;
    @Convert(converter = StatisticsConverter.class)
    private ArrayList<Integer> notFocus;
    @Convert(converter = StatisticsConverter.class)
    private ArrayList<Integer> focus;

}
