package server;

import java.net.URI;
import java.util.Date;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

public class Main {
	
	public static void main(String[] args) {
		ObjectMapper mapper = new ObjectMapper();
		Ablesung a = new Ablesung("a",  new Date(), "b",false, 0);
		System.out.println(a);
		try {
			System.out.println(mapper.writeValueAsString(a));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final String pack = "server";
		final String url = "http://localhost:8080/test";
		System.out.println("Start Server");
		final ResourceConfig rc = new ResourceConfig().packages(pack);
		final HttpServer server =
				JdkHttpServerFactory.createHttpServer(URI.create(url), rc);
		System.out.println("Ready");
	}

}
