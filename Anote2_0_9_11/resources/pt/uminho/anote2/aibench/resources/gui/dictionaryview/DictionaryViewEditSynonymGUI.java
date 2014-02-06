package pt.uminho.anote2.aibench.resources.gui.dictionaryview;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.resources.views.LexicalWordsView;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class DictionaryViewEditSynonymGUI extends DialogGenericView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean ischange = true;
	private LexicalWordsView lexicalWordsView;
	private JTextField jTextFieldNewSynonyms;
	private JLabel jLabelTem;
	private JTextField jTextFieldOldSynonym;
	private JLabel jLabelOldSynonym;
	private JList jListSynonyms;
	private DictionaryAibench dictionary;
	private JPanel jPanelTermInfo;
	private JLabel jLabelTerm;
	private JTextField jTextFieldTerm;
	private String term;
	private String oldSynonym;
	private int termID;
	private Map<Integer, Set<String>> termIDSynonyms;


	public DictionaryViewEditSynonymGUI(DictionaryAibench dictionary ,JList jListterms,JList JListSynonyms,Map<Integer, Set<String>> termIDSynonyms, String oldsynonym,int termID, String term){
		super();
		this.oldSynonym = oldsynonym;
		this.term = term;
		this.termID=termID;
		this.dictionary = dictionary;
		this.jListSynonyms = JListSynonyms;
		this.termIDSynonyms = termIDSynonyms;
		this.setTitle("Edit Synonym - "+term);
		this.setSize(GlobalOptions.smallWidth, GlobalOptions.smallHeight);
		Utilities.centerOnOwner(this);
		initGUI();
		this.setModal(true);
		this.setVisible(true);
	}

	private void initGUI() {
		
		GridBagLayout thisLayout = new GridBagLayout();	
		thisLayout.rowWeights = new double[] {0.1,0.0};
		thisLayout.rowHeights = new int[] {7, 7};
		thisLayout.columnWeights = new double[] {0.1};
		thisLayout.columnWidths = new int[] {7};
		getContentPane().setLayout(thisLayout);
		getContentPane().add(getJPanelTermInfo(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 1, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}
	
	private JPanel getJPanelTermInfo() {
		if(jPanelTermInfo == null) {
			jPanelTermInfo = new JPanel();
			jPanelTermInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Edit Synonym", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelTermInfoLayout = new GridBagLayout();
			jPanelTermInfoLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.0};
			jPanelTermInfoLayout.rowHeights = new int[] {20, 7, 7, 7};
			jPanelTermInfoLayout.columnWeights = new double[] {0.1, 0.0, 0.1};
			jPanelTermInfoLayout.columnWidths = new int[] {7, 313, 7};
			jPanelTermInfo.setLayout(jPanelTermInfoLayout);
			jPanelTermInfo.add(getJLabelTerm(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJLabelOldSynonym(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJTextFieldOldSynonym(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJLabelTem(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJTextField1(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJTextFieldNewSynonyms(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelTermInfo;
	}
	
	private JLabel getJLabelTerm() {
		if(jLabelTerm == null) {
			jLabelTerm = new JLabel();
			jLabelTerm.setText("Synonym :");
		}
		return jLabelTerm;
	}

	public boolean ischange() {
		return ischange;
	}

	@Override
	protected void okButtonAction() {
		try {
			update();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}

	private void update() throws DatabaseLoadDriverException, SQLException {
		String newSynonym = jTextFieldNewSynonyms.getText();
		if(newSynonym.equals(oldSynonym))
		{
			Workbench.getInstance().warn("No Changes Found");
		}
		else if(newSynonym.equals(""))
		{
			Workbench.getInstance().warn("Synonym cannot be empty");
		}
		else if(dictionary.verifyIfTermExist(new ResourceElement(newSynonym)))
		{
			Workbench.getInstance().warn("Synonym already exists in Dictionary");
		}
		else {
			editSynInDB(termID, oldSynonym, newSynonym);
			int selectedIndex = jListSynonyms.getSelectedIndex();
			((DefaultComboBoxModel) jListSynonyms.getModel()).insertElementAt(newSynonym,selectedIndex);
			((DefaultComboBoxModel) jListSynonyms.getModel()).removeElementAt(selectedIndex+1);
			if(termIDSynonyms.containsKey(termID))
			{
				if(termIDSynonyms.get(termID).contains(oldSynonym))
				{
					termIDSynonyms.get(termID).remove(oldSynonym);
					termIDSynonyms.get(termID).add(newSynonym);
				}
			}
			jListSynonyms.updateUI();
			new ShowMessagePopup("Synonym Updated");		
			finish();
		}
	}

	private void editSynInDB(int termID, String element, String newEditTerm) throws DatabaseLoadDriverException, SQLException {
		Connection con = Configuration.getDatabase().getConnection();
		PreparedStatement editDic = con.prepareStatement(QueriesResources.updateSynonym);
		editDic.setNString(1,newEditTerm);
		editDic.setInt(2,termID);
		editDic.setNString(3,element);
		editDic.execute();
		editDic.close();
	}

	private JLabel getJLabelOldSynonym() {
		if(jLabelOldSynonym == null) {
			jLabelOldSynonym = new JLabel();
			jLabelOldSynonym.setText("Old Synonym :");
		}
		return jLabelOldSynonym;
	}
	
	private JTextField getJTextFieldOldSynonym() {
		if(jTextFieldOldSynonym == null) {
			jTextFieldOldSynonym = new JTextField();
			jTextFieldOldSynonym.setEditable(false);
			jTextFieldOldSynonym.setText(oldSynonym);
		}
		return jTextFieldOldSynonym;
	}
	
	private JLabel getJLabelTem() {
		if(jLabelTem == null) {
			jLabelTem = new JLabel();
			jLabelTem.setText("Term :");
		}
		return jLabelTem;
	}
	
	private JTextField getJTextField1() {
		if(jTextFieldTerm == null) {
			jTextFieldTerm = new JTextField();
			jTextFieldTerm.setText(term);
			jTextFieldTerm.setEditable(false);
		}
		return jTextFieldTerm;
	}
	
	private JTextField getJTextFieldNewSynonyms() {
		if(jTextFieldNewSynonyms == null) {
			jTextFieldNewSynonyms = new JTextField();
			jTextFieldNewSynonyms.setText(oldSynonym);
		}
		return jTextFieldNewSynonyms;
	}
	
	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Dictionary_Management_Content";
	}

}
