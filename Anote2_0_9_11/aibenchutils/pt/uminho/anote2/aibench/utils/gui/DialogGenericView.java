package pt.uminho.anote2.aibench.utils.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;

import es.uvigo.ei.aibench.workbench.Workbench;

/* This class is a generic JDialog that contains a panel button's with cancel and OK actions. Each class that extends
 * this one, have to add a action listener to the button OK, the cancel button have already an associated action.
 * This class have also the three methods of the ImputGUI interface already implemented and the ParamsReceiver variable. */
public abstract class DialogGenericView extends JDialog{

	private static final long serialVersionUID = -2L;
	
	protected JButton cancelButton;
	protected JButton okButton;
	protected JPanel buttonsPanel;
	
	private JButton jButtonHelp;
	
	public DialogGenericView(String title){
		super(Workbench.getInstance().getMainFrame());
		this.setTitle(title);
	}
	
	public DialogGenericView(){
		super(Workbench.getInstance().getMainFrame());
	}

	public JPanel getButtonsPanel() {
		if(buttonsPanel == null) {
			buttonsPanel = new JPanel();
			GridBagLayout buttonsPanelLayout = new GridBagLayout();
			buttonsPanelLayout.rowWeights = new double[] {0.1};
			buttonsPanelLayout.rowHeights = new int[] {7};
			buttonsPanelLayout.columnWidths = new int[] {7, 7};
			buttonsPanel.setLayout(buttonsPanelLayout);
			if(getHelpLink()!=null)
			{
				buttonsPanelLayout.columnWeights = new double[] {0.1, 0.0,0.1};
				buttonsPanel.add(getOkButton(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 1, 3, 4), 0, 0));
				buttonsPanel.add(getJButtonHelp(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 4, 3, 1), 0, 0));

			}
			else
			{
				buttonsPanelLayout.columnWeights = new double[] {0.1,0.1};
				buttonsPanel.add(getOkButton(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 1, 3, 4), 0, 0));

			}
			buttonsPanel.add(getCancelButton(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 4, 3, 1), 0, 0));
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
			okButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/ok22.png")));	
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					okButtonAction();
				}
			});
		}
		return okButton;
	}
	
	public void addActionListnerToOkButton(ActionListener istner)
	{
		if(okButton!=null)
		{
			okButton.addActionListener(istner);
		}
	}
	
	private JButton getCancelButton() {
		if(cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			URL str = getClass().getClassLoader().getResource("icons/cancel22.png");
			cancelButton.setIcon(new ImageIcon(str));
			cancelButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					finish();
				}
			});
		}
		return cancelButton;
	}
	
	public void finish(){
		setModal(false);
		setVisible(false);
		dispose();
	}
	
	public void changeOkButtonName(String newNAme) {
		okButton.setName(newNAme);
	}
	
	/* ----------------------------------- Abstract Methods ----------------------------------- */
	
	protected abstract void okButtonAction();
	
	protected abstract String getHelpLink();



	
}
