package database;

import java.util.UUID;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import server.Kunde;

@Path("hausverwaltung/v2")
public class HausverwaltungRessource implements IHausverwaltungRessource{
	

	@Override
	public Response addKunde(Kunde k) {
		System.out.println("Endpoint: POST Kunde");
		UUID id = UUID.randomUUID();
		k.setId(id);
		return Response.status(Response.Status.OK).entity(k).build();
	}

	@Override
	public Response addReading(Ablesung r) {
		System.out.println("Endpoint: POST Ablesung");
		r.setId(UUID.randomUUID());
		return Response.status(Response.Status.OK).entity(r).build();
	}

	

}
