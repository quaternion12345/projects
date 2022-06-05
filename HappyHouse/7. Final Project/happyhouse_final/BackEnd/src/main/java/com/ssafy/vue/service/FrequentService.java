package com.ssafy.vue.service;

import java.util.List;

import com.ssafy.vue.dto.Frequent;

public interface FrequentService {
	public List<Frequent> retrieveFrequent(String member_id);
//	public Frequent detailBoard(int articleno);
	public boolean writeFrequent(Frequent frequent);
	public boolean updateFrequent(Frequent frequent);
	public boolean deleteFrequent(int no);
}
