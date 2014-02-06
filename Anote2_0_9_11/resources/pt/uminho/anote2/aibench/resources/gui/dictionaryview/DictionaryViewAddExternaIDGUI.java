package pt.uminho.anote2.aibench.resources.gui.dictionaryview;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import pt.uminho.anote2.datastructures.annotation.ExternalID;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class DictionaryViewAddExternaIDGUI extends DialogGenericView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean ischange = true;
	private JLabel jLabelTem;
	private DictionaryAibench dictionary;
	private JPanel jPanelTermInfo;
	private JTextField jTextAFieldTerm;
	private String term;
	private String source;
	private JComboBox externalIDds;
	private int termID;
	private JPanel jPanelAddExternalID;
	private JButton jButtonAddExternalID;
	private JTextField jTextFieldSource;
	private JTextField jTextFieldExternalID;
	private JLabel jLabelSource;
	private JLabel jLabelExternalID;
	private JPanel jPanelExternalID;
	private JPanel jPanelExternalIdList;
	private JScrollPane jScrollPane1;
	private JList jListExternalIds;
	private JPopupMenu jPopupMenuExternalID;
	private List<IExternalID> externalIDs; 



	public DictionaryViewAddExternaIDGUI(DictionaryAibench dictionary,JComboBox externalIDds, int termID,String term){
		super();
		this.termID=termID;
		this.term = term;
		this.dictionary = dictionary;
		this.externalIDds = externalIDds;
		this.externalIDs = new ArrayList<IExternalID>();
		this.setTitle("Add External ID - "+term);
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
			jPanelTermInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Add External ID", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelTermInfoLayout = new GridBagLayout();
			jPanelTermInfoLayout.rowWeights = new double[] {0.025, 0.1, 0.0};
			jPanelTermInfoLayout.rowHeights = new int[] {20, 7, 7};
			jPanelTermInfoLayout.columnWeights = new double[] {0.1, 0.0, 0.1};
			jPanelTermInfoLayout.columnWidths = new int[] {7, 313, 7};
			jPanelTermInfo.setLayout(jPanelTermInfoLayout);
			jPanelTermInfo.add(getJLabelTem(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJTextField1(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJPanelExternalID(), new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelTermInfo;
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
		}
	}

	private void update() throws SQLException {
		
		if(externalIDs.size()==0)
		{
			Workbench.getInstance().warn("Please insert at least one external ID");
		}
		else
		{
			for(IExternalID ext:externalIDs)
			{
				int sourceID = dictionary.addSource(ext.getSource());
				if(dictionary.existExternalID(termID,ext))
				{
					Workbench.getInstance().warn("Term already contains external Id :"+ext+" ("+ext.getSource()+")");
					new ShowMessagePopup("Term already contains external Id :"+ext+" ("+ext.getSource()+")");
				}
				else
				{
					dictionary.addExternalID(termID, ext.getExternalID(), sourceID);
					((DefaultComboBoxModel) externalIDds.getModel()).addElement(ext.getExternalID()+" ("+ext.getSource()+")");
				}
			}
			externalIDds.updateUI();
			new ShowMessagePopup("External ID(s) Added .");
			finish();
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
		if(jTextAFieldTerm == null) {
			jTextAFieldTerm = new JTextField();
			jTextAFieldTerm.setText(term);
			jTextAFieldTerm.setEditable(false);
		}
		return jTextAFieldTerm;
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
					try {
						addExternalID();
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});

		}
		return jButtonAddExternalID;
	}
	


	protected void addExternalID() throws SQLException {
		String source = jTextFieldSource.getText();
		String externalID = jTextFieldExternalID.getText();
		int sourceID = dictionary.addSource(source);
		IExternalID ext = new ExternalID(externalID,source,sourceID);
		if(source.equals(""))
		{
			Workbench.getInstance().warn("Source cannot be empty");
		}
		else if(externalID.equals(""))
		{
			Workbench.getInstance().warn("ExternalID cannot be empty");
		}
		else if(externalIDs.contains(ext))
		{
			Workbench.getInstance().warn("ExternalID already in the list");
		}
		else if(dictionary.existExternalID(termID, ext))
		{
			Workbench.getInstance().warn("Term \""+term+"\" already contains this External ID");
		}
		else
		{
			((DefaultComboBoxModel) jListExternalIds.getModel()).addElement(ext.toString());
			externalIDs.add(ext);
			jListExternalIds.updateUI();
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
	
	private JScrollPane getJScrollPane1() {
		if(jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getJListExternalIds());
		}
		return jScrollPane1;
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
	
	protected void removeExternalID(ActionEvent evt) {
		int index = jListExternalIds.getSelectedIndex();
		if(index!=-1)
		{
			((DefaultComboBoxModel) jListExternalIds.getModel()).removeElementAt(index);
			externalIDs.remove(index);
			jListExternalIds.updateUI();
		}
	}
	
	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Dictionary_Management_Content";
	}
}
