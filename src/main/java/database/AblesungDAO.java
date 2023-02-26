package database;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface AblesungDAO {
	
	public static final String DATE_BORDER = LocalDate.of(LocalDate.now().getYear()-2, 1, 1).toString(); 

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
			Select a.uuid as a_id, zaehlernummer as a_zaehlernummer, datum as a_datum,
				kommentar as a_kommentar, neuEingebaut as a_neuEingebaut,
				zaehlerstand as a_zaehlerstand, k.uuid as k_id, k.name as k_name, k.vorname as k_vorname
			From ablesung a left join kunde k on k.id = a.kundenId
			Where a.uuid = :uuid 
			""")
	@RegisterRowMapper(AblesungRowMapper.class)
	Ablesung getAblesung(@Bind("uuid") UUID uuid);
	
	@SqlUpdate("""
			Update Ablesung set kundenId = null where kundenId = (Select id from Kunde where uuid = :uuid)
			""")
	void kundeDeleted(@BindBean Kunde k);

	@SqlQuery("""
			Select a.uuid as a_id, zaehlernummer as a_zaehlernummer, datum as a_datum,
				kommentar as a_kommentar, neuEingebaut as a_neuEingebaut,
				zaehlerstand as a_zaehlerstand, k.uuid as k_id, k.name as k_name, k.vorname as k_vorname
			From ablesung a left join kunde k on k.id = a.kundenId
			Where datum >= :dateLine
			""")
	@RegisterRowMapper(AblesungRowMapper.class)
	List<Ablesung> getAblesungenForClientStart(@Bind("dateLine") LocalDate dateLine);

}
