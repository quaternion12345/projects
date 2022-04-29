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
 * 3. parameter data를 이용해서  
 *    parameter data filter의 값이  author인 경우에만 인증 처리 확인
 */
@WebFilter("*.do")
public class LoginCheckFillter2 implements Filter {

	public LoginCheckFillter2() {
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
		String filter = req.getParameter("filter");
		if(filter!=null && filter.equals("author")) {
			HttpSession session = req.getSession();
			String id = (String) session.getAttribute("id");
			if (id == null) {
				request.setAttribute("referer", req.getParameter("action"));
				request.setAttribute("msg", "로그인 하세요");
				request.getRequestDispatcher(req.getContextPath() + "/main.do?action=loginform.do").forward(req, res);
			} else {
				chain.doFilter(request, response);
			}
		}else {
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
