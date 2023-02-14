package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class KundeRowMapper implements RowMapper<Kunde>{

	@Override
	public Kunde map(ResultSet rs, StatementContext ctx) throws SQLException {
		Kunde k = new Kunde();
		k.setName(rs.getString(Kunde.FIELD_KUNDEN_NAME));
		k.setVorname(rs.getString(Kunde.FIELD_KUNDEN_VORNAME));
		k.setUuid(UUID.fromString(rs.getString(Kunde.FIELD_KUNDEN_ID)));
		return k;
	}

}
