package server;

import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
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
public class Kunde {
	@EqualsAndHashCode.Include
	private int kdnr;
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
