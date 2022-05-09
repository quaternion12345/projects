package com.ssafy.happyhouse.service;

import java.util.List;

import com.ssafy.happyhouse.model.dto.Notice;
import com.ssafy.happyhouse.util.PageNavigation;

public interface NoticeService {
	void registerNotice(Notice noticeDto) throws Exception;
	void updateNotice(Notice noticeDto) throws Exception;
	void deleteNotice(int noticeNo) throws Exception;
	List<Notice> listNotice(String pg, String key, String word) throws Exception;
	PageNavigation makePageNavigation(String pg, String key, String word) throws Exception;
}
