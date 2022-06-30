package com.example.zboard.dao;

import java.util.*;

import org.apache.ibatis.annotations.*;

import lombok.*;

public interface MemberCheckCodeDao {
	@Insert("insert into member_checkcode values(#{username}, #{checkcode})")
	public int insert(@Param("username") String username, @Param("checkcode") String checkcode);
	
	
	@Select("select username from member_checkcode where checkcode=#{checkcode} and rownum=1")
	public Optional<String> findUsernameByCheckcode(String checkcode);
	
	// 가입신청 정보 삭제
	@Delete("delete from member_checkcode where username=#{username} and rownum=1")
	public int deleteByUsername(String username);
	
	// 주기적으로 확인하지 않은 가입신청 모두 삭제
	@Delete("delete from member_checkcode")
	public int deleteAll();

}
