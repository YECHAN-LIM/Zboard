package com.example.zboard.util;

import org.springframework.http.MediaType;

public class ZBoardUtil {
	public static String getProfileName(String username, String originalFileName) {
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		return username + extension.toUpperCase();
	}
	
	public static MediaType getMediaType(String fileName) {
		String extension = fileName.substring(fileName.lastIndexOf(".")).toUpperCase();
		MediaType type = MediaType.IMAGE_JPEG;
		if(extension.equals("PNG"))
			type = MediaType.IMAGE_PNG;
		else if(extension.equals("GIF"))
			type=MediaType.IMAGE_GIF;
		return type;
	}
}
