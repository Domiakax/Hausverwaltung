package server;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import jakarta.ws.rs.core.Response;

public class ResponseBuilder {
	
	public static Response badRequest() {
		return Response.status(Response.Status.BAD_REQUEST).entity(Messages.BAD_REQUEST.toString()).build();
	}
	
	public static Response kundeCreated(Kunde k) {
		return Response.status(Response.Status.CREATED).entity(k).build();
	}
	
	public static Response kundeModified() {
		return Response.status(Response.Status.OK).entity(Messages.KUNDE_MODIFIED.toString()).build();
	}
	
	public static Response kundeNotFound() {
		return Response.status(Response.Status.NOT_FOUND).entity((Messages.KUNDE_NOT_FOUND.toString())).build();
	}
	
	public static Response kundeFound(Kunde k) {
		return Response.status(Response.Status.OK).entity(k).build();
	}
	
	public static Response kundeDeleted(HashMap<Kunde, List<Ablesung>> ablesungen) {
		return Response.status(Response.Status.OK).entity(ablesungen).build();
	}
	
	public static Response kundeNotDeleted() {
		return Response.status(Response.Status.NOT_FOUND).entity(Messages.KUNDE_NOT_DELETED.toString()).build();
	}
	
	public static Response ablesungCreated(Ablesung a) {
		return Response.status(Response.Status.CREATED).entity(a).build();
	}
	
	public static Response ablesungNotCreated() {
		return Response.status(Response.Status.NOT_FOUND).entity(Messages.ABLESUNG_NOT_CREATED.toString()).build();
	}
	
	public static Response ablesungModified() {
		return Response.status(Response.Status.OK).entity(Messages.ABLESUNG_MODIFIED.toString()).build();
	}
	
	public static Response ablesungNotModified() {
		return Response.status(Response.Status.NOT_FOUND).entity(Messages.ABLESUNG_NOT_MODIFIED.toString()).build();
	}
	
	public static Response ablesungDeleted(Ablesung a) {
		return Response.status(Response.Status.OK).entity(a).build();
	}
	
	public static Response ablesungNotFound() {
		return Response.status(Response.Status.NOT_FOUND).entity(Messages.ABLESUNG_NOT_FOUND.toString()).build();
	}
	
	public static Response ablesungFound(Ablesung a) {
		return Response.status(Response.Status.OK).entity(a).build();
	}
	
	public static Response getLastWrite(long lastWrite) {
		return Response.status(Response.Status.OK).entity(lastWrite).build();
	}
	
	public static Response getEveryKunde(List<Kunde> kunden) {
		return Response.status(Response.Status.OK).entity(kunden).build();
	}
	
}
