package serverTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import server.Ablesung;
import server.Kunde;
import server.Server;

@TestMethodOrder(MethodOrderer.MethodName.class)
class ServerTest {

	private static final String url = "http://localhost:8080/test";
	private static final Client client = ClientBuilder.newClient();
	private WebTarget target = client.target(url);

	private static final String endpointHasuverwaltung = "/hausverwaltung";
	private static final String endpointKunden = "kunden";
	private static final String endpointAblesungen = "ablesungen";
	private static final String endpointAblesungClientStart = "ablesungenVorZweiJahrenHeute";

	private static final Kunde k1_crudTest = new Kunde("C", "c");
	private static final Kunde k2_RangeTest = new Kunde("A", "a");
	private static final Kunde k3_RangeTest = new Kunde("B", "b");

	private static final int lastYear = LocalDate.now().getYear() - 1;
	private static final Ablesung ablesung_crudTest = new Ablesung("1", LocalDate.of(lastYear, 8, 25), k2_RangeTest, "test", false,
			100);
	private static final Ablesung ablesung_kundeDeletedDuringTest = new Ablesung("1", LocalDate.of(lastYear, 12, 1), k1_crudTest, "test", false, 0);;

	private static List<Kunde> kunden;
	private static HashMap<Kunde, List<Ablesung>> ablesungen;
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@BeforeAll
	static void setUp() {
		setUpKundenList();
		Server.startServer(url, false);
	}

	private static void setUpKundenList() {
		kunden = new ArrayList<>();
		kunden.add(k1_crudTest);
		kunden.add(k2_RangeTest);
		kunden.add(k3_RangeTest);
	}

	@AfterAll
	static void shutDown() {
		Server.stopServer(false);
	}

	@AfterEach
	void prepareNextTest() {

	}

	@BeforeEach
	void resetClient() {
		target = client.target(url.concat(endpointHasuverwaltung));
	}

	@Test
	@DisplayName("Kunden werden erfolgreich via POST an den Server gesendet")
	void t01_createNewKunden() {
		for (Kunde k : kunden) {
			Response response = postNeuerKunde(k);
			assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
			Kunde kResponse = response.readEntity(Kunde.class);
			assertNotNull(kResponse.getId());
			k.setId(kResponse.getId());
		}
		setUpForRangeTest();
	}

