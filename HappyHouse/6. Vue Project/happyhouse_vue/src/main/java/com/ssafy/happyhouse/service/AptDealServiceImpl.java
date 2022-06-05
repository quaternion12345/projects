package com.ssafy.happyhouse.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.happyhouse.model.dao.AptDealDao;
import com.ssafy.happyhouse.model.dto.AptDeal;
import com.ssafy.happyhouse.model.dto.PageBean;

@Service
public class AptDealServiceImpl implements AptDealService {

	@Autowired
	private AptDealDao aptDealDao;
	
	@Override
	public List<AptDeal> init() throws Exception {
		return aptDealDao.searchAll(new PageBean("init", "init", 1));
	}
	@Override
	public List<AptDeal> getAptInDong(String dong, int pageNo) throws Exception {
		return aptDealDao.searchAll(new PageBean("dong", dong, 1));
	}
	@Override
	public List<AptDeal> getAptByName(String aptName, int pageNo) throws Exception {
		return aptDealDao.searchAll(new PageBean("apt", aptName, 1));
	}
	
}
