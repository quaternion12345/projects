package com.ssafy.happyhouse.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.happyhouse.model.dto.AptDeal;
import com.ssafy.happyhouse.model.dto.PageBean;

@Mapper
public interface AptDealDao {
	public AptDeal search(int no);
	public List<AptDeal> searchAll(PageBean bean);
	public void insert(AptDeal aptdeal);
	public void update(AptDeal aptdeal);
	public void remove(int no);
	public int count(PageBean bean);
}
