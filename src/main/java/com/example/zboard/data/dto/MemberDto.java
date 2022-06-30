package com.example.zboard.data.dto;

import java.time.*;
import java.util.*;

import org.springframework.format.annotation.*;
import org.springframework.web.multipart.*;

import com.example.zboard.data.member.*;

import lombok.*;
import lombok.experimental.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberDto {
	@Data
	public static class Join {
		private String username;
		private String password;
		private String email;
		private String irum;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private LocalDate birthday;
		private MultipartFile sajin;
		public Member toEntity() {
			return Member.builder().username(username).password(password).email(email).irum(irum).birthday(birthday).build();
		}
	}
	
	@Data
	@Accessors(chain = true)
	public static class Update {
		private String username;
		private String password;
		private String newPassword;
		private String email;
		private String irum;
		private MultipartFile sajin;
	}
	
	@Data
	public class ResetPwd {
		private String username;
		private String email;
	}
	
	@Data
	public static class ReadWithAuthority {
		private String username;
		private String password;
		private boolean enabled;
		private List<String> roles;
	}
	
	@Data
	@Builder
	public static class Read {
		private String username;
		private String irum;
		private String email;
		private String birthday;
		private String joinday;
		private long days;
		private String level;
		private String profile;
	}
}
