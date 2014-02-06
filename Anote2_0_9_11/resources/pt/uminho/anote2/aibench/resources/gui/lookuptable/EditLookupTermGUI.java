package pt.uminho.anote2.aibench.resources.gui.lookuptable;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.resources.datatypes.LookupTableAibench;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.schema.TableResourcesElements;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class EditLookupTermGUI extends DialogGenericView{
	
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
	private JPanel jPanelTermInfo;

	private LookupTableAibench look;
	private IResourceElement elem;
	private JTextField jTextFieldTermOld;
	private JLabel jLabelOldTermName;

	public EditLookupTermGUI(LookupTableAibench look,IResourceElement elem)  {
		super("Edit Lookup Table Element : "+elem.getTerm() + " Class: "+ClassProperties.getClassIDClass().get(elem.getTermClassID()));
		this.look = look;
		this.elem = elem;
		initGUI(); 
		this.setSize(GlobalOptions.smallWidth, GlobalOptions.smallHeight);
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}


	private void initGUI() {
		GridBagLayout thisLayout = new GridBagLayout();	
		thisLayout.rowWeights = new double[] {0.0, 0.1};
		thisLayout.rowHeights = new int[] {92, 108};
		thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		thisLayout.columnWidths = new int[] {7, 7, 7, 7};
		getContentPane().setLayout(thisLayout);
		getContentPane().add(getJPanelTermInfo(), new GridBagConstraints(0, 0, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		{
			getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
	}



	protected void okButtonAction() {
		int classID;
		String word = jTextFieldTerm.getText();
		String classSelected = jComboBoxBiologicalClass.getSelectedItem().toString();
		String newClass = jTextFieldNewClass.getText();
		if(!word.isEmpty())
		{
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
			else if(look.verifyIfTermExist(new ResourceElement(word)))
			{
				new ShowMessagePopup("The term \""+word+"\" is already in Lookup Table");	
				Workbench.getInstance().warn("The term \""+word+"\" is already in Lookup Table");
			}
			else if(classSelected.equals("New Class") && jTextFieldNewClass.getText().equals(""))
			{
				Workbench.getInstance().warn("Please fill New Class");
				new ShowMessagePopup("Please fill New Class");	
			}
			else 
			{
				try {
					String oldWord = elem.getTerm();
					if(!classSelected.equals("New Class"))
					{
						classID = ClassProperties.getClassClassID().get(classSelected);
					}
					else
					{
						classID = look.addElementClass(newClass);
					}
					if(word.equals(oldWord) && classID == elem.getTermClassID())
					{
						Workbench.getInstance().warn("Lookup Table Term don´t have changes");
						new ShowMessagePopup("Lookup Table Term don´t have changes");	
					}
					else
					{
						IResourceElement newElement = new ResourceElement(elem.getID(), word, classID, "");
						look.updateElement(newElement);
						look.notifyViewObservers();
						Workbench.getInstance().warn("Term Updated");
						new ShowMessagePopup("Term Updated");	
						finish();
					}
				} catch (DatabaseLoadDriverException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				} catch (SQLException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				}

			}		
		}
		else
		{
			Workbench.getInstance().warn("Please insert a Term name");
			new ShowMessagePopup("Please insert a Term name");
		}
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

	private JPanel getJPanelTermInfo() {
		if(jPanelTermInfo == null) {
			jPanelTermInfo = new JPanel();
			jPanelTermInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Term Details", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelTermInfoLayout = new GridBagLayout();
			jPanelTermInfoLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelTermInfoLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelTermInfoLayout.columnWeights = new double[] {0.05, 0.0, 0.05};
			jPanelTermInfoLayout.columnWidths = new int[] {7, 313, 7};
			jPanelTermInfo.setLayout(jPanelTermInfoLayout);
			jPanelTermInfo.add(getJLabelTerm(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJLabelClass(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJLabelNewClass(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJComboBoxBiologicalClass(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJTextFieldTerm(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJTextFieldNewClass(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJLabelOldTermName(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJTextFieldTermOld(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelTermInfo;
	}
	
	private JLabel getJLabelTerm() {
		if(jLabelTerm == null) {
			jLabelTerm = new JLabel();
			jLabelTerm.setText("Term Changes:");
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
			jComboBoxBiologicalClass.setSelectedItem(ClassProperties.getClassIDClass().get(elem.getTermClassID()));
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
			jTextFieldTerm.setText(elem.getTerm());
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
		return GlobalOptions.wikiGeneralLink+"LookupTable_Edit_Element";
	}
	
	private JLabel getJLabelOldTermName() {
		if(jLabelOldTermName == null) {
			jLabelOldTermName = new JLabel();
			jLabelOldTermName.setText("Term (Old) :");
		}
		return jLabelOldTermName;
	}
	
	private JTextField getJTextFieldTermOld() {
		if(jTextFieldTermOld == null) {
			jTextFieldTermOld = new JTextField();
			jTextFieldTermOld.setText(this.elem.getTerm());
			jTextFieldTermOld.setEditable(false);
		}
		return jTextFieldTermOld;
	}

}


