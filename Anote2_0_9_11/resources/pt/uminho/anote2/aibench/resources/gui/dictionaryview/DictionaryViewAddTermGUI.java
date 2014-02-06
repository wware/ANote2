package pt.uminho.anote2.aibench.resources.gui.dictionaryview;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.annotation.ExternalID;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class DictionaryViewAddTermGUI extends DialogGenericView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean ischange = true;
	private JTextField jTextFieldNewTErm;
	private JLabel jLabelTem;
	private DictionaryAibench dictionary;
	private JPanel jPanelTermInfo;
	private JLabel jLabelTerm;
	private JTextField jTextFieldClass;
	private String oldterm;
	private String classe;
	private JList jListterms;
	private JPanel jPanelExternalID;
	private JPopupMenu jPopupMenuSynonyms;
	private JList jListSynonyms;
	private JPanel jPanelSynonysList;
	private JTextField jTextFieldSynonym;
	private JButton jButtonAddSynonym;
	private JPanel jPanelAddSynonym;
	private JPanel jPanelSynonyms;
	private JPanel jPanelTermNameInfo;
	private JPanel jPanelClassPanel;
	private SortedMap<String, Integer> terms;
	private Map<Integer, Integer> lineTerm;
	private JPopupMenu jPopupMenuExternalID;
	private JList jListExternalIds;
	private JPanel jPanelExternalIdList;
	private JLabel jLabelExternalID;
	private JLabel jLabelSource;
	private JTextField jTextFieldExternalID;
	private JTextField jTextFieldSource;
	private JButton jButtonAddExternalID;
	private JPanel jPanelAddExternalID;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private int classID;
	private List<String> synonyms;
	private List<IExternalID> externalIDs; 


	public DictionaryViewAddTermGUI(DictionaryAibench dictionary ,Map<Integer, Integer> lineTerm, SortedMap<String, Integer> terms,JList jListterms,int classID,String classe){
		super();
		this.classID = classID;
		this.classe = classe;
		this.dictionary = dictionary;
		this.terms = terms;
		this.lineTerm = lineTerm;
		this.jListterms = jListterms;
		this.synonyms = new ArrayList<String>();
		this.externalIDs = new ArrayList<IExternalID>();
		this.setTitle("Add Term - "+classe);
		this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);
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
			jPanelTermInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Add Term", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelTermInfoLayout = new GridBagLayout();
			jPanelTermInfoLayout.rowWeights = new double[] {0.0, 0.025, 0.1, 0.1, 0.0};
			jPanelTermInfoLayout.rowHeights = new int[] {20, 20, 20, 7, 7};
			jPanelTermInfoLayout.columnWeights = new double[] {0.1, 0.0, 0.1};
			jPanelTermInfoLayout.columnWidths = new int[] {7, 313, 7};
			jPanelTermInfo.setLayout(jPanelTermInfoLayout);
			jPanelTermInfo.add(getJPanelClassPanel(), new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJPanelTermNameInfo(), new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJPanelSynonyms(), new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJPanelExternalID(), new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}

	private void update() throws DatabaseLoadDriverException, SQLException {
		String newTerm = jTextFieldNewTErm.getText();
		if(newTerm.equals(""))
		{
			Workbench.getInstance().warn("Term cannot be empty");
		}
		else
		{
			ResourceElement elemResource = new ResourceElement(-1,newTerm, classID, "");
			if(this.dictionary.verifyIfTermExist(elemResource))
			{
				Workbench.getInstance().warn("Term already exists in Dictionary");
			}
			else if (this.dictionary.addElement(elemResource)) {
				((DefaultComboBoxModel) jListterms.getModel()).addElement(newTerm);
				int nextElem = HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements);
				if(lineTerm == null)
					lineTerm = new HashMap<Integer, Integer>();
				lineTerm.put(jListterms.getModel().getSize()-1, nextElem-1);
				for(String syn:synonyms)
				{
					this.dictionary.addSynonymsNoRestritions(new ResourceElement(nextElem -1, syn));
				}
				for(IExternalID extID:externalIDs)
				{
					int sourceID = this.dictionary.addSource(extID.getSource());
					this.dictionary.addExternalID(nextElem -1, extID.getExternalID(), sourceID);
				}
				jListterms.updateUI();
				dictionary.notifyViewObservers();
				new ShowMessagePopup("Term Added .");
				finish();
			}
		}
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
			jTextFieldClass.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		}
		return jTextFieldClass;
	}
	
	private JTextField getJTextFieldNewTerm() {
		if(jTextFieldNewTErm == null) {
			jTextFieldNewTErm = new JTextField();
			jTextFieldNewTErm.setText(this.oldterm);
			jTextFieldNewTErm.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		}
		return jTextFieldNewTErm;
	}
	
	private JPanel getJPanelClassPanel() {
		if(jPanelClassPanel == null) {
			jPanelClassPanel = new JPanel();
			GridBagLayout jPanelClassPanelLayout = new GridBagLayout();
			jPanelClassPanelLayout.rowWeights = new double[] {0.1};
			jPanelClassPanelLayout.rowHeights = new int[] {7};
			jPanelClassPanelLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
			jPanelClassPanelLayout.columnWidths = new int[] {7, 7, 7};
			jPanelClassPanel.setLayout(jPanelClassPanelLayout);
			jPanelClassPanel.setBorder(BorderFactory.createTitledBorder(null, "Class Info", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
			jPanelClassPanel.add(getJLabelTem(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelClassPanel.add(getJTextFieldClass(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelClassPanel;
	}
	
	private JPanel getJPanelTermNameInfo() {
		if(jPanelTermNameInfo == null) {
			jPanelTermNameInfo = new JPanel();
			GridBagLayout jPanelTermNameInfoLayout = new GridBagLayout();
			jPanelTermNameInfo.setBorder(BorderFactory.createTitledBorder("Term (Name)*"));
			jPanelTermNameInfoLayout.rowWeights = new double[] {0.1};
			jPanelTermNameInfoLayout.rowHeights = new int[] {7};
			jPanelTermNameInfoLayout.columnWeights = new double[] {0.0, 0.1, 0.025};
			jPanelTermNameInfoLayout.columnWidths = new int[] {7, 7, 20};
			jPanelTermNameInfo.setLayout(jPanelTermNameInfoLayout);
			jPanelTermNameInfo.add(getJLabelTerm(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
			jPanelTermNameInfo.add(getJTextFieldNewTerm(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
		}
		return jPanelTermNameInfo;
	}
	
	private JPanel getJPanelSynonyms() {
		if(jPanelSynonyms == null) {
			jPanelSynonyms = new JPanel();
			GridBagLayout jPanelSynonymsLayout = new GridBagLayout();
			jPanelSynonyms.setBorder(BorderFactory.createTitledBorder("Synonyms"));
			jPanelSynonymsLayout.rowWeights = new double[] {0.025, 0.1};
			jPanelSynonymsLayout.rowHeights = new int[] {7, 7};
			jPanelSynonymsLayout.columnWeights = new double[] {0.1};
			jPanelSynonymsLayout.columnWidths = new int[] {7};
			jPanelSynonyms.setLayout(jPanelSynonymsLayout);
			jPanelSynonyms.add(getJPanelAddSynonym(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSynonyms.add(getJPanelSynonysList(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelSynonyms;
	}
	
	private JPanel getJPanelAddSynonym() {
		if(jPanelAddSynonym == null) {
			jPanelAddSynonym = new JPanel();
			GridBagLayout jPanelAddSynonymLayout = new GridBagLayout();
			jPanelAddSynonymLayout.rowWeights = new double[] {0.1};
			jPanelAddSynonymLayout.rowHeights = new int[] {7};
			jPanelAddSynonymLayout.columnWeights = new double[] {0.05, 0.1, 0.1, 0.05};
			jPanelAddSynonymLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelAddSynonym.setLayout(jPanelAddSynonymLayout);
			jPanelAddSynonym.add(getJButtonAddSynonym(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
			jPanelAddSynonym.add(getJTextFieldSynonym(), new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelAddSynonym;
	}
	
	private JButton getJButtonAddSynonym() {
		if(jButtonAddSynonym == null) {
			jButtonAddSynonym = new JButton();
			jButtonAddSynonym.setText("Add");
			jButtonAddSynonym.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/bottom16.png")));
			jButtonAddSynonym.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					addSynonym();
				}
			});
		}
		return jButtonAddSynonym;
	}
	
	protected void addSynonym() {
		String synonym = jTextFieldSynonym.getText();
		if(synonym.equals(""))
		{
			Workbench.getInstance().warn("Synonym cannot be empty");
		}
		else
		{
			if(synonyms.contains(synonym))
			{
				Workbench.getInstance().warn("Synonym already exists");
			}
			else if(synonym.equals(jTextFieldNewTErm.getText()))
			{
				Workbench.getInstance().warn("Synonym has the same name as primary term");
			}
			else if(this.dictionary.verifyIfTermExist(new ResourceElement(synonym)))
			{
				Workbench.getInstance().warn("Synonym already exists in Dictionary as a Primary Term or Synonym");
			}
			else
			{
				((DefaultComboBoxModel) jListSynonyms.getModel()).addElement(synonym);
				synonyms.add(synonym);
				jListSynonyms.updateUI();
			}
		}
	}
	
	private JTextField getJTextFieldSynonym() {
		if(jTextFieldSynonym == null) {
			jTextFieldSynonym = new JTextField();
			jTextFieldSynonym.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		}
		return jTextFieldSynonym;
	}
	
	private JPanel getJPanelSynonysList() {
		if(jPanelSynonysList == null) {
			jPanelSynonysList = new JPanel();
			GridBagLayout jPanelSynonysListLayout = new GridBagLayout();
			jPanelSynonysListLayout.rowWeights = new double[] {0.1};
			jPanelSynonysListLayout.rowHeights = new int[] {7};
			jPanelSynonysListLayout.columnWeights = new double[] {0.1};
			jPanelSynonysListLayout.columnWidths = new int[] {7};
			jPanelSynonysList.setLayout(jPanelSynonysListLayout);
			jPanelSynonysList.add(getJScrollPane2(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelSynonysList;
	}
	
	private JList getJListSynonyms() {
		if(jListSynonyms == null) {
			ListModel jListSynonymsModel = 
					new DefaultComboBoxModel();
			jListSynonyms = new JList();
			jListSynonyms.setModel(jListSynonymsModel);
			jListSynonyms.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

			jListSynonyms.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
			setComponentPopupMenu(jListSynonyms, getJPopupMenuSynonyms());
		}
		return jListSynonyms;
	}
	
	private JPopupMenu getJPopupMenuSynonyms() {
		if(jPopupMenuSynonyms == null) {
			jPopupMenuSynonyms = new JPopupMenu();
			JMenuItem item = new JMenuItem("Remove");
			item.setBackground(Color.decode("#F0F0F0"));
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					removeSynonym(evt);
				}
			});
			jPopupMenuSynonyms.add(item);
		}
		return jPopupMenuSynonyms;
	}
	
	protected void removeSynonym(ActionEvent evt) {
		int index = jListSynonyms.getSelectedIndex();
		if(index!=-1)
		{
			((DefaultComboBoxModel) jListSynonyms.getModel()).removeElementAt(index);
			synonyms.remove(index);
			jListSynonyms.updateUI();
		}
	}

	/**
	* Auto-generated method for setting the popup menu for a component
	*/
	private void setComponentPopupMenu(final java.awt.Component parent, final javax.swing.JPopupMenu menu) {
		parent.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent e) {
				if(e.isPopupTrigger())
					menu.show(parent, e.getX(), e.getY());
			}
			public void mouseReleased(java.awt.event.MouseEvent e) {
				if(e.isPopupTrigger())
					menu.show(parent, e.getX(), e.getY());
			}
		});
	}
	
	private JPanel getJPanelExternalID() {
		if(jPanelExternalID == null) {
			jPanelExternalID = new JPanel();
			GridBagLayout jPanelExternalIDLayout = new GridBagLayout();
			jPanelExternalIDLayout.rowWeights = new double[] {0.025, 0.1};
			jPanelExternalIDLayout.rowHeights = new int[] {7, 7};
			jPanelExternalIDLayout.columnWeights = new double[] {0.1};
			jPanelExternalIDLayout.columnWidths = new int[] {7};
			jPanelExternalID.setLayout(jPanelExternalIDLayout);
			jPanelExternalID.setBorder(BorderFactory.createTitledBorder("External IDs"));
			jPanelExternalID.add(getJPanelAddExternalID(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelExternalID.add(getJPanelExternalIdList(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelExternalID;
	}
	
	private JPanel getJPanelAddExternalID() {
		if(jPanelAddExternalID == null) {
			jPanelAddExternalID = new JPanel();
			GridBagLayout jPanelAddExternalIDLayout = new GridBagLayout();
			jPanelAddExternalIDLayout.rowWeights = new double[] {0.1, 0.1};
			jPanelAddExternalIDLayout.rowHeights = new int[] {7, 7};
			jPanelAddExternalIDLayout.columnWeights = new double[] {0.025, 0.1, 0.1, 0.05};
			jPanelAddExternalIDLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelAddExternalID.setLayout(jPanelAddExternalIDLayout);
			jPanelAddExternalID.add(getJButtonAddExternalID(), new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
			jPanelAddExternalID.add(getJTextFieldSource(), new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelAddExternalID.add(getJTextFieldExternalID(), new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelAddExternalID.add(getJLabelSource(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelAddExternalID.add(getJLabelExternalID(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelAddExternalID;
	}
	
	private JButton getJButtonAddExternalID() {
		if(jButtonAddExternalID == null) {
			jButtonAddExternalID = new JButton();
			jButtonAddExternalID.setText("Add");
			jButtonAddExternalID.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/bottom16.png")));
			jButtonAddExternalID.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					addExternalID();
				}
			});

		}
		return jButtonAddExternalID;
	}
	


	protected void addExternalID() {
		String source = jTextFieldSource.getText();
		String externalID = jTextFieldExternalID.getText();
		if(source.equals(""))
		{
			Workbench.getInstance().warn("Source cannot be empty");
		}
		else if(externalID.equals(""))
		{
			Workbench.getInstance().warn("ExternalID cannot be empty");
		}
		else
		{
			IExternalID ext = new ExternalID(externalID,source,-1);
			if(externalIDs.contains(ext))
			{
				Workbench.getInstance().warn("The external ID already exists");
			}
			else
			{
				((DefaultComboBoxModel) jListExternalIds.getModel()).addElement(externalID + " (" + source + ") ");
				externalIDs.add(ext);
				jListExternalIds.updateUI();
			}
		}
		
	}

	private JTextField getJTextFieldSource() {
		if(jTextFieldSource == null) {
			jTextFieldSource = new JTextField();
			jTextFieldSource.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		}
		return jTextFieldSource;
	}
	
	private JTextField getJTextFieldExternalID() {
		if(jTextFieldExternalID == null) {
			jTextFieldExternalID = new JTextField();
			jTextFieldExternalID.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		}
		return jTextFieldExternalID;
	}
	
	private JLabel getJLabelSource() {
		if(jLabelSource == null) {
			jLabelSource = new JLabel();
			jLabelSource.setText("Source :");
		}
		return jLabelSource;
	}
	
	private JLabel getJLabelExternalID() {
		if(jLabelExternalID == null) {
			jLabelExternalID = new JLabel();
			jLabelExternalID.setText("External ID :");
		}
		return jLabelExternalID;
	}
	
	private JPanel getJPanelExternalIdList() {
		if(jPanelExternalIdList == null) {
			jPanelExternalIdList = new JPanel();
			GridBagLayout jPanelExternalIdListLayout = new GridBagLayout();
			jPanelExternalIdListLayout.rowWeights = new double[] {0.1};
			jPanelExternalIdListLayout.rowHeights = new int[] {7};
			jPanelExternalIdListLayout.columnWeights = new double[] {0.1};
			jPanelExternalIdListLayout.columnWidths = new int[] {7};
			jPanelExternalIdList.setLayout(jPanelExternalIdListLayout);
			jPanelExternalIdList.add(getJScrollPane1(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelExternalIdList;
	}
	
	private JList getJListExternalIds() {
		if(jListExternalIds == null) {
			ListModel jListExternalIdsModel = 
					new DefaultComboBoxModel();
			jListExternalIds = new JList();
			jListExternalIds.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
			jListExternalIds.setModel(jListExternalIdsModel);
			jListExternalIds.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
			setComponentPopupMenu(jListExternalIds, getJPopupMenuExternalID());
		}
		return jListExternalIds;
	}
	
	private JPopupMenu getJPopupMenuExternalID() {
		if(jPopupMenuExternalID == null) {
			jPopupMenuExternalID = new JPopupMenu();
			jPopupMenuExternalID.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
			JMenuItem item = new JMenuItem("Remove");
			item.setBackground(Color.decode("#F0F0F0"));
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					removeExternalID(evt);
				}
			});
			jPopupMenuExternalID.add(item);
		}
		return jPopupMenuExternalID;
	}

	protected void removeExternalID(ActionEvent evt) {
		int index = jListExternalIds.getSelectedIndex();
		if(index!=-1)
		{
			((DefaultComboBoxModel) jListExternalIds.getModel()).removeElementAt(index);
			externalIDs.remove(index);
			jListExternalIds.updateUI();
		}
	}

	private JScrollPane getJScrollPane2() {
		if(jScrollPane2 == null) {
			jScrollPane2 = new JScrollPane();
			jScrollPane2.setViewportView(getJListSynonyms());
		}
		return jScrollPane2;
	}
	
	private JScrollPane getJScrollPane1() {
		if(jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getJListExternalIds());
		}
		return jScrollPane1;
	}
	
	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Dictionary_Management_Content";
	}

}
