package pt.uminho.anote2.aibench.resources.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.resources.datatypes.LookupTableAibench;
import pt.uminho.anote2.aibench.resources.datatypes.Resources;
import pt.uminho.anote2.aibench.utils.exceptions.MissingDatatypesException;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.core.database.IDatabase;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.workbench.Workbench;


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
public class AddLookupTermGUI extends DialogGenericView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<Integer, String> idclassesClasses;
	private JComboBox jComboBoxBiologicalClass;
	private JLabel jLabelNewClass;
	private JTextPane jTextPaneNewClass;
	private JTextArea jTextAreaTerm;
	private JLabel jLabelClass;
	private JLabel jLabelTerm;
	private JComboBox jComboBoxlookupTable;
	private JLabel jLabel1;
	private JPanel jPanelTermInfo;
	private JPanel jPanellookupTAvble;
	
	private LookupTableAibench look;

	public AddLookupTermGUI() throws MissingDatatypesException {
		setTitle("Add Element to Lookup Table");
		confirm();
		initGUI(); 
	}
	
	private void confirm() {
		List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(LookupTableAibench.class);
		if(items==null||items.size()==0)
		{
			Workbench.getInstance().warn("No Lookup Tables on clipboard");
			return;
		}
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
		this.setSize(500, 350);		
	}



	protected void okButtonAction() {
		
		if(!this.jTextAreaTerm.equals(""))
		{

			String classe = jComboBoxBiologicalClass.getSelectedItem().toString();
			if(jComboBoxBiologicalClass.getSelectedItem().equals("New Class"))
			{
				if(jTextPaneNewClass.getText().equals(""))
				{
					Workbench.getInstance().warn("Biological Class is empty");
					return;
				}
				else
				{
					classe=jTextPaneNewClass.getText();
				}

			}
			
			paramsRec.paramsIntroduced( new ParamSpec[]{
					new ParamSpec("lookupTable", LookupTableAibench.class, this.look, null),
					new ParamSpec("name", String.class,jTextAreaTerm.getText(), null),
					new ParamSpec("class", String.class,classe, null)
			});
		}
		else
		{
			Workbench.getInstance().warn("Please insert a Term name!");
		}
	}

	public Map<Integer,String> getClasses() throws SQLException
	{
		List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(Resources.class);
		Resources resources = (Resources) items.get(0).getUserData();
		IDatabase db = resources.getDb();
		ResultSet rs = null;
		Map<Integer,String> classIDClass = new HashMap<Integer, String>();
		
		String q = "SELECT * FROM classes";
		
		db.openConnection();
		Connection connection = db.getConnection();
		if(connection == null)
		{
			db.closeConnection();
			return null;
		}
		else
		{
			rs = db.executeQuery(q);
			while(rs.next())
			{
				classIDClass.put(rs.getInt(1),rs.getString(2));
			}
		}
		return classIDClass;
	}
	
	protected void changeCombo() {
		if(jComboBoxBiologicalClass.getSelectedItem().equals("New Class"))
		{
			this.jTextPaneNewClass.setEditable(true);
			this.jTextPaneNewClass.setEnabled(true);
		}
		else
		{
			this.jTextPaneNewClass.setEnabled(false);
			this.jTextPaneNewClass.setEditable(false);
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
			jPanelTermInfo.add(getJTextAreaTerm(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJTextPaneNewClass(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
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
			jLabelNewClass.setText("NewClass :");
		}
		return jLabelNewClass;
	}

	private JComboBox getJComboBoxBiologicalClass() {
		if(jComboBoxBiologicalClass == null) {
			DefaultComboBoxModel jComboBoxBiologicalClassModel = 
				new DefaultComboBoxModel();
			
			jComboBoxBiologicalClass = new JComboBox();

			try {
				this.idclassesClasses = getClasses();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(String value:idclassesClasses.values())
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
	
	private JTextArea getJTextAreaTerm() {
		if(jTextAreaTerm == null) {
			jTextAreaTerm = new JTextArea();
		}
		return jTextAreaTerm;
	}
	
	private JTextPane getJTextPaneNewClass() {
		if(jTextPaneNewClass == null) {
			jTextPaneNewClass = new JTextPane();
		}
		return jTextPaneNewClass;
	}

}


