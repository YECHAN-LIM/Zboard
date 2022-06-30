package com.example.zboard.security;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.springframework.security.core.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.savedrequest.*;
import org.springframework.stereotype.*;

import com.example.zboard.dao.*;
import com.example.zboard.data.member.*;

import lombok.*;

@RequiredArgsConstructor
@Component("loginSuccessHandler")
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final MemberDao dao;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		
		String username = authentication.getName();
		dao.update(Member.builder().username(username).loginFailCnt(0).build());
		// 로그인한 사용자에게 출력할 메시지가 있다면 이곳에 작성
		
		// RequestCache : 사용자가 가려던 목적지를 저장하는 인터페이스
		SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
					
		// 이동할 주소가 있으면 그곳으로, 없으면 ROLE_USER의 경우 /로, ROLE_ADMIN의 경우 /system으로
		RedirectStrategy rs = new DefaultRedirectStrategy();
		if(savedRequest!=null)
			rs.sendRedirect(request, response, savedRequest.getRedirectUrl());
		else
			rs.sendRedirect(request, response, "/");
	}
}