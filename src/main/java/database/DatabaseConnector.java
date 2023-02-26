package database;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class DatabaseConnector {
	
	private static DatabaseConnector connector;
	private static final String urlDatabase = "jdbc:mariadb://localhost:3306/hausverwaltung";
	private static final String dbUser = "root";
	private static final String dbUserPW = "";
	private static KundeDAO kundeDao;
	private static AblesungDAO ablesungDao;
	
	private static final LocalDate DATELINE = LocalDate.of(LocalDate.now().getYear()-2, 1, 1);
	
	public static DatabaseConnector getDatabaseConnector() {
		if(connector == null) {
			connector = new DatabaseConnector();
		}
		return connector;
	}
	
	private DatabaseConnector() {
		System.out.println("Setup DatabaseConnector");
		final Jdbi jdbi  = Jdbi.create(urlDatabase, dbUser, dbUserPW);
		jdbi.installPlugin(new SqlObjectPlugin());
		final Handle handle = jdbi.open();
		handle.registerArgument(new UUIDArgumentFactory());
		handle.registerRowMapper(BeanMapper.factory(Kunde.class));
		handle.registerRowMapper(BeanMapper.factory(Ablesung.class));
		System.out.println("Setup Create Tables");
		kundeDao = handle.attach(KundeDAO.class);
//		System.out.println("Table Kunde created");
		ablesungDao = handle.attach(AblesungDAO.class);
		System.out.println("create Table");
		kundeDao.createTable();
		ablesungDao.createTable();
	}
	
	public void addKunde(Kunde k) {
		System.out.println("addKunde: " + k.toString());
		kundeDao.insert(k);
	}
	
	public int updateKunde(Kunde k) {
		return kundeDao.update(k);
	}
	
	public Kunde getKunde(String uuid) {
		System.out.println("Before SQL Query");
		Kunde k = kundeDao.get(UUID.fromString(uuid));
		System.out.println("Found");
		return k;
	}
	
	public Ablesung getAblesung(String uuid) {
		System.out.println("Before SQL");
		Ablesung a = ablesungDao.getAblesung(UUID.fromString(uuid));
		return a;
	}
	
	public void addAblesung(Ablesung a) {
		System.out.println("Before SQL");
		ablesungDao.addAblesung(a);
	}
	
	public boolean updateAblesung(Ablesung a) {
		if(a == null) {
			return false;
		}
		System.out.println("Before SQL Update");
		int changedRows = ablesungDao.updateAblesung(a);
		return changedRows == 0 ? false : true;
	}
	
	
	public List<Kunde> getEveryKunde(){
		return kundeDao.getAll();
	}
	
	
	public List<Ablesung> getAblesungenForClientStart(){
		
		return ablesungDao.getAblesungenForClientStart(DATELINE);
	}

}
