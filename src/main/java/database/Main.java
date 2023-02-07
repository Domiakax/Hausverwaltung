package database;

import java.net.URI;
import java.util.UUID;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import com.sun.net.httpserver.HttpServer;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class Main {
	
	public  static final String url = "http://localhost:8080/test";
	
	public static void main(String[] args) {
//		DatabaseConnector con = DatabaseConnector.getDatabaseConnector();
//		Kunde k = new Kunde();
//		k.setName("a");
//		k.setVorname("a");
//		k.setId(UUID.randomUUID());
//		
//		con.addKunde(k);
//		
//		k.setName("B");
//		con.updateKunde(k);
//		
//		final KundeDAO kundeDao = handle.attach(KundeDAO.class);
//		kundeDao.createTable();
//		
//		Kunde k = new Kunde("A", "a");
//		System.out.println(kundeDao.insert(k.getName(), k.getVorname()));
//		
//		System.out.println("Done");
		final ResourceConfig rc = new ResourceConfig().packages("database");
		HttpServer server = JdkHttpServerFactory.createHttpServer(URI.create(url), rc);
		System.out.println("Server ready");
		
	}

}
