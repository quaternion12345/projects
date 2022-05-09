package com.ssafy.happyhouse.service;

import java.util.List;

import com.ssafy.happyhouse.model.dao.NoticeDao;
import com.ssafy.happyhouse.model.dao.NoticeDaoImp;
import com.ssafy.happyhouse.model.dto.ListParameterDto;
import com.ssafy.happyhouse.model.dto.Notice;
import com.ssafy.happyhouse.util.PageNavigation;

public class NoticeServiceImpl implements NoticeService {
	
	private NoticeDao noticeDao = NoticeDaoImp.getNoticeDao();
	
	private static NoticeService noticeService = new NoticeServiceImpl();
	
	private NoticeServiceImpl() {}
	
	public static NoticeService getNoticeService() {
		return noticeService;
	}
	
	@Override
	public void registerNotice(Notice notice) throws Exception {
		noticeDao.add(notice);
	}

	@Override
	public void updateNotice(Notice notice) throws Exception {
		noticeDao.update(notice);
		
	}

	@Override
	public void deleteNotice(int noticeNo) throws Exception {
		noticeDao.remove(noticeNo);
		
	}

	@Override
	public List<Notice> listNotice(String pg, String key, String word) throws Exception {
		ListParameterDto listParameterDto = new ListParameterDto();
		
		int pgno = pg != null ? Integer.parseInt(pg) : 1;
		int currentPerPage = 5;
		int start = (pgno - 1) * currentPerPage;
		
		listParameterDto.setKey(key == null ? "" : key.trim());
		listParameterDto.setWord(word == null ? "" : key.trim());
		listParameterDto.setStart(start);
		listParameterDto.setCurrentPerPage(currentPerPage);
		
		return noticeDao.searchAll(listParameterDto);
	}

	@Override
	public PageNavigation makePageNavigation(String pg, String key, String word) throws Exception {
		PageNavigation pageNavigation = new PageNavigation();
		int currentPage = pg != null ? Integer.parseInt(pg) : 1;
		int naviSize = 10;
		int countPerPage = 5;
		
		pageNavigation.setCurrentPage(currentPage);
		pageNavigation.setCountPerPage(countPerPage);
		pageNavigation.setNaviSize(naviSize);
		
		ListParameterDto listParameterDto = new ListParameterDto();
		
		listParameterDto.setKey(key == null ? "" : key.trim());
		listParameterDto.setWord(word == null ? "" : key.trim());
		
		int totalCount = noticeDao.getTotalCount(listParameterDto);
		pageNavigation.setTotalCount(totalCount);
		int totalPageCount = (totalCount - 1) / countPerPage + 1;
		pageNavigation.setTotalPageCount(totalPageCount);
		pageNavigation.setStartRange(currentPage <= naviSize);
		boolean endRange = (totalPageCount - 1) / naviSize * naviSize < currentPage;
		pageNavigation.setEndRange(endRange);
		
		return pageNavigation;
	}
	
}
