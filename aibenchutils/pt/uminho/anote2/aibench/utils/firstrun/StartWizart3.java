package pt.uminho.anote2.aibench.utils.firstrun;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.utils.wizart.WizartStandard;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
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
public class StartWizart3 extends WizartStandard{

	private static final long serialVersionUID = 1L;
	private JTextPane jTextPaneInfo;
	private JLabel jLabel2;
	private JTextField jTextFieldUser;
	private JLabel jLabel1;
	private JTextField jTextFieldDatabaseShema;
	private JLabel Database;
	private JPanel jPanelDatabaseCredentials;
	private JPanel jpanelUp;
	private JPasswordField jTextPanePwd;
	
	public StartWizart3(int sizeH, int sizeV, List<Object> param) {
		super(sizeH, sizeV, param);
		changeInit();
		this.setTitle("Start Wizart Step 3/4");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	

	private void changeInit() {
		setEnableDoneButton(false);
		{
			jTextPaneInfo = new JTextPane();
			jTextPaneInfo.setText("Database Credentials");
			jTextPaneInfo.setEditable(false);
			jTextPaneInfo.setBackground(Color.WHITE);
			jTextPaneInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "", TitledBorder.LEADING, TitledBorder.TOP));

			
		}
		
		jpanelUp = new JPanel();
		GridBagLayout jpanelUpLayout = new GridBagLayout();
		getJScrollPaneUpPanel().setViewportView(jpanelUp);
		jpanelUpLayout.rowWeights = new double[] {0.0, 0.1};
		jpanelUpLayout.rowHeights = new int[] {7, 7};
		jpanelUpLayout.columnWeights = new double[] {0.1};
		jpanelUpLayout.columnWidths = new int[] {7};
		jpanelUp.setLayout(jpanelUpLayout);
		jpanelUp.add(jTextPaneInfo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0), 0, 0));
		jpanelUp.add(getjPanelDatabaseCredentials(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	
	}
	
	public JPanel getjPanelDatabaseCredentials()
	{
		if(jPanelDatabaseCredentials==null)
		{
		jPanelDatabaseCredentials = new JPanel();
		GridBagLayout jPanelDatabaseCredentialsLayout = new GridBagLayout();
		jPanelDatabaseCredentialsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		jPanelDatabaseCredentialsLayout.rowHeights = new int[] {7, 7, 7, 7};
		jPanelDatabaseCredentialsLayout.columnWeights = new double[] {0.0, 0.05, 0.1, 0.05};
		jPanelDatabaseCredentialsLayout.columnWidths = new int[] {7, 7,7, 7};
		jPanelDatabaseCredentials.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""),"Database Credentials", TitledBorder.LEADING, TitledBorder.TOP));
		jPanelDatabaseCredentials.setLayout(jPanelDatabaseCredentialsLayout);
		{
			Database = new JLabel();
			jPanelDatabaseCredentials.add(Database, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			Database.setText("DataBase Shema :");
		}
		{
			jTextFieldDatabaseShema = new JTextField();
			jPanelDatabaseCredentials.add(jTextFieldDatabaseShema, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jTextFieldDatabaseShema.setText("anote_db");
			jTextFieldDatabaseShema.setEditable(false);
		}
		{
			jLabel1 = new JLabel();
			jPanelDatabaseCredentials.add(jLabel1, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jLabel1.setText("User :");
		}
		{
			jTextFieldUser = new JTextField();
			jPanelDatabaseCredentials.add(jTextFieldUser, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jTextFieldUser.setText("root");
		}
		{
			jLabel2 = new JLabel();
			jPanelDatabaseCredentials.add(jLabel2, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jLabel2.setText("Password :");
		}
		{
			jTextPanePwd = new JPasswordField();
			jPanelDatabaseCredentials.add(jTextPanePwd, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		}
		return jPanelDatabaseCredentials;
	}

	public void done() {}

	public void goBack() {
		this.setVisible(false);
		new StartWizart2(600,350,new ArrayList<Object>());
	}

	public void goNext() {
		if(jTextFieldUser.getText().equals(""))
		{
			Workbench.getInstance().warn("Insert user credentials");
		}
		IDatabase db = new MySQLDatabase("localhost","3306","mysql",jTextFieldUser.getText(),String.valueOf(jTextPanePwd.getPassword()));
		db.openConnection();
		if(db.getConnection()==null)
		{
			Workbench.getInstance().warn("Credentials not valid or Mysql not instaled");
		}
		else
		{
			this.setVisible(false);
			this.getParam().add(3,jTextFieldUser.getText());
			this.getParam().add(4,String.valueOf(jTextPanePwd.getPassword()));
			new StartWizart4(600,350,this.getParam());
		}
	}
}
