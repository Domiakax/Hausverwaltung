package server;

import org.json.JSONObject;

import jakarta.ws.rs.core.Response;

public class ResponseBuilder {
	

	public static Response kundeCreated(Kunde k) {
		return Response.status(Response.Status.CREATED).entity(k).build();
	}
	
	public static Response kundeModified() {
		return Response.status(Response.Status.OK).entity(createMessageResponseObject(Messages.KUNDE_MODIFIED)).build();
	}
	
	public static Response kundeNotModified() {
		return Response.status(Response.Status.NOT_FOUND).entity(createMessageResponseObject(Messages.KUNDE_NOT_FOUND)).build();
	}
	public static Response ablesungCreated() {
		return Response.status(Response.Status.CREATED).
				entity(createMessageResponseObject(Messages.ABLESUNG_CREATED)).build();
	}
	
	public static Response ablesungNotCreated() {
		return Response.status(Response.Status.CONFLICT).entity(createMessageResponseObject(Messages.ABLESUNG_NOT_CREATED)).build();
	}
	
	public static Response ablesungModified() {
		return Response.status(Response.Status.OK).entity(createMessageResponseObject(Messages.ABLESUNG_MODIFIED)).build();
	}
	
	public static Response ablesungNotModified() {
		return Response.status(Response.Status.NOT_FOUND).entity(createMessageResponseObject(Messages.ABLESUNG_NOT_MODIFIED)).build();
	}
	
	public static Response ablesungDeleted() {
		return Response.status(Response.Status.OK).entity(createMessageResponseObject(Messages.ABLESUNG_DELETED)).build();
	}
	
	private static JSONObject createMessageResponseObject(Messages message) {
		return new JSONObject().put("message", message.toString());
	}
}
