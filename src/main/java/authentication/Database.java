package authentication;

import zookeeper.Configuration;

public class Database {
	public static String getURL() {
		return Configuration.getDBURL();
	}
	
	public static String getUsername() {
		return Configuration.getDBUSER();
	}
	
	public static String getPassword() {
		return Configuration.getDBPASS();
	}

}
