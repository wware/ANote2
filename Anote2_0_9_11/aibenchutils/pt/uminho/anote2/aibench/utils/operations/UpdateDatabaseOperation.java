package pt.uminho.anote2.aibench.utils.operations;

import java.io.IOException;
import java.sql.SQLException;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation(description="Update Database")
public class UpdateDatabaseOperation {

	@Port(name="update",direction=Direction.OUTPUT,order=1)
	public String about()
	{
		System.gc();
		IDatabase db;
		try {
			db = Configuration.getDatabase();
			if(!db.isfill())
			{
				db.fillDataBaseTables();
			}
			db.updateDatabase();
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Database Update Fail.");	
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			new ShowMessagePopup("Database Update Fail.");	
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (IOException e) {
			new ShowMessagePopup("Database Update Fail.");	
			TreatExceptionForAIbench.treatExcepion(e);
		}
		new ShowMessagePopup("Database Update Done.");
		return null;
	}
}
