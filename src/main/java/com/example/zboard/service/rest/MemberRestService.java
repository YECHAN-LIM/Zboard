package com.example.zboard.service.rest;

import java.io.*;
import java.util.*;

import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.*;
import org.springframework.web.multipart.*;

import com.example.zboard.dao.*;
import com.example.zboard.data.dto.*;
import com.example.zboard.data.member.*;
import com.example.zboard.exception.*;
import com.example.zboard.util.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class MemberRestService {
	private final MemberDao memberDao;
	private final AuthorityDao authorityDao;
	private final MemberCheckCodeDao memberCheckCodeDao;
	private final PasswordEncoder passwordEncoder;
	private final MailUtil mailUtil;
	
	@Value("${profile.uploadFolder}")
	private String profileFolder;
	
	@Transactional(readOnly=true)
	public void idCheck(String username) {
		if(memberDao.existsById(username)==true)
			throw new MemberFail.MemberExistException();
	}
	
	@Transactional(readOnly=true)
	public void emailCheck(String email) {
		if(memberDao.existsByEmail(email)==true)
			throw new MemberFail.EmailExistException();
	}
	
	@Transactional(readOnly=true)
	public String findId(String email) {
		return memberDao.findByEmail(email).orElseThrow(MemberFail.MemberNotFoundException::new).getUsername();
	}
	
	@Transactional
	public void join(MemberDto.Join dto) {
		Member member = dto.toEntity();
		String profile = "default.png";
		MultipartFile sajin = dto.getSajin();
		if(Objects.isNull(sajin)==false || sajin.isEmpty()==false) {
			int lastPositionOfDot = sajin.getOriginalFilename().lastIndexOf("."); 
			String ext = sajin.getOriginalFilename().substring(lastPositionOfDot);
			
			File file = new File(profileFolder, dto.getUsername() + ext);
			try {
				FileCopyUtils.copy(sajin.getBytes(), file);
				profile = dto.getUsername() + ext;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		String checkcode = RandomStringUtils.randomAlphanumeric(20);
		String encodedPasssword = passwordEncoder.encode(member.getPassword());
		member.setProfile(profile).setPassword(encodedPasssword);
		memberDao.insert(member);
		authorityDao.insert(member.getUsername(), "ROLE_USER");
		memberCheckCodeDao.insert(member.getUsername(), checkcode);
		
		System.out.println(checkcode);
		
		mailUtil.sendJoinCheckMail("admin@zmall.com", member.getEmail(), checkcode);
	}

	public void update(MemberDto.Update dto) {
		Member member = memberDao.findById(dto.getUsername()).orElseThrow(MemberFail.MemberNotFoundException::new);
		Member params = Member.builder().username(dto.getUsername()).build();
		if(dto.getEmail()!=null)
			params.setEmail(dto.getEmail());
		if(dto.getIrum()!=null)
			params.setIrum(dto.getIrum());
		if(dto.getPassword()!=null && dto.getNewPassword()!=null) {
			if(passwordEncoder.matches(dto.getPassword(), member.getPassword())==false)
				throw new MemberFail.PasswordCheckFailException();
			params.setPassword(passwordEncoder.encode(dto.getNewPassword()));
		}
		memberDao.insert(params);
	}

}
