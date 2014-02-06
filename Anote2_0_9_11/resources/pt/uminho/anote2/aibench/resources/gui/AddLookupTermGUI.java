package pt.uminho.anote2.aibench.resources.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.resources.datatypes.LookupTableAibench;
import pt.uminho.anote2.aibench.utils.exceptions.MissingDatatypesException;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewInputGUI;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.datastructures.database.schema.TableResourcesElements;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class AddLookupTermGUI extends DialogGenericViewInputGUI{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox jComboBoxBiologicalClass;
	private JLabel jLabelNewClass;
	private JTextField jTextFieldNewClass;
	private JTextField jTextFieldTerm;
	private JLabel jLabelClass;
	private JLabel jLabelTerm;
	private JComboBox jComboBoxlookupTable;
	private JLabel jLabel1;
	private JPanel jPanelTermInfo;
	private JPanel jPanellookupTAvble;
	
	private LookupTableAibench look;

	public AddLookupTermGUI() throws MissingDatatypesException {
		super("Add Element to Lookup Table");
		initGUI(); 
		this.setModal(true);
	}


	private void initGUI() throws MissingDatatypesException {
		GridBagLayout thisLayout = new GridBagLayout();	
		thisLayout.rowWeights = new double[] {0.1, 0.0, 0.0, 0.1};
		thisLayout.rowHeights = new int[] {7, 92, 108, 7};
		thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		thisLayout.columnWidths = new int[] {7, 7, 7, 7};
		getContentPane().setLayout(thisLayout);
		getContentPane().add(getJPanellookupTAvble(), new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		getContentPane().add(getJPanelTermInfo(), new GridBagConstraints(0, 1, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		{
			getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
	}



	protected void okButtonAction() {
		
		if(!this.jTextFieldTerm.getText().equals(""))
		{
			String word = jTextFieldTerm.getText();
			String classe = jComboBoxBiologicalClass.getSelectedItem().toString();
			if(word.length()<2)
			{
				new ShowMessagePopup("The term must contain more than one character.");	
				Workbench.getInstance().warn("The term must contain more than one character.");
			}
			else if(word.length()>TableResourcesElements.elementSize)
			{		
				new ShowMessagePopup("the term must not contain more than "+TableResourcesElements.elementSize+" characters.");	
				Workbench.getInstance().warn("the term must not contain more than "+TableResourcesElements.elementSize+" characters.");
			}
			else if(((LookupTableAibench) jComboBoxlookupTable.getSelectedItem()).verifyIfTermExist(new ResourceElement(word)))
			{
				new ShowMessagePopup("The term \""+word+"\" is already in Lookup Table");	
				Workbench.getInstance().warn("The term \""+word+"\" is already in Lookup Table");
			}
			else if(jComboBoxBiologicalClass.getSelectedItem().toString().equals("New Class"))
			{
				if(jTextFieldNewClass.getText().equals(""))
				{
					Workbench.getInstance().warn("Class is empty");
				}
				else
				{
					classe=jTextFieldNewClass.getText();
					callOperation(classe);
				}
			}
			else
			{
				callOperation(classe);
			}		
		}
		else
		{
			Workbench.getInstance().warn("Please insert a Term name");
			new ShowMessagePopup("Please insert a Term name");
		}
	}

	private void callOperation(String classe) {
		paramsRec.paramsIntroduced( new ParamSpec[]{
				new ParamSpec("lookupTable", LookupTableAibench.class, this.look, null),
				new ParamSpec("name", String.class,jTextFieldTerm.getText(), null),
				new ParamSpec("class", String.class,classe, null)
		});
	}
	
	protected void changeCombo() {
		if(jComboBoxBiologicalClass.getSelectedItem().equals("New Class"))
		{
			this.jTextFieldNewClass.setEditable(true);
			this.jTextFieldNewClass.setEnabled(true);
		}
		else
		{
			this.jTextFieldNewClass.setEnabled(false);
			this.jTextFieldNewClass.setEditable(false);
		}
	}
	
	private JPanel getJPanellookupTAvble() throws MissingDatatypesException {
		if(jPanellookupTAvble == null) {
			jPanellookupTAvble = new JPanel();
			GridBagLayout jPanellookupTAvbleLayout = new GridBagLayout();
			jPanellookupTAvbleLayout.rowWeights = new double[] {0.1};
			jPanellookupTAvbleLayout.rowHeights = new int[] {7};
			jPanellookupTAvbleLayout.columnWeights = new double[] {0.0, 0.1};
			jPanellookupTAvbleLayout.columnWidths = new int[] {92, 7};
			jPanellookupTAvble.setLayout(jPanellookupTAvbleLayout);
			jPanellookupTAvble.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Lookup Table", TitledBorder.LEADING, TitledBorder.TOP));
			jPanellookupTAvble.add(getJLabel1(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanellookupTAvble.add(getJComboBoxlookupTable(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanellookupTAvble;
	}
	
	private JPanel getJPanelTermInfo() {
		if(jPanelTermInfo == null) {
			jPanelTermInfo = new JPanel();
			jPanelTermInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Term Details", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelTermInfoLayout = new GridBagLayout();
			jPanelTermInfoLayout.rowWeights = new double[] {0.1, 0.0, 0.1};
			jPanelTermInfoLayout.rowHeights = new int[] {7, 7, 7};
			jPanelTermInfoLayout.columnWeights = new double[] {0.1, 0.0, 0.1};
			jPanelTermInfoLayout.columnWidths = new int[] {7, 313, 7};
			jPanelTermInfo.setLayout(jPanelTermInfoLayout);
			jPanelTermInfo.add(getJLabelTerm(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJLabelClass(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJLabelNewClass(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJComboBoxBiologicalClass(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJTextFieldTerm(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJTextFieldNewClass(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelTermInfo;
	}
	
	private JLabel getJLabel1() {
		if(jLabel1 == null) {
			jLabel1 = new JLabel();
			jLabel1.setText("LookupTable :");
		}
		return jLabel1;
	}
	
	private JComboBox getJComboBoxlookupTable() throws MissingDatatypesException {
		if(jComboBoxlookupTable == null) {
			ComboBoxModel jComboBoxlookupTableModel = 
				new DefaultComboBoxModel();
			jComboBoxlookupTable = new JComboBox();
			jComboBoxlookupTable.setModel(jComboBoxlookupTableModel);
			List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(LookupTableAibench.class);

			for (ClipboardItem item : cl) {
				jComboBoxlookupTable.addItem((LookupTableAibench)item.getUserData());
			}
			if (jComboBoxlookupTable.getModel().getSize() > 0)
			{
				Object obj = HelpAibench.getSelectedItem(LookupTableAibench.class);
				LookupTableAibench dicAibnech = (LookupTableAibench) obj;
				jComboBoxlookupTable.setSelectedItem(dicAibnech);
				look = dicAibnech;
			}
			else
			{
				look = (LookupTableAibench) jComboBoxlookupTable.getModel().getElementAt(0);
			}
			jComboBoxlookupTable.addActionListener(new ActionListener(){			
				public void actionPerformed(ActionEvent arg0) {
					changelookuptable();
				}
			});
		}
		return jComboBoxlookupTable;
	}
	
	private void changelookuptable() {
		
		this.look=(LookupTableAibench) ((ClipboardItem) jComboBoxlookupTable.getSelectedItem()).getUserData();
	}
	
	private JLabel getJLabelTerm() {
		if(jLabelTerm == null) {
			jLabelTerm = new JLabel();
			jLabelTerm.setText("Term :");
		}
		return jLabelTerm;
	}
	
	private JLabel getJLabelClass() {
		if(jLabelClass == null) {
			jLabelClass = new JLabel();
			jLabelClass.setText("Class :");
		}
		return jLabelClass;
	}
	
	private JLabel getJLabelNewClass() {
		if(jLabelNewClass == null) {
			jLabelNewClass = new JLabel();
			jLabelNewClass.setText("New Class :");
		}
		return jLabelNewClass;
	}

	private JComboBox getJComboBoxBiologicalClass() {
		if(jComboBoxBiologicalClass == null) {
			DefaultComboBoxModel jComboBoxBiologicalClassModel = 
				new DefaultComboBoxModel();
			
			jComboBoxBiologicalClass = new JComboBox();
			for(String value:ClassProperties.getClassClassID().keySet())
			{
				jComboBoxBiologicalClassModel.addElement(value);
			}
			jComboBoxBiologicalClassModel.addElement("New Class");
			jComboBoxBiologicalClass.setModel(jComboBoxBiologicalClassModel);
			jComboBoxBiologicalClass.setSelectedItem("New Class");
			jComboBoxBiologicalClass.addActionListener(new ActionListener(){			
				public void actionPerformed(ActionEvent arg0) {
					changeCombo();
				}
			});

		}
		return jComboBoxBiologicalClass;
	}
	
	private JTextField getJTextFieldTerm() {
		if(jTextFieldTerm == null) {
			jTextFieldTerm = new JTextField();
		}
		return jTextFieldTerm;
	}
	
	private JTextField getJTextFieldNewClass() {
		if(jTextFieldNewClass == null) {
			jTextFieldNewClass = new JTextField();
		}
		return jTextFieldNewClass;
	}
	
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"LookupTable_Add_Element";
	}
	
	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		Object obj = HelpAibench.getSelectedItem(LookupTableAibench.class);
		if(obj==null)
		{
			Workbench.getInstance().warn("No Lookup Table selected on clipboard");
			dispose();
		}
		else
		{
			this.paramsRec = arg0;
			this.setSize(GlobalOptions.smallWidth,GlobalOptions.smallHeight);
			Utilities.centerOnOwner(this);
			this.setVisible(true);
		}

	}

}


