package pt.uminho.anote2.aibench.utils.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pt.uminho.anote2.aibench.utils.conf.Configuration;
import pt.uminho.anote2.aibench.utils.settings.proxy.ProxyUtils;
import pt.uminho.anote2.core.configuration.IProxy;
import pt.uminho.anote2.datastructures.configuration.Proxy;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;




/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class ChangeProxySettingsGUI extends DialogGenericView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 0 - ProxyEnable
	 * 1 - ProxyHost
	 * 2 - ProxyPort
	 */
	private ArrayList<String> properties;
	private JLabel jLabel5;
	private JLabel jLabel4;
	private JPanel proxyPanel;
	private JTextField hostTextField;
	private JLabel hostLabel;
	private JCheckBox jCheckBox1;

	protected JTextField portTextField;

	protected JLabel portLabel;
	
	public ChangeProxySettingsGUI() {
		super("Change Proxy Settings");
		ArrayList<String> keys = new ArrayList<String>();
		keys.add("HttpProxy-Enable");
		keys.add("HttpProxy-Host");
		keys.add("HttpProxy-Port");
		properties = Configuration.getElementByXMLFile("conf/settings.conf", keys);
		initGUI();
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.05, 0.1, 0.1, 0.05};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};	
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			getContentPane().setLayout(thisLayout);
			{
				getContentPane().add(getProxyPanel(), new GridBagConstraints(0, 1, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		{
			Utilities.centerOnOwner(this);
			this.setSize(400, 250);
			this.setVisible(true);
			
		}

	}

	
	protected void okButtonAction() {
		IProxy proxy;
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
					proxy = new Proxy(hostTextField.getText(),portTextField.getText());
					this.paramsRec.paramsIntroduced( new ParamSpec[]{ 
							new ParamSpec("proxy",IProxy.class,proxy,null),
						});
				}
				else
				{
					Workbench.getInstance().warn("Proxy is not valid");
				}
			}
		}
		else
		{
			Workbench.getInstance().warn("The System verifying if you proxy settings is correct...wait a moment");
			if(ProxyUtils.validConnection())
			{
				proxy = new Proxy();
				this.paramsRec.paramsIntroduced( new ParamSpec[]{ 
						new ParamSpec("proxy",IProxy.class,proxy,null),
					});
			}
			else
			{
				Workbench.getInstance().warn("You don´t have internet connection or your system is under proxy server");
			}
		}
		

	}
	
	public JPanel getProxyPanel()
	{
		if(proxyPanel==null)
		{
			proxyPanel = new JPanel();
			GridBagLayout proxyPanelLayout = new GridBagLayout();
			proxyPanelLayout.columnWidths = new int[] {100, 250, 20};
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

				jCheckBox1.setPreferredSize(new java.awt.Dimension(71, 18));
				jCheckBox1.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent evt) {
						boolean value = jCheckBox1.isSelected();
						portTextField.setEnabled(value);
						hostTextField.setEnabled(value);
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
				hostTextField = new JTextField();
				proxyPanel.add(hostTextField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				hostTextField.setEnabled(true);
				hostTextField.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
			}
			{
				portTextField = new JTextField();
				proxyPanel.add(portTextField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				portTextField.setEnabled(true);
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
			if(properties.get(0).equals("true"))
			{
				jCheckBox1.setSelected(true);
				hostTextField.setText(properties.get(1));
				portTextField.setText(properties.get(2));			
			}
			else
			{
				jCheckBox1.setSelected(false);
			}
		}
		return proxyPanel;
		
	}
	
	public static void main(String[] args) throws IOException{
		new ChangeProxySettingsGUI();
		
	}





}
