package server;

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
		Server.startServer("http://localhost:8080/test", false);
	}

}
