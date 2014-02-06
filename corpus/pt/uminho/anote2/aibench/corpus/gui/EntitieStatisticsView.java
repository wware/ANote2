package pt.uminho.anote2.aibench.corpus.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.datastructures.utils.GenericPair;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

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
public class EntitieStatisticsView extends JDialog{

	private static final long serialVersionUID = -4109319601514560306L;
	
	private String entity;
	private List<GenericPair<String, String>> terms_stats;
	private JScrollPane jScrollPane1;
	private JButton closeButton;
	private JTable statsTable;

	public EntitieStatisticsView(String entity, List<GenericPair<String, String>> terms_stats){
		super(Workbench.getInstance().getMainFrame());
		this.entity = entity;
		this.terms_stats = terms_stats;
		initGUI();
	}
	
	private void initGUI(){
		
		
		
		
		this.setVisible(true);
		GridBagLayout thisLayout = new GridBagLayout();
		this.setTitle(entity + "'s statistics");
		thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.0};
		thisLayout.rowHeights = new int[] {7, 7, 7, 30};
		thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		thisLayout.columnWidths = new int[] {7, 7, 7, 7};
		getContentPane().setLayout(thisLayout);
		{
			jScrollPane1 = new JScrollPane();
			getContentPane().add(jScrollPane1, new GridBagConstraints(0, 0, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 0, 3), 0, 0));
			getContentPane().add(getCloseButton(), new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 0, 3, 0), 0, 0));
			{
				statsTable = new JTable();
				jScrollPane1.setViewportView(statsTable);
				statsTable.setModel(fillModelPubTable());
				statsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {  

					private static final long serialVersionUID = -7239900848576528572L;

					public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {  
				         super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);  
				         if (row % 2 == 0) {  
				        	 setBackground(Color.decode("#F0F0F0"));  
				         } else {  
				        	 setBackground(null);  
				         }  
				         return this;  
				    }  
				}); 
			}
		}
		this.setSize(316, 247);
		Utilities.centerOnOwner(this);
		
	}
	
	private TableModel fillModelPubTable(){
		String[] columns = new String[] {"Term", "Frequencie"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);
		
		Object[][] data;
		int i = 0;
		
		data = new Object[this.terms_stats.size()][2];
			
		for(GenericPair<String, String> pair: this.terms_stats)
		{
			data[i][0] = pair.getX();
			data[i][1] = pair.getY();
					
			tableModel.addRow(data[i++]);
		}
			
		return tableModel;		
	}
	
	private void closeButtonActionPerformed(ActionEvent evt){
		this.setVisible(false);
		this.dispose();	
	}
	
	private JButton getCloseButton() {
		if(closeButton == null) {
			closeButton = new JButton();
			closeButton.setText("Close");
			closeButton.setIcon(new ImageIcon("plugins_src/pt.uminho.di.anote/icons/cancel.png"));
			closeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					closeButtonActionPerformed(evt);
				}
			});
		}
		return closeButton;
	}
}
