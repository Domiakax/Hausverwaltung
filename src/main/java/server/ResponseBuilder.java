package server;

import org.json.JSONObject;

import jakarta.ws.rs.core.Response;

public class ResponseBuilder {
	

	public static Response kundeCreated(Kunde k) {
		return Response.status(Response.Status.CREATED).entity(k).build();
	}
	
	public static Response ablesungCreated() {
		return Response.status(Response.Status.CREATED).
				entity(createMessageResponseObject(Messages.ABLESUNG_CREATED)).build();
	}
	
	public static Response ablesungNotCreated() {
		return Response.status(Response.Status.CONFLICT).entity(createMessageResponseObject(Messages.ABLESUNG_NOT_CREATED)).build();
	}
	
	public static Response ablesungDeleted() {
		return Response.status(Response.Status.OK).entity(createMessageResponseObject(Messages.ABLESUNG_DELETED)).build();
	}
	
	private static JSONObject createMessageResponseObject(Messages message) {
		return new JSONObject().put("message", message.toString());
	}
}
