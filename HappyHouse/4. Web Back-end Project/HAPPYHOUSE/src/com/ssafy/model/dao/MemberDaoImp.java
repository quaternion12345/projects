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
	public Member search(Connection con, String id) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = " select * from member where id=? ";
			stmt = con.prepareStatement(sql); 
			stmt.setString(1, id);
			System.out.println(stmt);
			rs = stmt.executeQuery();
			if(rs.next()) {
				return new Member (
							rs.getString("id"),
							rs.getString("password"),
							rs.getString("name"),
							rs.getString("email"),
							rs.getString("phone"));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			DBUtil.close(rs);
			DBUtil.close(stmt);
		}
		return null;
	}

	@Override
	public List<Member> searchAll(Connection con) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement("select * from member"); 
			
			rs = stmt.executeQuery();
			List<Member> list = new ArrayList<Member>(20);
			while(rs.next()) {
				list.add(new Member (
					rs.getString("id"),
					rs.getString("password"),
					rs.getString("name"),
					rs.getString("email"),
					rs.getString("phone")));
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			DBUtil.close(stmt);
		}
		return null;
	}

	@Override
	public void add(Connection con, Member member) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement stmt = null;
		try {
			String sql = "insert into member(id, password, name, email, phone)"
					+ "values(?, ?, ?, ?, ?)" ;	
			
			stmt = con.prepareStatement(sql);
			int idx = 1;
			stmt.setString(idx++, member.getId());
			stmt.setString(idx++, member.getPassword());
			stmt.setString(idx++, member.getName());
			stmt.setString(idx++, member.getEmail());
			stmt.setString(idx++, member.getPhone());
			stmt.executeUpdate();
			
		} finally {
			DBUtil.close(stmt);
		}
	}

	@Override
	public void update(Connection con, Member member) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement stmt = null;
		try {
			String sql = " update member set password=?, name=?, email=?, phone=? " + " where id = ?";
			stmt = con.prepareStatement(sql);
			
			int idx = 1;
			stmt.setString(idx++, member.getPassword());
			stmt.setString(idx++, member.getName());
			stmt.setString(idx++, member.getEmail());
			stmt.setString(idx++, member.getPhone());
			stmt.setString(idx++, member.getId());
			
			stmt.executeUpdate();
		} finally {
			DBUtil.close(stmt);
		}
	}

	@Override
	public void remove(Connection con, String id) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("delete from member where id = ?");
			stmt.setString(1, id);
			stmt.executeUpdate();
		} finally {
			DBUtil.close(stmt);
		}
	}

}
