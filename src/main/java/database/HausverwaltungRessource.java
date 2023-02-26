package database;

import java.util.List;
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
	public static final String ENDPOINT_ABLESUNG = "readings";
	public static final String ENDPOINT_CLIENT_START = "clientStart";
	
	@GET
	@Path("test")
	@Produces(MediaType.APPLICATION_JSON)
	public String test() {
		System.out.println("Test-Endpoint");
		return "test";
	}

	@Path(ENDPOINT_KUNDE)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public Response addKunde(Kunde k) {
		System.out.println("Endpoint: POST Kunde");
		UUID id = UUID.randomUUID();
		k.setUuid(id);
		System.out.println("To DatabaseConnector");
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
		if (k == null || k.getUuid() == null) {
			System.out.println("Ungültiges Objekt bzw. Kunden-Id");
			return ResponseBuilderDatabase.kunde_notFound();
		}
		int rowsChanged = DatabaseConnector.getDatabaseConnector().updateKunde(k);
		return rowsChanged == 0 ? ResponseBuilderDatabase.kunde_notFound() : ResponseBuilderDatabase.kunde_updated(k);
	}

	@Path(ENDPOINT_KUNDE + "/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Response getKunde(@PathParam("uuid") String uuid) {
		System.out.println("Endpoint: Kunde GET");
		Kunde k = DatabaseConnector.getDatabaseConnector().getKunde(uuid);
		return ResponseBuilderDatabase.getKunde(k);
	}
	
	@Path(ENDPOINT_ABLESUNG + "/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Response getAblesung(@PathParam("uuid") String uuid) {
		System.out.println("Endpoint: Get Ablesung");
		Ablesung a = DatabaseConnector.getDatabaseConnector().getAblesung(uuid);
		return ResponseBuilderDatabase.getAblesung(a);
	}

	@Path(ENDPOINT_ABLESUNG)
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addReading(Ablesung r) {
		System.out.println("Endpoint: POST Ablesung with: " + r.toString());
		r.setUuid(UUID.randomUUID());
		DatabaseConnector.getDatabaseConnector().addAblesung(r);
		return Response.status(Response.Status.OK).entity(r).build();
	}

	@Path(ENDPOINT_ABLESUNG)
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response updateAblesung(Ablesung a) {
		System.out.println("PUT Ablesung");
		return DatabaseConnector.getDatabaseConnector().updateAblesung(a)
				? ResponseBuilderDatabase.updateAblesungSuccesful()
				: ResponseBuilderDatabase.updateAblesungFailed();
	}

	
	@Path(ENDPOINT_KUNDE)
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEveryKunde() {
		System.out.println("GET Every Kunde");
		List<Kunde> kunden = DatabaseConnector.getDatabaseConnector().getEveryKunde();
		return ResponseBuilderDatabase.getEveryKunde(kunden);
	}
	
	@Path(ENDPOINT_CLIENT_START)
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAblesungenForClientStart() {
		System.out.println("GET ClientStart");
		List<Ablesung> ablesungen = DatabaseConnector.getDatabaseConnector().getAblesungenForClientStart();
		return ResponseBuilderDatabase.getAblesungenForClientStart(ablesungen);
	}
}
