package com.ssafy.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.ssafy.model.dto.Member;

public interface MemberDAO {
	public Member search(Connection con, String id) throws SQLException;
	public List<Member> searchAll(Connection con) throws SQLException;
	public void add(Connection con, Member member)	 throws SQLException;
	public void update(Connection con, Member member)throws SQLException;
	public void remove(Connection con, String id)    throws SQLException;
}












