package com.example.zboard.security;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.*;

import com.example.zboard.dao.*;
import com.example.zboard.data.member.*;

import lombok.*;

@RequiredArgsConstructor
@Component("loginFailureHandler")
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	private final MemberDao dao;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		HttpSession session = request.getSession();
		if(exception instanceof InternalAuthenticationServiceException) 
			session.setAttribute("msg", "아이디를 확인하세요");
		else if(exception instanceof BadCredentialsException) {
			String username = request.getParameter("username");
			Member member = dao.findById(username).get();
			if(member.getLoginFailCnt()<4) {
				dao.update(Member.builder().username(username).loginFailCnt(member.getLoginFailCnt()+1).build());
				session.setAttribute("msg", "로그인에 " + member.getLoginFailCnt() + "회 실패했습니다");
			} else {
				dao.update(Member.builder().username(username).loginFailCnt(5).enabled(false).build());
				session.setAttribute("msg", "로그인에 5회이상 실패해 계정이 블록되었습니다");
			}	
		} else if(exception instanceof DisabledException) 
			session.setAttribute("msg", "블록된 계정입니다. 관리자에게 연락하세요");
		
		new DefaultRedirectStrategy().sendRedirect(request, response, "/user/login?error");
	}
}





