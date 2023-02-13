package database;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public interface IKunde {

	void setName(String string);

	void setVorname(String string);

	void setUuid(UUID uuid);

	UUID getUuid();
	

}
