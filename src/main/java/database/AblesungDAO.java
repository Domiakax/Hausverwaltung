package database;

import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface AblesungDAO {
	
	@SqlUpdate("""
			Create Table if not exists Ablesung(
				id int primary key auto_increment,
				uuid uuid not null unique,
				zaehlernummer varchar(255),
				datum Date;
				kundenId int,
				kommentar varchar(255),
				neuEingebaut boolean,
				zaehlerstand double
			)
			""")
	void createTable();
	
	@SqlUpdate("""
			Insert into Ablesung(uuid, zaehlernummer, datum, kundenId, kommentar, neuEingebaut, zaehlerstand)
			Select :id, :zaehlernummer, :datum, k.id, :kommentar, :neuEingebaut, :zaehlerstand
			From kunde k where k.uuid = :kunde.id 
			""")
	void addAblesung(@BindBean Ablesung a);

}