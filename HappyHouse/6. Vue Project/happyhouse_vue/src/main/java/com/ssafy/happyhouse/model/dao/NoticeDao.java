package com.ssafy.happyhouse.model.dao;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.happyhouse.model.dto.ListParameterDto;
import com.ssafy.happyhouse.model.dto.Notice;

public interface NoticeDao {
	public Notice search(String name) throws SQLException;
	public int getTotalCount(ListParameterDto listParameterDto) throws SQLException;
	List<Notice> searchAll(ListParameterDto listParameterDto) throws SQLException;
	void add(Notice notice) throws SQLException;
	void update(Notice notice) throws SQLException;
	public void remove(int id) throws SQLException;
}
