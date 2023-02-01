package database;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class Main {
	public static void main(String[] args) {
		Jdbi jdbi = Jdbi.create("jdbc:mariadb://localhost:3306/hausverwaltung", "root", "");
		System.out.println("connected");
		jdbi.installPlugin(new SqlObjectPlugin());
		final Handle handle = jdbi.open();
		
		final KundeDAO kundeDao = handle.attach(KundeDAO.class);
		kundeDao.createTable();
		
		Kunde k = new Kunde("A", "a");
		System.out.println(kundeDao.insert(k.getName(), k.getVorname()));
		
		System.out.println("Done");
	}

}
