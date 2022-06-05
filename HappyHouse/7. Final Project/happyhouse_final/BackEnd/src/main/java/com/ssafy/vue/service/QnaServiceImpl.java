package com.ssafy.vue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.vue.dto.Qna;
import com.ssafy.vue.mapper.QnaMapper;

@Service
public class QnaServiceImpl implements QnaService {
	
    @Autowired
	private QnaMapper qnaMapper;

    @Override
	public List<Qna> retrieveQna() {
		return qnaMapper.selectQna();
	}
    
  	@Override
	public boolean writeQna(Qna qna) {
		return qnaMapper.insertQna(qna) == 1;
	}

	@Override
	public Qna detailQna(int articleno) {
		return qnaMapper.selectQnaByNo(articleno);
	}

	@Override
	@Transactional
	public boolean updateQna(Qna qna) {
		return qnaMapper.updateQna(qna) == 1;
	}

	@Override
	@Transactional
	public boolean deleteQna(int articleno) {
		return qnaMapper.deleteQna(articleno) == 1;
	}
}