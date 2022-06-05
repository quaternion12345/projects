package com.ssafy.happyhouse.model.dto;

import java.io.Serializable;

public class Notice implements Serializable {
	static final long serialVersionUID = 1L;
	
	private int no;
	private String rg_date;
	private String title;
	private String contents;
	private String author;
	
	
	public Notice() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Notice(int no, String rg_date, String title, String contents, String author) {
		super();
		this.no = no;
		this.rg_date = rg_date;
		this.title = title;
		this.contents = contents;
		this.author = author;
	}
	@Override
	public String toString() {
		return "Notice [no=" + no + ", rg_date=" + rg_date + ", title=" + title + ", contents=" + contents + ", author="
				+ author + "]";
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getRg_date() {
		return rg_date;
	}
	public void setRg_date(String rg_date) {
		this.rg_date = rg_date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	

	

}
