package client;

import java.util.Date;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation.Builder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import server.Ablesung;
import server.Kunde;

public class Main {
	public static void main(String[] args) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/test");
		Builder build = target.path("ablesung").path("hello").request(MediaType.TEXT_PLAIN);
		System.out.println(build.get());
		
		target = client.target("http://localhost:8080/test/ablesung");
		Kunde k = new Kunde("Huber", "Hans");
//		System.out.println(Entity.entity(k, MediaType.APPLICATION_JSON));
		 Response b =
		target.path("neuerKunde").request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(Entity.entity(k, MediaType.APPLICATION_JSON));
		System.out.println(b.readEntity(String.class));
		
		target = client.target("http://localhost:8080/test/ablesung");
//		k.setKdnr(1);
		Ablesung a = new Ablesung("1a", new Date(System.currentTimeMillis()),k, "test", false, 0);
//		System.out.println(Entity.entity(a, MediaType.APPLICATION_JSON));
		Response c =
				target.path("postAblesung").request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(Entity.entity(a, MediaType.APPLICATION_JSON));
		System.out.println(c);
	}
}