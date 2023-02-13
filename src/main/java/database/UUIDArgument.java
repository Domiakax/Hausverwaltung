package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.statement.StatementContext;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UUIDArgument implements Argument{
	
	private UUID uuid;
	
	public UUIDArgument(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void apply(int position, PreparedStatement statement, StatementContext ctx) throws SQLException {
		statement.setString(position, uuid.toString());
	}

}
