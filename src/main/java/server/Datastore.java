package server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Datastore {
	
	private static Datastore datastore = null;
	private static HashMap<Kunde, List<Ablesung>> database;
	
	private final File file;
	private static final String filePath = "database.json";
	
	public static Datastore getDataStore() {
		if(datastore == null) {
			datastore = new Datastore();
		}
		return datastore;
	}
	
	private Datastore() {
		database = new HashMap<>();
		file = new File(filePath);
	}
	
	public Kunde addNewKunde(Kunde k) {
		k.setKdnr(database.size()+1);
		database.put(k, new ArrayList<>());
		return k;
	}
	
	public boolean postAblesung(Ablesung a) {
		Kunde k = a.getKunde();
		if(!database.containsKey(k)){
			return false;
		}
		List<Ablesung> ablesungen = database.get(k);
		ablesungen.add(a);
		return true;
	}
	
	private void saveToFile() {
		
	}

}
