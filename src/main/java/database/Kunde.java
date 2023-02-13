package database;

import java.util.UUID;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

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
public class Kunde implements IKunde{
	
	public static final String FIELD_KUNDEN_ID = "k_id";
	public static final String FIELD_KUNDEN_NAME = "k_name";
	public static final String FIELD_KUNDEN_VORNAME = "k_vorname";
	
		@EqualsAndHashCode.Include
		@ColumnName(FIELD_KUNDEN_ID)
		private UUID uuid;
		@ColumnName(FIELD_KUNDEN_NAME)
		private String name;
		@ColumnName(FIELD_KUNDEN_VORNAME)
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
