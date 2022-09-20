package server;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class test {
	
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public static void main(String[] args) {
//		String dateString = "2022-8-19";
//		for(int i = 0; i<10;i++) {
//			Thread t = new Thread(
//					() -> {try{
//						Date date = format.parse(dateString);
//						System.out.println("Parsed " + date);
//					}
//					catch(Exception e) {
//						e.printStackTrace();
//					}
//					});
//			t.start();
//		}
		HashMap<String, Kunde> map1 = new HashMap<>();
		HashMap<Kunde, Integer> map2 = new HashMap<>();
		Kunde k1 = new Kunde("a", "b");
//		String test = "abc";
		map1.put("1", k1);
		map2.put(k1, 12);

		System.out.println(map1.get("1"));
		System.out.println(map2.get(k1));
		map1.get("1").setName("b");
		System.out.println(map1.get("1"));
		System.out.println(map2.get(k1));
		Kunde k2 = new Kunde();
		k2.setKdnr(UUID.randomUUID());
		System.out.println(map2.get(k2));
		UUID.fromString("abc");
	}
}