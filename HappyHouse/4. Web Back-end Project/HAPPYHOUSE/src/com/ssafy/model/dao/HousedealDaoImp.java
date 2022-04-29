package com.ssafy.model.dao;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ssafy.model.dto.Housedeal;
import com.ssafy.util.DBUtil;

public class HousedealDaoImp implements HousedealDAO {

	@Override
	public Housedeal search(int no) throws SQLException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			stmt = con.prepareStatement(" select * from housedeal where no=? ");
			stmt.setInt(1, no);
			rs = stmt.executeQuery();
			if(rs.next()) {
				return new Housedeal(
									rs.getInt("no"), 
									rs.getInt("aptCode"), 
									rs.getString("dealAmount"), 
									rs.getString("dealYear"), 
									rs.getString("dealMonth"), 
									rs.getString("dealDay"),
									rs.getString("area"),
									rs.getString("floor"),
									rs.getString("type"),
									rs.getString("rentMoney")
									);
			}
		} finally {
			DBUtil.close(rs);
			DBUtil.close(stmt);
			DBUtil.close(con);
		}		
		return null;
	}

	@Override
	public List<Housedeal> searchAll() throws SQLException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs =null;
		try {
			con = DBUtil.getConnection();
			stmt = con.prepareStatement(" select * from housedeal ");
			
			rs = stmt.executeQuery();
			List<Housedeal>  list = new ArrayList<Housedeal>();
			while(rs.next()) {
				list.add( new Housedeal(
						rs.getInt("no"), 
						rs.getInt("aptCode"), 
						rs.getString("dealAmount"), 
						rs.getString("dealYear"), 
						rs.getString("dealMonth"), 
						rs.getString("dealDay"),
						rs.getString("area"),
						rs.getString("floor"),
						rs.getString("type"),
						rs.getString("rentMoney")						
						));				
			}
			return list;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(stmt);
			DBUtil.close(con);
		}
	}

	@Override
	public void add(Housedeal housedeal) throws SQLException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection();
			String sql = " insert into housedeal(no, aptCode, dealAmount, dealYear, dealMonth, dealDay, area, floor, type, rentMoney) "
					   + " values(?,?,?,?,?,?,?,?,?,?) ";
			stmt = con.prepareStatement(sql);
			int idx = 1;
			stmt.setInt(idx++, housedeal.getNo());
			stmt.setInt(idx++, housedeal.getAptCode());
			stmt.setString(idx++, housedeal.getDealAmount());
			stmt.setString(idx++, housedeal.getDealYear());
			stmt.setString(idx++, housedeal.getDealMonth());
			stmt.setString(idx++, housedeal.getDealDay());
			stmt.setString(idx++, housedeal.getArea());
			stmt.setString(idx++, housedeal.getFloor());
			stmt.setString(idx++, housedeal.getType());
			stmt.setString(idx++, housedeal.getRentMoney());
			stmt.executeUpdate();
		} finally {
			DBUtil.close(stmt);
			DBUtil.close(con);
		}
	}

	@Override
	public void update(Housedeal housedeal) throws SQLException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection();
			String sql = " update housedeal set aptCode=?, dealAmount=?, dealYear=?, dealMonth=?, dealDay=?, area=?, floor=?, type=?, rentMoney=? "
					   + " where no=? ";
			stmt = con.prepareStatement(sql);
			int idx = 1;
			stmt.setInt(idx++, housedeal.getAptCode());
			stmt.setString(idx++, housedeal.getDealAmount());
			stmt.setString(idx++, housedeal.getDealYear());
			stmt.setString(idx++, housedeal.getDealMonth());
			stmt.setString(idx++, housedeal.getDealDay());
			stmt.setString(idx++, housedeal.getArea());
			stmt.setString(idx++, housedeal.getFloor());
			stmt.setString(idx++, housedeal.getType());
			stmt.setString(idx++, housedeal.getRentMoney());
			stmt.setInt(idx++, housedeal.getNo());
			stmt.executeUpdate();
		} finally {
			DBUtil.close(stmt);
			DBUtil.close(con);
		}
	}

	@Override
	public void remove(int no) throws SQLException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection();
			stmt = con.prepareStatement(" delete from housedeal where no=? ");
			stmt.setInt(1, no);
			stmt.executeUpdate();
		} finally {
			DBUtil.close(stmt);
			DBUtil.close(con);
		}
	}

}
