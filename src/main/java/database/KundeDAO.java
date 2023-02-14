package database;

import java.util.List;
import java.util.UUID;

import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
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
				uuid uuid unique not null,
				name varchar(255),
				vorname varchar(255)
			)
			""")
	void createTable();
	
	@SqlUpdate("""
			Insert into kunde(uuid, name, vorname)
			values(:uuid, :name, :vorname)
			""")
	void insert(@BindBean Kunde k);
	
	@SqlUpdate("""
			Update Kunde set name = :name, vorname = :vorname where uuid = :uuid; 
			""")
	int update(@BindBean Kunde k);
	
	@SqlQuery("""
			Select uuid as k_id, name as k_name, vorname as k_vorname from kunde where uuid = :p_uuid;
			""")
	@RegisterRowMapper(KundeRowMapper.class)
	Kunde get(@Bind("p_uuid") UUID p_uuid);
	
	@SqlQuery("""
			Select uuid as k_id, name as k_name, vorname as k_vorname from kunde
			""")
	@RegisterRowMapper(KundeRowMapper.class)
	List<Kunde> getAll();

}
