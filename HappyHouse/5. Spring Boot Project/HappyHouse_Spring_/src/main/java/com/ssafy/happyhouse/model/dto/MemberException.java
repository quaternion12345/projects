package com.ssafy.happyhouse.model.dto;

public class MemberException extends RuntimeException {
	public MemberException() {
		super("처리 중 오류 발생");
	}
	public MemberException(String msg) {
		super(msg);
	}
}
