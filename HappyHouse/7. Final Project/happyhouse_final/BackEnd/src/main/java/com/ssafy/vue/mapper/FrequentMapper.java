package com.ssafy.vue.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.vue.dto.Frequent;
@Mapper
public interface FrequentMapper {
	public List<Frequent> selectFrequent(String member_id); // selectAll
//	public Frequent selectBoardByNo(int articleno);
	public int insertFrequent(Frequent frequent);
	public int updateFrequent(Frequent frequent);
	public int deleteFrequent(int no);
}