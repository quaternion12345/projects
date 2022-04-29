package com.ssafy.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ssafy.model.dto.Member;
import com.ssafy.util.DBUtil;

public class MemberDaoImp implements MemberDAO {

	@Override
	public Member search(String id) throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs =null;
		try {
			con = DBUtil.getConnection();
			stmt = con.prepareStatement(" select * from member where id=? ");
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			if(rs.next()) {
				return new Member(
									rs.getString("id"), 
									rs.getString("password"), 
									rs.getString("name"), 
									rs.getString("email"), 
									rs.getString("phone"), 
									rs.getString("address"));
			}
		} finally {
			DBUtil.close(rs, stmt, con);
		}
		return null;
	}

	@Override
	public List<Member> searchAll() throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs =null;
		try {
			con = DBUtil.getConnection();
			stmt = con.prepareStatement(" select * from member ");
			
			rs = stmt.executeQuery();
			List<Member>  list = new ArrayList<Member>(20);
			while(rs.next()) {
				list.add( new Member(	rs.getString("id"), 
										rs.getString("name"), 
										rs.getString("email"), 
										rs.getString("phone")));
				
			}
			return list;
		} finally {
			DBUtil.close(rs, stmt, con);
		}
	}

	@Override
	public void add(Member member) throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection();
			String sql = " insert into member(id, password, name, email, phone, address) "
					   + " values(?,?,?,?,?,?) ";
			stmt = con.prepareStatement(sql);
			int idx = 1;
			stmt.setString(idx++, member.getId());
			stmt.setString(idx++, member.getPassword());
			stmt.setString(idx++, member.getName());
			stmt.setString(idx++, member.getEmail());
			stmt.setString(idx++, member.getPhone());
			stmt.setString(idx++, member.getAddress());
			stmt.executeUpdate();
		} finally {
			DBUtil.close(stmt, con);
		}
	}
	@Override
	public void update(Member member) throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection();
			String sql = " update member set password=?, name=?, email=?, phone=?, address=? "
					   + " where id=? ";
			stmt = con.prepareStatement(sql);
			int idx = 1;
			stmt.setString(idx++, member.getPassword());
			stmt.setString(idx++, member.getName());
			stmt.setString(idx++, member.getEmail());
			stmt.setString(idx++, member.getPhone());
			stmt.setString(idx++, member.getAddress());
			stmt.setString(idx++, member.getId());
			
			stmt.executeUpdate();
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	@Override
	public void remove(String id) throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection();
			stmt = con.prepareStatement(" delete from member where id=? ");
			stmt.setString(1, id);
			stmt.executeUpdate();
		} finally {
			DBUtil.close(stmt, con);
		}
	}
}
