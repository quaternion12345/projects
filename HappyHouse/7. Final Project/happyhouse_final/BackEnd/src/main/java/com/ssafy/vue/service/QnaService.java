package com.ssafy.vue.service;

import java.util.List;

import com.ssafy.vue.dto.Qna;

public interface QnaService {
	public List<Qna> retrieveQna();
	public Qna detailQna(int articleno);
	public boolean writeQna(Qna qna);
	public boolean updateQna(Qna qna);
	public boolean deleteQna(int articleno);
}
