package database;

import java.util.UUID;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("hausverwaltung/v2")
public class HausverwaltungRessource {
	
	public static final String ENDPOINT_KUNDE = "customers";
	

	@Path(ENDPOINT_KUNDE)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public Response addKunde(Kunde k) {
		System.out.println("Endpoint: POST Kunde");
		UUID id = UUID.randomUUID();
		k.setId(id);
		DatabaseConnector.getDatabaseConnector().addKunde(k);
		System.out.println("Post Kunde Done");
		return ResponseBuilderDatabase.kunde_added(k);
	}

	
	@Path(ENDPOINT_KUNDE)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PUT
	public Response updateKunde(Kunde k) {
		System.out.println("Endpoint: PUT Kunde");
		if(k == null || k.getId() == null) {
			System.out.println("Ungültiges Objekt bzw. Kunden-Id");
			return ResponseBuilderDatabase.kunde_notFound();
		}
		int rowsChanged = DatabaseConnector.getDatabaseConnector().updateKunde(k);
		return rowsChanged == 0 ? ResponseBuilderDatabase.kunde_notFound() : ResponseBuilderDatabase.kunde_updated(k);
	}
	
	@Path(ENDPOINT_KUNDE + "/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Response getKunde(@PathParam("uuid")String uuid) {
		Kunde k = DatabaseConnector.getDatabaseConnector().getKunde(uuid);
		return ResponseBuilderDatabase.getKunde(k);
	}
	
	public Response addReading(Ablesung r) {
		System.out.println("Endpoint: POST Ablesung");
		r.setId(UUID.randomUUID());
		return Response.status(Response.Status.OK).entity(r).build();
	}

	

}
