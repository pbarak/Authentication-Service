package authentication;



import java.util.List;
import java.util.Map;

import zookeeper.Configuration;

public class Systems {
	//TODO Look for all systems and return the one where id=system identifier
	public static  Map getSystem(String id) {				
		List<Map> systems=Configuration.getSystems();
		for(Map system:systems) {
			System.out.println(system.get("identifier"));
			if (id.equals(system.get("identifier").toString())) {
				return system;
				}
		}
		return null;
		
	}

}
