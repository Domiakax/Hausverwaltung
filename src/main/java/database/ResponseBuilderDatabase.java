package database;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import server.Messages;

public class ResponseBuilderDatabase {
	
	public static Response kunde_added(Kunde k) {
		return Response.status(Response.Status.CREATED).entity(k).build();
	}
	
	public static Response kunde_updated(Kunde k) {
		return Response.status(Response.Status.OK).entity(k).build();
	}
	
	public static Response kunde_notFound() {
		return Response.status(Response.Status.NOT_FOUND).entity(Messages.KUNDE_NOT_FOUND.toString()).build();
	}
	
	public static Response getKunde(Kunde k) {
		return Response.status(Response.Status.OK).entity(k).build();
	}
	
	public static Response updateAblesungSuccesful() {
		return Response.status(Response.Status.OK).entity(Messages.ABLESUNG_MODIFIED).build();
	}

	public static Response updateAblesungFailed() {
		return Response.status(Response.Status.NOT_FOUND).entity(Messages.ABLESUNG_NOT_MODIFIED).build();
	}
}
