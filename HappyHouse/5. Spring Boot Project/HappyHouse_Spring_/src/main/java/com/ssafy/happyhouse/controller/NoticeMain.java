package com.ssafy.happyhouse.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ssafy.happyhouse.model.dto.Notice;
import com.ssafy.happyhouse.service.NoticeService;
import com.ssafy.happyhouse.service.NoticeServiceImpl;
import com.ssafy.happyhouse.util.PageNavigation;

/**
 * Servlet implementation class NoticeMain
 */
@WebServlet({ "/NoticeMain", "/notice" })
public class NoticeMain extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private NoticeService noticeService= NoticeServiceImpl.getNoticeService();
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = "index.jsp";
		String action = request.getParameter("action");
		
		System.out.println("action....................." + action);
		
		try {
			if (action.equals("list")) {
				path=listNotice(request, response);
				request.getRequestDispatcher(path).forward(request, response);
			} else if (action.equals("register")) {
				path=registerNotice(request, response);
				request.getRequestDispatcher(path).forward(request, response);
			} else if (action.equals("modify")) {
				path=modifyNotice(request, response);
				response.sendRedirect(request.getContextPath() + path);
			} else if (action.equals("delete")) {
				path=deleteNotice(request, response);
				response.sendRedirect(request.getContextPath() + path);
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			path = "NoticeErrorHandler.jsp";
			response.sendRedirect(request.getContextPath() + "/" + path);
		}
		
		
	}

	private String deleteNotice(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
//		MemberDto memberDto = (MemberDto) session.getAttribute("userInfo");
//		if(memberDto != null) {
			int no = Integer.parseInt(request.getParameter("no"));
			System.out.println(no);
			try {
				noticeService.deleteNotice(no);
				
				return "/notice?action=list";
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("msg", "글 삭제중 에러가 발생했습니다.");
				
				return "./NoticeErrorHandler.jsp";
			}
//		} else {			
//			return "/user?act=mvlogin";
//		}
	}

	private String modifyNotice(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
//		MemberDto memberDto = (MemberDto) session.getAttribute("userInfo");
//		if(memberDto != null) {
			Notice notice = new Notice();
			
			notice.setNo(Integer.parseInt(request.getParameter("no")));
			notice.setTitle(request.getParameter("title"));
			notice.setContents(request.getParameter("contents"));
			try {
				noticeService.updateNotice(notice);
				
				return "/notice?action=list";
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("msg", "글 수정중 에러가 발생했습니다.");
				
				return "./NoticeErrorHandler.jsp";
			}
//		} else {			
//			return "/user?act=mvlogin";
//		}
	}

	private String registerNotice(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
//		MemberDto memberDto = (MemberDto) session.getAttribute("userInfo");
//		if(memberDto != null) {
			Notice notice = new Notice();
			
			notice.setAuthor(request.getParameter("author"));
			notice.setTitle(request.getParameter("title"));
			notice.setContents(request.getParameter("contents"));
			try {
				noticeService.registerNotice(notice);
				
				return "/notice?action=list";
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("msg", "글 작성중 에러가 발생했습니다.");
				
				return "./NoticeErrorHandler.jsp";
			}
//		} else {			
//			return "/user?act=mvlogin";
//		}
	}

	private String listNotice(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
//		MemberDto memberDto = (MemberDto) session.getAttribute("userInfo");
//		if(memberDto != null) {
			String pg = request.getParameter("pg");
			String key = request.getParameter("key");
			String word = request.getParameter("word");
			
			try {
				List<Notice> list = noticeService.listNotice(pg, key, word);
				PageNavigation navigation = noticeService.makePageNavigation(pg, key, word);
				
				request.setAttribute("articles", list);
				request.setAttribute("navi", navigation);
				
				return "/Notice.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("msg", "글목록 얻기중 에러가 발생했습니다.");
				return "./NoticeErrorHandler.jsp";
			}
//		} else {			
//			return "/user?act=mvlogin";
//		}
	}

}
