package pt.uminho.anote2.aibench.resources.gui.dictionaryview;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
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


public class DictionaryViewEditTermGUI extends DialogGenericView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean ischange = true;
	private JTextField jTextFieldNewTErm;
	private JLabel jLabelTem;
	private JTextField jTextFieldOldTerm;
	private JLabel jLabelOldSynonym;
	private DictionaryAibench dictionary;
	private JPanel jPanelTermInfo;
	private JLabel jLabelTerm;
	private JTextField jTextFieldClass;
	private String oldterm;
	private String classe;
	private JList jListterms;
	private int termID;
	private int classID;


	public DictionaryViewEditTermGUI(DictionaryAibench dictionary ,JList jListterms,int classID,String classe,int termID, String term){
		super();
		this.oldterm = term;
		this.termID=termID;
		this.classID = classID;
		this.classe = classe;
		this.dictionary = dictionary;
		this.jListterms = jListterms;
		this.setTitle("Edit Term - "+classe);
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
			jPanelTermInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Edit Term", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelTermInfoLayout = new GridBagLayout();
			jPanelTermInfoLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.0};
			jPanelTermInfoLayout.rowHeights = new int[] {20, 7, 7, 7};
			jPanelTermInfoLayout.columnWeights = new double[] {0.1, 0.0, 0.1};
			jPanelTermInfoLayout.columnWidths = new int[] {7, 313, 7};
			jPanelTermInfo.setLayout(jPanelTermInfoLayout);
			jPanelTermInfo.add(getJLabelTerm(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJLabelOldSynonym(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJTextFieldOldTerm(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJLabelTem(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJTextFieldClass(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJTextFieldNewTerm(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelTermInfo;
	}
	
	private JLabel getJLabelTerm() {
		if(jLabelTerm == null) {
			jLabelTerm = new JLabel();
			jLabelTerm.setText("New Term Name :");
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
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}

	private void update() throws DatabaseLoadDriverException, SQLException {
		String newTerm = jTextFieldNewTErm.getText();
		if(newTerm.equals(oldterm))
		{
			Workbench.getInstance().warn("No Changes Found");
		}
		else if(newTerm.equals(""))
		{
			Workbench.getInstance().warn("Term cannot be empty");
		}
		else if(dictionary.verifyIfTermExist(new ResourceElement(newTerm)))
		{
			Workbench.getInstance().warn("Term already exists in Dictionary");
		}
		else
		{
			getTermEdit(termID, classID, newTerm);
			int selectedIndex = jListterms.getSelectedIndex();
			((DefaultComboBoxModel) jListterms.getModel()).insertElementAt(newTerm,selectedIndex);
			((DefaultComboBoxModel) jListterms.getModel()).removeElementAt(selectedIndex+1);
			jListterms.updateUI();
			new ShowMessagePopup("Term Updated");
			finish();
		}
	}
	
	private void getTermEdit(int termID, int classID, String newEditTerm) throws DatabaseLoadDriverException, SQLException {
		Connection con = Configuration.getDatabase().getConnection();
			PreparedStatement editDic = con.prepareStatement(QueriesResources.updateResourceElement);
			editDic.setNString(1,newEditTerm);
			editDic.setInt(2,termID);
			editDic.setInt(3,dictionary.getID());
			editDic.setInt(4,classID);
			editDic.execute();
			editDic.close();
	}
	
	private JLabel getJLabelOldSynonym() {
		if(jLabelOldSynonym == null) {
			jLabelOldSynonym = new JLabel();
			jLabelOldSynonym.setText("Old Term Name :");
		}
		return jLabelOldSynonym;
	}
	
	private JTextField getJTextFieldOldTerm() {
		if(jTextFieldOldTerm == null) {
			jTextFieldOldTerm = new JTextField();
			jTextFieldOldTerm.setEditable(false);
			jTextFieldOldTerm.setText(this.oldterm);
		}
		return jTextFieldOldTerm;
	}
	
	private JLabel getJLabelTem() {
		if(jLabelTem == null) {
			jLabelTem = new JLabel();
			jLabelTem.setText("Class :");
		}
		return jLabelTem;
	}
	
	private JTextField getJTextFieldClass() {
		if(jTextFieldClass == null) {
			jTextFieldClass = new JTextField();
			jTextFieldClass.setText(this.classe);
			jTextFieldClass.setEditable(false);
		}
		return jTextFieldClass;
	}
	
	private JTextField getJTextFieldNewTerm() {
		if(jTextFieldNewTErm == null) {
			jTextFieldNewTErm = new JTextField();
			jTextFieldNewTErm.setText(this.oldterm);
		}
		return jTextFieldNewTErm;
	}
	
	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Dictionary_Management_Content";
	}

}
