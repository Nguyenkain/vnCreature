package com.example.vncreatures.common;

public class ServerConfig {
	public static final String ROOT = "http://113.164.1.45";
	// public static final String ROOT = "http://192.168.137.1";
	public static final String IMAGE_PATH = ROOT
			+ "/website/images/pictures/%s/%s.jpg";

	// SEARCH CREATURE
	public static final String GET_CREATURE = ROOT
			+ "/refactor/public/user/get-creature/";

	public static final String GET_GROUP = ROOT
			+ "/refactor/public/user/filter/";

	// NEWS
	public static final String GET_CAREGORY = ROOT
			+ "/refactor/public/user/get-category";
	public static final String GET_NEWS = ROOT
			+ "/refactor/public/user/get-news/";
	public static final String NEWS_IMAGE_PATH = ROOT
			+ "/website/images/forumpic/%s.jpg";
	public static final String ICON_PATH = ROOT
			+ "/website/images/forumpic/icon/%s.png";

	// MAP
	public static final String GET_PROVINCE = ROOT
			+ "/refactor/public/user/get-province/";
	public static final String GET_NATIONAL_PARK = ROOT
			+ "/refactor/public/user/get-park/";

	// DISCUSSION
	public static final String ADD_USER = ROOT
			+ "/refactor/public/user/set-user";
	public static final String PROFILE_PICTURE = "http://graph.facebook.com/%s/picture.";
	public static final String GET_THREAD = ROOT
			+ "/refactor/public/user/get-thread";
	public static final String ADD_THREAD = ROOT
			+ "/refactor/public/user/set-thread";
	public static final String ADD_POST = ROOT
			+ "/refactor/public/user/set-post";
	public static final String GET_POST = ROOT
			+ "/refactor/public/user/get-post";
	public static final String GET_NOTIFICATION = ROOT
			+ "/refactor/public/user/get-notification";
	public static final String SET_NOTIFICATION = ROOT
			+ "/refactor/public/user/set-notification";
	public static final String GET_REPORT_TYPE = ROOT
			+ "/refactor/public/user/get-report-type";
	public static final String GET_SUGGESTION = ROOT
            + "/refactor/public/user/suggest";
	public static final String ADD_REPORT = ROOT
			+ "/refactor/public/user/set-report";
	public static final String GET_THREAD_IMAGE = ROOT
			+ "/refactor/public/user/get-thread-image";

	public static final String NUM_PER_PAGE = "10";
	public static final int TIMEOUT = 30000; // 30 seconds

}