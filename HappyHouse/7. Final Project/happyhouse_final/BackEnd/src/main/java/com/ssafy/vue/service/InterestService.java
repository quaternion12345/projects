package com.ssafy.vue.service;

import java.util.List;

import com.ssafy.vue.dto.Interest;

public interface InterestService {
	public List<Interest> retrieveInterest(String member_id);
//	public Board detailBoard(int articleno);
	public boolean writeInterest(Interest interest);
	public boolean updateInterest(Interest interest);
	public boolean deleteInterest(int no);
}
