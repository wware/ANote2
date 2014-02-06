package pt.uminho.anote2.aibench.resources.views;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.resources.datatypes.LexicalWordsAibench;
import pt.uminho.anote2.aibench.resources.datatypes.Resources;
import pt.uminho.anote2.aibench.resources.gui.ChangeLexicalWordGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfoSmall;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;


public class LexicalWordsView extends JPanel implements Observer{


	private static final long serialVersionUID = 3739180981313329259L;
	private JPanel jPanelLWInfo;
	private JTextField jTextFieldID;
	private JLabel jLabel1ID;
	private JButton jButtonHelp;
	private JButton jButtonExport;
	private JButton Import;
	private JList jlistLWContent;
	private JScrollPane jScrollPaneContent;
	private JButton jButton;
	private JTextPane jTextPaneNote;
	private JTextPane jTextPaneName;
	private JLabel jLabelInfo;
	private JLabel jLabelDicName;
	private JPanel jPanelOperations;
	private JPanel jPanelContent;
	private LexicalWordsAibench lexical;
	private JPanel jPanelLWStats;
	private JLabel jLabelTerms;
	private int numberTerms;
	private JTextField jTextFieldTerms;
	private JTabbedPane jTabbedPane1;
	private JPanel jPanelSearchandInfoPanel;
	private JTextField jTextPaneSearch;
	private JLabel jLabelSerach;
	private JCheckBox jCheckBoxCaseSensitive;
	private JPopupMenu menu;

	
	public LexicalWordsView(LexicalWordsAibench lexical)
	{
		this.lexical=lexical;
		menu = getPopup();
		this.lexical.addObserver(this);
		try {
			initGUI();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}
	
	private JPopupMenu getPopup(){
		if(menu==null)
		{
			menu = new JPopupMenu("Options");		

			JMenuItem item = new JMenuItem("Edit");
			item.setBackground(Color.decode("#F0F0F0"));
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					edit(evt);
				}
			});
			menu.add(item);

