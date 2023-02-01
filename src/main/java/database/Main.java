package database;

import java.net.URI;

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
	
	private static final String url = "http://localhost:8080/test";
	
	public static void main(String[] args) {
//		Jdbi jdbi = Jdbi.create("jdbc:mariadb://localhost:3306/hausverwaltung", "root", "");
//		System.out.println("connected");
//		jdbi.installPlugin(new SqlObjectPlugin());
//		final Handle handle = jdbi.open();
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
