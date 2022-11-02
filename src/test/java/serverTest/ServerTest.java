package serverTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

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

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class ServerTest {

	private static final String url = "http://localhost:8080/test";
	private static final Client client = ClientBuilder.newClient();
	private WebTarget target = client.target(url);

	private static final String endpointKunden = "kunden";
	private static final String endpointAblesungen = "ablesungen";

	private static final Kunde k1 = new Kunde("C", "c");
	private static final Kunde k2_RangeTest = new Kunde("A", "a");
	private static final Kunde k3_RangeTest = new Kunde("B", "b");

	private static List<Kunde> kunden;
	private static HashMap<Kunde, List<Ablesung>> ablesungen;
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@BeforeAll
	static void setUp() {
		setUpKundenList();
		Server.startServer(url, false);
		System.out.println("Server Ready");
	}

	@AfterAll
	static void shutDown() {
		Server.stopServer(false);
		System.out.println("Test fertig");
	}

	private static void setUpKundenList() {
		kunden = new ArrayList<>();
		kunden.add(k1);
		kunden.add(k2_RangeTest);
		kunden.add(k3_RangeTest);

	}

	private static void setUpForRangeTest() {
		ablesungen = new HashMap<>();
		for (Kunde k : kunden) {
			ablesungen.put(k, new ArrayList<>());
		}
		Ablesung a1 = new Ablesung("1", LocalDate.of(2021, 1, 1), k2_RangeTest, "test", false, 0);
		Ablesung a2 = new Ablesung("1", LocalDate.of(2021, 4, 1), k2_RangeTest, "test", false, 0);
		Ablesung a3 = new Ablesung("1", LocalDate.of(2021, 8, 1), k2_RangeTest, "test", false, 0);
		Ablesung a4 = new Ablesung("1", LocalDate.of(2021, 12, 1), k2_RangeTest, "test", false, 0);
		Ablesung a5 = new Ablesung("1", LocalDate.of(2021, 1, 1), k3_RangeTest, "test", false, 0);
		Ablesung a6 = new Ablesung("1", LocalDate.of(2021, 4, 1), k3_RangeTest, "test", false, 0);
		Ablesung a7 = new Ablesung("1", LocalDate.of(2021, 8, 1), k3_RangeTest, "test", false, 0);
		Ablesung a8 = new Ablesung("1", LocalDate.of(2021, 12, 1), k3_RangeTest, "test", false, 0);
		ablesungen.get(k2_RangeTest).add(a1);
		ablesungen.get(k2_RangeTest).add(a2);
		ablesungen.get(k2_RangeTest).add(a3);
		ablesungen.get(k2_RangeTest).add(a4);
		ablesungen.get(k3_RangeTest).add(a5);
		ablesungen.get(k3_RangeTest).add(a6);
		ablesungen.get(k3_RangeTest).add(a7);
		ablesungen.get(k3_RangeTest).add(a8);
	}

	@BeforeEach
	void resetClient() {
		target = client.target("http://localhost:8080/test/hausverwaltung");
	}

	@Test
	void t01_addNewKunden() {
		for (Kunde k : kunden) {
			Response response = postNeuerKunde(k);
			assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
			Kunde kResponse = response.readEntity(Kunde.class);
			assertNotNull(kResponse.getKdnr());
			k.setKdnr(kResponse.getKdnr());
		}
		setUpForRangeTest();
	}

	private Response postNeuerKunde(Kunde k) {
		return target.path(endpointKunden).request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(k, MediaType.APPLICATION_JSON));
	}

	@Test
	void t02_1_addNewKundeFailsIfKundeIsNull() {
		Response re = postNeuerKunde(null);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), re.getStatus());
		assertFalse(re.readEntity(String.class).isBlank());
	}

	@Test
	void t02_getEveryKunde() {
		Response re = target.path(endpointKunden).request().accept(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.OK.getStatusCode(), re.getStatus());
		List<Kunde> resopnseKunden = re.readEntity(new GenericType<List<Kunde>>() {
		});
		List<Kunde> sortedKunden = kunden.stream().sorted((k1, k2) -> k1.getName().compareTo(k2.getName()))
				.collect(Collectors.toList());
		for (Kunde k : sortedKunden) {
			assertTrue(resopnseKunden.contains(k));
			assertEquals(k, resopnseKunden.get(sortedKunden.indexOf(k)));
		}
	}

	@Test
	void t03_getSingleKunde() {
		Response re = target.path(endpointKunden.concat("/").concat(k1.getKdnr().toString()))
				.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.OK.getStatusCode(), re.getStatus());
		Kunde result = re.readEntity(Kunde.class);
		assertEquals(k1, result);
	}

	@Test
	void t04_getSingleKundeFails() {
		Response re = target.path(endpointKunden.concat("/null")).request().accept(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), re.getStatus());
		assertFalse(re.readEntity(String.class).isBlank());
	}

	@Test
	void t05_modifyExistingKunde() {
		String newName = "Aa";
		k1.setName(newName);
		Response re = target.path(endpointKunden).request(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN)
				.put(Entity.entity(k1, MediaType.APPLICATION_JSON));
		assertEquals(Response.Status.OK.getStatusCode(), re.getStatus());
		resetClient();
		String path = endpointKunden.concat("/").concat(k1.getKdnr().toString());
		re = target.path(path).request().accept(MediaType.APPLICATION_JSON).get();
		Kunde responseKunde = re.readEntity(Kunde.class);
		assertEquals(newName, responseKunde.getName());
	}

	@Test
	void t06_modifyNotExisitngKundeFails() {
		Kunde notInServer = new Kunde("No", "No");
		Response re = target.path(endpointKunden).request(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN)
				.put(Entity.entity(notInServer, MediaType.APPLICATION_JSON));
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), re.getStatus());
	}

	@Test
	void t07_createAblesungForKunde() {
		LocalDate d = LocalDate.of(2022, 8, 25);
		Ablesung a1 = new Ablesung("1", d, k1, "test", false, 100);
		ablesungen.get(k1).add(a1);
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
//		List<Ablesung> k1Ablesungen = ablesungen.get(k1);
//		k1Ablesungen.add(result);
	}

	private Response postNeueAblesung(Ablesung a) {
		return target.path(endpointAblesungen).request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(a, MediaType.APPLICATION_JSON));
	}

	@Test
	void t08_createAblesungForNotExistingKundeFails() {
		LocalDate now = LocalDate.now();
		Ablesung a = new Ablesung("1", now, null, "test", false, 0);
		Response re = postNeueAblesung(a);
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), re.getStatus());
		String response = re.readEntity(String.class);
		assertFalse(response.isBlank());
	}

	@Test
	void t09_modifyExistingAblesung() {
		Ablesung a = ablesungen.get(k1).get(0);
		final int newZaehlerstand = a.getZaehlerstand().intValue() + 100;
		a.setZaehlerstand(newZaehlerstand);
		Response re = target.path(endpointAblesungen).request(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN)
				.put(Entity.entity(a, MediaType.APPLICATION_JSON));
		assertEquals(Response.Status.OK.getStatusCode(), re.getStatus());
		resetClient();
		Response re2 = target.path(endpointAblesungen.concat("/").concat(a.getId().toString())).request()
				.accept(MediaType.APPLICATION_JSON).get();
		Ablesung result = re2.readEntity(Ablesung.class);
		assertEquals(a.getZaehlerstand(), result.getZaehlerstand());
	}

	@Test
	void t10_modifyNotExistingAblesungFails() {
		Ablesung a = new Ablesung();
		a.setId(null);
		Response re = target.path(endpointAblesungen).request(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN)
				.put(Entity.entity(a, MediaType.APPLICATION_JSON));
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), re.getStatus());
		assertFalse(re.readEntity(String.class).isBlank());
	}

	@Test
	void t11_deleteAblesung() {
		Ablesung stored = ablesungen.get(k1).remove(0);
		String aid = stored.getId().toString();
		Response re = target.path(endpointAblesungen.concat("/").concat(aid)).request()
				.accept(MediaType.APPLICATION_JSON).delete();
		assertEquals(Response.Status.OK.getStatusCode(), re.getStatus());
		Ablesung result = re.readEntity(Ablesung.class);
		assertEquals(stored, result);
		re = target.path(endpointAblesungen.concat("/").concat(stored.getId().toString())).request()
				.accept(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), re.getStatus());
		assertFalse(re.readEntity(String.class).isBlank());
	}

	@Test
	void t12_deleteAblesungFailsForNoneExistingAblesung() {
		Response re = target.path(endpointAblesungen.concat("/null")).request().accept(MediaType.APPLICATION_JSON)
				.delete();
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), re.getStatus());
		assertFalse(re.readEntity(String.class).isBlank());
	}

	@Test
	void t13_getAblesungenFailsWithWrongDateFormat() {
		String wrongDate = "26.09.2022";
		Response re = target.path(endpointAblesungen).queryParam("beginn", wrongDate).request()
				.accept(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), re.getStatus());
		assertFalse(re.readEntity(String.class).isBlank());
	}

	@Test
	void t14_getEveryAblesungInRangeForSpecificKunde() {
		LocalDate beginn = LocalDate.of(2021, 2, 1);
		LocalDate ende = LocalDate.of(2021, 9, 1);
		List<Ablesung> filter = ablesungen.get(k2_RangeTest).stream()
				.filter(x -> x.getDatum().isAfter(beginn) && x.getDatum().isBefore(ende)).collect(Collectors.toList());
		String kid = k2_RangeTest.getKdnr().toString();
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
	void t15_getEveryAblesungSince() {
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
	void t16_getEveryAblesungUntil() {
		LocalDate ende = LocalDate.of(2021, 7, 1);
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
	void t17_loadFromFileWorks() {
		Server.stopServer(true);
		Server.startServer(url, true);
		Response re = target.path(endpointKunden).request().accept(MediaType.APPLICATION_JSON).get();
		List<Kunde> kundenFromServer = re.readEntity(new GenericType<List<Kunde>>() {});
		assertEquals(kunden.size(), kundenFromServer.size());
		for(Kunde k : kunden) {
			assertTrue(kundenFromServer.contains(k));
		}
		resetClient();
		re = target.path(endpointAblesungen).request().accept(MediaType.APPLICATION_JSON).get();
		List<Ablesung> ablesungenFromServer = re.readEntity(new GenericType<List<Ablesung>>() {});
		Collection<List<Ablesung>> lists = ablesungen.values();
		AtomicInteger counter = new AtomicInteger();
		lists.stream().forEach(x -> x.stream().forEach(y -> {
			assertTrue(ablesungenFromServer.contains(y));
			counter.getAndIncrement();
		}));
		assertTrue(ablesungenFromServer.size() == counter.get());
	}

}
