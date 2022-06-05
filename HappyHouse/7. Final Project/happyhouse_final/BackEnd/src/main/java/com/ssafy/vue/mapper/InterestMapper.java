package com.ssafy.vue.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.vue.dto.Interest;
@Mapper
public interface InterestMapper {
	public List<Interest> selectInterest(String member_id); // seletAll
//	public Interest selectInterestByNo(int no);
	public int insertInterest(Interest interest);
	public int updateInterest(Interest interest);
	public int deleteInterest(int no);
}