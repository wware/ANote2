package pt.uminho.anote2.aibench.resources.gui.dictionaryview;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.datastructures.database.schema.TableResourcesElements;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class DictionaryViewAddSynonymGUI extends DialogGenericView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean ischange = true;
	private JTextField jTextFieldNewSynonyms;
	private JLabel jLabelTem;
	private JList jListSynonyms;
	private DictionaryAibench dictionary;
	private JPanel jPanelTermInfo;
	private JLabel jLabelTerm;
	private JTextField jTextFieldTerm;
	private String term;
	private int termID;
	private Map<Integer, Set<String>> termIDSynonyms;


	public DictionaryViewAddSynonymGUI(DictionaryAibench dictionary ,JList jListterms,JList JListSynonyms,Map<Integer, Set<String>> termIDSynonyms, int termID, String term){
		super();
		this.term = term;
		this.termID=termID;
		this.dictionary = dictionary;
		this.jListSynonyms = JListSynonyms;
		this.termIDSynonyms = termIDSynonyms;
		this.setTitle("Add Synonym - "+term);
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
			jPanelTermInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Add Synonym", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelTermInfoLayout = new GridBagLayout();
			jPanelTermInfoLayout.rowWeights = new double[] {0.1, 0.1, 0.0};
			jPanelTermInfoLayout.rowHeights = new int[] {20, 7, 7};
			jPanelTermInfoLayout.columnWeights = new double[] {0.1, 0.0, 0.1};
			jPanelTermInfoLayout.columnWidths = new int[] {7, 313, 7};
			jPanelTermInfo.setLayout(jPanelTermInfoLayout);
			jPanelTermInfo.add(getJLabelTerm(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJLabelTem(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJTextField1(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJTextFieldNewSynonyms(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
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
		update();
	}

	private void update() {
		String newSynonym = jTextFieldNewSynonyms.getText();
		if(newSynonym.equals(""))
		{
			Workbench.getInstance().warn("Synonym cannot be empty");
		}
		else
		{
			ResourceElement termResource = new ResourceElement(termID,newSynonym, 0, "");
			if(newSynonym.length()<2)
			{
				new ShowMessagePopup("The term must contain more than one character.");	
				Workbench.getInstance().warn("The term must contain more than one character.");
			}
			else if(newSynonym.length()>TableResourcesElements.elementSize)
			{		
				new ShowMessagePopup("the term must not contain more than "+TableResourcesElements.elementSize+" characters.");	
				Workbench.getInstance().warn("the term must not contain more than "+TableResourcesElements.elementSize+" characters.");
			}
			else if(this.dictionary.verifyIfTermExist(termResource))
			{
				Workbench.getInstance().warn("Synonym already exists in Dictionary");
			}
			else
			{
				this.dictionary.addSynonymsNoRestritions(termResource);
				((DefaultComboBoxModel) jListSynonyms.getModel()).addElement(newSynonym);
				dictionary.notifyViewObservers();
				jListSynonyms.updateUI();
				if(termIDSynonyms.containsKey(termID))
					termIDSynonyms.get(termID).add(newSynonym);
				new ShowMessagePopup("Synonym Added .");
				finish();
			}
		}
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
		}
		return jTextFieldNewSynonyms;
	}
	
	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Dictionary_Management_Content";
	}

}
