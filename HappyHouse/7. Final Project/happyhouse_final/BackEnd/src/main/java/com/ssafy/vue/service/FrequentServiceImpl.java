package com.ssafy.vue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.vue.dto.Frequent;
import com.ssafy.vue.mapper.FrequentMapper;

@Service
public class FrequentServiceImpl implements FrequentService {
	
    @Autowired
	private FrequentMapper frequentMapper;

    @Override
	public List<Frequent> retrieveFrequent(String member_id) {
		return frequentMapper.selectFrequent(member_id);
	}
    
  	@Override
	public boolean writeFrequent(Frequent frequent) {
		return frequentMapper.insertFrequent(frequent) == 1;
	}

//	@Override
//	public Board detailBoard(int articleno) {
//		return frequentMapper.selectBoardByNo(articleno);
//	}

	@Override
	@Transactional
	public boolean updateFrequent(Frequent frequent) {
		return frequentMapper.updateFrequent(frequent) == 1;
	}

	@Override
	@Transactional
	public boolean deleteFrequent(int no) {
		return frequentMapper.deleteFrequent(no) == 1;
	}
}