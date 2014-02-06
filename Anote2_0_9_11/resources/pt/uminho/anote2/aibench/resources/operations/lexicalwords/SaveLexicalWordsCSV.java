package pt.uminho.anote2.aibench.resources.operations.lexicalwords;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import pt.uminho.anote2.aibench.resources.datatypes.LexicalWordsAibench;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation(description="Save lookup information into csv file",enabled=false)
public class SaveLexicalWordsCSV {
	
	private LexicalWordsAibench lexical;

	@Port(name="lexicalwords",direction=Direction.INPUT,order=1)
	public void setDLookupTable(LexicalWordsAibench lexical)
	{
		this.lexical=lexical;
	}
	
	@Port(name="file",direction=Direction.INPUT,order=2)
	public void setfile(File file)
	{
		System.gc();
		if(file.isDirectory())
			file = new File(file.getAbsoluteFile()+"/"+"lexicawords_"+lexical.getID()+".tsv");
		try {
			this.lexical.exportCSVFile(file.getAbsolutePath());
		} catch (IOException e) {
			new ShowMessagePopup("Export Lexical Words Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		} catch (SQLException e) {
			new ShowMessagePopup("Export Lexical Words Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Export Lexical Words Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		}
		new ShowMessagePopup("Export Lexical Words Done .");
	}
}
