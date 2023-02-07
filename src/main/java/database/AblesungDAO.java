package database;

import java.time.LocalDate;

import org.jdbi.v3.core.mapper.Nested;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface AblesungDAO {
	
	@SqlUpdate("""
			Create Table if not exists Ablesung(
				id int primary key auto_increment,
				uuid uuid not null,
				zaehlernummer varchar(255),
				 LocalDate datum;
	@Nested
	private IKunde kunde;
	private String kommentar;
	private boolean neuEingebaut;
	private Number zaehlerstand;
			)
			""")
	void createTable();
	
	@SqlUpdate("""
			
			""")
	IAblesung addAblesung();

}
