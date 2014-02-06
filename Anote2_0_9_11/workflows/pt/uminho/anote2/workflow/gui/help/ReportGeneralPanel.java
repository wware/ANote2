package pt.uminho.anote2.workflow.gui.help;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;

import pt.uminho.anote2.datastructures.utils.Utils;

public class ReportGeneralPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanelProcessesList;
	private JPanel jPanelGeneralInfo;
	private JTextField jTextFieldTime;
	private JLabel jLabelTime;
	private JPanel jPanelTime;
	private JList jListProcessList;
	private JScrollPane jScrollPaneProcessesList;
	private long time;

	public ReportGeneralPanel()
	{
		time = 0;
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.025, 0.1};
				thisLayout.rowHeights = new int[] {7, 7};
				thisLayout.columnWeights = new double[] {0.1};
				thisLayout.columnWidths = new int[] {7};
				this.setLayout(thisLayout);
				{
					jPanelProcessesList = new JPanel();
					BoxLayout jPanelProcessesListLayout = new BoxLayout(jPanelProcessesList, javax.swing.BoxLayout.X_AXIS);
					jPanelProcessesList.setLayout(jPanelProcessesListLayout);
					this.add(jPanelProcessesList, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelProcessesList.setBorder(BorderFactory.createTitledBorder("Workflow Steps"));
					{
						jScrollPaneProcessesList = new JScrollPane();
						jPanelProcessesList.add(jScrollPaneProcessesList);
						{
							ListModel jListProcessListModel = 
									new DefaultComboBoxModel();
							jListProcessList = new JList();
							jScrollPaneProcessesList.setViewportView(jListProcessList);
							jListProcessList.setModel(jListProcessListModel);
						}
					}
				}
				{
					jPanelGeneralInfo = new JPanel();
					GridBagLayout jPanelGeneralInfoLayout = new GridBagLayout();
					this.add(jPanelGeneralInfo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelGeneralInfoLayout.rowWeights = new double[] {0.1};
					jPanelGeneralInfoLayout.rowHeights = new int[] {7};
					jPanelGeneralInfoLayout.columnWeights = new double[] {0.1, 0.1};
					jPanelGeneralInfoLayout.columnWidths = new int[] {7, 7};
					jPanelGeneralInfo.setLayout(jPanelGeneralInfoLayout);
					{
						jPanelTime = new JPanel();
						GridBagLayout jPanelTimeLayout = new GridBagLayout();
						jPanelTimeLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
						jPanelTimeLayout.rowHeights = new int[] {7, 7, 7};
						jPanelTimeLayout.columnWeights = new double[] {0.1, 0.1};
						jPanelTimeLayout.columnWidths = new int[] {7, 7};
						jPanelTime.setLayout(jPanelTimeLayout);
						jPanelGeneralInfo.add(jPanelTime, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						{
							jLabelTime = new JLabel();
							jPanelTime.add(jLabelTime, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							jLabelTime.setText("Processing Time :");
						}
						{
							jTextFieldTime = new JTextField();
							jTextFieldTime.setEditable(false);
							jPanelTime.add(jTextFieldTime, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setTime(float time)
	{
		this.time += time;
		if(jTextFieldTime!=null)
			jTextFieldTime.setText(Utils.convertTimeToString((long) this.time));
	}
	
	public void addStepToWorkflow(String stepSummary)
	{
		if(jListProcessList!=null)
			((DefaultComboBoxModel)jListProcessList.getModel()).addElement(stepSummary);
	}

}
