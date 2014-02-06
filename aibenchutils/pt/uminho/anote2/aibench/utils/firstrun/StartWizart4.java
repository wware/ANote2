package pt.uminho.anote2.aibench.utils.firstrun;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.utils.wizart.WizartStandard;
import pt.uminho.anote2.core.configuration.IProxy;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.configuration.Proxy;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class StartWizart4 extends WizartStandard{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextPane jTextPaneInfo;
	private JLabel jLabel1;
	private JPanel jpanelUp;

	public StartWizart4(int sizeH, int sizeV, List<Object> param) {
		super(sizeH, sizeV, param);
		changeInit();
		this.setBackground(Color.BLACK);
		this.setTitle("Start Wizart Step 4/4");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}


	private void changeInit() {
		
		setEnableNextButton(false);
		{
			jTextPaneInfo = new JTextPane();
			jTextPaneInfo.setText("You Can Change Settings in MenuBar @note");
			jTextPaneInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "", TitledBorder.LEADING, TitledBorder.TOP));

		}
		{
			jLabel1 = new JLabel();			
		}
		jpanelUp = new JPanel();
		GridBagLayout jpanelUpLayout = new GridBagLayout();
		getJScrollPaneUpPanel().setViewportView(jpanelUp);
		jpanelUpLayout.rowWeights = new double[] {0.0, 0.1};
		jpanelUpLayout.rowHeights = new int[] {7, 7};
		jpanelUpLayout.columnWeights = new double[] {0.1};
		jpanelUpLayout.columnWidths = new int[] {7};
		jpanelUp.setLayout(jpanelUpLayout);
		jTextPaneInfo.setEditable(false);
		jTextPaneInfo.setBackground(Color.WHITE);
		jpanelUp.add(jTextPaneInfo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0), 0, 0));
		jpanelUp.add(jLabel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jLabel1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/help.png")));
		
	}


	public void done() {		
		this.setVisible(false);
		boolean proxyEnable = (Boolean) this.getParam().get(0);
		IProxy proxy;
		if(!proxyEnable)
		{
			proxy = new Proxy();
		}
		else
		{
			String host = (String) this.getParam().get(1);
			String port = (String) this.getParam().get(2);
			proxy = new Proxy(host,port);
		}
		String user = (String) this.getParam().get(3);
		String pwd = (String) this.getParam().get(4);
		IDatabase database = new MySQLDatabase("localhost","3306","anote_db",user,pwd);
		
		ParamSpec[] para = new ParamSpec[]{ 
				new ParamSpec("proxy",IProxy.class,proxy,null),
				new ParamSpec("database",IDatabase.class,database,null),
			};
		
		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations())
		{
			if (def.getID().equals("operations.createconfigurationfile"))
			{
				Workbench.getInstance().executeOperation(def,para);
			}
		}
		
	}

	public void goBack() {
		new StartWizart3(600,350,this.getParam());
		this.setVisible(false);
	}


	public void goNext() {}

	

}
