package pt.uminho.anote2.aibench.resources.gui.dictionaryview;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.SortedMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class DictionaryViewEditClassGUI extends DialogGenericView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean ischange = true;
	private JTextField jTextFieldNewClass;
	private JTextField jTextFieldOldClass;
	private JLabel jLabelOldClass;
	private JPanel jPanelTermInfo;
	private JLabel jLabelTerm;
	private String classe;
	private JList jListClass;
	private int termID;
	private int classID;
	private JTable content;
	private SortedMap<Integer, String> classes;


	public DictionaryViewEditClassGUI(DictionaryAibench dictionary , SortedMap<Integer, String> classes,JTable content,JList jListClass,int classID,String classe){
		super();
		this.classID = classID;
		this.classe = classe;
		this.classes = classes;
		this.content = content;
		this.jListClass = jListClass;
		this.setTitle("Edit Class ");
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
			jPanelTermInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Edit Class", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelTermInfoLayout = new GridBagLayout();
			jPanelTermInfoLayout.rowWeights = new double[] {0.1, 0.1, 0.0};
			jPanelTermInfoLayout.rowHeights = new int[] {7, 7, 7};
			jPanelTermInfoLayout.columnWeights = new double[] {0.1, 0.0, 0.1};
			jPanelTermInfoLayout.columnWidths = new int[] {7, 313, 7};
			jPanelTermInfo.setLayout(jPanelTermInfoLayout);
			jPanelTermInfo.add(getJLabelTerm(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJLabelOldSynonym(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJTextFieldOldTerm(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJTextFieldNewTerm(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelTermInfo;
	}
	
	private JLabel getJLabelTerm() {
		if(jLabelTerm == null) {
			jLabelTerm = new JLabel();
			jLabelTerm.setText("New Class Name :");
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
		String newTerm = jTextFieldNewClass.getText().toLowerCase();
		if(newTerm.equals(classe))
		{
			Workbench.getInstance().warn("No Changes Found");
			return;
		}
		else if(newTerm.equals(""))
		{
			Workbench.getInstance().warn("Class cannot be empty");
			return;
		}
		else if(ClassProperties.getClassClassID().containsKey(newTerm))
		{
			Workbench.getInstance().warn("The new \""+newTerm+"\" class already exists");
			return;
		}
		updateTerm(newTerm);
		int selectedIndex = jListClass.getSelectedIndex();
		((DefaultComboBoxModel) jListClass.getModel()).insertElementAt(newTerm,selectedIndex);
		((DefaultComboBoxModel) jListClass.getModel()).removeElementAt(selectedIndex+1);
		content.setValueAt(newTerm, selectedIndex, 0);
		classes.put(classID, newTerm);
		ClassProperties.getClassIDClass().put(classID, newTerm);
		ClassProperties.getClassClassID().remove(classe);
		ClassProperties.getClassClassID().put(newTerm, classID);
		new ShowMessagePopup("Class Content Updated");
		jListClass.updateUI();
		content.updateUI();
		finish();
	}

	private void updateTerm(String newTerm) throws DatabaseLoadDriverException, SQLException {
		Connection con = Configuration.getDatabase().getConnection();
		PreparedStatement editDic = con.prepareStatement(QueriesResources.updateClass);
		editDic.setNString(1,newTerm);
		editDic.setInt(2,classID);
		editDic.execute();
		editDic.close();
	}
	
	private JLabel getJLabelOldSynonym() {
		if(jLabelOldClass == null) {
			jLabelOldClass = new JLabel();
			jLabelOldClass.setText("Old Class Name :");
		}
		return jLabelOldClass;
	}
	
	private JTextField getJTextFieldOldTerm() {
		if(jTextFieldOldClass == null) {
			jTextFieldOldClass = new JTextField();
			jTextFieldOldClass.setEditable(false);
			jTextFieldOldClass.setText(classe);
		}
		return jTextFieldOldClass;
	}

	private JTextField getJTextFieldNewTerm() {
		if(jTextFieldNewClass == null) {
			jTextFieldNewClass = new JTextField();
			jTextFieldNewClass.setText(classe);
		}
		return jTextFieldNewClass;
	}
	
	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Dictionary_Management_Content";
	}

}
