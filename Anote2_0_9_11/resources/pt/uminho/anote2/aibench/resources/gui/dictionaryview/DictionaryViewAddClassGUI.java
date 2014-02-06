package pt.uminho.anote2.aibench.resources.gui.dictionaryview;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.Map;
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
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class DictionaryViewAddClassGUI extends DialogGenericView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean ischange = true;
	private JTextField jTextFieldNewTErm;
	private DictionaryAibench dictionary;
	private JPanel jPanelTermInfo;
	private JLabel jLabelTerm;
	private JList content;
	private SortedMap<Integer, String> classes; // classeID,classe
	private Map<Integer, Integer> lineClassID;


	public DictionaryViewAddClassGUI(DictionaryAibench dictionary,SortedMap<Integer, String> classes, Map<Integer, Integer> lineClassID,JList content,JTable jtableContent){
		super();
		this.dictionary = dictionary;
		this.content = content;
		this.classes = classes;
		this.lineClassID = lineClassID;
		this.setTitle("Add Class ");
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
			jPanelTermInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Add Class", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelTermInfoLayout = new GridBagLayout();
			jPanelTermInfoLayout.rowWeights = new double[] {0.1, 0.0};
			jPanelTermInfoLayout.rowHeights = new int[] {7, 7};
			jPanelTermInfoLayout.columnWeights = new double[] {0.1, 0.0, 0.1};
			jPanelTermInfoLayout.columnWidths = new int[] {7, 313, 7};
			jPanelTermInfo.setLayout(jPanelTermInfoLayout);
			jPanelTermInfo.add(getJLabelTerm(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJTextFieldNewTerm(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
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

	private void update() throws SQLException, DatabaseLoadDriverException {
		String newTerm = jTextFieldNewTErm.getText();
		if(newTerm.equals(""))
		{
			Workbench.getInstance().warn("Class cannot be empty");
		}
		else
		{
			int classID = dictionary.addElementClass(newTerm);
			if(dictionary.getClassContent().contains(classID))
			{
				Workbench.getInstance().warn("Class already exists in Dictionary");
			}
			else if (!(classID == -1)) {
				((DefaultComboBoxModel) content.getModel()).addElement(newTerm);
				dictionary.addResourceContent(newTerm);
				classes.put(classID, newTerm);
				lineClassID.put(content.getModel().getSize()- 1,classID);
				content.updateUI();
				dictionary.notifyViewObservers();
				new ShowMessagePopup("Class Added");
				finish();
			}
			else
			{
				Workbench.getInstance().warn("Class cannot be added");
			}
		}
	}

	private JTextField getJTextFieldNewTerm() {
		if(jTextFieldNewTErm == null) {
			jTextFieldNewTErm = new JTextField();
		}
		return jTextFieldNewTErm;
	}

	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Dictionary_Management_Content";
	}

}
