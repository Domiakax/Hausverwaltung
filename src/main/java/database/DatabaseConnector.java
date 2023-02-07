package database;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class DatabaseConnector {
	
	private static DatabaseConnector connector;
	private static final String urlDatabase = "jdbc:mariadb://localhost:3306/hausverwaltung";
	private static final String dbUser = "root";
	private static final String dbUserPW = "";
	private static KundeDAO kundeDao;
	private static AblesungDAO ablesungDao;
	
	public static DatabaseConnector getDatabaseConnector() {
		if(connector == null) {
			connector = new DatabaseConnector();
		}
		return connector;
	}
	
	private DatabaseConnector() {
		final Jdbi jdbi  = Jdbi.create(urlDatabase, dbUser, dbUserPW);
		jdbi.installPlugin(new SqlObjectPlugin());
		final Handle handle = jdbi.open();
		kundeDao = handle.attach(KundeDAO.class);
		ablesungDao = handle.attach(AblesungDAO.class);
		System.out.println("create Table");
		kundeDao.createTable();
		ablesungDao.createTable();
	}
	
	public void addKunde(Kunde k) {
		System.out.println("hier");
		kundeDao.insert(k.getId().toString(), k.getName(), k.getVorname());;
	}
	
	public int updateKunde(Kunde k) {
		return kundeDao.update(k.getId().toString(), k.getName(), k.getVorname());
	}
	
	public Kunde getKunde(String uuid) {
		System.out.println("Before SQL Query");
		Kunde k = kundeDao.get(uuid);
		return k;
	}

}
