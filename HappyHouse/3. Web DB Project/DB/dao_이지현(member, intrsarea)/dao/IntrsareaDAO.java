package com.ssafy.model.dao;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.model.dto.Intrsarea;

public interface IntrsareaDAO {
	public Intrsarea search(String member_id) throws SQLException;
	public List<Intrsarea> searchAll( ) throws SQLException;
	public void add(Intrsarea intrsarea)	 throws SQLException;
	public void update(Intrsarea intrsarea)throws SQLException;
	public void remove(int intno)    throws SQLException;
}
