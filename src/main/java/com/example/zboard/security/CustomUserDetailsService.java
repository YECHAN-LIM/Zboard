package com.example.zboard.security;

import java.util.*;
import java.util.stream.*;

import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import com.example.zboard.dao.*;
import com.example.zboard.data.dto.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
	private final MemberDao memberDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberDto.ReadWithAuthority member = memberDao.readWithAuthority(username).orElseThrow(()->new InternalAuthenticationServiceException("USER NOT FOUND"));
		Account account = Account.builder().username(member.getUsername()).password(member.getPassword()).isEnabled(member.isEnabled()).build();
		Collection<GrantedAuthority> authorities = member.getRoles().stream().map(a->new SimpleGrantedAuthority(a)).collect(Collectors.toList());
		account.setAuthorities(authorities);
		return account;
	}
}
