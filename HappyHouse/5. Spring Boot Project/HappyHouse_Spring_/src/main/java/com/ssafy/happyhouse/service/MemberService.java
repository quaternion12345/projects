package com.ssafy.happyhouse.service;

import java.sql.SQLException;

import com.ssafy.happyhouse.model.dto.Member;

public interface MemberService {
	//회원가입
	void register(Member member) throws SQLException;
	//회원탈퇴
	void delete(String id) throws SQLException;
	//로그인
	Member login(String id, String password) throws SQLException;
	//회원정보수정
	void modify(Member member) throws SQLException;
	//마이페이
	Member inquire(String id) throws SQLException;
}
