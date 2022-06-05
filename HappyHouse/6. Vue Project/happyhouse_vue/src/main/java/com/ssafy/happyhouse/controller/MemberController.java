package com.ssafy.happyhouse.controller;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ssafy.happyhouse.model.dto.Member;
import com.ssafy.happyhouse.model.dto.MemberException;
import com.ssafy.happyhouse.service.MemberService;

@Controller
@CrossOrigin("*")
public class MemberController {
	private static final Logger logger  = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private MemberService service;
	
	@ExceptionHandler	
	public ModelAndView handler(Exception e) {
		ModelAndView mav = new ModelAndView("error");
		if(e instanceof MemberException) {
			mav.addObject("msg", e.getMessage());
		}else {
			mav.addObject("msg", "회원 정보 처리 중 오류 발생");
		}
		return mav;
	}
	@GetMapping("/member/loginForm")
	public String loginForm(Model model,
	                        @CookieValue(value="loginid", required = false) Cookie loginid)
	{	   
	    logger.debug("loginForm");
		if (loginid != null){	        
	    	model.addAttribute("svid", loginid);
	    }
		return "login";
	}	
	@PostMapping("/member/login")
	public String login(String id, String password, String idsv, Model model, HttpSession session, HttpServletResponse response) throws SQLException {
		logger.debug("login");
		Member member = service.login(id, password);
		if(member != null) {
			session.setAttribute("memberInfo", member);
			// Cookie setting
			Cookie cookie = new Cookie("loginid", id);
			cookie.setPath("/member/login");				
			if("saveok".equals(idsv)) { // 아이디 저장 체크
				cookie.setMaxAge(60*60*24*365*20);
			} else { // 아이디 저장 체크 X				
				cookie.setMaxAge(0);				
			}	
			response.addCookie(cookie);
			
		}else {
			model.addAttribute("msg", "아이디 또는 비밀번호 확인 후 다시 로그인하세요.");
			return "redirect:member/loginForm";
		}
		return "redirect:index";
	}
	@GetMapping("/member/logout")
	public String logout(HttpSession session) {
		logger.debug("loginout");
		session.invalidate();
		return "redirect:index";
	}
	
	@GetMapping("/member/registerForm")
	public String registerForm() {
		logger.debug("registerForm");
		return "join";
	}
	
	@PostMapping("/member") // INSERT	
	public String register(Member member) throws SQLException{
		logger.debug("register");
		try {
			service.register(member);
		}catch(Exception e) {
			return "error";
		}
		return "redirect:member/loginForm";		
	}
	
	@GetMapping("/member/{id}") // RETRIEVE
	public String retrieve(@PathVariable("id") String id, Model model) throws SQLException {
		logger.debug("search");
		Member member = service.inquire(id);
		if(member != null) {
			model.addAttribute("member", member);
			return "userInfo";
		}
		else return "error";
	}	
	
	@PostMapping("/member/update") // UPDATE
	public String update(Member member) throws SQLException {
		logger.debug("update");
		try {
			service.modify(member);
		}catch(Exception e) {
			return "error";
		}
		return "redirect:" + member.getId();
	}
	
	@GetMapping("/member/delete/{id}") // DELETE
	public String delete(@PathVariable("id") String id, HttpSession session) throws SQLException {
		logger.debug("delete");
		try {
			service.delete(id);
			session.invalidate();
		}catch(Exception e){
			return "error";
		}
		return "redirect:/index";
	}	
}
