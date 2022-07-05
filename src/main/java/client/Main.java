package client;

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
		
		target = client.target("http://localhost:8080/test");
		Kunde k = new Kunde("Huber", "Hans");
		System.out.println(Entity.entity(k, MediaType.APPLICATION_JSON));
		String b =
		target.path("ablesung").path("neuerKunde").request(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN).put(Entity.entity(k, MediaType.APPLICATION_JSON), String.class);
		System.out.println(b);
		
		Ablesung a = new Ablesung("1a", null, "test", false, 0);
		target = client.target("http://localhost:8080/test");
		String c =
				target.path("ablesung").path("postAblesung").request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(Entity.entity(k, MediaType.APPLICATION_JSON), String.class);
		System.out.println(c);
	}

}
