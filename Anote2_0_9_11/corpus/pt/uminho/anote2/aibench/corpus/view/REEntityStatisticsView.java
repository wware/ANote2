package pt.uminho.anote2.aibench.corpus.view;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.corpus.gui.help.NERGeneralStatistics;
import pt.uminho.anote2.aibench.corpus.gui.help.NERStatisticsPanel;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;

/**
 * 
 * @author Hugo Costa,Rafael Carreira
 * 
 * Objectivo : Visualizar estatisticas das anota��es presentes no corpus
 *
 */
public class REEntityStatisticsView extends JPanel implements Observer{

	private static final long serialVersionUID = 6866512520510402222L;

	private JSplitPane jSplitPane1;	
	private RESchema reProcess;
	private NERStatisticsPanel jTabbedPaneNERStatistics;
	private NERGeneralStatistics jPanelGeneralStatistics;

	public REEntityStatisticsView(RESchema process)
	{
		this.reProcess=process;
		try {
			initGUI();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}
	
	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		{
			{
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1};
				thisLayout.rowHeights = new int[] {100, 7, 7, 7};
				thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				thisLayout.columnWidths = new int[] {7, 7, 7, 7};
				this.setLayout(thisLayout);
				
				jSplitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				jSplitPane1.setOneTouchExpandable(true);
				jSplitPane1.setResizeWeight(0.25);
				this.add(jSplitPane1, new GridBagConstraints(0, 0, 4, 5, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				this.jSplitPane1.setTopComponent(getNERGobalStatitics());
				this.jSplitPane1.setBottomComponent(getNERStatiticsPane());
				this.jSplitPane1.setDividerLocation(0.2);
			}
		}	
	}
	
	private JTabbedPane getNERStatiticsPane() throws SQLException, DatabaseLoadDriverException {
		if(jTabbedPaneNERStatistics ==  null)
		{
			jTabbedPaneNERStatistics = new NERStatisticsPanel(reProcess.getStatistics(),true);
		}
		return jTabbedPaneNERStatistics;
	}
	
	private JPanel getNERGobalStatitics() throws SQLException, DatabaseLoadDriverException {
		if(jPanelGeneralStatistics == null)
		{
			jPanelGeneralStatistics = new NERGeneralStatistics(reProcess.getStatistics());
		}
		return jPanelGeneralStatistics;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
	}


	public RESchema getNerProcess() {
		return reProcess;
	}
	
}
