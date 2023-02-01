package database;

import java.net.URI;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;

public class RestServerDatabase {

	private static boolean serverStarted = false;
	private static final String pack = "database";
	private static HttpServer server;
	
	
	public static void startServer(String url) {
		if(serverStarted) {
			return;
		}
		System.out.println("Start Server");
		final ResourceConfig rc = new ResourceConfig().packages(pack);
		server =
				JdkHttpServerFactory.createHttpServer(URI.create(url), rc);
		serverStarted = true;
		System.out.println("Server Ready");
	}
	
	public void stopServer() {
		if(serverStarted) {
			server.stop(0);
			serverStarted = false;
			server = null;
			System.out.println("Server stopped");
		}
		
	}
	
	

}
