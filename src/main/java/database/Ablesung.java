package database;


import java.time.LocalDate;
import java.util.UUID;

import org.jdbi.v3.core.mapper.Nested;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ablesung implements IAblesung{
	@EqualsAndHashCode.Include
	@ColumnName("a_id")
	private UUID uuid;
	@ColumnName("a_zaehlernummer")
	private String zaehlernummer;
	@ColumnName("a_datum")
	private LocalDate datum;
	@Nested
	private Kunde kunde;
	@ColumnName("a_Kommentar")
	private String kommentar;
	@ColumnName("a_neuEingabut")
	private boolean neuEingebaut;
	@ColumnName("a_zaehlerstand")
	private Number zaehlerstand;
	
	public void updateAblesung(Ablesung a) {
		this.zaehlernummer = a.zaehlernummer;
//		this.datum = a.datum;
		this.kunde = a.kunde;
		this.kommentar = a.kommentar;
		this.neuEingebaut = a.neuEingebaut;
		this.zaehlerstand = a.zaehlerstand;
	}

	public Ablesung(String zaehlernummer, LocalDate datum, Kunde kunde, String kommentar, boolean neuEingebaut,
			int zaehlerstand) {
		this.zaehlernummer = zaehlernummer;
//		this.datum = datum;
		this.kunde = kunde;
		this.kommentar = kommentar;
		this.neuEingebaut = neuEingebaut;
		this.zaehlerstand = zaehlerstand;
	}

}
