package pt.uminho.anote2.datastructures.settings.proxy;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.core.configuration.IProxy;
import pt.uminho.anote2.datastructures.configuration.Proxy;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.IPropertiesPanel;


public class ProxyChangeSettingsGUI extends javax.swing.JPanel implements IPropertiesPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel jLabel5;
	private JLabel jLabel4;
	private JTextField hostTextField;
	private JLabel jLabelHost;
	private JCheckBox jCheckBoxUSeProxy;
	private JTextField portTextField;
	private JLabel jLabelPort;
	
	private Map<String, Object> initial_props;

	private Proxy proxy;

	
	public ProxyChangeSettingsGUI() {
		
	}
	
	public ProxyChangeSettingsGUI(Map<String, Object> initial_props, Map<String, Object> defaultProps)
	{
		if(initial_props.isEmpty()) 
			initial_props = defaultProps;
		this.initial_props = initial_props;
		initGUI();
		fillSettings();
	}

	private void fillSettings() {
		if(((IProxy)initial_props.get(ProxyDefaultSettings.PROXY)).isEnable())
		{
			jCheckBoxUSeProxy.setSelected(true);
			hostTextField.setText(((IProxy)initial_props.get(ProxyDefaultSettings.PROXY)).getProxyHost());
			portTextField.setText(((IProxy)initial_props.get(ProxyDefaultSettings.PROXY)).getProxyPort());	
			hostTextField.setEnabled(true);
			portTextField.setEnabled(true);
			jLabelHost.setEnabled(true);
			jLabelPort.setEnabled(true);
			jLabel4.setEnabled(true);
			jLabel5.setEnabled(true);
		}
		else
		{
			jCheckBoxUSeProxy.setSelected(false);
			hostTextField.setEnabled(false);
			portTextField.setEnabled(false);
			jLabelHost.setEnabled(false);
			jLabelPort.setEnabled(false);
			jLabel4.setEnabled(false);
			jLabel5.setEnabled(false);
		}	
	}

	private void initGUI() {
		{
			GridBagLayout proxyPanelLayout = new GridBagLayout();
			proxyPanelLayout.columnWidths = new int[] {7, 7, 7};
			proxyPanelLayout.rowHeights = new int[] {7, 7, 7, 7, 7};
			proxyPanelLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
			proxyPanelLayout.rowWeights = new double[] {0.1, 0.025, 0.025, 0.025, 0.1};
			this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Proxy Settings", TitledBorder.LEADING, TitledBorder.TOP));
			this.setLayout(proxyPanelLayout);
			{
				jCheckBoxUSeProxy = new JCheckBox();
				jCheckBoxUSeProxy.setText("Use proxy");
				jCheckBoxUSeProxy.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						boolean value = jCheckBoxUSeProxy.isSelected();
						portTextField.setEnabled(value);
						hostTextField.setEnabled(value);
						jLabelHost.setEnabled(value);
						jLabelPort.setEnabled(value);
						jLabel4.setEnabled(value);
						jLabel5.setEnabled(value);
					}
				});
				this.add(jCheckBoxUSeProxy, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));

			}
			{
				jLabelPort = new JLabel();
				jLabelPort.setText("Port:");
				this.add(jLabelPort, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));

			}
			{
				jLabelHost = new JLabel();
				jLabelHost.setText("Host:");
				this.add(jLabelHost, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));

			}
			{
				hostTextField = new JTextField();
				this.add(hostTextField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

			}
			{
				portTextField = new JTextField();
				portTextField.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
				this.add(portTextField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

			}
			{
				jLabel4 = new JLabel();
				jLabel4.setText("*");
				this.add(jLabel4, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));

			}
			{
				jLabel5 = new JLabel();
				jLabel5.setText("*");
				this.add(jLabel5, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));
			}
		}
	}

	
	@Override
	public Map<String, Object> getProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
		proxy = getProxyFromGUI();
		properties.put(ProxyDefaultSettings.PROXY, proxy);
		return properties;
	}

	private Proxy getProxyFromGUI() {
		if(jCheckBoxUSeProxy.isSelected())
		{
			return new Proxy(hostTextField.getText(),portTextField.getText());
		}
		else
		{
			return new Proxy();
		}
	}

	@Override
	public boolean haveChanged() {
		proxy = getProxyFromGUI();
		if(!proxy.equals(initial_props.get(ProxyDefaultSettings.PROXY)))
		{
			proxy.setProxyToSystem();
			return true;
		}
		return false;
	}

}
