package server;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//@EqualsAndHashCode
@XmlRootElement
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName(value="kunde")
//@JsonDeserialize(keyUsing = KundeJSONDeserializer.class)
public class Kunde {
	@EqualsAndHashCode.Include
	private UUID id;
	private String name, vorname;

	public Kunde(String name, String vorname) {
		setName(name);
		setVorname(vorname);
	}
	
	public void updateKunde(Kunde update) {
		setName(update.name);
		setVorname(update.vorname);
	}
	
//	@Override
//	public String toString() {
//		try {
//			return JSONUtil.toJSON(this);
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}
}
