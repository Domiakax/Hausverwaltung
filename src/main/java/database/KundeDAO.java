package database;

import java.util.UUID;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface KundeDAO {
	
//	@SqlUpdate(""" 
//			Create Table if not exists kunde(
//				uuid uuid primary key default uuid(),
//				name varchar(255),
//				vorname varchar(255)
//			)
//			""")
	@SqlUpdate(""" 
			Create Table if not exists kunde(
				id int primary key auto_increment, 
				uuid uuid not null,
				name varchar(255),
				vorname varchar(255)
			)
			""")
	void createTable();
	
	@SqlUpdate("""
			Insert into kunde(uuid, name, vorname)
			values(:id, :name, :vorname)
			""")
	void insert(@BindBean Kunde k);

}
