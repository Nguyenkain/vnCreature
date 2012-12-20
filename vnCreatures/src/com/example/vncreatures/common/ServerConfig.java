package com.example.vncreatures.common;

public class ServerConfig {
	public static final String ROOT = "http://192.168.137.120";
	public static final String IMAGE_PATH = ROOT
			+ "/webData/pictures/insect/%s.jpg";

	public static final String GET_ALL_CREATURE = ROOT
			+ "/webservice/webservice.php?getAllNameCreature&format=json";
	public static final String GET_ALL_CREATURE_BY_NAME = ROOT
			+ "/webservice/webservice.php?getCreatureByName&format=json";
	public static final String GET_CREATURE_BY_ID = ROOT
			+ "/webservice/webservice.php?getCreatureById&format=json";

	public static final String NUM_PER_PAGE = "10";
	public static final int TIMEOUT = 30000; // 30 seconds
}
