package test;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class MainClient {
	private static final String url = "http://localhost:8080/testen";
	private static final Client client = ClientBuilder.newClient();
	private static WebTarget target = client.target(url);
	
	public static void main(String[] args) {
		IA a = new Aa();
		B b= new B();
		b.ia = a;
		
		System.out.println(Entity.entity(b, MediaType.APPLICATION_JSON).getEntity());
		Response re2 = target.path("test").path("b").request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).post(Entity.entity(b, MediaType.APPLICATION_JSON));
		System.out.println(re2);
	}

}
