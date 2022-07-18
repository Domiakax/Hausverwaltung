package serverTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.util.ArrayList;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.HttpServer;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import server.AblesungRessource;
import server.Kunde;

class ServerTest {
	
	private static final String url = "http://localhost:8080/test";
	private static final Client client = ClientBuilder.newClient();
	private WebTarget target = client.target(url);
	private static ArrayList<Kunde> kunden;
	
	@BeforeAll
	static void setUp() {
		setUpKundenList();
		final ResourceConfig rc = new ResourceConfig().register(AblesungRessource.class);
		final HttpServer server =
				JdkHttpServerFactory.createHttpServer(URI.create(url), rc);
		
		System.out.println("Server Ready");
		
	}
	
	private static void setUpKundenList() {
		kunden = new ArrayList<>();
		Kunde k1 = new Kunde("A", "a");
		Kunde k2 = new Kunde("B", "b");
		Kunde k3 = new Kunde("C", "c");
		kunden.add(k1);
		kunden.add(k2);
		kunden.add(k3);
	}

	@BeforeEach
	void resetClient() {
		target = client.target("http://localhost:8080/test/ablesung");
	}

	@Test
	void addNewKunden() {
		for(Kunde k : kunden) {
			Response response = postNeuerKunde(k);
			assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
			Kunde kResponse = response.readEntity(Kunde.class);
			assertNotNull(kResponse.getKdnr());
		}
	}
	
	@Test
	void modifyExistingKunde() {
		
	}
	
	private Response postNeuerKunde(Kunde k) {
		return  target.path("neuerKunde").request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(Entity.entity(k, MediaType.APPLICATION_JSON));
	}
	
//	private Response () {
//		
//	}

}
