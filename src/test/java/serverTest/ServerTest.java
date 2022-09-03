package serverTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.sun.net.httpserver.HttpServer;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import server.Ablesung;
import server.AblesungRessource;
import server.Kunde;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class ServerTest {

	private static final String url = "http://localhost:8080/test";
	private static final Client client = ClientBuilder.newClient();
	private WebTarget target = client.target(url);

	private static final String endpointKunden = "kunden";
	private static final String endpointAblesungen = "ablesungen";
	
	private static final Kunde k1 = new Kunde("C", "c");
	private static final Kunde k2 = new Kunde("A", "a");
	private static final Kunde k3 = new Kunde("B", "b");

	private static List<Kunde> kunden;
	private static HashMap<Kunde, List<Ablesung>> ablesungen;

	@BeforeAll
	static void setUp() {
		setUpKundenList();
		final ResourceConfig rc = new ResourceConfig().register(AblesungRessource.class);
		final HttpServer server = JdkHttpServerFactory.createHttpServer(URI.create(url), rc);

		System.out.println("Server Ready");
	}

	private static void setUpKundenList() {
		kunden = new ArrayList<>();
		kunden.add(k1);
		kunden.add(k2);
		kunden.add(k3);
		ablesungen = new HashMap<>();
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
			ablesungen.put(k, new ArrayList<>());
		}
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
		List<Kunde> sortedKunden = kunden.stream().sorted((k1, k2) -> k1.getName().compareTo(k2.getName())).collect(Collectors.toList());
		for (Kunde k : sortedKunden) {
			assertTrue(resopnseKunden.contains(k));
			assertEquals(k, resopnseKunden.get(sortedKunden.indexOf(k)));
		}
	}

	@Test
	void t03_getSingleKunde() {
		Response re = target.path(endpointKunden.concat("/").concat(k1.getKdnr().toString())).request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.OK.getStatusCode(), re.getStatus());
		Kunde result = re.readEntity(Kunde.class);
		assertEquals(k1, result);
	}

	@Test
	void t04_getSingleKundeFails() {
		Response re = target.path(endpointKunden.concat("/null")).request().accept(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), re.getStatus());
		System.out.println(re.readEntity(String.class));
//		assertFalse(response.isBlank());
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
		Response re = target.path("modifyKunde").request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(notInServer, MediaType.APPLICATION_JSON));
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), re.getStatus());
		System.out.println(re.readEntity(String.class));
	}

	@Test
	void t07_createAblesungForKunde() {
		LocalDate d = LocalDate.of(2022, 8, 25);
		Ablesung a1 = new Ablesung("1", d, k1, "test", false, 100);
		Response re = postNeueAblesung(a1);
		assertEquals(Response.Status.CREATED.getStatusCode(), re.getStatus());
		
		Ablesung result = re.readEntity(Ablesung.class);
		assertNotNull(result.getId());
		List<Ablesung> k1Ablesungen = ablesungen.get(k1);
		k1Ablesungen.add(result);
	}

	private Response postNeueAblesung(Ablesung a) {
		return target.path("postAblesung").request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(a, MediaType.APPLICATION_JSON));
	}

	@Test
	void t08_createAblesungForNotExistingKundeFails() {
		LocalDate now = LocalDate.now();
		Ablesung a = new Ablesung("1", now, null, "test", false, 0);
		Response re = target.path("postAblesung").request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(a, MediaType.APPLICATION_JSON));
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), re.getStatus());
		String response = re.readEntity(String.class);
		assertFalse(response.isBlank());
	}

	@Test
	void t09_modifyExistingAblesung() {
		Ablesung a = ablesungen.get(k1).get(0);
		final int newZaehlerstand = a.getZaehlerstand() + 100;
		a.setZaehlerstand(newZaehlerstand);
		Response re = target.path("modifyAblesung").request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).put(Entity.entity(a, MediaType.APPLICATION_JSON));
		assertEquals(Response.Status.OK.getStatusCode(), re.getStatus());
		resetClient();
		Response re2 = target.path("getSingleAblesung/".concat(a.getId().toString())).request().accept(MediaType.APPLICATION_JSON).get();
		Ablesung result = re2.readEntity(Ablesung.class);
		assertEquals(a.getZaehlerstand(), result.getZaehlerstand());
	}
	
	@Test
	void t10_modifyExistingAblesungFails() {
		Ablesung a = new Ablesung();
		a.setId(null);
		Response re = target.path("modifyAblesung").request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).put(Entity.entity(a, MediaType.APPLICATION_JSON));
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), re.getStatus());
		assertFalse(re.readEntity(String.class).isBlank());
	}
	
	@Test
	void t11_deleteAblesung() {
		Ablesung stored = ablesungen.get(k1).remove(0);
		Response re = target.path("deleteAblesung/".concat(stored.getId().toString())).request().accept(MediaType.APPLICATION_JSON).delete();
		assertEquals(Response.Status.OK.getStatusCode(), re.getStatus());
		//ToDo 
	}
	
	@Test
	void t12_deleteAblesungFailsForNoneExistingAblesung() {
		Response re = target.path("deleteAblesung/null").request().accept(MediaType.APPLICATION_JSON).delete();
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), re.getStatus());
		assertFalse(re.readEntity(String.class).isBlank());
	}

//	private Response () {
//		
//	}

}
