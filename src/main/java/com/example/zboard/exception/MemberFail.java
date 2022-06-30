  package com.example.zboard.exception;

public class MemberFail {
	public static class EmailExistException extends RuntimeException {
	}

	public static class MemberExistException extends RuntimeException {
	}
	
	public static class PasswordCheckFailException extends RuntimeException {
	}
	
	public static class MemberNotFoundException extends RuntimeException {
	}
	
	public static class CheckCodeFailException extends RuntimeException {
	}
}
