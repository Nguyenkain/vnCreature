package com.example.vncreatures.common;

public class ServerConfig {
	public static final String ROOT = "http://192.168.137.66";
	public static final String GET_ALL_CREATURE = ROOT + "/webservice/webservice.php?getAllNameCreature&format=json";
	
	public static final int TIMEOUT = 30000; // 30 seconds
}
