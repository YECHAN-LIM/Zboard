package com.example.zboard.controller.mvc;

import java.security.*;
import java.util.*;

import javax.servlet.http.*;

import org.springframework.security.access.prepost.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.logout.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.support.*;
import org.springframework.web.servlet.support.*;

import com.example.zboard.service.mvc.*;

import lombok.*;

@RequiredArgsConstructor
@Controller
public class MemberController {
	private final MemberService service;
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/member/join")
	public void join() {
	}
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/member/find")
	public void find() {
	}
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/member/login")
	public void login() {
	}
	
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/member/password_check")
	public void passwordCheck() {
	}
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/member/join_check")
	public String joinCheck(@RequestParam @NonNull String checkcode) {
		service.joinCheck(checkcode);
		return "redirect:/member/login";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/member/password_check")
	public String passwordCheck(@RequestParam @NonNull String password, Principal principal, RedirectAttributes ra) {
		service.passwordCheck(password, principal.getName());
		ra.addFlashAttribute("passwordCheck", true);
		return "redirect:/member/read";
	} 
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/member/read")
	public String read(Principal principal, HttpServletRequest req, Model model) {
		Map<String,?> flashMap = RequestContextUtils.getInputFlashMap(req);
		if(flashMap==null)
			return "redirect:/member/password_check";
		model.addAttribute("member", service.read(principal.getName()));
		return "/member/read";
	} 
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/member/resign")
	public ModelAndView resign(SecurityContextLogoutHandler handler, HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		service.resign(authentication.getName());
		handler.logout(request, response, authentication);
		return new ModelAndView("redirect:/");
	}
}