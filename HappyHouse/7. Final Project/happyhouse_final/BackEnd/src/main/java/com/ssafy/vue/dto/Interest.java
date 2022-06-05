package com.ssafy.vue.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Interest (관심지역)", description = "번호, 회원아이디, 시이름, 구군이름, 동이름, 동번호을 가진   Domain Class")
public class Interest {
	@ApiModelProperty(value = "번호") // PK, auto inc
	private int no;
	@ApiModelProperty(value = "회원아이디") // FK
	private String member_id;
	@ApiModelProperty(value = "시이름")
	private String sidoName;
	@ApiModelProperty(value = "구군이름")
	private String gugunName;
	@ApiModelProperty(value = "동이름")
	private String dongName;
	@ApiModelProperty(value = "동번호")
	private String dongCode;

	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getSidoName() {
		return sidoName;
	}
	public void setSidoName(String sidoName) {
		this.sidoName = sidoName;
	}
	public String getGugunName() {
		return gugunName;
	}
	public void setGugunName(String gugunName) {
		this.gugunName = gugunName;
	}
	public String getDongName() {
		return dongName;
	}
	public void setDongName(String dongName) {
		this.dongName = dongName;
	}
	public String getDongCode() {
		return dongCode;
	}
	public void setDongCode(String dongCode) {
		this.dongCode = dongCode;
	}
	
	public Interest(String member_id, String sidoName, String gugunName, String dongName, String dongCode) {
		super();
		this.member_id = member_id;
		this.sidoName = sidoName;
		this.gugunName = gugunName;
		this.dongName = dongName;
		this.dongCode = dongCode;
	}
	@Override
	public String toString() {
		return "Interest [no=" + no + ", member_id=" + member_id + ", sidoName=" + sidoName + ", gugunName=" + gugunName
				+ ", dongName=" + dongName + ", dongCode=" + dongCode + "]";
	}

}