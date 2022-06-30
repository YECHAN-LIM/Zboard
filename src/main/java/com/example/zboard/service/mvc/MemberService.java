package com.example.zboard.service.mvc;

import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import com.example.zboard.dao.*;
import com.example.zboard.data.dto.*;
import com.example.zboard.data.member.*;
import com.example.zboard.exception.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class MemberService {
	private final MemberDao memberDao;
	private final AuthorityDao authorityDao;
	private final MemberCheckCodeDao memberCheckCodeDao;
	private final PasswordEncoder passwordEncoder;
	
	@Transactional
	public void joinCheck(String checkCode) {
		String username = memberCheckCodeDao.findUsernameByCheckcode(checkCode).orElseThrow(MemberFail.CheckCodeFailException::new);
		memberCheckCodeDao.deleteByUsername(checkCode);
		memberDao.update(Member.builder().username(username).enabled(true).build());
	}

	@Transactional(readOnly=true)
	public void passwordCheck(String password, String username) {
		Member member = memberDao.findById(username).orElseThrow(MemberFail.PasswordCheckFailException::new);
		if(passwordEncoder.matches(password, member.getPassword())==false) 
			throw new MemberFail.PasswordCheckFailException();
	}

	@Transactional
	public void resign(String username) {
		memberDao.findById(username).orElseThrow(MemberFail.MemberNotFoundException::new);
		memberDao.deleteById(username);
		authorityDao.deleteByUsername(username);
	}

	@Transactional(readOnly=true)
	public MemberDto.Read read(String username) {
		Member member = memberDao.findById(username).orElseThrow(MemberFail.MemberNotFoundException::new);
		return member.toDto();
	}
}
