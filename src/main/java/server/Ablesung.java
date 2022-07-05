package server;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ablesung {
	@EqualsAndHashCode.Include
	private String zaehlernummer;
	@EqualsAndHashCode.Include
	private Date datum;
	private String kommentar;
	private boolean neuEingebaut;
	private int zaehlerstand;
}
