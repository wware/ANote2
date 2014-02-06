package pt.uminho.anote2.aibench.corpus.view;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.NERDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.datatypes.NERProcess;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.core.document.IAnnotatedDocument;


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
public class NERProcessAnnotationDocumentsView extends JPanel{

	private static final long serialVersionUID = 1L;
	private JPanel jPanelPublications;
	private JPanel jPanelOptions;
	private JPanel jPanelOtherInformation;
	private JTable jTableArticle;
	private JButton jButtonHelp;
	private JScrollPane jScrollPanePubbs;	
	private NERProcess nerProcess;

	public NERProcessAnnotationDocumentsView(NERProcess process) throws IOException, SQLException
	{
		nerProcess = process;
		initGUI();
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.05, 0.1, 0.1, 0.05};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			this.setLayout(thisLayout);
		{
			jPanelPublications = new JPanel();
			GridBagLayout jPanelPublicationsLayout = new GridBagLayout();
			this.add(jPanelPublications, new GridBagConstraints(0, 1, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelPublicationsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelPublicationsLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelPublicationsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelPublicationsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelPublications.setLayout(jPanelPublicationsLayout);
			{
				jScrollPanePubbs = new JScrollPane();
				jPanelPublications.add(jScrollPanePubbs, new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					TableModel jTable1Model = getTableModel();
					jTableArticle = new JTable();
					jScrollPanePubbs.setViewportView(jTableArticle);
					jTableArticle.setModel(jTable1Model);
					completeTable();
				}
			}
		}
		{
			jPanelOptions = new JPanel();
			this.add(jPanelOptions, new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			{
				jButtonHelp = new JButton();
				jPanelOptions.add(jButtonHelp);
				jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
				jButtonHelp.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Help.internetAcess(GlobalOptions.wikiGeneralLink+"Process_Load_Annotated_Document");
					}
				});
			}
		}
		{
			jPanelOtherInformation = new JPanel();
			this.add(jPanelOtherInformation, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		}
	}
	
	private TableModel getTableModel() {
		DefaultTableModel tableModel = new DefaultTableModel();
		String[] columns = new String[] {"Title", "Authors", "Date","ID"};
		tableModel.setColumnIdentifiers(columns);		
		Object[][] data;	
		data = new Object[nerProcess.getAllProcessDocs().size()][4];
		Iterator<Integer> itDocs = nerProcess.getAllProcessDocs().keySet().iterator();
		int i=0;
		while(itDocs.hasNext())
		{
			IAnnotatedDocument pub = nerProcess.getAllProcessDocs().get(itDocs.next());
			data[i][0] = pub.getTitle();
			data[i][1] = pub.getAuthors();
			data[i][2] = pub.getDate();
			data[i][3] = pub.getID();	
			tableModel.addRow(data[i]);
			i++;
		}	
		return tableModel;
	}

	private void completeTable() {

		jTableArticle.getColumnModel().getColumn(1).setMaxWidth(120);
		jTableArticle.getColumnModel().getColumn(1).setMinWidth(120);
		jTableArticle.getColumnModel().getColumn(1).setPreferredWidth(120);		
		jTableArticle.getColumnModel().getColumn(2).setMaxWidth(80);
		jTableArticle.getColumnModel().getColumn(2).setMinWidth(80);
		jTableArticle.getColumnModel().getColumn(2).setPreferredWidth(80);		
		jTableArticle.getColumnModel().getColumn(3).setMaxWidth(45);
		jTableArticle.getColumnModel().getColumn(3).setMinWidth(45);
		jTableArticle.getColumnModel().getColumn(3).setPreferredWidth(45);		
		jTableArticle.setRowHeight(20);		
		TableColumn viewColumn2 = new TableColumn();
		viewColumn2.setHeaderValue("Load");
		viewColumn2.setMinWidth(45);
		viewColumn2.setMaxWidth(45);
		viewColumn2.setPreferredWidth(45);
		viewColumn2.setCellRenderer(new TableCellRenderer(){

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/colorscm.png")));
				viewButton.setBackground(Color.WHITE);
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						addAnnotationDoc();
					}
				});
				return viewButton;
			}
				
		});
		
		viewColumn2.setCellEditor(new TableCellEditor(){

			public Component getTableCellEditorComponent(JTable table, Object value, boolean flag, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/colorscm.png")));
				viewButton.setBackground(Color.WHITE);
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						addAnnotationDoc();
					}
				});
				return viewButton;
			}

			public void addCellEditorListener(CellEditorListener arg0) {
			}

			public void cancelCellEditing() {
			}

			public Object getCellEditorValue() {
				return null;
			}

			public boolean isCellEditable(EventObject arg0) {
				return true;
			}

			public void removeCellEditorListener(CellEditorListener arg0) {
			}

			public boolean shouldSelectCell(EventObject arg0) {
				return true;
			}

			public boolean stopCellEditing() {
				return true;
			}
		});
		jTableArticle.addColumn(viewColumn2);	
		
	}
	
	private void addAnnotationDoc() {
		IAnnotatedDocument doc = nerProcess.getAllProcessDocs().get(jTableArticle.getModel().getValueAt(jTableArticle.getSelectedRow(),3));
		nerProcess.addAnnotatedDocument((NERDocumentAnnotation) doc);
	}



}
