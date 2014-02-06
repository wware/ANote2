package pt.uminho.anote2.aibench.utils.settings.proxy;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;



public class ProxyInformationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel proxyLabel;
	private JRadioButton noProxyRadioButton;
	private JFormattedTextField portTextField;
	private JLabel portLabel;
	private JLabel hostLabel;
	private JFormattedTextField hostTextField;
	private JRadioButton socksProxyRadioButton;
	private JRadioButton httpProxyRadioButton;
	private ButtonGroup proxyTypeButtonGroup;

	public ProxyInformationPanel(){
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				GridBagLayout thisLayout = new GridBagLayout();
				this.setPreferredSize(new java.awt.Dimension(592, 98));
				thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				thisLayout.rowHeights = new int[] {7, 7, 7};
				thisLayout.columnWeights = new double[] {0.2, 0.2, 0.1, 0.6};
				thisLayout.columnWidths = new int[] {7, 7, 20, 7};
				this.setLayout(thisLayout);
				this.setFont(new java.awt.Font("DejaVu Sans",1,12));
				{
					proxyLabel = new JLabel();
					this.add(getProxyLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					proxyLabel.setText("Proxy:");
					proxyLabel.setFont(new java.awt.Font("DejaVu Sans",1,12));
				}
				{
					noProxyRadioButton = new JRadioButton();
					this.add(noProxyRadioButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					noProxyRadioButton.setText("None");
					noProxyRadioButton.setFont(new java.awt.Font("DejaVu Sans",1,12));
					noProxyRadioButton.setSelected(true);
					noProxyRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
					noProxyRadioButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							noProxyRadioButtonActionPerformed(evt);
						}
					});
				}
				{
					httpProxyRadioButton = new JRadioButton();
					this.add(httpProxyRadioButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					httpProxyRadioButton.setText("HTTP");
					httpProxyRadioButton.setFont(new java.awt.Font("DejaVu Sans",1,12));
					httpProxyRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
					httpProxyRadioButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							httpProxyRadioButtonActionPerformed(evt);
						}
					});
				}
				{
					socksProxyRadioButton = new JRadioButton();
					this.add(socksProxyRadioButton, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					this.add(getPortTextField(), new GridBagConstraints(3, 1, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 20), 0, 0));
					this.add(getHostTextField(), new GridBagConstraints(3, 0, 1, 2, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 20), 0, 0));
					this.add(getHostLabel(), new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
					this.add(getPortLabel(), new GridBagConstraints(2, 1, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
					socksProxyRadioButton.setText("SOCKS");
					socksProxyRadioButton.setFont(new java.awt.Font("DejaVu Sans",1,12));
					socksProxyRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
					socksProxyRadioButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							socksProxyRadioButtonActionPerformed(evt);
						}
					});
				}
				{
					getProxyTypeButtonGroup().add(noProxyRadioButton);
					getProxyTypeButtonGroup().add(httpProxyRadioButton);
					getProxyTypeButtonGroup().add(socksProxyRadioButton);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public JLabel getProxyLabel() {
		return proxyLabel;
	}
	
	public ButtonGroup getProxyTypeButtonGroup() {
		if(proxyTypeButtonGroup == null) {
			proxyTypeButtonGroup = new ButtonGroup();
		}
		return proxyTypeButtonGroup;
	}
	
	public JRadioButton getNoProxyRadioButton() {
		return noProxyRadioButton;
	}
	
	public JFormattedTextField getPortTextField() {
		if(portTextField == null) {			
			portTextField = new JFormattedTextField(){
				
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isEnabled(){
					return !noProxyRadioButton.isSelected();
				}
				
				@Override
				public boolean isEditable(){
					return !noProxyRadioButton.isSelected();
				}
				
				@Override
				public boolean isOpaque(){
					return !noProxyRadioButton.isSelected();
				}
				
			};
			portTextField.setInputVerifier(new PortInputVerifier());
		}
		return portTextField;
	}
	
	public JFormattedTextField getHostTextField() {
		if(hostTextField == null) {					
			hostTextField = new JFormattedTextField(){

				private static final long serialVersionUID = 1L;

				@Override
				public boolean isEnabled(){
					return !noProxyRadioButton.isSelected();
				}
				
				@Override
				public boolean isEditable(){
					return !noProxyRadioButton.isSelected();
				}
				
				@Override
				public boolean isOpaque(){
					return !noProxyRadioButton.isSelected();
				}

			};
			hostTextField.setInputVerifier(new URLIPv4InputVerifier());			

		}
		return hostTextField;
	}

	public JRadioButton getSocksProxyRadioButton() {
		return socksProxyRadioButton;
	}

	public JRadioButton getHttpProxyRadioButton() {
		return httpProxyRadioButton;
	}
	
	public static void main(String...args){
		JDialog diag = new JDialog();
		diag.setLayout(new BorderLayout());
		diag.setTitle("Test ProxyInformationPanel");
		diag.setPreferredSize(new Dimension(500,200));
		diag.add(new ProxyInformationPanel(),BorderLayout.CENTER);
		diag.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		diag.pack();
		diag.setVisible(true);
		
	}
	
	public JLabel getHostLabel() {
		if(hostLabel == null) {
			hostLabel = new JLabel();
			hostLabel.setText("Host:");
			hostLabel.setFocusTraversalPolicyProvider(true);
			hostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
			hostLabel.setFont(new java.awt.Font("DejaVu Sans",1,12));
		}
		return hostLabel;
	}
	
	public JLabel getPortLabel() {
		if(portLabel == null) {
			portLabel = new JLabel();
			portLabel.setText("Port:");
			portLabel.setFocusTraversalPolicyProvider(true);
			portLabel.setHorizontalAlignment(SwingConstants.TRAILING);
			portLabel.setFont(new java.awt.Font("DejaVu Sans",1,12));
		}
		return portLabel;
	}
	
	private void noProxyRadioButtonActionPerformed(ActionEvent evt) {
		portTextField.updateUI();
		hostTextField.updateUI();
	}
	
	private void httpProxyRadioButtonActionPerformed(ActionEvent evt) {
		portTextField.updateUI();
		hostTextField.updateUI();
	}
	
	private void socksProxyRadioButtonActionPerformed(ActionEvent evt) {
		portTextField.updateUI();
		hostTextField.updateUI();
	}

}
