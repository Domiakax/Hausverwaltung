package serverTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;

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
	
	
	@BeforeAll
	static void setUp() {
		final ResourceConfig rc = new ResourceConfig().register(AblesungRessource.class);
		final HttpServer server =
				JdkHttpServerFactory.createHttpServer(URI.create(url), rc);
		System.out.println("Server Ready");
		
	}
	
	@BeforeEach
	void resetClient() {
		target = client.target("http://localhost:8080/test/ablesung");
	}

	@Test
	void addNewKunde() {
		Kunde neu = new Kunde("Huber", "Hans");
		Response response = postNeuerKunde(neu);
		assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
		Kunde kResponse = response.readEntity(Kunde.class);
		assertNotNull(kResponse.getKdnr());
	}
	
	@Test
	void modifyExistingKunde() {
		
	}
	
	private Response postNeuerKunde(Kunde k) {
		return  target.path("neuerKunde").request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(Entity.entity(k, MediaType.APPLICATION_JSON));
	}
	
	private Response () {
		
	}

}
