package com.ssafy.happyhouse.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.happyhouse.model.dao.MemberDao;
import com.ssafy.happyhouse.model.dao.MemberDaoImp;
import com.ssafy.happyhouse.model.dto.Member;

@Service
public class MemberServiceImp implements MemberService {
	
	@Autowired
	MemberDao memberDao;//=new MemberDaoImp();
	
	@Override
	public void register(Member member) throws SQLException {
		memberDao.register(member);
	}

	@Override
	public void delete(String id) throws SQLException {
		memberDao.delete(id);

	}

	@Override
	public Member login(String id, String password) throws SQLException {
		return memberDao.login(id, password);
	}

	@Override
	public void modify(Member member) throws SQLException {
		memberDao.modify(member);

	}

	@Override
	public Member inquire(String id) throws SQLException {
		return memberDao.inquire(id);
	}

}
