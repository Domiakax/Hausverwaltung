package server;


import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;

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
	private UUID id; 
	private String zaehlernummer;
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate datum;
	private Kunde kunde;
	private String kommentar;
	private boolean neuEingebaut;
	private int zaehlerstand;
	
	public void updateAblesung(Ablesung a) {
		this.zaehlernummer = a.zaehlernummer;
		this.datum = a.datum;
		this.kunde = a.kunde;
		this.kommentar = a.kommentar;
		this.neuEingebaut = a.neuEingebaut;
		this.zaehlerstand = a.zaehlerstand;
	}

	public Ablesung(String zaehlernummer, LocalDate datum, Kunde kunde, String kommentar, boolean neuEingebaut,
			int zaehlerstand) {
		this.zaehlernummer = zaehlernummer;
		this.datum = datum;
		this.kunde = kunde;
		this.kommentar = kommentar;
		this.neuEingebaut = neuEingebaut;
		this.zaehlerstand = zaehlerstand;
	}
}
