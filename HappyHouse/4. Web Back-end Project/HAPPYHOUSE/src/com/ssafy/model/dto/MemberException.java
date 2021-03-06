package com.ssafy.model.dto;

public class MemberException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public MemberException() {
		super("사원정보 처리 중 오류 발생");
	}
	public MemberException(String msg) {
		super(msg);
	}
}
