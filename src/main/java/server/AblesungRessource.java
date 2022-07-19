package server;

import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
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
	
	
	@Path("hello")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		System.out.println("anfrage");
		return "hello world";
	}
	
//	@Path("ablesungen")
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response getAblesungen(Kunde k) {
////		List<Ablesung> ablesungen = Datastore.getDataStore().
//		return Response.status(Response.Status.OK).entity(ablesungen).build();
//	}
	
	// To DO
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/ablesungen/{id}/startdatum/{beginn}/enddatum/{ende}")
//	public String getAblesung(@PathParam("id") int id, @PathParam("beginn") Date beginn, @PathParam("ende") Date ende) {
//		return null;
//	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("postAblesung")
	public Response postAblesung(Ablesung a) {
		Kunde k = a.getKunde();
		System.out.println(k);
		if(Datastore.getDataStore().postAblesung(a)) {
			return ResponseBuilder.ablesungCreated();
		}
		return ResponseBuilder.ablesungNotCreated();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("modifyAblesung")
	public Response modifyExistingAblesung(Ablesung a) {
		if(Datastore.getDataStore().modifyExistingAblesung(a)) {
			return ResponseBuilder.ablesungModified();
		}
		return ResponseBuilder.ablesungNotModified();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("neuerKunde")
	public Response neuerKunde(Kunde k) {
		Datastore.getDataStore().addNewKunde(k);
		System.out.println("Kunde angelegt");
		return ResponseBuilder.kundeCreated(k);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getEveryKunde")
	public Response getEveryKunde() {
		return ResponseBuilder.getEveryKunde(Datastore.getDataStore().getCopyOfEveryKunde());
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getKunde/{id}")
	public Response getKunde(@PathParam("id") String id) {
		Kunde toSearch = Datastore.getDataStore().getKunde(id);
		if(toSearch == null) {
			return ResponseBuilder.kundeNotFound();
		}
		return ResponseBuilder.kundeFound(toSearch);
	}
	
	//GET alle Ablesungen 
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("modifyKunde")
	public Response modifyKunde(Kunde toUpdate) {
		if(Datastore.getDataStore().modifyKunde(toUpdate)) {
			return ResponseBuilder.kundeModified();
		}
		return ResponseBuilder.kundeNotFound();
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("deleteKunde/{id}")
	public Response deleteKunde(@PathParam("id") String id) {
		List<Ablesung> ablesungen = Datastore.getDataStore().deleteKunde(id);
		if(ablesungen == null) {
			return ResponseBuilder.kundeNotDeleted();
		}
		return ResponseBuilder.kundeDeleted(ablesungen);
	}
	
	@GET
	@Path("getLastWrite")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLastWrite() {
		return ResponseBuilder.getLastWrite(Datastore.getDataStore().getLastWrite());
	}
	
	@GET
	@Path("getEveryAblesung/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEveryAblesung(@PathParam("id") String id) {
		
	}
	
	//ToDo
//	@DELETE
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("deletAblesung/{id}")
//	public Response deleteAblesung(Ablesung a) {
//		if(Datastore.getDataStore().deleteAblesung(a)) {
//			return ResponseBuilder.ablesungDeleted();
//		}
//		return null;
//	}
	
}