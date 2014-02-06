package pt.uminho.anote2.aibench.utils.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;

import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;

/* This class is a generic JDialog that contains a panel button's with cancel and OK actions. Each class that extends
 * this one, have to add a action listener to the button OK, the cancel button have already an associated action.
 * This class have also the three methods of the ImputGUI interface already implemented and the ParamsReceiver variable. */


public abstract class DialogGenericViewOkButtonOnly extends JDialog{

	private static final long serialVersionUID = -5858255309204568268L;

	protected JButton okButton;
	protected JPanel buttonsPanel;
	private String title="";
	
	protected ParamsReceiver paramsRec = null;

	private JButton jButtonHelp;
	
	public DialogGenericViewOkButtonOnly(String title){
		super(Workbench.getInstance().getMainFrame());
		this.title = title;
	}
	
	public DialogGenericViewOkButtonOnly(){
		super(Workbench.getInstance().getMainFrame());
	}
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public JPanel getButtonsPanel() {
		if(buttonsPanel == null) {
			buttonsPanel = new JPanel();
			buttonsPanel.setBackground(Color.WHITE);
			GridBagLayout buttonsPanelLayout = new GridBagLayout();
			buttonsPanelLayout.rowWeights = new double[] {0.1};
			buttonsPanelLayout.rowHeights = new int[] {7};
			if(getHelpLink()!=null)
			{
				buttonsPanelLayout.columnWeights = new double[] {0.0,0.1};
				buttonsPanelLayout.columnWidths = new int[] {7,7};
				buttonsPanel.setLayout(buttonsPanelLayout);
				buttonsPanel.add(getJButtonHelp(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 1, 3, 4), 0, 0));
				buttonsPanel.add(getOkButton(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 1, 3, 4), 0, 0));
			}
			else
			{
				buttonsPanelLayout.columnWeights = new double[] {0.1};
				buttonsPanelLayout.columnWidths = new int[] {7};
				buttonsPanel.setLayout(buttonsPanelLayout);
				buttonsPanel.add(getOkButton(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 1, 3, 4), 0, 0));
			}
			buttonsPanel.setOpaque(false);

		}
		return buttonsPanel;
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {

			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						Help.internetAccess(getHelpLink());
					} catch (IOException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
			jButtonHelp.setEnabled(true);
			jButtonHelp.setVisible(true);
		}
		return jButtonHelp;
	}

	private JButton getOkButton() {
		if(okButton == null) {
			okButton = new JButton();
			okButton.setText("Ok");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					okButtonAction();
				}
			});
		}
		return okButton;
	}

	public void finish(){
		this.setModal(false);
		this.setVisible(false);
		this.dispose();
	}
	
	/* ----------------------------------- Abstract Methods ----------------------------------- */
	
	protected abstract void okButtonAction();
	
	protected abstract String getHelpLink();

	
}
