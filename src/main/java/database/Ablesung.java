package database;


import java.time.LocalDate;
import java.util.UUID;

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
	private UUID id; 
	private String zaehlernummer;
	private LocalDate datum;
	private IKunde kunde;
	private String kommentar;
	private boolean neuEingebaut;
	private Number zaehlerstand;
	
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

	@Override
	public void bla() {
		// TODO Auto-generated method stub
		
	}
}
