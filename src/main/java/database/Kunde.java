package database;

import java.util.UUID;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

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
public class Kunde implements IKunde{
	
		@ColumnName("k_id")
		@EqualsAndHashCode.Include
		private UUID id;
		@ColumnName("k_name")
		private String name;
		@ColumnName("k_vorname")
		private String vorname;

		public Kunde(String name, String vorname) {
			setName(name);
			setVorname(vorname);
		}
		
		public void updateKunde(Kunde update) {
			setName(update.name);
			setVorname(update.vorname);
		}

}
