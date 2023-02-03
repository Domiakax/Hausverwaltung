	package test;
	
	import javax.print.attribute.standard.Media;
	
	import jakarta.ws.rs.Consumes;
	import jakarta.ws.rs.POST;
	import jakarta.ws.rs.Path;
	import jakarta.ws.rs.Produces;
	import jakarta.ws.rs.core.MediaType;
	
	@Path("test")
	public class Ressource {
		
		@POST
		@Path("b")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		public B post(B b) {
			return new B();
		}
	
	}
