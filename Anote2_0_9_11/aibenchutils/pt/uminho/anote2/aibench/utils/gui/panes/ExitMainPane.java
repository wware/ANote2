package pt.uminho.anote2.aibench.utils.gui.panes;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;



public class ExitMainPane extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanelThreeButtonPanel;
	private JButton jButtonYes;
	private JButton jButtonCancel;
	private JButton jButtonHelp;
	private JLabel jLabelMessage;
	private JButton jButtonNo;

	public ExitMainPane()
	{
		initGUI();
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.0};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			{
				jPanelThreeButtonPanel = new JPanel();
				GridBagLayout jPanelThreeButtonPanelLayout = new GridBagLayout();
				this.add(jPanelThreeButtonPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelThreeButtonPanelLayout.rowWeights = new double[] {0.1};
				jPanelThreeButtonPanelLayout.rowHeights = new int[] {7};
				jPanelThreeButtonPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.0, 0.1};
				jPanelThreeButtonPanelLayout.columnWidths = new int[] {7, 7, 20, 7};
				jPanelThreeButtonPanel.setLayout(jPanelThreeButtonPanelLayout);
				{
					jButtonYes = new JButton();
					jPanelThreeButtonPanel.add(jButtonYes, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jButtonYes.setText("Yes");
					jButtonYes.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/ok22.png")));	
				}
				{
					jButtonNo = new JButton();
					jPanelThreeButtonPanel.add(jButtonNo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jButtonNo.setText("No");
					jButtonNo.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/ballshutdown.png")));				
				}
				{
					jButtonCancel = new JButton();
					jPanelThreeButtonPanel.add(jButtonCancel, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jButtonCancel.setText("Cancel");
					jButtonCancel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/cancel22.png")));				
				}
				{
					jButtonHelp = new JButton();
					jPanelThreeButtonPanel.add(jButtonHelp, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
					jButtonHelp.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							try {
								Help.internetAccess(GlobalOptions.wikiGeneralLink+"Exit_Program");
							} catch (IOException e) {
								TreatExceptionForAIbench.treatExcepion(e);
							}
						}
					});
				}
			}
			{
				jLabelMessage = new JLabel();
				this.add(jLabelMessage, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelMessage.setText("Would you like to save session - yes, no, cancel ?");
				jLabelMessage.setFont(new java.awt.Font("Segoe UI",0,18));
			}
		}

	}

	public JButton getjButtonYes() {
		return jButtonYes;
	}

	public JButton getjButtonCancel() {
		return jButtonCancel;
	}

	public JButton getjButtonNo() {
		return jButtonNo;
	}
	
	

}
