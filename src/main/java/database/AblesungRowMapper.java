package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class AblesungRowMapper implements RowMapper<Ablesung>{

	@Override
	public Ablesung map(ResultSet rs, StatementContext ctx) throws SQLException {
		System.out.println("Create Ablesung");
		Ablesung a = new Ablesung();
		a.setUuid(UUID.fromString(rs.getString(Ablesung.FIELD_ABLESUNG_ID)));
		a.setDatum(LocalDate.parse(rs.getString(Ablesung.FIELD_ABLESUNG_DATUM)));
		a.setKommentar(rs.getString(Ablesung.FIELD_ABLESUNG_KOMMENTAR));
		a.setNeuEingebaut(rs.getBoolean(Ablesung.FIELD_ABLESUNG_NEUEINGEBAUT));
		a.setZaehlernummer(rs.getString(Ablesung.FIELD_ABLESUNG_ZAEHLERNUMMER));
		a.setZaehlerstand(rs.getDouble(Ablesung.FIELD_ABLESUNG_ZAEHLERSTAND));
		
		Kunde k = new Kunde();
		String sUUID = rs.getString(Kunde.FIELD_KUNDEN_ID);
		if(sUUID!=null) {
			k.setUuid(UUID.fromString(sUUID));
			k.setName(rs.getString(Kunde.FIELD_KUNDEN_NAME));
			k.setVorname(rs.getString(Kunde.FIELD_KUNDEN_VORNAME));
		}
		System.out.println("Ablesung created");
		return a;
	}
	

}
