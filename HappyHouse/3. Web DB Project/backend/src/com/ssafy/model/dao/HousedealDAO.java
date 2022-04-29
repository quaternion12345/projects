package com.ssafy.model.dao;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.model.dto.Housedeal;

public interface HousedealDAO {
	public Housedeal search(int no) throws SQLException;
	public List<Housedeal> searchAll( ) throws SQLException;
	public void add(Housedeal housedeal)	 throws SQLException;
	public void update(Housedeal housedeal)throws SQLException;
	public void remove(int no)    throws SQLException;
}












