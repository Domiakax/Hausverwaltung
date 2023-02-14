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
	
	public static final String FIELD_ABLESUNG_ID = "a_id";
	public static final String FIELD_ABLESUNG_ZAEHLERNUMMER = "a_zaehlernummer";
	public static final String FIELD_ABLESUNG_DATUM = "a_datum";
	public static final String FIELD_ABLESUNG_KOMMENTAR = "a_kommentar";
	public static final String FIELD_ABLESUNG_NEUEINGEBAUT = "a_neuEingabut";
	public static final String FIELD_ABLESUNG_ZAEHLERSTAND = "a_zaehlerstand";
	
	@EqualsAndHashCode.Include
	@ColumnName(FIELD_ABLESUNG_ID)
	private UUID uuid;
	@ColumnName(FIELD_ABLESUNG_ZAEHLERNUMMER)
	private String zaehlernummer;
	@ColumnName(FIELD_ABLESUNG_DATUM)
	private LocalDate datum;
	@Nested
	private IKunde kunde;
	@ColumnName(FIELD_ABLESUNG_KOMMENTAR)
	private String kommentar;
	@ColumnName(FIELD_ABLESUNG_NEUEINGEBAUT)
	private boolean neuEingebaut;
	@ColumnName(FIELD_ABLESUNG_ZAEHLERSTAND)
	private double zaehlerstand;
	
	public void updateAblesung(Ablesung a) {
		this.zaehlernummer = a.zaehlernummer;
//		this.datum = a.datum;
		this.kunde = a.kunde;
		this.kommentar = a.kommentar;
		this.neuEingebaut = a.neuEingebaut;
		this.zaehlerstand = a.zaehlerstand;
	}

	public Ablesung(String zaehlernummer, LocalDate datum, IKunde kunde, String kommentar, boolean neuEingebaut,
			int zaehlerstand) {
		this.zaehlernummer = zaehlernummer;
//		this.datum = datum;
		this.kunde = kunde;
		this.kommentar = kommentar;
		this.neuEingebaut = neuEingebaut;
		this.zaehlerstand = zaehlerstand;
	}

}
