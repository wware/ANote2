package pt.uminho.anote2.workflow.gui.wizard.panes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.re.IREConfiguration;
import pt.uminho.anote2.workflow.datastructures.REWorkflowProcessesAvailableEnum;
import pt.uminho.anote2.workflow.gui.AREPanel;

public abstract class AREPaneWizard extends AREPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JComboBox jComboBoxREProcesses;
	private AREPanel jPanelUnder;
	private JPanel jPanelComboBox;
	private List<String> defaultSettings;

	public AREPaneWizard(List<String> defaultSettings) throws SQLException, DatabaseLoadDriverException
	{
		this.defaultSettings = defaultSettings;
		initGUI();
		defaultSetting();
	}
	
	protected abstract void defaultSetting();
	
	private void populateGUI() throws SQLException, DatabaseLoadDriverException {
		changeMainPanel();		
	}

	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.9};
			thisLayout.rowHeights = new int[] {20, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			{
				jPanelComboBox = new JPanel();
				GridBagLayout jPanelComboBoxLayout = new GridBagLayout();
				jPanelComboBoxLayout.rowWeights = new double[] {0.1};
				jPanelComboBoxLayout.rowHeights = new int[] {7};
				jPanelComboBoxLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
				jPanelComboBoxLayout.columnWidths = new int[] {7, 7, 7};
				jPanelComboBox.setLayout(jPanelComboBoxLayout);
				this.add(jPanelComboBox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelComboBox.setBorder(BorderFactory.createTitledBorder("Select RE Process"));
				{
					ComboBoxModel jComboBoxNERProcessesModel = getComboBoxModel();
					jComboBoxREProcesses = new JComboBox();
					jPanelComboBox.add(jComboBoxREProcesses, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 10, 5), 0, 0));
					jComboBoxREProcesses.setModel(jComboBoxNERProcessesModel);
					jComboBoxREProcesses.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent arg0) {
							try {
								changeMainPanel();
							} catch (SQLException e) {
								TreatExceptionForAIbench.treatExcepion(e);
							} catch (DatabaseLoadDriverException e) {
								TreatExceptionForAIbench.treatExcepion(e);
							}
						}
					});
					populateGUI();
				}
			}
			{
				this.add(jPanelUnder, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}

	}

	protected void changeMainPanel() throws SQLException, DatabaseLoadDriverException {
		REWorkflowProcessesAvailableEnum reEnum = (REWorkflowProcessesAvailableEnum) jComboBoxREProcesses.getSelectedItem();
		jPanelUnder = reEnum.getMainComponent(defaultSettings);
		jPanelUnder.updateUI();
	}

	private ComboBoxModel getComboBoxModel() {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for(REWorkflowProcessesAvailableEnum reEnum :REWorkflowProcessesAvailableEnum.values())
		{
			model.addElement(reEnum);
		}
		return model;
	}

	@Override
	public boolean validateOptions() {
		return jPanelUnder.validateOptions();
	}

	@Override
	public IREConfiguration getConfiguration(ICorpus corpus, IIEProcess process) {
		return jPanelUnder.getConfiguration(corpus, process);
	}

	@Override
	public REWorkflowProcessesAvailableEnum getREProcess() {
		return jPanelUnder.getREProcess();
	}


}
