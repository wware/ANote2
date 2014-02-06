package pt.uminho.anote2.datastructures.test;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.mysql.MySQLDatabase;

public class UpdateDatabaseTest {
	
	public static void main(String[] args) {
		IDatabase database = new MySQLDatabase("localhost", "3306", "anote_db", "root", "admin");

	}

}
