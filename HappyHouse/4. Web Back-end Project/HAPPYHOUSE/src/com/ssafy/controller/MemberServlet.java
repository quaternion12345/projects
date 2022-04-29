package com.ssafy.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.ssafy.model.dto.Member;
//import com.ssafy.model.dto.Board;
//import com.ssafy.model.dto.PageBean;
//import com.ssafy.model.service.BoardService;
//import com.ssafy.model.service.BoardServiceImpl;
import com.ssafy.model.service.MemberService;
import com.ssafy.model.service.MemberServiceImp;

@WebServlet({"*.do"})
public class MemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	private BoardService boardService = new BoardServiceImpl();
	private MemberService memberService=new MemberServiceImp();
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		process(request, response);
	}
	public void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String url = "index.jsp";
		String action = request.getServletPath();

		System.out.println("action.........................."+action);
		try {
			if(action !=null) {
				if(action.endsWith("loginform.do")) {
					request.setAttribute("referer", request.getAttribute("referer"));
					url ="login.jsp";
				}else if(action.endsWith("login.do")) {
					url =login(request, response);
				}else if(action.endsWith("logout.do")) {
					url =logout(request, response);
				}else if(action.endsWith("memberUpdateForm.do")) {
					url =memberUpdateForm(request, response);
				}else if(action.endsWith("insertMember.do")) {
					url =insertMember(request, response);
				}else if(action.endsWith("modifyMember.do")) {
					url =modifyMember(request, response);
				}else if(action.endsWith("updateMember.do")) {
					url =updateMember(request, response);
				}else if(action.endsWith("removeMember.do")) {
					url =removeMember(request, response);
				}
//				}else if(action.endsWith("insertBoardForm.log")) {
//					url ="board/insertBoard.jsp";
//				}else if(action.endsWith("insertBoard.log")) {
//					url =insertBoard(request, response);
//				}else if(action.endsWith("listBoard.do")) {
//					url =listBoard(request, response);
//				}else if(action.endsWith("searchBoard.do")) {
//					url =searchBoard(request, response);
//				}else if(action.endsWith("searchBoardAjax.do")) {
//					url =searchBoardAjax(request, response);
//				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			url = "ErrorHandler.jsp";
		}
		if(url.startsWith("{") ||url.startsWith("[") ) {
			//url이 { 또는 [로 시작하면 JSON 데이타 이므로  contents타입 변경후 
			response.setContentType("application/json;charset=utf-8");
			response.setCharacterEncoding("UTF-8");
			//직접 출력 
			response.getWriter().append(url);
		}else if(url.startsWith("redirect:")) {
			response.sendRedirect(url.substring(url.indexOf(":")+1));
		}else {
			request.getRequestDispatcher(url).forward(request, response);
		}
	}
private String removeMember(HttpServletRequest request, HttpServletResponse response) {
		String id=getId(request);
		memberService.remove(id);
		HttpSession session = request.getSession();
		session.invalidate();
		return "redirect:index.jsp";
	}
private String modifyMember(HttpServletRequest request, HttpServletResponse response) {
		String id =getId(request);
		request.setAttribute("member", memberService.search(id));
		return "modinfo.jsp";
	}
private String updateMember(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String pw = request.getParameter("password");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		Member member = new Member(id, pw, name, email, phone);
		memberService.update(member);
		return "redirect:memberUpdateForm.do";
	}
private String insertMember(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String pw = request.getParameter("password");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		Member member = new Member(id, pw, name, email, phone);
		memberService.add(member);
		return "redirect:login.jsp";		
	}
//	private String insertBoard(HttpServletRequest request, HttpServletResponse response) {
//		String title= request.getParameter("title");
//		String contents= request.getParameter("content");
//		String id = getId(request);
//		Board board = new Board(id, title, contents);
//		boardService.insertBoard(board);
//		
//		return "redirect:listBoard.do";
//	}
//	private String searchBoardAjax(HttpServletRequest request, HttpServletResponse response) {
//		String no = request.getParameter("no");
//		Board board = boardService.search(no);
//		Gson gson = new Gson();
//		System.out.println(gson.toJson(board));
//		return gson.toJson(board);
//	}
//	private String searchBoard(HttpServletRequest request, HttpServletResponse response) {
//		String no = request.getParameter("no");
//		Board board = boardService.search(no);
//		request.setAttribute("board", board );
//		return "/board/searchBoard.jsp";
//	}
//	private String listBoard(HttpServletRequest request, HttpServletResponse response) {
//		String  key= request.getParameter("key");			//조회 조건
//		String  word= request.getParameter("word");			//검색할 단어
//		String  pageNo= request.getParameter("pageNo");		//현재 페이지 번호
//		
//		PageBean bean = new PageBean(key, word, pageNo);
//		request.setAttribute("bean", bean);
//		Map<String, Object> map = boardService.searchAll(bean);
//		System.out.println("pageLinK..........................................");
//		System.out.println(bean.getPageLink());
//		request.setAttribute("list", map.get("list"));
//		return "/board/listBoard.jsp";
//	}
	private String memberUpdateForm(HttpServletRequest request, HttpServletResponse response) {
		String id =getId(request);
		System.out.println("USER:"+id);
		request.setAttribute("member", memberService.search(id));
		return "userinfo.jsp";
	}
	private String logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.invalidate();
		return "redirect:loginform.do";
	}
	private String login(HttpServletRequest request, HttpServletResponse response) {
		//1. 요청 데이타 추출 
		String id = request.getParameter("id");
		String pw = request.getParameter("password");
//		String referer = request.getParameter("referer");
		String referer = null;
		System.out.println(id);
		System.out.println(pw);
		//2. 데이타 처리 		
		//2.2 인증 처리 
		try {
			memberService.login(id, pw);
			addToSession(request, "id", id);  //인증 정보를 session에 저장
			if(referer !=null) {
				referer = referer.lastIndexOf("/")>1?referer.substring(0,referer.lastIndexOf("/")):referer;
				System.out.println("referer................................"+referer);
				return  String.format("redirect:%s%s", request.getContextPath(),referer);
			}else {
				return "redirect:index.jsp";
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			return "loginform.do";
		}
	}
	private void addToSession(HttpServletRequest request, String key, Object value) {
		HttpSession session = request.getSession();
		session.setAttribute(key, value);
	}
	
	public String getId(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (String)session.getAttribute("id");
	}
}











