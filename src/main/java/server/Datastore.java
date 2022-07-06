package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Datastore {
	
	private static Datastore datastore = null;
	private static HashMap<Kunde, List<Ablesung>> database;
	
	public static Datastore getDataStore() {
		if(datastore == null) {
			datastore = new Datastore();
		}
		return datastore;
	}
	
	private Datastore() {
		database = new HashMap<>();
	}
	
	public Kunde addNewKunde(Kunde k) {
		k.setKdnr(database.size()+1);
		database.put(k, new ArrayList<>());
		return k;
	}

}
