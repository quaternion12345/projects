package com.ssafy.vue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.vue.dto.MemberDto;
import com.ssafy.vue.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberMapper memberMapper;
	
	@Override
	public MemberDto login(MemberDto memberDto) throws Exception {
		System.out.println(memberDto.getUserid() + " " + memberDto.getUserpwd());
		if(memberDto.getUserid() == null || memberDto.getUserpwd() == null) {
			return null;
		}
		return memberMapper.login(memberDto);
	}

	@Override
	public MemberDto userInfo(String userid) throws Exception {
		return memberMapper.userInfo(userid);
	}

	@Override
	public boolean insertUser(MemberDto memberDto) {
		// TODO Auto-generated method stub
		return memberMapper.insertUser(memberDto) == 1;
	}

	@Override
	public boolean updateUser(MemberDto memberDto) {
		// TODO Auto-generated method stub
		return memberMapper.updateUser(memberDto) == 1;
	}

	@Override
	public boolean deleteUser(String userid) {
		// TODO Auto-generated method stub
		return memberMapper.deleteUser(userid) == 1;
	}

	
}
