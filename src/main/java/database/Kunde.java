package database;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Kunde {
	
	@EqualsAndHashCode.Include
	private UUID id;
	private String name, vorname;
	
	public Kunde(String name, String vorname) {
		this.name = name;
		this.vorname = vorname;
	}
	

}
