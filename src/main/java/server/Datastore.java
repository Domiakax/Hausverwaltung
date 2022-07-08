package server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Datastore {
	
	private static Datastore datastore = null;
	private static ConcurrentHashMap<Kunde, List<Ablesung>> database;
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final Path filePath = Paths.get("src", "main", "resources", "database.json");
	private static final SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy");
	
	public static Datastore getDataStore() {
		if(datastore == null) {
			datastore = new Datastore();
		}
		return datastore;
	}
	
	private Datastore() {
		database = new ConcurrentHashMap<>();
		loadFromFile();
	}
	
	public Kunde addNewKunde(Kunde k) {
		k.setKdnr(database.size()+1);
		database.put(k, Collections.synchronizedList(new ArrayList<Ablesung>()));
		return k;
	}
	
	public boolean postAblesung(Ablesung a) {
		Kunde k = a.getKunde();
		if(!database.containsKey(k)){
			return false;
		}
		List<Ablesung> ablesungen = database.get(k);
		ablesungen.add(a);
		saveToFile();
		return true;
	}
	
	public boolean modifyExistingAblesung(Ablesung a) {
		Kunde k = a.getKunde();
		List<Ablesung> ablesungen = database.get(k);
		if(ablesungen == null) {
			return false;
		}
		for(Ablesung toUpdate : ablesungen) {
			if(toUpdate.equals(a)) {
				
			}
		}
	}
	
	public boolean deleteAblesung(Ablesung a) {
		
		
	}
	
	private void saveToFile() {
		System.out.println(filePath);
		File file = filePath.toFile();
		System.out.println(file.exists());
		System.out.println(file.getAbsolutePath());
		try {
			mapper.setDateFormat(dateformat);
			mapper.writeValue(file, database);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadFromFile() {
		File file = filePath.toFile();
		if(file.exists()) {
		try {
			System.out.println("HashMap geladen mit size:");
			database = mapper.readValue(file, ConcurrentHashMap.class);
			System.out.println(database.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

}
