package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtil {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	public static String toJSON(Kunde k) throws JsonProcessingException {
		return mapper.writeValueAsString(k);
	}
	
	public static Kunde toKunde(String json) throws JsonMappingException, JsonProcessingException {
		return mapper.readValue(json, Kunde.class);
	}

}
