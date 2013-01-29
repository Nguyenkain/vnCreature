package com.example.vncreatures.common;

public class ServerConfig {
    public static final String ROOT = "http://113.164.1.45";
    // public static final String ROOT = "http://192.168.137.1";
    public static final String IMAGE_PATH = ROOT
            + "/webData/pictures/%s/%s.jpg";

    //SEARCH CREATURE
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
            + "/webData/forumpic/%s.jpg";

    // MAP
    public static final String GET_PROVINCE = ROOT
            + "/refactor/public/user/get-province/";

    public static final String NUM_PER_PAGE = "10";
    public static final int TIMEOUT = 30000; // 30 seconds
}