	private Response postNeuerKunde(Kunde k) {
		return target.path(endpointKunden).request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(k, MediaType.APPLICATION_JSON));
	}

	private void setUpForRangeTest() {
		ablesungen = new HashMap<>();
		for (Kunde k : kunden) {
			ablesungen.put(k, new ArrayList<>());
		}
		int jahr = LocalDate.now().getYear() - 1;
		Ablesung a1 = new Ablesung("1", LocalDate.of(jahr, 1, 1), k2_RangeTest, "test", false, 0);
		Ablesung a2 = new Ablesung("1", LocalDate.of(jahr, 4, 1), k2_RangeTest, "test", false, 0);
		Ablesung a3 = new Ablesung("1", LocalDate.of(jahr, 8, 1), k2_RangeTest, "test", false, 0);
		Ablesung a4 = new Ablesung("1", LocalDate.of(jahr, 12, 1), k2_RangeTest, "test", false, 0);
		Ablesung a5 = new Ablesung("1", LocalDate.of(jahr, 1, 1), k3_RangeTest, "test", false, 0);
		Ablesung a6 = new Ablesung("1", LocalDate.of(jahr, 4, 1), k3_RangeTest, "test", false, 0);
		Ablesung a7 = new Ablesung("1", LocalDate.of(jahr, 8, 1), k3_RangeTest, "test", false, 0);
		Ablesung a8 = new Ablesung("1", LocalDate.of(jahr, 12, 1), k3_RangeTest, "test", false, 0);
		Ablesung notAtClientStart = new Ablesung("1", LocalDate.of(jahr - 3, 8, 1), k2_RangeTest, endpointAblesungen,
				false, 0);
		ablesungen.get(k2_RangeTest).add(a1);
		ablesungen.get(k2_RangeTest).add(a2);
		ablesungen.get(k2_RangeTest).add(a3);
		ablesungen.get(k2_RangeTest).add(a4);
		ablesungen.get(k3_RangeTest).add(a5);
		ablesungen.get(k3_RangeTest).add(a6);
		ablesungen.get(k3_RangeTest).add(a7);
		ablesungen.get(k3_RangeTest).add(a8);
		ablesungen.get(k2_RangeTest).add(notAtClientStart);
		ablesungen.get(k1_crudTest).add(ablesung_crudTest);
		ablesungen.get(k1_crudTest).add(ablesung_kundeDeletedDuringTest);
	}

	@Test
	@DisplayName("Senden eines nicht gültigen Kunden-Objekts via POST führt zu Bad-Request")
	void t02_createNewKundeFailsIfKundeIsNull() {
		Response re = postNeuerKunde(null);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), re.getStatus());
		assertFalse(re.readEntity(String.class).isBlank());
	}

	@Test
	@DisplayName("Daten eines bestehenden Kunde können erfolgreich via PUT geändert werden")
	void t03_updateExistingKunde() {
		String newName = "Aa";
		k1_crudTest.setName(newName);
		Response re = target.path(endpointKunden).request(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN)
				.put(Entity.entity(k1_crudTest, MediaType.APPLICATION_JSON));
		assertEquals(Response.Status.OK.getStatusCode(), re.getStatus());
		assertFalse(re.readEntity(String.class).isBlank());
	}
	

	@Test
	@DisplayName("Senden eines nicht bestehenden Kunden-Objekts via PUT führt zu Not-Found")
	void t04_updateNonExisitngKundeFails() {
		Kunde notInServer = new Kunde("Not", "Found");
		Response re = target.path(endpointKunden).request(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN)
				.put(Entity.entity(notInServer, MediaType.APPLICATION_JSON));
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), re.getStatus());
	}
	
	@Test
	@DisplayName("Einzelner Kunde kann vom Server via GET empfangen werden")
	void t05_getSingleKunde() {
		Response re = target.path(endpointKunden.concat("/").concat(k1_crudTest.getId().toString()))
				.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.OK.getStatusCode(), re.getStatus());
		Kunde result = re.readEntity(Kunde.class);
		assertEquals(k1_crudTest, result);
		assertEquals(k1_crudTest.getName(), result.getName());
	}
	
	@Test
	@DisplayName("Wird ein nicht existierender Kunde via GET angefordert, wird mit Not Found geantwortet")
	void t06_getSingleKundeFailsForNonExistingKunde() {
		Response re = target.path(endpointKunden.concat("/null")).request().accept(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), re.getStatus());
		assertFalse(re.readEntity(String.class).isBlank());
	}
	
	@Test
	@DisplayName("Ablesungen können erfolgreich via POST an den Server gesendet werden")
	void t07_createAblesungForKunden() {
		Collection<List<Ablesung>> lists = ablesungen.values();
		for (List<Ablesung> l : lists) {
			for (Ablesung a : l) {
				Response re = postNeueAblesung(a);
				assertEquals(Response.Status.CREATED.getStatusCode(), re.getStatus());

				Ablesung result = re.readEntity(Ablesung.class);
				assertNotNull(result.getId());
				a.setId(result.getId());
			}
		}
	}

	private Response postNeueAblesung(Ablesung a) {
		return target.path(endpointAblesungen).request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(a, MediaType.APPLICATION_JSON));
	}
	
	@Test
	@DisplayName("Senden einer Ablesung mit einem nicht existierenden Kunden via POST führt zu Not Found")
	void t08_createAblesungForNonExistingKundeFails() {
		LocalDate now = LocalDate.now();
		Ablesung a = new Ablesung("1", now, null, "test", false, 0);
		Response re = postNeueAblesung(a);
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), re.getStatus());
		String response = re.readEntity(String.class);
		assertFalse(response.isBlank());
	}

	@Test
	@DisplayName("Senden eines nicht gültigen Ablesungsobjekt führt zu Bad Request")
	void t09_createAblesungFailsIfAblesungIsNull() {
		Response re = postNeueAblesung(null);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), re.getStatus());
		assertFalse(re.readEntity(String.class).isBlank());
	}
	
	@Test
	@DisplayName("Ablesung kann erfolgreich via PUT aktualisiert werden")
	void t10_updateExistingAblesung() {
		final int newZaehlerstand = ablesung_crudTest.getZaehlerstand().intValue() + 100;
		ablesung_crudTest.setZaehlerstand(newZaehlerstand);
		Response re = target.path(endpointAblesungen).request(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN)
				.put(Entity.entity(ablesung_crudTest, MediaType.APPLICATION_JSON));
		assertEquals(Response.Status.OK.getStatusCode(), re.getStatus());
		assertFalse(re.readEntity(String.class).isBlank());
	}

	@Test
	@DisplayName("Senden einer nicht existierenden Ablesung via PUT führt zu Not Found")
	void t11_updateNonExistingAblesungFails() {
		Ablesung a = new Ablesung();
		a.setId(null);
		Response re = target.path(endpointAblesungen).request(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN)
				.put(Entity.entity(a, MediaType.APPLICATION_JSON));
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), re.getStatus());
		assertFalse(re.readEntity(String.class).isBlank());
	}
	
	@Test
	@DisplayName("Einzelne Ablesung kann via GET vom Server empfangen werde")
	void t12_getSingleAblesung() {
		Response re = target.path(endpointAblesungen.concat("/").concat(ablesung_crudTest.getId().toString())).request()
				.accept(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.OK.getStatusCode(), re.getStatus());
		Ablesung result = re.readEntity(Ablesung.class);
		assertEquals(ablesung_crudTest, result);
		assertEquals(ablesung_crudTest.getZaehlerstand(), result.getZaehlerstand());
	}

	@Test
	@DisplayName("Anfordern einer nicht existierenden Ablesung via GET führt zu Not Found")
	void t13_getSingleAblesungFailsForNonExistingAblesung() {
		Response re = target.path(endpointAblesungen.concat("/null")).request()
				.accept(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), re.getStatus());
		assertFalse(re.readEntity(String.class).isBlank());
	}
	
	@Test
	@DisplayName("Ablesung kann erfolgreich gelöscht werden")
	void t14_deleteAblesung() {
		ablesungen.get(k1_crudTest).remove(ablesung_crudTest);
		String aid = ablesung_crudTest.getId().toString();
		Response re = target.path(endpointAblesungen.concat("/").concat(aid)).request()
				.accept(MediaType.APPLICATION_JSON).delete();
		assertEquals(Response.Status.OK.getStatusCode(), re.getStatus());
		Ablesung result = re.readEntity(Ablesung.class);
		assertEquals(ablesung_crudTest, result);
	}

	@Test
	@DisplayName("Wird eine nicht existierende Ablesng versucht zu löschen, wird mit Not Found geantwortet")
	void t15_deleteAblesungFailsForNonExistingAblesung() {
		Response re = target.path(endpointAblesungen.concat("/null")).request().accept(MediaType.APPLICATION_JSON)
				.delete();
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), re.getStatus());
		assertFalse(re.readEntity(String.class).isBlank());
	}
	
	@Test
	@DisplayName("Alle entsprechenden Ablesungen für den Start des Clients können vom Server empfangen werden")
	void t16_getAblesungenForClientStart() {
		Response re = target.path(endpointAblesungClientStart).request().accept(MediaType.APPLICATION_JSON).get();
		List<Ablesung> ablesungenResult = re.readEntity(new GenericType<List<Ablesung>>() {
		});
		for (List<Ablesung> l : ablesungen.values()) {
			l.stream().filter(a -> a.getDatum().isAfter(LocalDate.of(LocalDate.now().getYear() - 2, 1, 1)))
					.forEach(a -> assertTrue(ablesungenResult.contains(a)));
			;
		}
	}

	
	@Test
	@DisplayName("Ein bestehender Kunde kann erfolgreich gelöscht werden")
	void t17_deleteKunde() {
		String k1ID = k1_crudTest.getId().toString();
		kunden.remove(k1_crudTest);
		ablesungen.get(k1_crudTest).forEach(a -> a.setKunde(null));
		Response re = target.path(endpointKunden.concat("/").concat(k1ID)).request().accept(MediaType.APPLICATION_JSON)
				.delete();
		assertEquals(Response.Status.OK.getStatusCode(), re.getStatus());
		Map<Kunde, List<Ablesung>> result = re.readEntity(new GenericType<Map<Kunde, List<Ablesung>>>() {
		});
		assertEquals(1, result.keySet().size());
		assertTrue(result.keySet().contains(k1_crudTest));

		List<Ablesung> ablesungenExpected = ablesungen.get(k1_crudTest);
		List<Ablesung> ablesungenResult = result.get(k1_crudTest);
		assertEquals(ablesungenExpected.size(), ablesungenResult.size());

		for (Ablesung a : ablesungenResult) {
			assertTrue(ablesungenExpected.contains(a));
		}
	}

	@Test
	@DisplayName("Löschen eines nicht existierenden Kunden führt zu Not-Found")
	void t18_deleteKundeFailsForNonExisitingKunde() {
		Response re = target.path(endpointKunden.concat("/null")).request().accept(MediaType.APPLICATION_JSON).delete();
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), re.getStatus());
		assertFalse(re.readEntity(String.class).isBlank());
	}
	
	@Test
	@DisplayName("Ablesung eines gelöschten Kunden ist auf dem Server noch vorhanden")
	void t19_ablesungFromDeletedKundeStillOnServer(){
		Response re = target.path(endpointAblesungen.concat("/").concat(ablesung_kundeDeletedDuringTest.getId().toString())).request().accept(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.OK.getStatusCode(), re.getStatus());
		Ablesung result = re.readEntity(Ablesung.class);
		assertEquals(ablesung_kundeDeletedDuringTest, result);
		assertNull(result.getKunde());
	}
	
	@Test
	@DisplayName("Liste aller Kunden vom Server enthält alle gesendeten Kunden")
	void t20_getEveryKunde() {
		Response re = target.path(endpointKunden).request().accept(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.OK.getStatusCode(), re.getStatus());
		List<Kunde> resopnseKunden = re.readEntity(new GenericType<List<Kunde>>() {
		});
		assertTrue(resopnseKunden.size() == kunden.size());
//		List<Kunde> sortedKunden = kunden.stream().sorted((k1, k2) -> k1.getName().compareTo(k2.getName()))
//				.collect(Collectors.toList());
		for (Kunde k : kunden) {
			assertTrue(resopnseKunden.contains(k));
//			assertEquals(k, resopnseKunden.get(sortedKunden.indexOf(k)));
		}
	}


	@Test
	@DisplayName("Anfordern von bestimmten Ablesungen mit einem üngültigen Datumsformat führt zu Bad Request")
	void t21_getAblesungenFailsWithWrongDateFormat() {
		String wrongDate = "26.09.2022";
		Response re = target.path(endpointAblesungen).queryParam("beginn", wrongDate).request()
				.accept(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), re.getStatus());
		assertFalse(re.readEntity(String.class).isBlank());
	}

	@Test
	@DisplayName("Alle Ablesungen zwischen zwei Datumsvorgaben können erfolgreich vom Server empfangen werden")
	void t22_getEveryAblesungInRangeForSpecificKunde() {
		LocalDate beginn = LocalDate.of(2021, 2, 1);
		LocalDate ende = LocalDate.of(2021, 9, 1);
		List<Ablesung> filter = ablesungen.get(k2_RangeTest).stream()
				.filter(x -> x.getDatum().isAfter(beginn) && x.getDatum().isBefore(ende)).collect(Collectors.toList());
		String kid = k2_RangeTest.getId().toString();
		String beginnString = beginn.format(dateFormatter);
		String endeString = ende.format(dateFormatter);
		Response re = target.path(endpointAblesungen).queryParam("kunde", kid).queryParam("beginn", beginnString)
				.queryParam("ende", endeString).request().accept(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.OK.getStatusCode(), re.getStatus());
		List<Ablesung> result = re.readEntity(new GenericType<List<Ablesung>>() {
		});
		assertEquals(filter.size(), result.size());
		for (Ablesung a : filter) {
			assertTrue(result.contains(a));
		}
	}

	@Test
	@DisplayName("Alle Ablesungen ab einem bestimmten Ablesungsdatum können erfolgreich empfangen werden")
	void t23_getEveryAblesungSinceSpecificDate() {
		LocalDate beginn = LocalDate.of(2021, 2, 1);
		List<Ablesung> result = new ArrayList<>();
		for (List<Ablesung> toFilter : ablesungen.values()) {
			for (Ablesung a : toFilter) {
				if (a.getDatum().isAfter(beginn)) {
					result.add(a);
				}
			}
		}
		String beginnString = beginn.format(dateFormatter);
		Response re = target.path(endpointAblesungen).queryParam("beginn", beginnString).request()
				.accept(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.OK.getStatusCode(), re.getStatus());
		List<Ablesung> resultGot = re.readEntity(new GenericType<List<Ablesung>>() {
		});
		assertEquals(result.size(), resultGot.size());
		for (Ablesung a : result) {
			assertTrue(resultGot.contains(a));
		}
	}

	@Test
	@DisplayName("Alle Ablesungen bis zu einem bestimmten Ablesungsdatum können erfolgreich vom Server empfangen werden")
	void t24_getEveryAblesungUntilSpecificDate() {
		LocalDate ende = LocalDate.of(LocalDate.now().getYear() -1, 4, 1);
		List<Ablesung> result = new ArrayList<>();
		for (List<Ablesung> toFilter : ablesungen.values()) {
			for (Ablesung a : toFilter) {
				if (a.getDatum().isBefore(ende)) {
					result.add(a);
				}
			}
		}
		String endeString = ende.format(dateFormatter);
		Response re = target.path(endpointAblesungen).queryParam("ende", endeString).request()
				.accept(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.OK.getStatusCode(), re.getStatus());
		List<Ablesung> resultGot = re.readEntity(new GenericType<List<Ablesung>>() {
		});
		assertEquals(result.size(), resultGot.size());
		for (Ablesung a : result) {
			assertTrue(resultGot.contains(a));
		}
	}

	@Test
	@DisplayName("Wird der Server beendet und der Datenbestand soll gespeichert werden, so kann der Server mit diesem Datenbestand wieder gestartet werden")
	void t25_loadFromFileWorks() {
		Server.stopServer(true);
		assertThrows(ProcessingException.class, () -> postNeuerKunde(new Kunde("Fehler", "Fehler")));
		Server.startServer(url, true);
		Response re = target.path(endpointKunden).request().accept(MediaType.APPLICATION_JSON).get();
		List<Kunde> kundenFromServer = re.readEntity(new GenericType<List<Kunde>>() {
		});
		assertEquals(kunden.size(), kundenFromServer.size());
		for (Kunde k : kunden) {
			assertTrue(kundenFromServer.contains(k));
		}
		resetClient();
		re = target.path(endpointAblesungen).request().accept(MediaType.APPLICATION_JSON).get();
		List<Ablesung> ablesungenFromServer = re.readEntity(new GenericType<List<Ablesung>>() {
		});
		Collection<List<Ablesung>> lists = ablesungen.values();
		AtomicInteger counter = new AtomicInteger();
		lists.stream().forEach(x -> x.stream().forEach(y -> {
			assertTrue(ablesungenFromServer.contains(y));
			counter.getAndIncrement();
		}));
		assertTrue(ablesungenFromServer.size() == counter.get());
	}

}
