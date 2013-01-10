package com.example.vncreatures.common;

public class ServerConfig {
	public static final String ROOT = "http://192.168.137.1";
	// public static final String ROOT = "http://192.168.1.102";
	public static final String IMAGE_PATH = ROOT
			+ "/webData/pictures/%s/%s.jpg";

	public static final String GET_ALL_CREATURE = ROOT
			+ "/webservice/webservice.php?getAllNameCreature&format=json&kingdom=";
	public static final String GET_ALL_CREATURE_BY_NAME2 = ROOT
			+ "/webservice/webservice.php?getCreatureByName2&format=json&recordperpage=10&page=1&creatureName=&order=&family=&class=";
	public static final String GET_CREATURE_BY_ID = ROOT
			+ "/webservice/webservice.php?getCreatureById&format=json";

	public static final String GET_FAMILY = ROOT
			+ "/webservice/webservice.php?getFamily&format=json&kingdom=1&class=&order=";
	public static final String GET_CLASS = ROOT
			+ "/webservice/webservice.php?getClass&format=json&kingdom=1&family=&order=";
	public static final String GET_ORDER = ROOT
			+ "/webservice/webservice.php?getOrder&format=json&kingdom=1&class=&family=";

	public static final String GET_FAMILY_BY_ID = ROOT
			+ "/webservice/webservice.php?getFamilyById&format=json&id=";
	public static final String GET_CLASS_BY_ID = ROOT
			+ "/webservice/webservice.php?getClassById&format=json&id=";
	public static final String GET_ORDER_BY_ID = ROOT
			+ "/webservice/webservice.php?getOrderById&format=json&id=";

	public static final String NUM_PER_PAGE = "10";
	public static final int TIMEOUT = 30000; // 30 seconds
}