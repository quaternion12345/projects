package com.ssafy.happyhouse.service;

import java.util.List;

import com.ssafy.happyhouse.model.dto.AptDeal;

public interface AptDealService {
	List<AptDeal> init() throws Exception;
	
	List<AptDeal> getAptInDong(String dong, int pageNo) throws Exception;
	
	List<AptDeal> getAptByName(String aptName, int pageNo) throws Exception;
}
