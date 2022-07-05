package server;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("ablesung")
public class AblesungRessource {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	private final HashMap<Kunde, List<Ablesung>> datastore = new HashMap<>();
	private static final SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy");
	private Timestamp lastWritten;
	
	@Path("hello")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		System.out.println("anfrage");
		return "hello world";
	}
	
	@Path("ablesungen")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAblesungen(Kunde k) {
		List<Ablesung> ablesungen = datastore.get(k);
		return Response.status(Response.Status.OK).entity(ablesungen).build();
	}
	
	// To DO
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ablesungen/{id}/startdatum/{beginn}/enddatum/{ende}")
	public String getAblesung(@PathParam("id") int id, @PathParam("beginn") Date beginn, @PathParam("ende") Date ende) {
		return null;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("postAblesung")
	public Response postAblesung(Kunde k, Ablesung a) {
		if(!datastore.containsKey(k)) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		datastore.get(k).add(a);
		return Response.status(Response.Status.)
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("neuerKunde")
	public Response neuerKunde(Kunde k) {
		k.setKdnr(datastore.size()+1);
		datastore.put(k, new ArrayList<Ablesung>());
		System.out.println("Kunde angelegt");
		return Response.status(Response.Status.CREATED).entity(k).build();
	}
}
