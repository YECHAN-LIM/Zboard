package com.example.zboard.dao;

import org.apache.ibatis.annotations.*;

public interface AuthorityDao {
	@Insert("insert into authority values(#{username}, #{authorityName})")
	public int insert(@Param("username") String username, @Param("authorityName") String authorityName);

	@Delete("delete from authority where username=#{username} and rownum=1")
	public int deleteByUsername(String username);
}
