package server;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

public class KundeJSONDeserializer extends KeyDeserializer{
	
	@Override
	public Kunde deserializeKey(String key, DeserializationContext ctxt) throws IOException {
		// TODO Auto-generated method stub
//		System.out.println("JSON HIER");
//		System.out.println(key);
		return JSONUtil.toKunde(key);
	}

}
