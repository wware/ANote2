package pt.uminho.anote2.aibench.curator.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.corpus.datatypes.NERDocumentAnnotation;
import pt.uminho.anote2.aibench.curator.operation.help.CallLookupTablesOp;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;



public class CreateLookuptable extends DialogGenericView{



	private static final long serialVersionUID = 1L;
	private JLabel jLabelName;
	private JLabel jLabelNote;
	private JTextField jTextFieldNote;
	private JTextField jTextFieldName;
	private JPanel jPanelUpper;


	public CreateLookuptable(NERDocumentAnnotation ner) {
		super("Create Lookup Table");
		initGUI();
		this.setSize(GlobalOptions.smallWidth,GlobalOptions.smallHeight);
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);	
	}

	private void initGUI() 
	{
		GridBagLayout thisLayout = new GridBagLayout();	
		thisLayout.rowWeights = new double[] {0.1, 0.0, 0.0};
		thisLayout.rowHeights = new int[] {7, 7, 7};
		thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		thisLayout.columnWidths = new int[] {7, 7, 7, 7};
		getContentPane().setLayout(thisLayout);
		{
			jPanelUpper = new JPanel();
			jPanelUpper.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Dictionary Information", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelUpperLayout = new GridBagLayout();
			getContentPane().add(jPanelUpper, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelUpperLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelUpperLayout.rowHeights = new int[] {7, 7, 7, 7, 7};
			jPanelUpperLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelUpperLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7};
			jPanelUpper.setLayout(jPanelUpperLayout);
			{
				jLabelName = new JLabel();
				jPanelUpper.add(jLabelName, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelName.setText("Name :");
			}
			{
				jLabelNote = new JLabel();
				jPanelUpper.add(jLabelNote, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelNote.setText("Note :");
			}
			{
				jTextFieldName = new JTextField();
				jPanelUpper.add(jTextFieldName, new GridBagConstraints(1, 1, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextFieldNote = new JTextField();
				jPanelUpper.add(jTextFieldNote, new GridBagConstraints(1, 2, 5, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			
		}
		{
			getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
	}

	protected void okButtonAction() {
		if(jTextFieldName.getText().equals(""))
		{
			Workbench.getInstance().warn("You must Select a name for Lookup Table");
		}
		else
		{
			try {
				newResource(jTextFieldName.getText(), jTextFieldNote.getText(), "lookuptable");
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			}
			finish();
		}
	}
	
	public void newResource(String name,String info,String resourceType) throws DatabaseLoadDriverException, SQLException 
	{	
		Connection connection = Configuration.getDatabase().getConnection();
		int resourceTypeID = addResourceType(resourceType);	
		PreparedStatement ps1 = connection.prepareStatement(QueriesResources.insertResource);
		ps1.setNString(1,name);
		ps1.setInt(2,resourceTypeID);
		ps1.setNString(3,info);
		ps1.execute();
		ps1.close();
		CallLookupTablesOp.updateLookupTables();

	}

	private int addResourceType(String resourceType) throws SQLException, DatabaseLoadDriverException
	{
		int result = -1;
		{
			PreparedStatement ps1 = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectResourceTypes);
			ps1.setNString(1,resourceType);
			ResultSet rs = ps1.executeQuery();
			if(rs.next())
			{
				return rs.getInt(1);
			}
			else
			{
				PreparedStatement ps2 = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.insertResourceType);
				ps2.setNString(1,resourceType);
				ps2.execute();
				ps2.close();
				result =  HelpDatabase.getNextInsertTableID(GlobalTablesName.resourcesType)-1;
				ps2.close();
			}
			rs.close();
			ps1.close();
		}
		return result;	
	}

	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Curator_View#Use_Lookup_Table_to_save_Entity_Annotation_Changes";
	}

}
