package database;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class MainClient {

	private static final String url = "http://localhost:8080/test";
	private static final Client client = ClientBuilder.newClient();
	private static WebTarget target = client.target(url);

	public static void main(String[] args) {
		Kunde k = new Kunde();
		k.setName("A");
		k.setVorname("a");
		Response re = target.path("hausverwaltung/v2").path("customers").request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).post(Entity.entity(k, MediaType.APPLICATION_JSON));
		System.out.println(re.toString());
		Kunde kR = re.readEntity(Kunde.class);
		System.out.println(kR);
		k.setId(kR.getId());
		
		k.setName(";Select * from kunde;");
//		k.setId(null);
		re = target.path("hausverwaltung/v2").path("customers").request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).put(Entity.entity(k, MediaType.APPLICATION_JSON));
		
		System.out.println(re);
		System.out.println(re.readEntity(String.class));
		 
		re = target.path("hausverwaltung/v2").path("customers").path(k.getId().toString()).request().accept(MediaType.APPLICATION_JSON).get();
		System.out.println(re);
		
		
		
		
		//		Ablesung a = new Ablesung();
//		a.setDatum(LocalDate.now());
//		a.setKommentar("bla");
//		a.setNeuEingebaut(false);
//		a.setZaehlernummer("abc");
//		a.setKunde(k);
//		a.setZaehlerstand(123);
//		
//		Response re2 = target.path("hausverwaltung/v2").path("readings").request(MediaType.APPLICATION_JSON)
//				.accept(MediaType.APPLICATION_JSON).post(Entity.entity(a, MediaType.APPLICATION_JSON));
//		
//		System.out.println(re2);
//		System.out.println(re2.readEntity(Ablesung.class));
	}

}
