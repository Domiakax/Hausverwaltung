package database;

import java.beans.ConstructorProperties;
import java.util.UUID;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@ToString
public class Kunde{
	
		@EqualsAndHashCode.Include
		@ColumnName("k_id")
		private UUID uuid;
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


		public Kunde(UUID uuid, String name, String vorname) {
			this.uuid = uuid;
			this.name = name;
			this.vorname = vorname;
		}

}
