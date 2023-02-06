package database;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
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
	void insert(@Bind("id") String uuid, @Bind("name") String name, @Bind("vorname") String vorname);
	
	@SqlUpdate("""
			Update Kunde set name = :name, vorname = :vorname where uuid = :uuid; 
			""")
	int update(@Bind("uuid") String uuid, @Bind("name") String name, @Bind("vorname") String vorname);
	
	@SqlQuery("""
			Select uuid as k_id, name as k_name, vorname as k_vorname from kunde where uuid = :uuid;
			""")
	@RegisterBeanMapper(Kunde.class)
	Kunde get(@Bind("uuid") String uuid);

}
