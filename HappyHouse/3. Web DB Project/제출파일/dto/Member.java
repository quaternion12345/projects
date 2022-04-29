package com.ssafy.model.dto;

import java.io.Serializable;

public class Member implements Serializable {
	private String id        ;
	private String password  ;
	private String name      ;
	private String email     ;
	private String phone     ;

	public Member() {}
	public Member(String id, String password, String name, String email, String phone) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Member [id=").append(id).append(", password=").append(password).append(", name=").append(name)
				.append(", email=").append(email).append(", phone=").append(phone).append("]");
		return builder.toString();
	}
}
