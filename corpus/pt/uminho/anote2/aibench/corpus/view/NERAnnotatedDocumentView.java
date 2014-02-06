package pt.uminho.anote2.aibench.corpus.view;
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
import java.util.Set;
import javax.swing.JButton;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.utils.AnnotationPosition;
import pt.uminho.anote2.aibench.corpus.utils.AnnotationPositions;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;


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
public class NERAnnotatedDocumentView extends JPanel implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1229351096914888366L;
	
	
	private NERDocumentAnnotation nerAnnotDoc;
	private JPanel jPanelOtherInformation;
	private JButton jButtonHelp;
	private JTable jTableAnnotations;
	private JScrollPane jScrollPaneAnnotations;
	private JPanel jPanelAnnotationDocInfo;
	private JPanel jPanelAnnotations;

	public NERAnnotatedDocumentView(NERDocumentAnnotation nerAnnotDoc)
	{
		this.nerAnnotDoc=nerAnnotDoc;
		initGUI();
		this.nerAnnotDoc.addObserver(this);
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
				jPanelAnnotations = new JPanel();
				GridBagLayout jPanelAnnotationsLayout = new GridBagLayout();
				this.add(jPanelAnnotations, new GridBagConstraints(0, 1, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelAnnotationsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelAnnotationsLayout.rowHeights = new int[] {7, 7, 7, 7};
				jPanelAnnotationsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelAnnotationsLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelAnnotations.setLayout(jPanelAnnotationsLayout);
				{
					jScrollPaneAnnotations = new JScrollPane();
					jPanelAnnotations.add(jScrollPaneAnnotations, new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					{
						TableModel jTableAnnotationsModel = getModelAnnots();
						jTableAnnotations = new JTable();
						jScrollPaneAnnotations.setViewportView(jTableAnnotations);
						jTableAnnotations.setModel(jTableAnnotationsModel);
					}
				}
			}
			{
				jPanelAnnotationDocInfo = new JPanel();
				this.add(jPanelAnnotationDocInfo, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jPanelOtherInformation = new JPanel();
				this.add(jPanelOtherInformation, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				this.add(getJButtonHelp(), new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
		}

	}

	private TableModel getModelAnnots() {
		DefaultTableModel tableModel = new DefaultTableModel();
		String[] columns = new String[] {"Entity", "Class", "Start","End"};
		tableModel.setColumnIdentifiers(columns);
		AnnotationPositions annotPos = nerAnnotDoc.getAnnotationPositions();
		Map<Integer, String> classes = new HashMap<Integer, String>();
		try {
			classes = ResourcesHelp.getClassIDClassOnDatabase(((Corpus)nerAnnotDoc.getCorpus()).getCorpora().getDb());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Object[][] data;
		data = new Object[annotPos.getAnnotations().size()][4];
		annotPos.getAnnotations().keySet();
		Set<AnnotationPosition> annot = annotPos.getAnnotations().keySet();
		int i=0;
		for(AnnotationPosition pos :annot)
		{
			IEntityAnnotation elem = (IEntityAnnotation) annotPos.getAnnotations().get(pos);
			data[i][0] = elem.getAnnotationValue();
			data[i][1] = classes.get(elem.getClassAnnotationID());
			data[i][2] = pos.getStart();	
			data[i][3] = pos.getEnd();	
			tableModel.addRow(data[i]);
			i++;
		}	
		return tableModel;
	}

	public void update(Observable arg0, Object arg1) {
		jTableAnnotations.setModel(getModelAnnots());
		jTableAnnotations.updateUI();	
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"Annotated_Document_Default_View");
				}
			});
		}
		return jButtonHelp;
	}

}
