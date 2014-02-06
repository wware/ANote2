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
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERDocumentAnnotation;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
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
public class CreateLookuptable extends DialogGenericView{



	private static final long serialVersionUID = 1L;
	private JLabel jLabelName;
	private JLabel jLabelNote;
	private JTextArea jTextAreaNote;
	private JTextPane jTextPaneName;
	private JPanel jPanelUpper;
	private IDatabase db;


	public CreateLookuptable(NERDocumentAnnotation ner) {
		this.setSize(600,400);
		this.db= ((Corpus) ner.getCorpus()).getCorpora().getDb();
		initGUI();
		this.setTitle("Create Lookup Table");
		Utilities.centerOnOwner(this);
		this.setVisible(true);
		this.setModal(true);
		
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
				jTextPaneName = new JTextPane();
				jPanelUpper.add(jTextPaneName, new GridBagConstraints(1, 1, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextAreaNote = new JTextArea();
				jPanelUpper.add(jTextAreaNote, new GridBagConstraints(1, 2, 5, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			
		}
		{
			getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
	}

	protected void okButtonAction() {
		if(jTextPaneName.getText().equals(""))
		{
			Workbench.getInstance().warn("You must Select a name for Lookup Table");
		}
		else
		{
			newResource(jTextPaneName.getText(), jTextAreaNote.getText(), "lookuptable");
			this.setVisible(false);
			this.setModal(false);
			this.dispose();
		}
	}
	
	public boolean newResource(String name,String info,String resourceType) 
	{	
		Connection connection = db.getConnection();
		if(connection == null)
		{
			return false;			
		}
		else
		{
			int resourceTypeID = addResourceType(resourceType);	
			try {
				PreparedStatement ps1 = connection.prepareStatement(QueriesResources.insertResource);
				ps1.setString(1,name);
				ps1.setInt(2,resourceTypeID);
				ps1.setString(3,info);
				ps1.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	private int addResourceType(String resourceType)
	{
		
		Connection connection = this.db.getConnection();
		if(connection == null)
		{		
			return -1;			
		}
		else
		{
			try {
				PreparedStatement ps1 = connection.prepareStatement(QueriesResources.selectResourceTypes);
				ps1.setString(1,resourceType);
				ResultSet rs = ps1.executeQuery();
				if(rs.next())
				{
					return rs.getInt(1);
				}				
				PreparedStatement ps2 = connection.prepareStatement(QueriesResources.insertResourceType);
				ps2.setString(1,resourceType);
				ps2.execute();
				return HelpDatabase.getNextInsertTableID(db, "resources_type")-1;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return -1;	
	}

}
