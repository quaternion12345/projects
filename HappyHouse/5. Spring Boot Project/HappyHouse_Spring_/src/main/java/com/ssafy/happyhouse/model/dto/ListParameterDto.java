package com.ssafy.happyhouse.model.dto;

public class ListParameterDto {
	private String key;
	private String word;
	private int start;
	private int currentPerPage;
	
	public int getCurrentPerPage() {
		return currentPerPage;
	}
	public void setCurrentPerPage(int currentPerPage) {
		this.currentPerPage = currentPerPage;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	
	
}
