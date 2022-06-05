package com.ssafy.happyhouse.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ssafy.happyhouse.model.dto.ListParameterDto;
import com.ssafy.happyhouse.model.dto.Notice;
import com.ssafy.happyhouse.util.DBUtil2;

public class NoticeDaoImp implements NoticeDao {
	
	private static NoticeDao noticeDao = new NoticeDaoImp();
	
	public NoticeDaoImp() {
		super();
	}
	
	public static NoticeDao getNoticeDao() {
		return noticeDao;
	}

	@Override
	public Notice search(String name) throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs =null;
		try {
			con = DBUtil2.getConnection();
			stmt = con.prepareStatement(" select * from notice where author=? ");
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			if(rs.next()) {
				return new Notice(rs.getInt("no"), 
						rs.getString("rg_date"), 
						rs.getString("title"), 
						rs.getString("contents"), 
						rs.getString("author"));
			}
		} finally {
			DBUtil2.close(rs, stmt, con);
		}
		return null;
				
		
	}

	@Override
	public int getTotalCount(ListParameterDto listParameterDto) throws SQLException {
		int cnt = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil2.getConnection();
			StringBuilder listArticle = new StringBuilder();
			listArticle.append("select count(no) \n");
			listArticle.append("from notice \n");
			
			String key = listParameterDto.getKey();
			String word = listParameterDto.getWord();
			
			if (!word.isEmpty()) {
				if (key.equals("title")) {
					listArticle.append("and subject like concat('%', ?, '%') \n");
				} else {
					listArticle.append("and " + key + " = ? \n");		// column의 이름은 변수로 처리할 수 없다!
				}
			}
			
			System.out.println(listArticle.toString());
			pstmt = conn.prepareStatement(listArticle.toString());
			if (!word.isEmpty()) {
				pstmt.setString(1, word);
			}
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			cnt = rs.getInt(1);
			
		} finally {
			DBUtil2.close(rs, pstmt, conn);
		}
		
		return cnt;
	}

	@Override
	public List<Notice> searchAll(ListParameterDto listParameterDto) throws SQLException {
		List<Notice>  list = new ArrayList<Notice>(20);
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs =null;
		try {
			con = DBUtil2.getConnection();
			StringBuilder listArticle = new StringBuilder();
			listArticle.append(" select * from notice ");
			
			String key = listParameterDto.getKey();
			String word = listParameterDto.getWord();
			
			if (!word.isEmpty()) {
				if (key.equals("subject")) {
					listArticle.append("and g.subject like concat('%', ?, '%') \n");
				} else {
					listArticle.append("and g." + key + " = ? \n");		// column의 이름은 변수로 처리할 수 없다!
				}
			}
			listArticle.append("order by no desc limit ?, ? \n");
			
			stmt = con.prepareStatement(listArticle.toString());
			
			int idx = 0;
			
			if (!word.isEmpty()) {
				stmt.setString(++idx, word);
			}
			
			stmt.setInt(++idx,  listParameterDto.getStart());
			stmt.setInt(++idx,  listParameterDto.getCurrentPerPage());
			
			rs = stmt.executeQuery();
			while(rs.next()) {
				list.add( new Notice(	rs.getInt("no"), 
										rs.getString("rg_date"), 
										rs.getString("title"), 
										rs.getString("contents"), 
										rs.getString("author")));
			}
			for (Notice n : list) {
				System.out.println(n);
			}
			
		} finally {
			DBUtil2.close(rs, stmt, con);
		}
		return list;
	}

	@Override
	public void add(Notice notice) throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil2.getConnection();
			String sql = " insert into notice(no, rg_date, title, author, contents) "
					   + " values(?,?,?,?,?) ";
			stmt = con.prepareStatement(sql);
			int idx = 1;
			stmt.setInt(idx++, notice.getNo());
			stmt.setString(idx++, notice.getRg_date());
			stmt.setString(idx++, notice.getTitle());
			stmt.setString(idx++, notice.getAuthor());
			stmt.setString(idx++, notice.getContents());
			stmt.executeUpdate();
		} finally {
			DBUtil2.close(stmt, con);
		}
		
	}

	@Override
	public void update(Notice notice) throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil2.getConnection();
			String sql = " update notice set no=?, rg_date=?, title=?, author=?, contents=? "
					   + " where no=? ";
			stmt = con.prepareStatement(sql);
			int idx = 1;
			stmt.setInt(idx++, notice.getNo());
			stmt.setString(idx++, notice.getRg_date());
			stmt.setString(idx++, notice.getTitle());
			stmt.setString(idx++, notice.getAuthor());
			stmt.setString(idx++, notice.getContents());
			stmt.setInt(idx++, notice.getNo());
			stmt.executeUpdate();
		} finally {
			DBUtil2.close(stmt, con);
		}
		
	}

	@Override
	public void remove(int id) throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil2.getConnection();
			stmt = con.prepareStatement(" delete from notice where no=? ");
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} finally {
			DBUtil2.close(stmt, con);
		}
	}
}
