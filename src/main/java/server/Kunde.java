package server;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@XmlRootElement
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName(value="kunde")
@JsonDeserialize(keyUsing = KundeJSONDeserializer.class)
public class Kunde {
	@EqualsAndHashCode.Include
	private UUID kdnr;
	private String name, vorname;

	public Kunde(String name, String vorname) {
		setName(name);
		setVorname(vorname);
	}
	
	public void updateKunde(Kunde update) {
		setName(update.name);
		setVorname(update.vorname);
	}
}
