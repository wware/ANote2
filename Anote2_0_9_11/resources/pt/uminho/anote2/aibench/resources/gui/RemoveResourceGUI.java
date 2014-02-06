package pt.uminho.anote2.aibench.resources.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.resources.datatypes.Dictionaries;
import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.resources.datatypes.LexicalWordsAibench;
import pt.uminho.anote2.aibench.resources.datatypes.LexicalWordsSet;
import pt.uminho.anote2.aibench.resources.datatypes.LookupTableAibench;
import pt.uminho.anote2.aibench.resources.datatypes.LookupTables;
import pt.uminho.anote2.aibench.resources.datatypes.Ontologies;
import pt.uminho.anote2.aibench.resources.datatypes.OntologyAibench;
import pt.uminho.anote2.aibench.resources.datatypes.RulesAibench;
import pt.uminho.anote2.aibench.resources.datatypes.RulesSet;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfo;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.generic.genericpanel.database.DataBaseDelete;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class RemoveResourceGUI extends DataBaseDelete {
	
	private JPanel jPanelInformation;
	private JTextField jTextFieldID;
	private JTextField jTextFieldName;
	private JTextField jTextFieldType;
	private JTextField jTextResourceElements;
	private JLabel jLabelResourcesElements;
	private JLabel jLabelProcessType;
	private JLabel jLabelResourcesName;
	private JLabel jLabelresourcesID;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IResource<IResourceElement> resource = null;
	
	public RemoveResourceGUI(IResource<IResourceElement> resource) {
		super(GlobalTextInfo.databasedeleteResourceInfo);
		this.resource = resource;
		try {
			completeFields();
			this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);	
			this.setTitle("Remove  "+resource.getType()+"("+resource.getID()+") from Database");
			Utilities.centerOnOwner(this);
			this.setModal(true);
			this.setVisible(true);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}

	}

	private void completeFields() throws SQLException, DatabaseLoadDriverException {
		jTextFieldID.setText(String.valueOf(resource.getID()));
		jTextFieldName.setText(resource.getName());
		jTextFieldType.setText(resource.getType());
		jTextResourceElements.setText(String.valueOf(getResourceElement()));	
	}

	private int getResourceElement() throws SQLException, DatabaseLoadDriverException {
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.totalResourceTerms);
		ps.setInt(1,resource.getID());
		ResultSet rs = ps.executeQuery();
		int total = 0;
		if(rs.next())
		{
			total = rs.getInt(1);
		}
		ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.totalResourceSyn);
		ps.setInt(1,resource.getID());
		rs = ps.executeQuery();
		if(rs.next())
		{
			total = total + rs.getInt(1);
		}
		rs.close();
		ps.close();
		return total;
	}

	protected void okButtonAction() {
		try {
			removeFromDatabase();
			removeFromClipboard();
			finish();
		} catch (SQLException e) {
			new ShowMessagePopup("Resource Removed Fail");
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Resource Removed Fail");
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		}
		new ShowMessagePopup("Resource Removed");

	}


	private void removeFromDatabase() throws SQLException, DatabaseLoadDriverException {
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.inactiveResource);
		ps.setInt(1,resource.getID());
		ps.execute();
		ps.close();
	}

	private void removeFromClipboard() {
		int resourceID = resource.getID();
		if(resource instanceof DictionaryAibench)
		{
			List<ClipboardItem> dics = Core.getInstance().getClipboard().getItemsByClass(Dictionaries.class);
			Dictionaries dic = (Dictionaries) dics.get(0).getUserData();	
			for(DictionaryAibench lex : dic.getDictionariesClipboard())
			{
				if(lex.getID()==resourceID)
				{
					dic.getDictionariesClipboard().remove(lex);
					break;
				}
			}
			dic.notifyViewObservers();
		}
		else if(resource instanceof LookupTableAibench)
		{
			List<ClipboardItem> dics = Core.getInstance().getClipboard().getItemsByClass(LookupTables.class);
			LookupTables dic = (LookupTables) dics.get(0).getUserData();	
			for(LookupTableAibench lex : dic.getLookuptablesClipboard())
			{
				if(lex.getID()==resourceID)
				{
					dic.getLookuptablesClipboard().remove(lex);
					break;
				}
			}
			dic.notifyViewObservers();

		}
		else if(resource instanceof RulesAibench)
		{
			List<ClipboardItem> dics = Core.getInstance().getClipboard().getItemsByClass(RulesSet.class);
			RulesSet dic = (RulesSet) dics.get(0).getUserData();	
			for(RulesAibench lex : dic.getRulesClipboard())
			{
				if(lex.getID()==resourceID)
				{
					dic.getRulesClipboard().remove(lex);
					break;
				}
			}
			dic.notifyViewObservers();

		}
		else if(resource instanceof OntologyAibench)
		{
			List<ClipboardItem> dics = Core.getInstance().getClipboard().getItemsByClass(Ontologies.class);
			Ontologies dic = (Ontologies) dics.get(0).getUserData();	
			for(OntologyAibench lex : dic.getOntologiesClipboard())
			{
				if(lex.getID()==resourceID)
				{
					dic.getOntologiesClipboard().remove(lex);
					break;
				}
			}
			dic.notifyViewObservers();

		}
		else if(resource instanceof LexicalWordsAibench)
		{
			List<ClipboardItem> dics = Core.getInstance().getClipboard().getItemsByClass(LexicalWordsSet.class);
			LexicalWordsSet dic = (LexicalWordsSet) dics.get(0).getUserData();
			for(LexicalWordsAibench lex : dic.getLexicalWordsAIbenchClipboard())
			{
				if(lex.getID()==resourceID)
				{
					dic.getLexicalWordsAIbenchClipboard().remove(lex);
					break;
				}
			}
			dic.notifyViewObservers();
		}
	}

	@Override
	public JPanel getDetailPanel() {
		if(jPanelInformation == null) {
			jPanelInformation = new JPanel();
			jPanelInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Information", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelinformationLayout = new GridBagLayout();
			jPanelinformationLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelinformationLayout.rowHeights = new int[] {7, 7, 7, 20, 20, 7};
			jPanelinformationLayout.columnWeights = new double[] {0.1, 0.1};
			jPanelinformationLayout.columnWidths = new int[] {7, 7};
			jPanelInformation.setLayout(jPanelinformationLayout);
			{
				jLabelresourcesID = new JLabel();
				jPanelInformation.add(jLabelresourcesID, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelresourcesID.setText("Resource ID : ");
			}
			{
				jLabelResourcesName = new JLabel();
				jPanelInformation.add(jLabelResourcesName, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelResourcesName.setText("Name : ");
			}
			{
				jLabelProcessType = new JLabel();
				jPanelInformation.add(jLabelProcessType, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelProcessType.setText("Type : ");
			}
			{
				jLabelResourcesElements = new JLabel();
				jPanelInformation.add(jLabelResourcesElements, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelResourcesElements.setText("Elements : ");
			}
			{
				jTextFieldID = new JTextField();
				jTextFieldID.setEditable(false);
				jPanelInformation.add(jTextFieldID, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextFieldName = new JTextField();
				jTextFieldName.setEditable(false);
				jPanelInformation.add(jTextFieldName, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextFieldType = new JTextField();
				jTextFieldType.setEditable(false);
				jPanelInformation.add(jTextFieldType, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextResourceElements = new JTextField();
				jTextResourceElements.setEditable(false);
				jPanelInformation.add(jTextResourceElements, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}

		}
		return jPanelInformation;
	}

	
	protected String getHelpLink() {
		if(this.resource != null)
		{
			if(resource.getType().equals(GlobalOptions.resourcesDictionaryName))
				return GlobalOptions.wikiGeneralLink+"Dictionary_Remove";
			else if(resource.getType().equals(GlobalOptions.resourcesLexicalWords))
				return GlobalOptions.wikiGeneralLink+"LexicalWords_Remove";
			else if(resource.getType().equals(GlobalOptions.resourcesLookupTableName))
				return GlobalOptions.wikiGeneralLink+"LookupTable_Remove";
			else if(resource.getType().equals(GlobalOptions.resourcesOntologyName))
				return GlobalOptions.wikiGeneralLink+"Ontology_Remove";
			else if(resource.getType().equals(GlobalOptions.resourcesRuleSetName))
				return GlobalOptions.wikiGeneralLink+"RulesSet_Remove";
			else
			{
				return null;
			}
		}
		else
		{
			return new String();
		}
	}
}
	
	
