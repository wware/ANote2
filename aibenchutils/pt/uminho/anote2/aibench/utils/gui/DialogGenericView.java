package pt.uminho.anote2.aibench.utils.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

/* This class is a generic JDialog that contains a panel button's with cancel and OK actions. Each class that extends
 * this one, have to add a action listener to the button OK, the cancel button have already an associated action.
 * This class have also the three methods of the ImputGUI interface already implemented and the ParamsReceiver variable. */
public abstract class DialogGenericView extends JDialog implements InputGUI{

	private static final long serialVersionUID = -5858255309204568268L;
	
	protected JButton cancelButton;
	protected JButton okButton;
	protected JPanel buttonsPanel;
	private String title="";
	
	protected ParamsReceiver paramsRec = null;
	
	public DialogGenericView(String title){
		super(Workbench.getInstance().getMainFrame());
		this.title = title;
	}
	
	public DialogGenericView(){
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
			GridBagLayout buttonsPanelLayout = new GridBagLayout();
			buttonsPanelLayout.rowWeights = new double[] {0.1};
			buttonsPanelLayout.rowHeights = new int[] {7};
			buttonsPanelLayout.columnWeights = new double[] {0.1, 0.1};
			buttonsPanelLayout.columnWidths = new int[] {7, 7};
			buttonsPanel.setLayout(buttonsPanelLayout);
			buttonsPanel.add(getOkButton(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 1, 3, 4), 0, 0));
			buttonsPanel.add(getCancelButton(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 4, 3, 1), 0, 0));
		}
		return buttonsPanel;
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
		//this.setModal(false);
		this.setVisible(false);
		this.dispose();
		
	}

	public void init(ParamsReceiver arg0, @SuppressWarnings("rawtypes") OperationDefinition arg1) {
		this.paramsRec = arg0;
		this.setTitle(this.title);
		Utilities.centerOnOwner(this);
		this.setVisible(true);
		//this.setModal(true);
	}

	public void onValidationError(Throwable arg0) {
		Workbench.getInstance().error(arg0);		
	}
	
	
	
	/* ----------------------------------- Abstract Methods ----------------------------------- */
	
	protected abstract void okButtonAction();
	
}
