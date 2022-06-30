 package com.example.zboard.data.member;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;

import com.example.zboard.data.dto.*;

import lombok.*;
import lombok.experimental.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain=true)
public class Member {
	private String username;
	private String password;
	private String irum;
	private String email;
	private LocalDate birthday;
	private LocalDate joinday;
	private Integer loginCnt;
	private Integer writeCnt;
	private Integer loginFailCnt;
	private Boolean enabled;
	private Level level;
	private String profile;
	
	public MemberDto.Read toDto() {
		System.out.println(level);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
		return MemberDto.Read.builder().username(username).irum(irum).email(email).birthday(dtf.format(birthday)).joinday(dtf.format(joinday))
			.days(ChronoUnit.DAYS.between(joinday, LocalDate.now())).level(level.name()).profile(profile).build();
	}
}
