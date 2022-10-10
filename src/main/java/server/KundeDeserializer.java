package server;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

public class KundeDeserializer extends KeyDeserializer{

	@Override
	public Kunde deserializeKey(String key, DeserializationContext ctxt) throws IOException {
		// TODO Auto-generated method stub
		return new Kunde(key);
	}

}
