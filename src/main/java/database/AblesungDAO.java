package database;

import java.util.UUID;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface AblesungDAO {

	@SqlUpdate("""
			Create Table if not exists Ablesung(
				id int primary key auto_increment,
				uuid uuid not null unique,
				zaehlernummer varchar(255),
				datum Date,
				kundenId int,
				kommentar varchar(255),
				neuEingebaut boolean,
				zaehlerstand double
			)
			""")
	void createTable();

	@SqlUpdate("""
			Insert into Ablesung(uuid, kundenId, datum, kommentar, neuEingebaut, zaehlerstand)
			Select :uuid, k.id, :datum, :kommentar, :neuEingebaut, :zaehlerstand
			From kunde k where k.uuid = :kunde.uuid
			""")
	int addAblesung(@BindBean Ablesung a);

	@SqlUpdate("""
			Update Ablesung set zaehlernummer = :zaehlernummer,
			datum = :datum, kundenId = (Select id from kunde where uuid = :kunde.uuid),
			kommentar = :kommentar, neuEingebaut = :neuEingebaut, zaehlerstand = :zaehlerstand
			where uuid = :uuid
			""")
	int updateAblesung(@BindBean Ablesung a);
	
	@SqlUpdate("""
			Delete from Ablesung where uuid = :uuid
			""")
	int deleteAblesung(@Bind UUID uuid);
	
	@SqlQuery("""
			Select uuid,zaehlernummer,datum, (kundenId int,
				kommentar varchar(255),
				neuEingebaut boolean,
				zaehlerstand double 
			""")
	Ablesung getAblesung(UUID uuid);

}
