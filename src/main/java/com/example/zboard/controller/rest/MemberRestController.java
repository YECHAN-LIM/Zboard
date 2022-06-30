package com.example.zboard.controller.rest;

import java.net.*;
import java.security.*;

import javax.validation.*;

import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.validation.*;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.*;

import com.example.zboard.data.dto.*;
import com.example.zboard.service.rest.*;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class MemberRestController {
	private final MemberRestService service;
	
	@PreAuthorize("isAnonymous()")
	@GetMapping(path="/members/username", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<?> idCheck(@RequestParam @NonNull String username) {
		service.idCheck(username);
		return ResponseEntity.ok("아이디 사용 가능");
	}
	
	@PreAuthorize("isAnonymous()")
	@GetMapping(path="/members/email", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<?> emailCheck(@RequestParam @NonNull String email) {
		service.emailCheck(email);
		return ResponseEntity.ok("이메일 사용 가능");
	}
	
	@PreAuthorize("isAnonymous()")
	@GetMapping(path="/members/member/email", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<?> findId(@RequestParam @NonNull String email) {
		return ResponseEntity.ok(service.findId(email));
	}	
	
	@PreAuthorize("isAnonymous()")
	@PostMapping("/member/join")
	public ResponseEntity<?> join(@ModelAttribute @Valid MemberDto.Join dto, BindingResult bindingResult) throws BindException {
		if(bindingResult.hasErrors())
			throw new BindException(bindingResult);
		service.join(dto);
		URI location = UriComponentsBuilder.newInstance().path("/user/login").build().toUri();
		return ResponseEntity.created(location).body(location.toString());
	}
	
	@PreAuthorize("isAuthenticated()")
	@PutMapping(path="/members", produces=MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<?> update(@ModelAttribute @Valid MemberDto.Update dto, BindingResult bindingResult, Principal principal) throws BindException {
		if(bindingResult.hasErrors())
			throw new BindException(bindingResult);
		service.update(dto.setUsername(principal.getName()));
		return ResponseEntity.ok("정보 변경 성공");
	}
}
