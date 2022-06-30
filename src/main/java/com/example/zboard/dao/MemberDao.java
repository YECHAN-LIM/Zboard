package com.example.zboard.dao;

import java.util.*;

import com.example.zboard.data.dto.*;
import com.example.zboard.data.member.*;

public interface MemberDao {
	public boolean existsById(String username);
	
	public boolean existsByEmail(String email);
	
	public Optional<Member> findById(String username);
	
	public int insert(Member member);
	
	public Optional<MemberDto.ReadWithAuthority> readWithAuthority(String username);

	public int update(Member member);

	public int deleteById(String username);

	public Optional<Member> findByEmail(String email);
}
