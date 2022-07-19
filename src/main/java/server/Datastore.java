package server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Datastore {
	
	private static Datastore datastore = null;
	private static ConcurrentHashMap<Kunde, List<Ablesung>> database;
	private static ConcurrentHashMap<UUID, Kunde> database_kunde;
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final Path filePath = Paths.get("src", "main", "resources", "database.json");
	private static final SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy");
	private static long lastWrite;
	
	public static Datastore getDataStore() {
		if(datastore == null) {
			datastore = new Datastore();
			lastWrite = 0;
		}
		return datastore;
	}
	
	private Datastore() {
		database = new ConcurrentHashMap<>();
		database_kunde = new ConcurrentHashMap<>();
		loadFromFile();
	}
	
	public Kunde addNewKunde(Kunde k) {
		//Vektor wie ArrayList nur synchronisiert
		k.setKdnr(UUID.randomUUID());
		while(database.containsKey(k)) {
			k.setKdnr(UUID.randomUUID());
		}
		database.put(k, Collections.synchronizedList(new ArrayList<Ablesung>()));
		database_kunde.put(k.getKdnr(), k);
		System.out.println(k);
		return k;
	}
	
	public Kunde getKunde(String id) {
		try {
			UUID uuid = UUID.fromString(id);
			return database_kunde.get(uuid);
		}catch (Exception e) {
			return null;
		}
	}
	
	public boolean modifyKunde(Kunde toUpdate) {
		List<Ablesung> ablesungen = database.remove(toUpdate);
		if(ablesungen == null) {
			return false;
		}
		database.put(toUpdate, ablesungen);
		return true;
	}
	
	public List<Ablesung> deleteKunde(String id) {
		try {
			UUID uuid = UUID.fromString(id);
			Kunde toSearch = new Kunde();
			toSearch.setKdnr(uuid);
			List<Ablesung> ablesungen = database.remove(toSearch);
			return ablesungen;
		}catch (Exception e) {
			return null;
		}
	}
	
	public boolean postAblesung(Ablesung a) {
		Kunde k = a.getKunde();
		if(!database.containsKey(k)){
			return false;
		}
		List<Ablesung> ablesungen = database.get(k);
		ablesungen.add(a);
		saveToFile();
		aktualisiereLastWrite();
		return true;
	}
	
	public boolean modifyExistingAblesung(Ablesung a) {
		Kunde k = a.getKunde();
		List<Ablesung> ablesungen = database.get(k);
		Boolean updated = false;
		if(ablesungen == null) {
			return updated;
		}
		ablesungen.parallelStream().filter(x -> x.equals(a))
			.forEach(x -> updateAblesung(x ,a, updated));
//		for(Ablesung toUpdate : ablesungen) {
//			if(toUpdate.equals(a)) {
//				toUpdate.updateAblesung(a);
//				updated = true;
//				lastWrite = System.currentTimeMillis();
//			}
//		}
		saveToFile();
		return updated;
	}
	private synchronized void updateAblesung(Ablesung x, Ablesung a, Boolean updated) {
		x.updateAblesung(a);
		updated = true;
		aktualisiereLastWrite();
	}
	
	public long getLastWrite() {
		return lastWrite;
	}
	
	public List<Kunde> getCopyOfEveryKunde(){
		return new ArrayList<Kunde>(database.keySet());
	}
	
	
	private void aktualisiereLastWrite() {
		lastWrite = System.currentTimeMillis();
	}
	
	/**
	 * Von einem Kunden alle Ablesungen nach Datum sortiert
	 * 
	 * @param id
	 * @return null, wenn Kunde nicht existiert
	 */
	public List<Ablesung> getEveryAblesung(String id) {
		try {
			UUID uuid = UUID.fromString(id);
			Kunde toSearch = new Kunde();
			toSearch.setKdnr(uuid); 
			return database.get(toSearch).stream()
					.sorted((a1,a2) -> a1.getDatum().compareTo(a2.getDatum()))
					.collect(Collectors.toList());
		}
		catch(Exception e) {
			return null;
		}
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
