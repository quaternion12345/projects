package com.ssafy.vue.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.vue.dto.Qna;
@Mapper
public interface QnaMapper {
	public List<Qna> selectQna();
	public Qna selectQnaByNo(int articleno);
	public int insertQna(Qna qna);
	public int updateQna(Qna qna);
	public int deleteQna(int articleno);
}