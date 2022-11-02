package server;

import java.net.URI;

import javax.swing.JOptionPane;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;

public class Server {
		public static final String pack = "server";
		private static boolean serverStarted = false;
		private static HttpServer server;
	
	public static void startServer(String url, boolean loadFromFile) {
		if(serverStarted) {
			return;
		}
		System.out.println("Start Server");
		final ResourceConfig rc = new ResourceConfig().packages(pack);
		server =
				JdkHttpServerFactory.createHttpServer(URI.create(url), rc);
		serverStarted = true;
		if(loadFromFile) {
			Datastore.getDataStore().loadFromFile();
		}
		System.out.println("Ready");
//		JOptionPane.showMessageDialog(null, "Ende");
//		stopServer();
	}
	

	public static void stopServer(boolean saveToFile) {
		if(serverStarted) {
			server.stop(0);
			System.out.println("Geschlossen");
			if(saveToFile) {
				Datastore.getDataStore().saveToFile();
			}
			System.out.println("Saved");
			serverStarted = false;
		}
	}
}
