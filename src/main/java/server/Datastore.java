package server;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Datastore {
	
	private static Datastore datastore = null;
	private static HashMap<Kunde, List<Ablesung>> database;
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private static final Path filePath = FileSystems.getDefault().getPath("src", "main", "resources", "database.json");
	
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
	
	public boolean postAblesung(Ablesung a) {
		Kunde k = a.getKunde();
		if(!database.containsKey(k)){
			return false;
		}
		List<Ablesung> ablesungen = database.get(k);
		ablesungen.add(a);
		saveToFile();
		loadFromFile();
		return true;
	}
	
	private void saveToFile() {
		File file = filePath.toFile();
		System.out.println(file.exists());
		System.out.println(file.getAbsolutePath());
		try {
			mapper.writeValue(file, database);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadFromFile() {
		File file = filePath.toFile();
		StringBuilder build = new StringBuilder();
		try {
			Files.lines(filePath).forEach(x -> build.append(x));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject obj = new JSONObject(build.toString());
		System.out.println("Keys");
		ArrayList<Kunde> k = new ArrayList();
		obj.keySet().stream().forEach(x-> {
			try {
				k.add(mapper.readValue(x, Kunde.class));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		System.out.println("Kunden: ");
		k.stream().forEach(System.out::println);
	}

}
