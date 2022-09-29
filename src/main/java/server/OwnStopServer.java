package server;


import com.sun.net.httpserver.HttpServer;
public abstract class OwnStopServer extends HttpServer{

	@Override
	public void stop(int delay) {
		Datastore.getDataStore().saveToFile();
	}

}
