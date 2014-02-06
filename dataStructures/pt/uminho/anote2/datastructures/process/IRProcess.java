package pt.uminho.anote2.datastructures.process;

import pt.uminho.anote2.core.configuration.IProxy;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.configuration.ProxyAndDatabase;
import pt.uminho.anote2.process.IR.IIRProcess;

public abstract class IRProcess extends ProxyAndDatabase implements IIRProcess{

	public IRProcess(IProxy proxy, IDatabase db) {
		super(proxy, db);
	}

	public IDatabase getDB() {
		return this.getDatabase();
	}

	public int getID() {
		return -1;
	}
	
}
