package database;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
//@JsonTypeName(value="kunde")
public class Kunde{
	
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

}
