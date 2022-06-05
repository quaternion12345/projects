package com.ssafy.vue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.vue.dto.Interest;
import com.ssafy.vue.mapper.InterestMapper;

@Service
public class InterestServiceImpl implements InterestService {
	
    @Autowired
	private InterestMapper interestMapper;

    @Override
	public List<Interest> retrieveInterest(String member_id) {
		return interestMapper.selectInterest(member_id);
	}
    
  	@Override
	public boolean writeInterest(Interest interest) {
		return interestMapper.insertInterest(interest) == 1;
	}

//	@Override
//	public Interest detailBoard(int articleno) {
//		return interestMapper.selectBoardByNo(articleno);
//	}

	@Override
	@Transactional
	public boolean updateInterest(Interest interest) {
		return interestMapper.updateInterest(interest) == 1;
	}

	@Override
	@Transactional
	public boolean deleteInterest(int no) {
		return interestMapper.deleteInterest(no) == 1;
	}
}