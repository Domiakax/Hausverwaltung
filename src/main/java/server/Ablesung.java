package server;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@XmlRootElement
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonTypeName(value="ablesung")
public class Ablesung {
	@EqualsAndHashCode.Include
	private String zaehlernummer;
	@EqualsAndHashCode.Include
	private Date datum;
	@EqualsAndHashCode.Include
	private Kunde kunde;
	private String kommentar;
	private boolean neuEingebaut;
	private int zaehlerstand;
	
	public void updateAblesung(Ablesung a) {
		this.kommentar = a.kommentar;
		this.neuEingebaut = a.neuEingebaut;
		this.zaehlerstand = a.zaehlerstand;
	}
}
