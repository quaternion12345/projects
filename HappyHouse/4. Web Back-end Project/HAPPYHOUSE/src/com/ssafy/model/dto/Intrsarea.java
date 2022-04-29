package com.ssafy.model.dto;

import java.io.Serializable;

public class Intrsarea {
	private int intno;
	private String member_id;
	private String dongcode_dongCode;
	
	public Intrsarea() {}
	public Intrsarea(int intno, String member_id, String dongcode_dongCode) {
		super();
		this.intno = intno;
		this.member_id = member_id;
		this.dongcode_dongCode = dongcode_dongCode;
	}
	public int getIntno() {
		return intno;
	}
	public void setIntno(int intno) {
		this.intno = intno;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getDongcode_DongCode() {
		return dongcode_dongCode;
	}
	public void setDongcode_DongCode(String dongcode_dongCode) {
		this.dongcode_dongCode = dongcode_dongCode;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Intrsarea [intno=").append(intno).append(", member_id=").append(member_id).append(", dongcode_dongCode=")
				.append(dongcode_dongCode).append("]");
		return builder.toString();
	}
}
