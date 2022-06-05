package com.ssafy.vue.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Frequent (자주가는장소)", description = "번호, 회원아이디, 주소, 위도, 경도를 가진   Domain Class")
public class Frequent {
	@ApiModelProperty(value = "번호") // auto inc, PK
	private int no;
	@ApiModelProperty(value = "회원아이디") // FK
	private String member_id;
	@ApiModelProperty(value = "주소")
	private String address;
	@ApiModelProperty(value = "위도")
	private String lat;
	@ApiModelProperty(value = "경도")
	private String lng;

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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	
	public Frequent(String member_id, String address, String lat, String lng) {
		super();
		this.member_id = member_id;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
	}
	@Override
	public String toString() {
		return "Frequent [no=" + no + ", member_id=" + member_id + ", address=" + address + ", lat=" + lat + ", lng="
				+ lng + "]";
	}
	
}