			item = new JMenuItem("Remove");
			item.setBackground(Color.decode("#F0F0F0"));
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					try {
						remove(evt);
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
			menu.add(item);
		}
		return menu;
	}
	
	protected void edit(ActionEvent evt) {		
		String elem = (String) jlistLWContent.getSelectedValue();
		new ChangeLexicalWordGUI(lexical,jlistLWContent,elem);
	}

	protected void remove(ActionEvent evt) throws SQLException, DatabaseLoadDriverException {
		String elem = (String) jlistLWContent.getSelectedValue();
		int elemID = lexical.getLexicalWordID(elem);
		if(elemID!=-1)
		{
			lexical.removeElement(new ResourceElement(elemID, elem));
			lexical.getLexicalWords().remove(elem);
			numberTerms --;
			jTextFieldTerms.setText(String.valueOf(numberTerms));
			((DefaultListModel) jlistLWContent.getModel()).removeElement(elem);
			jlistLWContent.updateUI();
			new ShowMessagePopup("Element Removed.");
		}
		else
		{
			Workbench.getInstance().error("Cannot delete element");
		}
	}

	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.0, 0.5, 0.1, 0.0, 0.05};
			thisLayout.rowHeights = new int[] {7, 244, 7, 20, 7};
			thisLayout.columnWeights = new double[] {0.1};	
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			{
				jPanelLWInfo = getJPanelInfo();
				this.add(jPanelLWInfo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jPanelContent = new JPanel();
				GridBagLayout jPanelContentLayout = new GridBagLayout();
				jPanelContent.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Words", TitledBorder.LEADING, TitledBorder.TOP));
				this.add(jPanelContent, new GridBagConstraints(0, 1, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelContentLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelContentLayout.rowHeights = new int[] {7, 7, 7, 7};
				jPanelContentLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelContentLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelContent.setLayout(jPanelContentLayout);
				{
					jScrollPaneContent = new JScrollPane();
					jPanelContent.add(jScrollPaneContent, new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jScrollPaneContent.setViewportView(getJTableDicContent());
				}
				this.add(getJTabbedPane1(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				this.add(getJPanelSearchandInfoPanel(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
	}

	private JPanel getJPanelInfo() {
		if(jPanelLWInfo==null)
		{
			jPanelLWInfo = new JPanel();
			GridBagLayout jPanelLWInfoLayout = new GridBagLayout();
			jPanelLWInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Information", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelLWInfoLayout.rowWeights = new double[] { 0.1};
			jPanelLWInfoLayout.rowHeights = new int[] {7};
			jPanelLWInfoLayout.columnWeights = new double[] {0.05, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelLWInfoLayout.columnWidths = new int[] {7, 7, 7, 7, 20, 7};
			jPanelLWInfo.setLayout(jPanelLWInfoLayout);
			{
				jLabelDicName = new JLabel();
				jPanelLWInfo.add(jLabelDicName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelDicName.setText("Name :");
			}
			{
				jLabelInfo = new JLabel();
				jPanelLWInfo.add(jLabelInfo, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelInfo.setText("Notes :");
			}
			{
				jTextPaneName = new JTextPane();
				jTextPaneName.setText(lexical.getName());
				jTextPaneName.setEditable(false);
				jPanelLWInfo.add(jTextPaneName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextPaneNote = new JTextPane();
				jTextPaneNote.setEditable(false);
				jPanelLWInfo.add(jTextPaneNote, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jPanelLWInfo.add(getJLabel1ID(), new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelLWInfo.add(getJTextFieldID(), new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jTextPaneNote.setText(lexical.getInfo());
			}
		}
		return jPanelLWInfo;
	}

	private JPanel getOperationPAnel() {
		if(jPanelOperations==null)
		{
			jPanelOperations = new JPanel();
			GridBagLayout jPanelOperationsLayout = new GridBagLayout();
			jPanelOperations.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Operations", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelOperationsLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelOperationsLayout.rowHeights = new int[] {7, 7, 7};
			jPanelOperationsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelOperationsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelOperations.setLayout(jPanelOperationsLayout);
			{
				jButton = new JButton();
				jPanelOperations.add(jButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelOperations.add(getImport(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelOperations.add(getJButtonExport(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelOperations.add(getJButtonHelp(), new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButton.setText("Add Word");
				jButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Refresh.png")));
				jButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
							if (def.getID().equals("operations.addelementtolexicalwords")){			
								Workbench.getInstance().executeOperation(def);
								return;
							}
						}
					}
				});
			}
		}
		return jPanelOperations;
	}
	
	private DefaultListModel changeJlistContent() throws SQLException, DatabaseLoadDriverException{
		DefaultListModel list = new DefaultListModel();
		Set<String> elems =  lexical.getLexicalWords();
		for(String el:elems)
		{	
			list.addElement(el);
		}
		return list;
	}


	public void update(Observable arg0, Object arg1) {
		try {
			jlistLWContent.setModel(changeJlistContent());
			jlistLWContent.updateUI();
			numberTerms = Resources.getnumberTerms(lexical.getID());
			jTextPaneName.setText(lexical.getName());
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
		jTextFieldTerms.setText(String.valueOf(numberTerms));
	}
	
	private JList getJTableDicContent() throws SQLException, DatabaseLoadDriverException {
		if(jlistLWContent==null)
		{
			jlistLWContent = new JList();
			jlistLWContent.setModel(changeJlistContent());
			jlistLWContent.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e)  {check(e);}
				public void mouseReleased(MouseEvent e) {check(e);}

				public void check(MouseEvent e) {
				    if (e.isPopupTrigger()) { //if the event shows the menu
				    	jlistLWContent.setSelectedIndex(jlistLWContent.locationToIndex(e.getPoint())); //select the item
				        menu.show(jlistLWContent, e.getX(), e.getY()); //and show the menu
				    }
				}
			});
		}
		return jlistLWContent;
	}
	
	private JButton getImport() {
		if(Import == null) {
			Import = new JButton();
			Import.setText("Import");
			Import.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Download.png")));
			Import.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
						if (def.getID().equals("operations.loadcsvlexicalwords")){			
							Workbench.getInstance().executeOperation(def);
							return;
						}
					}
				}
			});
			
		}
		return Import;
	}
	
	private JButton getJButtonExport() {
		if(jButtonExport == null) {
			jButtonExport = new JButton();
			jButtonExport.setText("Export");
			jButtonExport.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Upload22.png")));
			jButtonExport.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
						if (def.getID().equals("operations.savecsvlexicalwords")){			
							Workbench.getInstance().executeOperation(def);
							return;
						}
					}
				}
			});
		}
		return jButtonExport;
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						Help.internetAccess(GlobalOptions.wikiGeneralLink+"LexicalWords_Add_Element");
					} catch (IOException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					}
				}
			});
		}
		return jButtonHelp;
	}
	
	private JPanel getJPanelSearchandInfoPanel() {
		if(jPanelSearchandInfoPanel == null) {
			jPanelSearchandInfoPanel = new JPanel();
			GridBagLayout jPanelSearchandInfoPanelLayout = new GridBagLayout();
			jPanelSearchandInfoPanelLayout.rowWeights = new double[] {0.0};
			jPanelSearchandInfoPanelLayout.rowHeights = new int[] {7};
			jPanelSearchandInfoPanelLayout.columnWeights = new double[] {0.1, 0.05, 0.1, 0.05};
			jPanelSearchandInfoPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelSearchandInfoPanel.setLayout(jPanelSearchandInfoPanelLayout);
			jPanelSearchandInfoPanel.add(getJTextPaneSearch(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSearchandInfoPanel.add(getJLabelSerach(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSearchandInfoPanel.add(getJCheckBoxCaseSensitive(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 6, 0, 0), 0, 0));
			jPanelSearchandInfoPanel.add(getJButtonHelp(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelSearchandInfoPanel;
	}
	
	private JTextField getJTextPaneSearch() {
		if (jTextPaneSearch == null) {
			jTextPaneSearch = new JTextField();
			jTextPaneSearch.setMinimumSize(new Dimension(60,30));
			jTextPaneSearch.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED), null));
			jTextPaneSearch.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent evt) {
					if (jCheckBoxCaseSensitive.isSelected()) {
						searchInTableKeySensetive(evt);

					} else {
						searchInTable(evt);
					}
				}
			});
		}
		return jTextPaneSearch;
	} 
	
	protected void searchInTable(KeyEvent evt) {
		String text;
		ArrayList<Integer> rows = new ArrayList<Integer>();
		DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();

		if (jTextPaneSearch.getText().compareTo("") != 0&& evt.getKeyChar() == KeyEvent.VK_BACK_SPACE)
			text = jTextPaneSearch.getText();
		else
			text = jTextPaneSearch.getText() + evt.getKeyChar();

		for (int i = 0; i < jlistLWContent.getModel().getSize(); i++) {

			if (((String) jlistLWContent.getModel().getElementAt(i)).toLowerCase().contains(text.toLowerCase()))
				rows.add(new Integer(i));
		}
		setTableChangesSearch(rows, selectionModel);

	}

	protected void searchInTableKeySensetive(KeyEvent evt) {
		String text;
		ArrayList<Integer> rows = new ArrayList<Integer>();
		DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();

		if (jTextPaneSearch.getText().compareTo("") != 0&& evt.getKeyChar() == KeyEvent.VK_BACK_SPACE)
			text = jTextPaneSearch.getText();
		else
			text = jTextPaneSearch.getText() + evt.getKeyChar();

		for (int i = 0; i < jlistLWContent.getModel().getSize(); i++) {

			if (((String) jlistLWContent.getModel().getElementAt(i)).contains(text))
				rows.add(new Integer(i));
		}
		setTableChangesSearch(rows, selectionModel);

	}

	@SuppressWarnings("static-access")
	private void setTableChangesSearch(ArrayList<Integer> rows,
			DefaultListSelectionModel selectionModel) {
		int row = 0;
		for (Integer r : rows) {
			row = r.intValue();
			selectionModel.addSelectionInterval(row, row);
		}

		this.jlistLWContent.setSelectionMode(selectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.jlistLWContent.setSelectionModel(selectionModel);
		if (selectionModel.isSelectionEmpty()&& (jTextPaneSearch.getText().compareTo("") != 0)) {
			jTextPaneSearch.setForeground(new java.awt.Color(255, 0, 0));
			jTextPaneSearch.setBackground(new java.awt.Color(174, 174, 174));
		} else {
			this.jTextPaneSearch.setForeground(Color.BLACK);
			this.jTextPaneSearch.setBackground(Color.WHITE);
		}
		if (rows.size() > 0) {

			this.jlistLWContent.scrollRectToVisible(jlistLWContent.getCellBounds(rows.get(0), rows.get(0)));
		}
	}
	
	private JLabel getJLabelSerach() {
		if (jLabelSerach == null) {
			jLabelSerach = new JLabel();
			jLabelSerach.setText("Search :  ");
			
		}
		return jLabelSerach;
	}

	private JCheckBox getJCheckBoxCaseSensitive() {
		if (jCheckBoxCaseSensitive == null) {
			jCheckBoxCaseSensitive = new JCheckBox();
			jCheckBoxCaseSensitive.setSelected(true);
			jCheckBoxCaseSensitive.setText("Case Sensitive");
		}
		return jCheckBoxCaseSensitive;
	}
	
	/**
	 * Stats
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	
	private JPanel getJPanelLWStats() throws SQLException, DatabaseLoadDriverException {
		if(jPanelLWStats == null) {
			jPanelLWStats = new JPanel();
			jPanelLWStats.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Information",TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelDicStatsLayout = new GridBagLayout();
			jPanelDicStatsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelDicStatsLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelDicStatsLayout.columnWeights = new double[] {0.0, 0.05};
			jPanelDicStatsLayout.columnWidths = new int[] {7, 7};
			jPanelLWStats.setLayout(jPanelDicStatsLayout);
			jPanelLWStats.add(getJLabelTerms(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelLWStats.add(getJTextFieldTerms(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelLWStats;
	}
	
	private JLabel getJLabelTerms() {
		if(jLabelTerms == null) {
			jLabelTerms = new JLabel();
			jLabelTerms.setText("Terms");
		}
		return jLabelTerms;
	}
	
	private JTextField getJTextFieldTerms() throws SQLException, DatabaseLoadDriverException {
		if(jTextFieldTerms == null) {
			jTextFieldTerms = new JTextField();
			jTextFieldTerms.setMinimumSize(new Dimension(60,25));
			numberTerms = Resources.getnumberTerms(lexical.getID());
			jTextFieldTerms.setText(String.valueOf(numberTerms));
			jTextFieldTerms.setEditable(false);
		}
		return jTextFieldTerms;
	}
	
	private JTabbedPane getJTabbedPane1() throws SQLException, DatabaseLoadDriverException {
		if(jTabbedPane1 == null) {
			jTabbedPane1 = new JTabbedPane();
			jTabbedPane1.addTab("Operations",getOperationPAnel());
			jTabbedPane1.addTab(GlobalTextInfoSmall.statistics,getJPanelLWStats());
		}
		return jTabbedPane1;
	}
	
	private JLabel getJLabel1ID() {
		if(jLabel1ID == null) {
			jLabel1ID = new JLabel();
			jLabel1ID.setText("ID :");
		}
		return jLabel1ID;
	}
	
	private JTextField getJTextFieldID() {
		if(jTextFieldID == null) {
			jTextFieldID = new JTextField();
			jTextFieldID.setText(String.valueOf(lexical.getID()));
			jTextFieldID.setEditable(false);
		}
		return jTextFieldID;
	}

}
