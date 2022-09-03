package client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

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
		
		List<Integer> test = new ArrayList<>();
		test.add(5);
		test.add(2);
		test.add(7);
		List<Integer> neu = test.stream().sorted((a1,a2) -> a1.compareTo(a2)).collect(Collectors.toList());
		neu.forEach(System.out::println);
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/test");
		Builder build = target.path("ablesung").path("hello").request(MediaType.TEXT_PLAIN);
		System.out.println(build.get());
		
		target = client.target("http://localhost:8080/test/hausverwaltung");
		Kunde k = new Kunde("Huber", "Hans");
//		System.out.println(Entity.entity(k, MediaType.APPLICATION_JSON));
		 Response b =
		target.path("kunden").request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(Entity.entity(k, MediaType.APPLICATION_JSON));
		 Kunde s = b.readEntity(Kunde.class);
//		System.out.println(b.readEntity(String.class));
		JSONObject obj = new JSONObject(s);
		System.out.println(obj);
		
		target = client.target("http://localhost:8080/test/hasuverwaltung");
//		k.setKdnr(1);
		Ablesung a = new Ablesung("1a", new Date(System.currentTimeMillis()),s, "test", false, 0);
//		System.out.println(Entity.entity(a, MediaType.APPLICATION_JSON));
		Response c =
				target.path("postAblesung").request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(Entity.entity(a, MediaType.APPLICATION_JSON));
		System.out.println(c.readEntity(String.class));
		
		target = client.target("http://localhost:8080/test/hausverwaltung");
		Response d = target.path("kunden").request(MediaType.APPLICATION_JSON).post(Entity.entity(null, MediaType.APPLICATION_JSON));
		System.out.println("Ab hier neu");
		System.out.println(d);
		System.out.println(d.readEntity(String.class));
		
//		target = client.target("http://localhost:8080/test/hausverwaltung");
//		Response neueResponse = target.path("getSingleAblesung").queryParam("kunde", "bla").request(MediaType.APPLICATION_JSON).get();
//		System.out.println(neueResponse.toString());
//		System.out.println(neueResponse.getStatus());
	}
}