package server;

import java.io.IOException;


import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KundeJSONDeserializer extends KeyDeserializer{
	
	private static final ObjectMapper map = new ObjectMapper();
	@Override
	public Kunde deserializeKey(String key, DeserializationContext ctxt) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("JSON HIER");
		System.out.println(key);
		return map.readValue(key, Kunde.class);
	}

}
