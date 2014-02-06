package pt.uminho.anote2.aibench.corpus.gui.report;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import pt.uminho.anote2.aibench.corpus.gui.help.NERExportPanel;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewOkButtonOnly;
import pt.uminho.anote2.core.report.processes.INERSchemaExportReport;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class NERExportReportGUI extends DialogGenericViewOkButtonOnly{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private INERSchemaExportReport report;

	public NERExportReportGUI(INERSchemaExportReport report)
	{
		super("NER Export Report");
		this.report = report;
		initGUI();
		this.setSize(GlobalOptions.smallWidth, GlobalOptions.smallHeight);
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}

	private void initGUI() {
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1, 0.0};
		thisLayout.rowHeights = new int[] {7, 7};
		thisLayout.columnWeights = new double[] {0.1};
		thisLayout.columnWidths = new int[] {7};
		getContentPane().setLayout(thisLayout);
		{
			getContentPane().add(new NERExportPanel(report), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));	
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
