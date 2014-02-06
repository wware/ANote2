package pt.uminho.anote2.aibench.utils.firstrun;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.utils.wizart.WizartStandard;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class StartWizart1 extends WizartStandard{
	
	private static final long serialVersionUID = 1L;
	private JTextPane jTextPaneInicial;
	private JPanel jpanelUp;

	public StartWizart1(int sizeH, int sizeV,List<Object> param) {
		super(sizeH,sizeV,param);
		changeGUI();
		this.setTitle("Start Wizart Step 1/4");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}

	private void changeGUI() {
		setEnableBackButton(false);
		setEnableDoneButton(false);
		{
			{
				jTextPaneInicial = new JTextPane();
				jTextPaneInicial.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "", TitledBorder.LEADING, TitledBorder.TOP));
				jTextPaneInicial.setText("This is probably the first time that you run @Note2 \n\n Take a few seconds to configure System \n\n Thanks");
				jTextPaneInicial.setEditable(false);
				jTextPaneInicial.setBackground(Color.white);
				jpanelUp = new JPanel();
				GridBagLayout jpanelUpLayout = new GridBagLayout();
				jpanelUp.setLayout(jpanelUpLayout);
				jpanelUp.add(jTextPaneInicial, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				getJScrollPaneUpPanel().setViewportView(jpanelUp);
				jpanelUpLayout.rowWeights = new double[] {0.1, 0.1};
				jpanelUpLayout.rowHeights = new int[] {7, 7};
				jpanelUpLayout.columnWeights = new double[] {0.1};
				jpanelUpLayout.columnWidths = new int[] {7};
			}
		}		
	}
	
	
	public void done() {}

	public void goBack() {}

	public void goNext() {
		this.setVisible(false);
		new StartWizart2(600,350,this.getParam());
	}

}
