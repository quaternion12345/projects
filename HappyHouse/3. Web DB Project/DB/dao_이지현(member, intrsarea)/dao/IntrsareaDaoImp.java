package com.ssafy.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ssafy.model.dto.Intrsarea;
import com.ssafy.model.dto.Member;
import com.ssafy.util.DBUtil;

public class IntrsareaDaoImp implements IntrsareaDAO {

	@Override
	public Intrsarea search(String member_id) throws SQLException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			stmt = con.prepareStatement("select * from intrsarea where member_id = ?"); 
			stmt.setString(1, member_id);
			rs = stmt.executeQuery();
			while(rs.next()) {
				return new Intrsarea (
							rs.getInt("intno"),
							rs.getString("member_id"),
							rs.getString("dongcode_dongCode"));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			DBUtil.close(rs, stmt, con);
		}
		return null;
	}

	@Override
	public List<Intrsarea> searchAll() throws SQLException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			stmt = con.prepareStatement("select * from intrsarea"); 
			
			rs = stmt.executeQuery();
			List<Intrsarea> list = new ArrayList<Intrsarea>(20);
			while(rs.next()) {
				list.add(new Intrsarea (
						rs.getInt("intno"),
						rs.getString("member_id"),
						rs.getString("dongcode_dongCode")));
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			DBUtil.close(stmt, con);
		}
		return null;
	}

	@Override
	public void add(Intrsarea intrsarea) throws SQLException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection();
			String sql = "insert into intrsarea(intno, member_id, dongcode_dongCode)"
					+ "values(?, ?, ?)" ;	
			
			stmt = con.prepareStatement(sql);
			int idx = 1;
			stmt.setInt(idx++, intrsarea.getIntno());
			stmt.setString(idx++, intrsarea.getMember_id());
			stmt.setString(idx++, intrsarea.getDongcode_DongCode());
			stmt.executeUpdate();
			
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	@Override
	public void update(Intrsarea intrsarea) throws SQLException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection();
			String sql = " update intrsarea set intno=?, member_id=?, dongcode_dongCode=? where intno = ?";
			stmt = con.prepareStatement(sql);
			
			int idx = 1;
			stmt.setInt(idx++, intrsarea.getIntno());
			stmt.setString(idx++, intrsarea.getMember_id());
			stmt.setString(idx++, intrsarea.getDongcode_DongCode());
			stmt.setInt(idx++, intrsarea.getIntno());
			
			stmt.executeUpdate();
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	@Override
	public void remove(int intno) throws SQLException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection();
			stmt = con.prepareStatement("delete from intrsarea where intno = ?");
			stmt.setInt(1, intno);
			stmt.executeUpdate();
		} finally {
			DBUtil.close(stmt, con);
		}
	}

}
