package pt.uminho.anote2.aibench.utils.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import pt.uminho.anote2.aibench.utils.conf.Configuration;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
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
public class ChangeDBSettingsGUI extends DialogGenericView{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField jTextFieldHost;
	private JLabel jLabelSchema;
	private JTextField jTextFieldSchema;
	private JLabel jLabelUser;
	private JLabel jLabelPwd;
	private JTextField jTextFieldUser;
	private JTextField jTextFieldPort;
	private JLabel JLabelPort;
	private JLabel jLabel1;

	private ArrayList<String> properties;
	private JPasswordField JPasswordField;

	
	public ChangeDBSettingsGUI() {
		super("Change DB Settings");
		ArrayList<String> keys=new ArrayList<String>();
		keys.add("DB-User");
		keys.add("DB-Pwd");
		properties = Configuration.getElementByXMLFile("conf/settings.conf", keys);
		initGUI();
	}


	private void initGUI() {
		
		{
			Utilities.centerOnOwner(this);
			this.setSize(400, 250);
			GridBagLayout thisLayout = new GridBagLayout();
			this.setVisible(true);
			thisLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.2};
			thisLayout.rowHeights = new int[] {12, 7, 7, 7, 7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.1};
			thisLayout.columnWidths = new int[] {45, 147, 149, 7};
			getContentPane().setLayout(thisLayout);
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel1.setText("Host :");
			}
			{
				jTextFieldHost = new JTextField();
				getContentPane().add(jTextFieldHost, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jTextFieldHost.setText("localHost");
				jTextFieldHost.setEditable(false);
			}
			{
				JLabelPort = new JLabel();
				getContentPane().add(JLabelPort, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				JLabelPort.setText("Port :");
			}
			{
				jTextFieldPort = new JTextField();
				getContentPane().add(jTextFieldPort, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jTextFieldPort.setText("3306");
				jTextFieldPort.setEditable(false);
			}
			{
				jLabelSchema = new JLabel();
				getContentPane().add(jLabelSchema, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelSchema.setText("Schema");
			}
			{
				jTextFieldSchema = new JTextField();
				getContentPane().add(jTextFieldSchema, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jTextFieldSchema.setText("anote_db");
				jTextFieldSchema.setEditable(false);
			}
			{
				jLabelUser = new JLabel();
				getContentPane().add(jLabelUser, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelUser.setText("User(*)");
			}
			{
				jTextFieldUser = new JTextField();
				getContentPane().add(jTextFieldUser, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jTextFieldUser.setText(properties.get(0));
			}
			{
				jLabelPwd = new JLabel();
				getContentPane().add(jLabelPwd, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelPwd.setText("Pwd(*)");
			}
			{
				JPasswordField = new JPasswordField();
				getContentPane().add(JPasswordField, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				JPasswordField.setText(properties.get(1));
			}
			{
				getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 7, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}

		}
	}

	protected void okButtonAction() {
		
		IDatabase database = new MySQLDatabase("localhost","3306","anote_db",jTextFieldUser.getText(),String.valueOf(JPasswordField.getPassword()));
		database.openConnection();
		if(database.getConnection()==null)
		{
			Workbench.getInstance().warn("Credentials are not valid");
		}
		else
		{
			this.paramsRec.paramsIntroduced( new ParamSpec[]{ 
					new ParamSpec("database",IDatabase.class,database,null),
			});
		}
	}

}
