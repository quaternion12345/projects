//package com.ssafy.happyhouse.controller;
//
//import java.io.IOException;
//import java.sql.SQLException;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import com.ssafy.happyhouse.model.dto.Member;
//import com.ssafy.happyhouse.service.MemberService;
//import com.ssafy.happyhouse.service.MemberServiceImp;
//
///**
// * Servlet implementation class MemberServlet
// */
//@WebServlet("/MemberServlet")
//public class MemberServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//       
//	MemberService MS=new MemberServiceImp();
//	
//	protected void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		request.setCharacterEncoding("utf-8");
//		doGet(request, response);
//	}
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String act = request.getParameter("act");
//		String path = "/index.jsp";
//		System.out.println("act >>>>>>"+act);
//		 if("register".equals(act)) {
//			registerMember(request, response);
//		} else if("login".equals(act)) {
//			path = loginMember(request, response);
//			request.getRequestDispatcher(path).forward(request, response);
//		} else if("logout".equals(act)) {
//			path = logOutMember(request, response);
//			response.sendRedirect("index.jsp");
//		} else if("memberInfo".equals(act)) {
//			path = memInfo(request, response);
//			request.getRequestDispatcher(path).forward(request, response);
//		}else if("update".equals(act)) {
//			updateInfo(request, response);
//		}else if("delete".equals(act)) {
//			delete(request, response);
//		}else if("afterJoin".equals(act)) {
//			path = "login.jsp";
//			request.setAttribute("msg", "회원가입 완료! 로그인 해주세요!");
//			request.getRequestDispatcher(path).forward(request, response);
//		}
//	}
//
//	private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		HttpSession session=request.getSession();
//		Member member=(Member)session.getAttribute("memberInfo");
//		
//		try {
//			MS.delete(member.getId());
//			session.invalidate();
//			response.sendRedirect("index.jsp");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			response.sendRedirect("error.jsp");
//			
//		}
//		
//	}
//	private void updateInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		HttpSession session=request.getSession();
//		Member member=(Member)session.getAttribute("memberInfo");
//
//		member.setPassword(request.getParameter("password"));
//		member.setEmail(request.getParameter("email"));
//		member.setAddress(request.getParameter("address"));
//		member.setPhone(request.getParameter("phone"));
//
//		System.out.println(member.toString());
//		try {
//			MS.modify(member);
//			response.sendRedirect("MemberServlet?act=memberInfo");
//			
//		} catch (SQLException e) {
//			response.sendRedirect("error.jsp");
//			e.printStackTrace();
//		}
//		
//	}
//	private String memInfo(HttpServletRequest request, HttpServletResponse response) {
//		HttpSession session=request.getSession();
//		Member member=(Member) session.getAttribute("memberInfo");
//		System.out.println(member.toString());
//		try {
//			member=MS.inquire(member.getId());
//			if(member!=null) {
//				request.setAttribute("member", member);
//				return "userInfo.jsp";
//			}else {
//				request.setAttribute("msg", "알 수 없는 오류 발생!");
//				return "error.jsp";
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return "error.jsp";
//		}
//	}
//
//	private String logOutMember(HttpServletRequest request, HttpServletResponse response) {
//		HttpSession session=request.getSession();
//		session.invalidate();
//		return "index.jsp";
//	}
//
//	private String loginMember(HttpServletRequest request, HttpServletResponse response) {
//		String id=request.getParameter("user_id");
//		String password=request.getParameter("password");
//		Member member=null;
//		try {
//			member=MS.login(id, password);
//			if(member!=null) {
//			HttpSession session=request.getSession();
//			session.setAttribute("memberInfo", member);
//			
//			String idsv = request.getParameter("idsave");
//			if("saveok".equals(idsv)) { // 아이디 저장 체크
////				Cookie setting
//				Cookie cookie = new Cookie("loginid", id);
//				cookie.setMaxAge(60*60*24*365*20);
//				cookie.setPath(request.getContextPath());
//				
//				response.addCookie(cookie);
//			} else { // 아이디 저장 체크 X
//				Cookie[] cookies = request.getCookies();
//				if(cookies != null) {
//					for(int i=0;i<cookies.length;i++) {
//						if(cookies[i].getName().equals("loginid")) {
//							cookies[i].setMaxAge(0);
//							response.addCookie(cookies[i]);
//							break;
//						}
//					}
//				}
//			}	
//			
//			
//			return "index.jsp";
//			}else {
//				request.setAttribute("msg", "아이디 또는 비밀번호 확인 후 다시 로그인하세요.");
//				return "login.jsp";
//			}
//		} catch (SQLException e) {
//			request.setAttribute("msg", "로그인 시도 중 오류 발생");
//			e.printStackTrace();
//			return "error.jsp";
//		}
//		
//	}
//
//	private void registerMember(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//		Member member=new Member();
//		member.setId(request.getParameter("id"));
//		member.setName(request.getParameter("user_name"));
//		member.setPassword(request.getParameter("password"));
//		member.setPhone(request.getParameter("phone"));
//		member.setEmail(request.getParameter("email"));
//		member.setAddress(request.getParameter("address"));
//		
//		try {
//			MS.register(member);
//			response.sendRedirect("MemberServlet?act=afterJoin");
//		} catch (SQLException e) {
//			request.setAttribute("msg", "회원가입 중 오류 발생!");
//			e.printStackTrace();
//			request.getRequestDispatcher("error.jsp").forward(request, response);
//		}
//
//	}
//
//}
