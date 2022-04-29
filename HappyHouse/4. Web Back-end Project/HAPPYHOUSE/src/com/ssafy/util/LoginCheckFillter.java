package com.ssafy.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 확장자 방식을 이용해서  .log로 끝나는 확장자로 요청한 경우만 인증 처리  확인
 */              
@WebFilter("*.log")        
public class LoginCheckFillter implements Filter {

	public LoginCheckFillter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		String id = (String) session.getAttribute("id");  //인증 정보
		if (id == null) {
			System.out.println("filter...........referer:"+req.getServletPath());
			request.setAttribute("referer", req.getServletPath());
			request.setAttribute("msg", "로그인 하세요");
			request.getRequestDispatcher(req.getContextPath() + "/loginform.do").forward(req, res);
		} else {
			
			chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
