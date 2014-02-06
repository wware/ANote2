package pt.uminho.anote2.aibench.utils.firstrun;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.aibench.utils.settings.proxy.PortInputVerifier;
import pt.uminho.anote2.aibench.utils.settings.proxy.ProxyUtils;
import pt.uminho.anote2.aibench.utils.settings.proxy.URLIPv4InputVerifier;
import pt.uminho.anote2.aibench.utils.wizart.WizartStandard;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class StartWizart2 extends WizartStandard{
	

	private static final long serialVersionUID = 1L;
	private JTextPane jTextPaneProxyInformation;
	private JPanel proxyPanel;
	private JCheckBox jCheckBox1;
	private JLabel portLabel;
	private JLabel hostLabel;
	private JFormattedTextField hostTextField;
	private JFormattedTextField portTextField;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JPanel jpanelUp;

	public StartWizart2(int sizeH, int sizeV,List<Object> param) {
		super(sizeH, sizeV,param);	
		changeGUI();
		this.setTitle("Start Wizart Step 2/4");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}


	private void changeGUI() {
		setEnableDoneButton(false);
		{
			jpanelUp = new JPanel();
			GridBagLayout jpanelUpLayout = new GridBagLayout();
			getJScrollPaneUpPanel().setViewportView(jpanelUp);
			jpanelUpLayout.rowWeights = new double[] {0.0, 0.1};
			jpanelUpLayout.rowHeights = new int[] {7, 7};
			jpanelUpLayout.columnWeights = new double[] {0.1};
			jpanelUpLayout.columnWidths = new int[] {7};
			jpanelUp.setLayout(jpanelUpLayout);
			jTextPaneProxyInformation = new JTextPane();
			jTextPaneProxyInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "", TitledBorder.LEADING, TitledBorder.TOP));
			jTextPaneProxyInformation.setEditable(false);
			jTextPaneProxyInformation.setBackground(Color.WHITE);
			jpanelUp.add(jTextPaneProxyInformation, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0), 0, 0));
			jTextPaneProxyInformation.setText("             You must configure a proxy for internet access.\n\n        If you don't have proxy in your System please click Next.\n\n Otherwise please select a proxy button and write proxy credentials.");
			jpanelUp.add(getProxyPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			
		}	
	}
	
	public JPanel getProxyPanel()
	{
		if(proxyPanel==null)
		{
			proxyPanel = new JPanel();
			GridBagLayout proxyPanelLayout = new GridBagLayout();
			proxyPanelLayout.columnWidths = new int[] {100, 400, 20};
			proxyPanelLayout.rowHeights = new int[] {7, 7, 7};
			proxyPanelLayout.columnWeights = new double[] {0.0, 0.0, 0.1};
			proxyPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			proxyPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Proxy Settings", TitledBorder.LEADING, TitledBorder.TOP));
			proxyPanel.setLayout(proxyPanelLayout);
			{
				jCheckBox1 = new JCheckBox();
				proxyPanel.add(jCheckBox1, new GridBagConstraints(
						0,
						0,
						1,
						1,
						0.0,
						0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL,
						new Insets(0, 0, 0, 0),
						0,
						0));
				jCheckBox1.setText("Use proxy");
				jCheckBox1.setSelected(false);
				jCheckBox1.setPreferredSize(new java.awt.Dimension(71, 18));
				jCheckBox1.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent evt) {
						boolean value = jCheckBox1.isSelected();
						hostLabel.setEnabled(value);
						portLabel.setEnabled(value);
						jLabel4.setEnabled(value);
						jLabel5.setEnabled(value);
					}
				});
			}
			{
				portLabel = new JLabel();
				proxyPanel.add(portLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 10, 0, 0), 0, 0));
				portLabel.setText("Port:");
				portLabel.setEnabled(true);
			}
			{
				hostLabel = new JLabel();
				proxyPanel.add(hostLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 10, 0, 0), 0, 0));
				hostLabel.setText("Host:");
				hostLabel.setEnabled(true);
			}
			{
				hostTextField = new JFormattedTextField(){
					
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isEnabled(){
						return true;
					}
					
					@Override
					public boolean isEditable(){
						return jCheckBox1.isSelected();
					}
					
					@Override
					public boolean isOpaque(){
						return jCheckBox1.isSelected();
					}
					
				};
				hostTextField.setInputVerifier(new URLIPv4InputVerifier());	

				proxyPanel.add(hostTextField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				hostTextField.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
			}
			{
				portTextField = new JFormattedTextField(){

					private static final long serialVersionUID = 1L;

					@Override
					public boolean isEnabled(){
						return true;
					}
					
					@Override
					public boolean isEditable(){
						return jCheckBox1.isSelected();
					}
					
					@Override
					public boolean isOpaque(){
						return jCheckBox1.isSelected();
					}

				};
				portTextField.setInputVerifier(new PortInputVerifier());
				proxyPanel.add(portTextField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				portTextField.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
			}
			{
				jLabel4 = new JLabel();
				proxyPanel.add(jLabel4, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel4.setText("*");
				jLabel4.setForeground(new java.awt.Color(0, 0, 255));
			}
			{
				jLabel5 = new JLabel();
				proxyPanel.add(jLabel5, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel5.setText("*");
				jLabel5.setForeground(new java.awt.Color(0, 0, 255));
			}
		}
		return proxyPanel;
		
	}


	public void done() {}

	public void goBack() {

		this.setVisible(false);
		new StartWizart1(600,350,new ArrayList<Object>());
	}

	@Override
	public void goNext() {		
		if(this.jCheckBox1.isSelected())
		{
			if(hostTextField.getText().equals("")||portTextField.getText().equals(""))
			{
				Workbench.getInstance().warn("Please fill mandatory fields");
			}
			else
			{
				if(ProxyUtils.validProxy(hostTextField.getText(),portTextField.getText()))
				{
					this.getParam().add(0,true);
					this.getParam().add(1,hostTextField.getText());
					this.getParam().add(2,portTextField.getText());
					this.setVisible(false);
					new StartWizart3(600,350,this.getParam());
				}
				else
				{
					this.getParam().add(0,true);
					this.getParam().add(1,hostTextField.getText());
					this.getParam().add(2,portTextField.getText());
					this.setVisible(false);
					new StartWizart3(600,350,this.getParam());
					Workbench.getInstance().warn("Proxy is not valid");
				}
			}
		}
		else
		{
			Workbench.getInstance().warn("The System verifying if you proxy settings is correct...wait a moment");		
			if(ProxyUtils.validConnection())
			{
				this.getParam().add(0,false);
				this.getParam().add(1,"");
				this.getParam().add(2,"");
				this.setVisible(false);
				new StartWizart3(600,350,this.getParam());
			}
			else
			{
				this.getParam().add(0,false);
				this.getParam().add(1,"");
				this.getParam().add(2,"");
				this.setVisible(false);
				if(next())
				{
					new StartWizart3(600,350,this.getParam());
				}	
			}
		}

	}
	
	private boolean next(){
		Object[] options = new String[]{"Yes", "No"};
		int opt = HelpAibench.showOptionPane("Database Warning", "You don´t have internet connection or your system is under proxy server... Continuing ?", options);
		switch (opt) {
		case 0:
			return true;
		default:
			return false;
		}
	}

}
