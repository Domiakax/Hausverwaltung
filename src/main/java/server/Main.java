package server;

import java.net.URI;
import java.util.Date;

import javax.swing.JOptionPane;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

public class Main {
	
	public static  boolean loadFromFile = true; 
	
	public static void main(String[] args) {
//		Runtime.getRuntime().addShutdownHook(new Thread() {
//			@Override
//			public void run() {
//				System.out.println("start saving");
//				Datastore.getDataStore().saveToFile();
//				System.out.println("saved");
//			}
//		});
//		ObjectMapper mapper = new ObjectMapper();
//		Kunde k = new Kunde("a", "b");
//		Ablesung a = new Ablesung("a",  null, k,"b",false, 0);
//		System.out.println(a);
//		try {
//			System.out.println(mapper.writeValueAsString(a));
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		ServerStart.startServer("http://localhost:8080/test", false);
	}

}
