package test;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, 
	include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@Type(value = A.class , name ="test"), 
	@Type(value = Aa.class, name = "test")})
public interface  IA {

}
