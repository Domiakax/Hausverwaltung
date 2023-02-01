package database;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface KundeDAO {
	
	@SqlUpdate(""" 
			Create Table if not exists kunde(
				uuid Binary(16) default uuid() primary key,
				name varchar(255),
				vorname varchar(255)
			)
			""")
	void createTable();
	
	@SqlUpdate("""
			Insert into kunde(name, vorname)
			values(:name, :vorname)
			""")
	@GetGeneratedKeys
	long insert(@Bind("name") String name, @Bind("vorname") String vorname);

}
