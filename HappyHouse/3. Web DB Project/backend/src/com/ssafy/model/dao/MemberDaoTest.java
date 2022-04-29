package com.ssafy.model.dao;

import java.util.List;

import com.ssafy.model.dto.Member;

public class MemberDaoTest {

	public static void main(String[] args) {
		MemberDAO dao = new MemberDaoImp();
		
		try {
//			dao.add(new Member("1", "1", "1", "1", "1", "1"));
//			dao.update(new Member("1", "1111", "1111", "1111", "1", "1"));
			dao.remove("1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			List<Member>  list = dao.searchAll();
			for (Member member : list) {
				System.out.println(member);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("======search");
		try {
			System.out.println(dao.search("ssafy"));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
