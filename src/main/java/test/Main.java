package test;

import java.net.URI;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;

public class Main {
	private static final String url = "http://localhost:8080/testen";
	public static void main(String[] args) {
		final ResourceConfig rc = new ResourceConfig().packages("test");
		HttpServer server = JdkHttpServerFactory.createHttpServer(URI.create(url), rc);
		System.out.println("Server ready");
	}

}
