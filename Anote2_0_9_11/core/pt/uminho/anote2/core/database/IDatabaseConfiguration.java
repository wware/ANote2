package pt.uminho.anote2.core.database;

public interface IDatabaseConfiguration {
	public IDatabase getDatabaseConfiguration();
	public boolean validConfiguration();
}
