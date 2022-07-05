package server;

import java.util.*;
import java.util.Date;


public interface BasicServer {
	
	/*
	 * 
	 */
	String neuerKunde(String nachname, String vorname);
	
	void postAblesung(Ablesung a);
	
	List<Ablesung> getSingleAblesung(String kundennumer, Date beginn, Date ende, String zaehlernummer);

	List<Ablesung> getEveryAblesung(String kundennumer, String zaehlernummer);

	List<Ablesung> getEveryAblesung(String kundennumer);
}
