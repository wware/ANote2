package pt.uminho.anote2.aibench.corpus.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

import pt.uminho.anote2.aibench.corpus.structures.CorporaPropertiesGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewInputGUI;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class ClassEditGUI extends DialogGenericViewInputGUI{


	private static final long serialVersionUID = 1L;

	public ClassEditGUI() 
	{
		super("Class Color Change");
		try {
			initGUI();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		}
		this.setModal(true);
	}

	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		GridBagLayout thisLayout = new GridBagLayout();	
		thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.0};
		thisLayout.rowHeights = new int[] {7, 7, 7, 7};
		thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		thisLayout.columnWidths = new int[] {7, 7, 7, 7};
		this.setLayout(thisLayout);
		getContentPane().add(CorporaPropertiesGUI.getjScrollPaneClassColor(), new GridBagConstraints(0, 0, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getButtonsPanel(), new GridBagConstraints(0, 3, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	protected void okButtonAction() {
		finish();
	}

	
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Change_Class_Colors";
	}

	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		this.paramsRec = arg0;
		this.setSize(600,400);
		Utilities.centerOnOwner(this);
		this.setVisible(true);
	}
}
