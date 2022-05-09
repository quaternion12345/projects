package com.ssafy.happyhouse.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ssafy.happyhouse.model.dto.Member;
import com.ssafy.happyhouse.util.DBUtil;

public class MemberDaoImp implements MemberDao {
	private DBUtil dbUtil=DBUtil.getInstance();
	@Override
	public void register(Member member) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dbUtil.getConnection();
			StringBuilder registerMember = new StringBuilder();
			registerMember.append("insert into member \n");
			registerMember.append("values (?, ?, ?, ?, ?, ?)"); //now()
			pstmt = conn.prepareStatement(registerMember.toString());
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getName());
			pstmt.setString(4, member.getEmail());
			pstmt.setString(5, member.getAddress());
			pstmt.setString(6, member.getPhone());
			pstmt.executeUpdate();
			System.out.println("sql>>>>"+pstmt);
			
		} finally {
			dbUtil.close(pstmt, conn);
		}

	}

	@Override
	public void delete(String id) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dbUtil.getConnection();
			String sql="delete from member where id = ? ";
			pstmt = conn.prepareStatement(sql);
			System.out.println("sql>>>>"+pstmt);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
		} finally {
			dbUtil.close(pstmt, conn);
		}
	}
	
	@Override
	public void modify(Member member) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dbUtil.getConnection();
			StringBuilder registerMember = new StringBuilder();
			registerMember.append("update member set password = ?, \n");
			registerMember.append("email = ?, \n");
			registerMember.append("address = ?, \n");
			registerMember.append("phone = ? \n");
			registerMember.append("where id = ?");
			pstmt = conn.prepareStatement(registerMember.toString());
			System.out.println("sql>>>>"+pstmt);
			int idx=1;
			pstmt.setString(idx++, member.getPassword());
			pstmt.setString(idx++, member.getEmail());
			pstmt.setString(idx++, member.getAddress());
			pstmt.setString(idx++, member.getPhone());
			pstmt.setString(idx++, member.getId());
			pstmt.executeUpdate();
		} finally {
			dbUtil.close(pstmt, conn);
		}
	}

	
	
	
	@Override
	public Member inquire(String id) throws SQLException {
		Member member = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dbUtil.getConnection();
			String sql="select * from member where id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				member = new Member();
				member.setId(rs.getString("id"));
				member.setName(rs.getString("name"));
				member.setAddress(rs.getString("address"));
				member.setEmail(rs.getString("email"));
				member.setPhone(rs.getString("phone"));
				member.setPassword(rs.getString("password"));
				System.out.println(member.toString());
				return member;
			}
		} finally {
			dbUtil.close(rs, pstmt, conn);
		}
		return null;
	}

	@Override
	public Member login(String id, String password) throws SQLException {
		Member member = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dbUtil.getConnection();
			StringBuilder loginMember = new StringBuilder();
			loginMember.append("select id, name \n");
			loginMember.append("from member \n");
			loginMember.append("where id = ? and password = ? \n");
			pstmt = conn.prepareStatement(loginMember.toString());
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				member = new Member();
				member.setId(rs.getString("id"));
				member.setName(rs.getString("name"));
				return member;
			}
		} finally {
			dbUtil.close(rs, pstmt, conn);
		}
		return null;
	}





}
