
package server;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("hausverwaltung")
public class AblesungRessource {

	private static final String endpointKunde = "kunden";
	private static final String endpointAblesung = "ablesungen";
	public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path(endpointKunde)
	public Response neuerKunde(Kunde k) {
		if (k == null) {
			return ResponseBuilder.badRequest();
		}
		System.out.println("kunde angelegt");
		System.out.println(k);
		return Datastore.getDataStore().addNewKunde(k) == null ? ResponseBuilder.badRequest()
				: ResponseBuilder.kundeCreated(k);
	}

	@PUT
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path(endpointKunde)
	public Response updateKunde(Kunde toUpdate) {
		return Datastore.getDataStore().modifyKunde(toUpdate) ? ResponseBuilder.kundeModified()
				: ResponseBuilder.kundeNotFound();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path(endpointAblesung)
	public Response postAblesung(Ablesung a) {
		if (a == null) {
			return ResponseBuilder.badRequest();
		}
		Ablesung result = Datastore.getDataStore().postAblesung(a);
		return result == null ? ResponseBuilder.ablesungNotCreated() : ResponseBuilder.ablesungCreated(result);
	}

	@PUT
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path(endpointAblesung)
	public Response modifyExistingAblesung(Ablesung a) {
		return Datastore.getDataStore().modifyExistingAblesung(a) ? ResponseBuilder.ablesungModified()
				: ResponseBuilder.ablesungNotModified();
	}
	// Bis hier gecheckt

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(endpointKunde)
	public Response getEveryKunde() {
		return ResponseBuilder.getEveryKunde(Datastore.getDataStore().getCopyOfEveryKunde());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(endpointKunde + "/{id}")
	public Response getKunde(@PathParam("id") String id) {
		Kunde toSearch = Datastore.getDataStore().getKunde(id);
		if (toSearch == null) {
			return ResponseBuilder.kundeNotFound();
		}
		return ResponseBuilder.kundeFound(toSearch);
	}

	// GET alle Ablesungen

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getEveryAblesungFromKunde/{kundeId}")
	public Response getEveryAblesung(@PathParam("kundeId") String kundeId) {
		List<Ablesung> results = Datastore.getDataStore().getAblesungenFromKunde(kundeId);
		return results == null ? ResponseBuilder.kundeNotFound() : null;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(endpointAblesung + "/{ablesungId}")
	public Response getSingleAblesung(@PathParam("ablesungId") String ablesungId) {
		System.out.println("Ablesung normal");
		Ablesung toSearch = Datastore.getDataStore().getSingleAblesung(ablesungId);
		return toSearch == null ? ResponseBuilder.ablesungNotFound() : ResponseBuilder.ablesungFound(toSearch);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(endpointAblesung)
	public Response getAblesungenFromKunde(@QueryParam("kunde") String kid, @QueryParam("beginn") String beginn,
			@QueryParam("ende") String ende) {
		try {
			LocalDate dateEnde;
			if (ende == null) {
				dateEnde = LocalDate.now();
			} else {
				dateEnde = LocalDate.parse(ende, dateFormatter);
			}
			List<Ablesung> result;
			if (beginn == null) {
				if (kid == null) {
					result = Datastore.getDataStore().getAblesungUntil(dateEnde);
				} else {
					result = Datastore.getDataStore().getAblesungenFromKundeUntil(kid, dateEnde);
				}
			} else {
				LocalDate dateBeginn = LocalDate.parse(beginn, dateFormatter);
				if (kid == null) {
					result = Datastore.getDataStore().getAblesungen(dateBeginn, dateEnde);
				}
				else {
					result = Datastore.getDataStore().getAblesungenFromKunde(kid, dateBeginn, dateEnde);
				}
			}
			System.out.println(result);

			if (result == null) {
				return ResponseBuilder.kundeNotFound();
			}
			return ResponseBuilder.getAblesungen(result);
		} catch (DateTimeParseException e) {
			return ResponseBuilder.wrongDateFormat();
		}
	}

//	@GET
//	@Path("getLastWrite/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response getLastWrite(@PathParam("id") String id) {
//		return ResponseBuilder.getLastWrite(Datastore.getDataStore().getLastWrite(id));
//	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path(endpointAblesung + "/{id}")
	public Response deleteAblesung(@PathParam("id") String id) {
		try {
			Ablesung a = Datastore.getDataStore().deleteAblesung(id);
			return a == null ? ResponseBuilder.ablesungNotFound() : ResponseBuilder.ablesungDeleted(a);
		} catch (Exception e) {
			return ResponseBuilder.badRequest();
		}
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("deleteKunde/{id}")
	public Response deleteKunde(@PathParam("id") String id) {
		HashMap<Kunde, List<Ablesung>> storedInformation = Datastore.getDataStore().deleteKunde(id);
		return storedInformation == null ? ResponseBuilder.kundeNotDeleted()
				: ResponseBuilder.kundeDeleted(storedInformation);
	}

}