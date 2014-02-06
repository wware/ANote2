package pt.uminho.anote2.aibench.resources.views;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.resources.datatypes.LookupTableAibench;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
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
public class LookupTableView extends JPanel implements Observer{


	private static final long serialVersionUID = 3739180981313329259L;
	private JPanel jPanelDicInfo;
	private JButton jButtonHelp;
	private JButton jButtonExport;
	private JButton Import;
	private JTable jTableDicContent;
	private JScrollPane jScrollPaneContent;
	private JButton jButtonEditNote;
	private JButton jButtonEditName;
	private JButton jButton;
	private JTextPane jTextPaneNote;
	private JTextPane jTextPaneName;
	private JLabel jLabelInfo;
	private JLabel jLabelDicName;
	private JPanel jPanelOperations;
	private JPanel jPanelContent;

	private LookupTableAibench lookup;
	
	public LookupTableView(LookupTableAibench lookup)
	{
		this.lookup=lookup;
		initGUI();
		this.lookup.addObserver(this);
	}
	
	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.0, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 244, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};	
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			this.setLayout(thisLayout);
			{
				jPanelDicInfo = new JPanel();
				GridBagLayout jPanelDicInfoLayout = new GridBagLayout();
				jPanelDicInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "LookupTable Information", TitledBorder.LEADING, TitledBorder.TOP));
				this.add(jPanelDicInfo, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelDicInfoLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1};
				jPanelDicInfoLayout.rowHeights = new int[] {15, 7, 7, 7};
				jPanelDicInfoLayout.columnWeights = new double[] {0.0, 0.0, 0.1, 0.1};
				jPanelDicInfoLayout.columnWidths = new int[] {80, 339, 7, 7};
				jPanelDicInfo.setLayout(jPanelDicInfoLayout);
				{
					jLabelDicName = new JLabel();
					jPanelDicInfo.add(jLabelDicName, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelDicName.setText("Name");
				}
				{
					jLabelInfo = new JLabel();
					jPanelDicInfo.add(jLabelInfo, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelInfo.setText("Notes");
				}
				{
					jTextPaneName = new JTextPane();
					jTextPaneName.setText(lookup.getName());
					jPanelDicInfo.add(jTextPaneName, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
				{
					jTextPaneNote = new JTextPane();
					jPanelDicInfo.add(jTextPaneNote, new GridBagConstraints(1, 2, 2, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jTextPaneNote.setText(lookup.getInfo());
				}
				{
					jButtonEditName = new JButton();
					jPanelDicInfo.add(jButtonEditName, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonEditName.setText("Edit");
				}
				{
					jButtonEditNote = new JButton();
					jPanelDicInfo.add(jButtonEditNote, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonEditNote.setText("Edit");
				}
			}
			{
				jPanelContent = new JPanel();
				GridBagLayout jPanelContentLayout = new GridBagLayout();
				jPanelContent.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "LookupTable Elements Information", TitledBorder.LEADING, TitledBorder.TOP));
				this.add(jPanelContent, new GridBagConstraints(0, 1, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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
			}
			{
				jPanelOperations = new JPanel();
				GridBagLayout jPanelOperationsLayout = new GridBagLayout();
				jPanelOperations.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "LookupTable Operations", TitledBorder.LEADING, TitledBorder.TOP));
				this.add(jPanelOperations, new GridBagConstraints(0, 4, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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
					jButton.setText("Add Term");
					jButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Refresh22.png")));
					jButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0) {
							for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
								if (def.getID().equals("operations.addelementtolookup")){			
									Workbench.getInstance().executeOperation(def);
									return;
								}
							}
						}
					});
				}
			}
			this.setSize(900, 600);
			constructTable();
		}

	}
	
	private TableModel fillModelPubTable(){
		String[] columns = new String[] {"Term","Class"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);
		Map<Integer,String> classIDclass=new HashMap<Integer, String>();
		
		Object[][] data;

		try {
			classIDclass = ResourcesHelp.getClassIDClassOnDatabase(lookup.getDb());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IResourceElementSet<IResourceElement> elems =  lookup.getResourceElements();
		data = new Object[elems.size()][2];
		int i=0;
		for(IResourceElement elem:elems.getElements())
		{
			data[i][0] = elem.getTerm();
			data[i][1] = classIDclass.get(elem.getTermClassID());
					
			tableModel.addRow(data[i]);
		}
			
		return tableModel;		
	}
	
	private void constructTable()
	{
		jTableDicContent.getColumnModel().getColumn(1).setMaxWidth(150);
		jTableDicContent.getColumnModel().getColumn(1).setMinWidth(150);
		jTableDicContent.getColumnModel().getColumn(1).setPreferredWidth(150);
		jTableDicContent.setRowHeight(20);
	}



	public void update(Observable arg0, Object arg1) {
		TableModel jTableDicContentModel = fillModelPubTable();
		jTableDicContent.setModel(jTableDicContentModel );
		constructTable();
		jTableDicContent.updateUI();
	}
	
	private JTable getJTableDicContent() {
		if(jTableDicContent==null)
		{
			jTableDicContent = new JTable();
			TableModel jTableDicContentModel = fillModelPubTable();
			jTableDicContent.setModel(jTableDicContentModel );
		}
		return jTableDicContent;
	}
	
	private JButton getImport() {
		if(Import == null) {
			Import = new JButton();
			Import.setText("Import");
			Import.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Download22.png")));
			Import.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
						if (def.getID().equals("operations.loadcsvlookup")){			
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
						if (def.getID().equals("operations.savecsvlookup")){			
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
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"LookupTable_Add_Element");
				}
			});
		}
		return jButtonHelp;
	}

}
