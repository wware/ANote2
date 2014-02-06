package pt.uminho.anote2.aibench.corpus.gui;
import java.sql.SQLException;

import pt.uminho.anote2.aibench.corpus.structures.CorporaProperties;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;


public class ChooseClassColorGUI extends ChooseColorGUI {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ChooseClassColorGUI(int classeID, String color){
		super("Change Class Color",color,classeID);
	}


	public void okButtonPerformed(int classID,String newColor){
		try {
			CorporaProperties.updateCorporaClassColor(classID,newColor);
		} catch (SQLException e) {
			new ShowMessagePopup("Class Color Change Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
			finish();
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Class Color Change Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
			finish();
		}
		new ShowMessagePopup("Class Color Change .");
		finish();		
	}

}
