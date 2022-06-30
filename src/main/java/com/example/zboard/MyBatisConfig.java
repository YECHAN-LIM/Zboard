package com.example.zboard;

import javax.sql.*;

import org.apache.ibatis.session.*;
import org.apache.ibatis.type.*;
import org.mybatis.spring.*;
import org.mybatis.spring.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.*;

import com.example.zboard.data.member.*;

@Configuration
@MapperScan(basePackages="com.example.zboard.dao")
public class MyBatisConfig {
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);
		
		// com.example.demo.entity.Board를 board로 줄여쓰고 싶다(마이바티스 TypeAlias)
		// 소문자 board인 이유는 마이바티스의 전통
		// TypeAlias로 등록할 엔티티 클래스의 위치 지정
		sqlSessionFactory.setTypeAliasesPackage("com.example.zboard.data.member");
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	
		// xml 파일의 위치
		sqlSessionFactory.setMapperLocations(resolver.getResources("mapper/*.xml"));
		return sqlSessionFactory.getObject();
	}
	@Bean
	public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}