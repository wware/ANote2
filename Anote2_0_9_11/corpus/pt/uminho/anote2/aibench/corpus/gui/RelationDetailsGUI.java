package pt.uminho.anote2.aibench.corpus.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

import pt.uminho.anote2.aibench.corpus.gui.help.RelationDetaillsPanel;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewOkButtonOnly;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.exceptions.process.reprocess.RelationDelimiterExeption;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IE.IIEProcess;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class RelationDetailsGUI extends DialogGenericViewOkButtonOnly{

	private static final long serialVersionUID = 1L;
	private RelationDetaillsPanel relationdelatils;
	
	private IEventAnnotation event;
	private IIEProcess process;

	public RelationDetailsGUI(IEventAnnotation event,IIEProcess process) throws SQLException, DatabaseLoadDriverException, RelationDelimiterExeption{
		super("Relation Details : "+event.getID());
		this.event = event;
		this.process = process;
		initGUI();
		this.setSize(GlobalOptions.superWidth, GlobalOptions.superHeight);
		Utilities.centerOnOwner(this);	
		this.setModal(true);
		this.setVisible(true);	
	}
	
	private void initGUI() throws SQLException, DatabaseLoadDriverException, RelationDelimiterExeption {
		GridBagLayout general = new GridBagLayout();
		general.rowWeights = new double[] {0.1,0.0};
		general.rowHeights = new int[] {7,7};
		general.columnWeights = new double[] {0.1};
		general.columnWidths = new int[] {7};
		this.setLayout(general);
		{
			getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		{
			relationdelatils = new RelationDetaillsPanel(event, process);
			getContentPane().add(relationdelatils, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}

	}

	@Override
	protected void okButtonAction() {
		finish();	
	}

	@Override
	protected String getHelpLink() {
		return null;
	}
}
