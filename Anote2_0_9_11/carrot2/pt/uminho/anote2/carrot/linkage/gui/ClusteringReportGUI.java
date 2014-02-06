package pt.uminho.anote2.carrot.linkage.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewOkButtonOnly;
import pt.uminho.anote2.carrot.linkage.gui.help.ClusteringPane;
import pt.uminho.anote2.carrot.linkage.report.ICLusteringReport;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class ClusteringReportGUI extends DialogGenericViewOkButtonOnly{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ICLusteringReport report;
	
	public ClusteringReportGUI(ICLusteringReport report)
	{
		super("Clustering Report");
		this.report = report;
		try {
			initGUI();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DatabaseLoadDriverException e) {
			e.printStackTrace();
		}
		this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}

	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1, 0.0};
		thisLayout.rowHeights = new int[] {7, 7};
		thisLayout.columnWeights = new double[] {0.1};
		thisLayout.columnWidths = new int[] {7};
		getContentPane().setLayout(thisLayout);
		{
			getContentPane().add(new ClusteringPane(report), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));	
		}
		{
			getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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
