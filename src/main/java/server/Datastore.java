package server;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Datastore {

	private static Datastore datastore = null;
	private static ConcurrentHashMap<Kunde, List<Ablesung>> database_kundeToAblesung;
	private static ConcurrentHashMap<UUID, Kunde> database_kunde;
	private static ConcurrentHashMap<UUID, Ablesung> database_ablesung;
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final Path filePathKunden = Paths.get("target", "database.json");
	private static final LocalDate ablesungGrenze = LocalDate.of(LocalDate.now().getYear() - 2, 1, 1);
	// private static ConcurrentHashMap<UUID, Long> lastWrite;

	public static Datastore getDataStore() {
		if (datastore == null) {
			datastore = new Datastore();
		}
		return datastore;
	}

	private Datastore() {
		database_kundeToAblesung = new ConcurrentHashMap<>();
		database_kunde = new ConcurrentHashMap<>();
		database_ablesung = new ConcurrentHashMap<UUID, Ablesung>();
//		lastWrite = new ConcurrentHashMap<UUID, Long>();
	}

	public Kunde addNewKunde(Kunde k) {
		// Vektor wie ArrayList nur synchronisiert
		try {
			saveKunde(k);
//			UUID kid = k.getKdnr();
			database_kundeToAblesung.put(k, Collections.synchronizedList(new ArrayList<Ablesung>()));
//			aktualisiereLastWrite(kid);
			return k;
		} catch (NullPointerException e) {
			return null;
		}
	}

	private synchronized void saveKunde(Kunde k) {
		UUID kid = UUID.randomUUID();
		while (database_kunde.containsKey(kid)) {
			kid = UUID.randomUUID();
		}
		k.setId(kid);
		database_kunde.put(kid, k);
	}

	public Kunde getKunde(String id) {
		try {
			UUID uuid = UUID.fromString(id);
			return database_kunde.get(uuid);
		} catch (Exception e) {
			return null;
		}
	}

	public boolean modifyKunde(Kunde toUpdate) {
		try {
			UUID toSearchId = toUpdate.getId();
			Kunde stored = database_kunde.get(toSearchId);
			if (stored == null) {
				return false;
			}
			stored.updateKunde(toUpdate);
//			aktualisiereLastWrite(stored.getKdnr());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Ablesung postAblesung(Ablesung a) {
		try {
			Kunde kid = a.getKunde();
			if (!database_kundeToAblesung.containsKey(kid)) {
				return null;
			}
			saveAblesung(a);

			List<Ablesung> ablesungen = database_kundeToAblesung.get(kid);
			ablesungen.add(a);
//			aktualisiereLastWrite(kid);
			return a;
		} catch (NullPointerException e) {
			return null;
		}
	}

	private synchronized void saveAblesung(Ablesung a) {
		UUID id = UUID.randomUUID();
		while (database_ablesung.containsKey(id)) {
			id = UUID.randomUUID();
		}
		a.setId(id);
		database_ablesung.put(id, a);
	}

	public boolean modifyExistingAblesung(Ablesung a) {
		try {
			UUID toSearch = a.getId();
			UUID kid = a.getKunde().getId();
			// UngÃ¼ltiger Kunde
			if (!database_kunde.containsKey(kid)) {
				return false;
			}
			Ablesung toUpdate = database_ablesung.get(toSearch);
			toUpdate.updateAblesung(a);
//			aktualisiereLastWrite(kid);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

//	public Long getLastWrite(String id) {
//		try {
//			UUID toSearch = UUID.fromString(id);
//			return lastWrite.get(toSearch);
//		} catch (Exception e) {
//			return null;
//		}
//	}

	public List<Kunde> getCopyOfEveryKunde() {
		System.out.println("Copy");
		List<Kunde> result = database_kunde.values().stream().sorted((k1, k2) -> k1.getName().compareTo(k2.getName()))
				.collect(Collectors.toList());
		for (Kunde k : result) {
			System.out.println(k);
		}
		System.out.println("Copy Ende");
		return result;
	}

//	private void aktualisiereLastWrite(UUID kid) {
//		lastWrite.put(kid, System.currentTimeMillis());
//	}

	public Ablesung deleteAblesung(String id) {
		try {
			UUID aid = UUID.fromString(id);
			Ablesung a = database_ablesung.remove(aid);
			if (a == null) {
				return null;
			}
			Kunde kid = a.getKunde();
			if (kid != null) {
				database_kundeToAblesung.get(kid).remove(a);
			}
			return a;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	public void saveToFile() {
		File file = filePathKunden.toFile();
//		System.out.println(file.exists());
//		System.out.println(file.getAbsolutePath());
		try {
//			mapper.setDateFormat();
			mapper.writerWithDefaultPrettyPrinter().writeValue(file, database_kundeToAblesung);
			System.out.println("File written");
		} catch (Exception e) {
//			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadFromFile() {
		if (datastore == null) {
			return;
		}
		File file = filePathKunden.toFile();
		if (file.exists()) {
			try {
				System.out.println("HashMap geladen mit size:");
				TypeReference<ConcurrentHashMap<Kunde, List<Ablesung>>> typeRef = new TypeReference<ConcurrentHashMap<Kunde, List<Ablesung>>>() {
				};
				database_kundeToAblesung = mapper.readValue(file, typeRef);
				System.out.println(database_kundeToAblesung.size());
				for (Kunde k : database_kundeToAblesung.keySet()) {
					database_kunde.put(k.getId(), k);
				}
				for (List<Ablesung> aList : database_kundeToAblesung.values()) {
					aList.forEach(a -> database_ablesung.put(a.getId(), a));
				}
				database_kunde.forEachValue(0, System.out::println);
				database_ablesung.forEachValue(0, System.out::println);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Ablesung getSingleAblesung(String ablesungId) {
		try {
			UUID aid = UUID.fromString(ablesungId);
			return database_ablesung.get(aid);
		} catch (Exception e) {
			return null;
		}
	}

	public List<Ablesung> getAblesungenForClientStart() {
		return database_ablesung.values().stream().filter(a -> a.getDatum().isAfter(ablesungGrenze))
				.collect(Collectors.toList());
	}

	public HashMap<Kunde, List<Ablesung>> deleteKunde(String id) {
		try {
			UUID kid = UUID.fromString(id);
			Kunde kundeToDelete = database_kunde.remove(kid);
			if (kundeToDelete == null) {
				return null;
			}
			List<Ablesung> storedAblesungen = database_kundeToAblesung.remove(kundeToDelete);
			storedAblesungen.stream().forEach(a -> {
				a.setKunde(null);
			});
			HashMap<Kunde, List<Ablesung>> toReturn = new HashMap<>();
			toReturn.put(kundeToDelete, storedAblesungen);
			return toReturn;
		} catch (Exception e) {
			return null;
		}
	}

	public List<Ablesung> getAblesungenFromKunde(String id) {
		try {
			UUID kid = UUID.fromString(id);
			Kunde k = database_kunde.get(kid);
			return database_kundeToAblesung.get(k).stream().sorted((a1, a2) -> a1.getDatum().compareTo(a2.getDatum()))
					.collect(Collectors.toList());
		} catch (Exception e) {
			return null;
		}
	}

	public List<Ablesung> getAblesungenFromKundeSince(String id, LocalDate beginn) {
		try {
			UUID kid = UUID.fromString(id);
			Kunde k = database_kunde.get(kid);
			return database_kundeToAblesung.get(k).stream().filter(x -> x.getDatum().isAfter(beginn))
					.sorted((a1, a2) -> a1.getDatum().compareTo(a2.getDatum())).collect(Collectors.toList());
		} catch (Exception e) {
			return null;
		}
	}

	public List<Ablesung> getAblesungenFromKundeUntil(String id, LocalDate ende) {
		try {
			UUID kid = UUID.fromString(id);
			Kunde k = database_kunde.get(kid);
			return database_kundeToAblesung.get(k).stream().filter(x -> x.getDatum().isBefore(ende))
					.sorted((a1, a2) -> a1.getDatum().compareTo(a2.getDatum())).collect(Collectors.toList());
		} catch (Exception e) {
			return null;
		}
	}

	public List<Ablesung> getAblesungenFromKunde(String id, LocalDate beginn, LocalDate ende) {
		try {
			UUID kid = UUID.fromString(id);
			Kunde k = database_kunde.get(kid);
			return database_kundeToAblesung.get(k).stream()
					.filter(x -> x.getDatum().isAfter(beginn) && x.getDatum().isBefore(ende))
					.sorted((a1, a2) -> a1.getDatum().compareTo(a2.getDatum())).collect(Collectors.toList());
		} catch (Exception e) {
			return null;
		}
	}

	public List<Ablesung> getAblesungen(LocalDate beginn, LocalDate ende) {
		return database_ablesung.values().stream()
				.filter(x -> x.getDatum().isAfter(beginn) && x.getDatum().isBefore(ende))
				.sorted((a1, a2) -> a1.getDatum().compareTo(a2.getDatum())).collect(Collectors.toList());
	}

	public List<Ablesung> getAblesungenSince(LocalDate beginn) {
		return database_ablesung.values().stream().filter(x -> x.getDatum().isAfter(beginn))
				.sorted((a1, a2) -> a1.getDatum().compareTo(a2.getDatum())).collect(Collectors.toList());
	}

	public List<Ablesung> getAblesungUntil(LocalDate end) {
		return database_ablesung.values().stream().filter(x -> x.getDatum().isBefore(end))
				.sorted((a1, a2) -> a1.getDatum().compareTo(a2.getDatum())).collect(Collectors.toList());
	}

}
