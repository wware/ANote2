package pt.uminho.anote2.aibench.corpus.gui.help;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.corpus.gui.help.listnodes.ClassPerDocumentListNode;
import pt.uminho.anote2.aibench.corpus.gui.help.listnodes.Top10Term;
import pt.uminho.anote2.aibench.corpus.gui.help.listrenders.ListClassPerDocumentRenderer;
import pt.uminho.anote2.aibench.corpus.gui.help.listrenders.ListTop10TermsRenderer;
import pt.uminho.anote2.aibench.corpus.stats.NerStatitics;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.datastructures.annotation.ner.SimpleEntity;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;



public class NERGeneralStatistics extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NerStatitics nerStatitics;
	private JTextField totalTextField;
	private JTextField ndocsTextField;
	private JPanel jPanelTotalAnnotations;
	private JList jListTop10terms;
	private JList jListClassDocuments;
	private JScrollPane jScrollPaneTop10Terms;
	private JScrollPane jScrollPaneClassPerDocuments;
	private JPanel jPanelDocuments;
	private JButton jButtonHelp;
	private JList top10List;

	public NERGeneralStatistics(NerStatitics nerStatitics)
	{
		this.nerStatitics = nerStatitics;
		initGUI();
		completeGUI();
	}

	private void completeGUI() {
		this.totalTextField.setText(String.valueOf(nerStatitics.getNerAnnotations()));
		this.ndocsTextField.setText(String.valueOf(nerStatitics.getDocAnnotations().keySet().size()));
		jListClassDocuments.setModel(claculateClassPerDocument());
		jListClassDocuments.setCellRenderer(new ListClassPerDocumentRenderer());
		jListTop10terms.setModel(calculateTop10Terms());
		jListTop10terms.setCellRenderer(new ListTop10TermsRenderer());
	}

	private ListModel calculateTop10Terms() {
		
		SortedMap<Integer,Set<SimpleEntity>> entities = new TreeMap<Integer, Set<SimpleEntity>>();
		for(SimpleEntity sp :nerStatitics.getEntityStatistics().keySet())
		{
			int occurences = nerStatitics.getEntityStatistics().get(sp).getOccurrencesNumber();
			if(entities.containsKey(occurences))
			{
				entities.get(occurences).add(sp);
			}
			else
			{
				Set<SimpleEntity> set = new TreeSet<SimpleEntity>();
				set.add(sp);
				entities.put(occurences, set );
			}
		}
		DefaultComboBoxModel listTop10tModel = new DefaultComboBoxModel();
		List<SimpleEntity> all = new ArrayList<SimpleEntity>();
		for(int numberOcuurences :entities.keySet())
		{
			all.addAll(entities.get(numberOcuurences));
		}
		for(int i=all.size()-1;i>all.size()-11 && i >= 0;i--)
		{
			SimpleEntity sp = all.get(i);
			Top10Term term = new Top10Term(sp.getName(), sp.getClassID(), nerStatitics.getEntityStatistics().get(sp).isHaveSynonyms(), nerStatitics.getEntityStatistics().get(sp).getOccurrencesNumber());
			listTop10tModel.addElement(term);
		}
		return listTop10tModel;
	}

	private ListModel claculateClassPerDocument() {
		DefaultComboBoxModel listClassPerDocumentModel = new DefaultComboBoxModel();
		for(Integer classID :nerStatitics.getClassOcurrences().keySet())
		{
			int classOcccurrences = nerStatitics.getClassOcurrences().get(classID);
			listClassPerDocumentModel.addElement(new ClassPerDocumentListNode(classID, classOcccurrences, nerStatitics.getDocAnnotations().keySet().size()));
		}
		return listClassPerDocumentModel;
	}

	private void initGUI() {
		GridBagLayout corpusStatPanelLayout = new GridBagLayout();
		corpusStatPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.0};
		corpusStatPanelLayout.rowHeights = new int[] {20, 20, 7};
		corpusStatPanelLayout.columnWeights = new double[] {0.2, 0.4, 0.4};
		corpusStatPanelLayout.columnWidths = new int[] {100, 7, 7};
		this.setLayout(corpusStatPanelLayout);
		this.setPreferredSize(new java.awt.Dimension(829, 332));
		{
			jButtonHelp = new JButton();
			this.add(jButtonHelp, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						Help.internetAccess(GlobalOptions.wikiGeneralLink+"Process_Entity_Details_View");
					} catch (IOException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					}
				}
			});
		}
		{
			jPanelTotalAnnotations = new JPanel();
			GridBagLayout jPanelTotalAnnotationsLayout = new GridBagLayout();
			this.add(jPanelTotalAnnotations, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTotalAnnotations.setBorder(BorderFactory.createTitledBorder("Annotations"));
			jPanelTotalAnnotationsLayout.rowWeights = new double[] {0.1};
			jPanelTotalAnnotationsLayout.rowHeights = new int[] {7};
			jPanelTotalAnnotationsLayout.columnWeights = new double[] {0.1};
			jPanelTotalAnnotationsLayout.columnWidths = new int[] {7};
			jPanelTotalAnnotations.setLayout(jPanelTotalAnnotationsLayout);
			{
				totalTextField = new JTextField();
				jPanelTotalAnnotations.add(totalTextField, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

				totalTextField.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEtchedBorder(BevelBorder.LOWERED), 
						null));
				totalTextField.setEditable(false);
				totalTextField.setBackground(Color.white);
			}
		}
		{
			jPanelDocuments = new JPanel();
			GridBagLayout jPanelDocumentsLayout = new GridBagLayout();
			this.add(jPanelDocuments, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDocuments.setBorder(BorderFactory.createTitledBorder("Documents with Annotations"));
			jPanelDocumentsLayout.rowWeights = new double[] {0.1};
			jPanelDocumentsLayout.rowHeights = new int[] {7};
			jPanelDocumentsLayout.columnWeights = new double[] {0.1};
			jPanelDocumentsLayout.columnWidths = new int[] {7};
			jPanelDocuments.setLayout(jPanelDocumentsLayout);
			{
				ndocsTextField = new JTextField();
				jPanelDocuments.add(ndocsTextField, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
				ndocsTextField.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEtchedBorder(BevelBorder.LOWERED), 
						null));
				ndocsTextField.setEditable(false);
				ndocsTextField.setBackground(Color.white);
			}
		}
		{
			jScrollPaneClassPerDocuments = new JScrollPane();
			this.add(jScrollPaneClassPerDocuments, new GridBagConstraints(1, 0, 1, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jScrollPaneClassPerDocuments.setBorder(BorderFactory.createTitledBorder(null, "Class Per Document", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
			{
				jListClassDocuments = new JList();
				jScrollPaneClassPerDocuments.setViewportView(jListClassDocuments);
			}
		}
		{
			jScrollPaneTop10Terms = new JScrollPane();
			this.add(jScrollPaneTop10Terms, new GridBagConstraints(2, 0, 1, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jScrollPaneTop10Terms.setBorder(BorderFactory.createTitledBorder("Top 10 terms"));
			{
				jListTop10terms = new JList();
				jScrollPaneTop10Terms.setViewportView(jListTop10terms);
			}
		}

	}

}
