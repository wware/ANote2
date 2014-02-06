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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import es.uvigo.ei.aibench.workbench.Workbench;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class DictionaryView extends JPanel implements Observer {

	private static final long serialVersionUID = 3739180981313329259L;
	private JPanel jPanelDicInfo;
	private JScrollPane jScrollPaneSyn;
	private JList jListSyn;
	private JList jListTerms;
	private JScrollPane jScrollPaneTerms;
	private JScrollPane jScrollPaneClassContent;
	private JPanel jPanelContent;
	private JCheckBox jCheckBoxCaseSensitive;
	private JLabel jLabelSerach;
	private JTextField jTextPaneSearch;
	private JButton jButtonAddTerm;
	private JTextPane jTextPaneAddTerm;
	private JPanel jPanelTermOperation;
	private JButton jButtonAddSyn;
	private JTextPane jTextPaneAddSyn;
	private JPanel jPanelSynOperation;
	private JButton jButtonAddContentClass;
	private JTextPane jTextPaneAddContentClassToDic;
	private JPanel jPanelDicContentOperations;
	private JPanel jPanelTermsAndSyn;
	private JList jListDicContent;
	private JButton jButtonHelp;
	private JPanel jPanelSynAdd;
	private JPanel jPanelAddTerm;
	private JPanel jPanelAddClassDic;
	private JTable jTableDicContent;
	private JScrollPane jScrollPaneDicContentStats;
	private JTextField jTextFieldNumberOfClasses;
	private JLabel jLabelClassesContent;
	private JTextField jTextFieldAverageTermsForSyn;
	private JTextField jTextFieldSyn;
	private JTextField jTextFieldTerms;
	private JLabel jLabelSyn;
	private JLabel jLabelTerms;
	private JPanel jPanelDicStats;
	private JButton jButtonSynEdit;
	private JTextPane jTextPaneSynEdit;
	private JButton jButtonTermEdit;
	private JTextPane jTextPaneTermEdit;
	private JButton jButtonContentEdit;
	private JTextPane jTextPaneContentEdit;

	private DictionaryAibench dictionary;

	private SortedMap<String, Integer> terms; // Term, termID
	private Map<Integer, Integer> lineTerm; // line, TermID
	private JLabel jLabelAveregeSynForTerm;
	
	
	private int numberSyn;
	private int numberTerms;


	
	DecimalFormat dec = new DecimalFormat("0.00");

	private SortedMap<Integer, String> classes; // classeID,classe
	private Map<Integer, Integer> lineClassID; // lineNumber/classID

	// private Map<Integer,List<String>> termIDSyn; // termID, LIsta Syn
	// private Map<Integer,Integer> lineTermID; // lineNUmber, TErmID

	public DictionaryView(DictionaryAibench dictionary) {
		this.dictionary = dictionary;
		classes = dictionary.getDicionaryContentClasses();
		initGUI();
		this.dictionary.addObserver(this);
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.0};
			thisLayout.rowHeights = new int[] {7, 7, 7, 244};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {265, 7, 7, 7};
			this.setLayout(thisLayout);
			{
				jPanelDicInfo = new JPanel();
				GridBagLayout jPanelDicInfoLayout = new GridBagLayout();
				jPanelDicInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""),"Dictionary Information and statitics", TitledBorder.LEADING,TitledBorder.TOP));
				this.add(jPanelDicInfo, new GridBagConstraints(0, 0, 4, 1, 0.0,0.0, GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));jPanelDicInfoLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1};
				jPanelDicInfoLayout.rowHeights = new int[] {7, 7, 7, 7};
				jPanelDicInfoLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.1, 0.0};
				jPanelDicInfoLayout.columnWidths = new int[] {80, 339, 20, 20, 20, 7, 7};
				jPanelDicInfo.setLayout(jPanelDicInfoLayout);
				jPanelDicInfo.add(getJPanelDicStats(), new GridBagConstraints(0, 1, 7, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jPanelContent = new JPanel();
				GridBagLayout jPanelContentLayout = new GridBagLayout();
				jPanelContent.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""),"Dictionary Class Content", TitledBorder.LEADING,TitledBorder.TOP));
				this.add(jPanelContent, new GridBagConstraints(0, 1, 4, 3, 0.0,0.0, GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelContentLayout.rowWeights = new double[] {0.1, 0.1, 0.0, 0.0};
				jPanelContentLayout.rowHeights = new int[] {300, 7, 20,80};
				jPanelContentLayout.columnWeights = new double[] {0.0, 0.1, 0.1};
				jPanelContentLayout.columnWidths = new int[] { 250,250,250 };
				jPanelContent.setLayout(jPanelContentLayout);
				jPanelContent.add(getJScrollPaneClassContent(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelContent.add(getJPanelDicContentOperations(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				//jPanelOperations = new JPanel();
				//add(jPanelOperations, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelContent.add(getJPanelTermsAndSyn(), new GridBagConstraints(1, 0, 2, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelContent.add(getJPanelTermOperation(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelContent.add(getJPanelSynOperation(), new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelContent.add(getJPanelAddClassDic(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelContent.add(getJPanelAddTerm(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelContent.add(getJPanelSynAdd(), new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelContent.add(getJButtonHelp(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				GridBagLayout jPanelOperationsLayout = new GridBagLayout();
				//jPanelOperations.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""),"Dictionary Operations", TitledBorder.LEADING,TitledBorder.TOP));
				jPanelOperationsLayout.rowWeights = new double[] { 0.1, 0.1,0.1 };
				jPanelOperationsLayout.rowHeights = new int[] { 7, 7, 7 };
				jPanelOperationsLayout.columnWeights = new double[] {0.1};
				jPanelOperationsLayout.columnWidths = new int[] {7};
				//jPanelOperations.setLayout(jPanelOperationsLayout);
			}
			this.setSize(900, 600);
		}

	}

//	protected void edit() {
//		Connection con = this.dictionary.getDb().getConnection();
//		
//		String editDicsql = "UPDATE resources "+
//					 "SET name=?, note=? "+
//					 "WHERE idresources='"+this.dictionary.getId()+"'";
//			
//		PreparedStatement editDic;
//		try {
//			editDic = con.prepareStatement(editDicsql);
//			editDic.setString(1,jTextPaneName.getText());
//			editDic.setString(2,jTextPaneNote.getText());
//			editDic.execute();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		dictionary.setNameDic(jTextPaneName.getText());
//		dictionary.setInfo(jTextPaneNote.getText());
//		new ShowMessagePopup("Dictionary Information Change");
//	}

	private DefaultComboBoxModel fillModelPubList() {
		DefaultComboBoxModel tableModel = new DefaultComboBoxModel();
		lineClassID = new HashMap<Integer, Integer>();
		int i=0;
		for (int classeID:classes.keySet()) {	
			tableModel.addElement(classes.get(classeID));
			lineClassID.put(i, classeID);
			i++;
		}

		return tableModel;
	}


	public void update(Observable arg0, Object arg1) {
		classes = dictionary.getDicionaryContentClasses();
		DefaultComboBoxModel jTableDicContentModel = fillModelPubList();
		jListDicContent.setModel(jTableDicContentModel);
		jTextPaneContentEdit.setText("");
		jButtonContentEdit.setEnabled(false);
		jTextPaneSynEdit.setText("");
		jButtonSynEdit.setEnabled(false);
		jTextPaneTermEdit.setText("");
		jButtonTermEdit.setEnabled(false);
		populateStats();
		
	}

	private JList getJListClassContent() {
		if (jListDicContent == null) {
			jListDicContent = new JList();
		}
		DefaultComboBoxModel jTableDicContentModel = fillModelPubList();
		jListDicContent.setModel(jTableDicContentModel);
		jListDicContent.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {
				changeTerms();
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
			}

			public void mouseReleased(MouseEvent arg0) {
			}
		});
		return jListDicContent;
	}

	protected void changeTerms() {

		terms = new TreeMap<String, Integer>();
		lineTerm = new TreeMap<Integer, Integer>();

		int classID = getClassID();
		if(classID==-1)
		{
			return;
		}
		String classe = classes.get(classID);
		terms = dictionary.getTerms(classe);
		String[] listTerms = new String[terms.size()];
		int i = 0;
		for (String termValue : terms.keySet()) {
			listTerms[i] = termValue;
			lineTerm.put(i, terms.get(termValue));
			i++;
		}
		ListModel jListTermsModel = new DefaultComboBoxModel(listTerms);
		jListTerms.setModel(jListTermsModel);
		ListModel jListTermsModel2 = new DefaultComboBoxModel();
		jListSyn.setModel(jListTermsModel2);
		jListSyn.updateUI();
		jListTerms.updateUI();
		jTextPaneContentEdit.setText("");
		jTextPaneSynEdit.setText("");
		jButtonSynEdit.setEnabled(false);
		jTextPaneTermEdit.setText("");
		jButtonTermEdit.setEnabled(false);
		jTextPaneContentEdit.setText(classe);
		jButtonContentEdit.setEnabled(true);
	}

	private int getClassID() {
		int selectClassConten = jListDicContent.getSelectedIndex();
		if(selectClassConten>-1)
		{
			return lineClassID.get(selectClassConten);
		}
		else
		{
			return -1;
		}
		
	}

	private JScrollPane getJScrollPaneClassContent() {
		if (jScrollPaneClassContent == null) {
			jScrollPaneClassContent = new JScrollPane();
			jScrollPaneClassContent.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Class Content",TitledBorder.LEADING, TitledBorder.TOP));
			jScrollPaneClassContent.setViewportView(getJListClassContent());
		}
		return jScrollPaneClassContent;
	}

	private JScrollPane getJScrollPaneTerms() {
		if (jScrollPaneTerms == null) {
			jScrollPaneTerms = new JScrollPane();
			jScrollPaneTerms.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Terms",TitledBorder.LEADING, TitledBorder.TOP));
			jScrollPaneTerms.setViewportView(getJListTerms());
		}
		return jScrollPaneTerms;
	}

	private JList getJListTerms() {
		if (jListTerms == null) {
			ListModel jListTermsModel = new DefaultComboBoxModel(new String[] {});
			jListTerms = new JList();
			jListTerms.setModel(jListTermsModel);
			jListTerms.addMouseListener(new MouseListener() {

				public void mouseClicked(MouseEvent arg0) {
					changeSyn();
				}

				public void mouseEntered(MouseEvent arg0) {
				}

				public void mouseExited(MouseEvent arg0) {
				}

				public void mousePressed(MouseEvent arg0) {
				}

				public void mouseReleased(MouseEvent arg0) {
				}
			});

		}
		return jListTerms;
	}

	protected void changeSyn() {
		if (jListTerms.getModel().getSize() > 0) {
			int jlistTermsIndex = jListTerms.getSelectedIndex();
			if (jlistTermsIndex != -1) {
				String term = (String) jListTerms.getSelectedValue();
				int termID = lineTerm.get(jlistTermsIndex);
				IResourceElementSet<IResourceElement> synElems = dictionary.getTermSynomns(termID);
				Set<String> syn = transformElementInSet(synElems);
				IResourceElement elemRes = dictionary.getTerm(termID);
				String originalTerm = elemRes.getTerm();
				syn.add(originalTerm);
				syn.remove(term);
				changeJListSyn(syn);
				jTextPaneSynEdit.setText("");
				jTextPaneTermEdit.setText(term);
				jButtonTermEdit.setEnabled(true);
				jButtonSynEdit.setEnabled(false);
			}
		}
	}

	private Set<String> transformElementInSet(
			IResourceElementSet<IResourceElement> synElems) {

		Set<String> elems = new HashSet<String>();
		for (IResourceElement elem : synElems.getElements()) {
			elems.add(elem.getTerm());
		}
		return elems;
	}

	private void changeJListSyn(Set<String> syns) {

		String[] listSyn = new String[syns.size()];
		int i = 0;
		for (String syn : syns) {
			listSyn[i] = syn;
			i++;
		}
		ListModel jListTermsModel = new DefaultComboBoxModel(listSyn);
		jListSyn.setModel(jListTermsModel);
		jListSyn.updateUI();
	}

	private JScrollPane getJScrollPaneSyn() {
		if (jScrollPaneSyn == null) {
			jScrollPaneSyn = new JScrollPane();
			jScrollPaneSyn.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createTitledBorder(""), "Synonyms",
					TitledBorder.LEADING, TitledBorder.TOP));
			jScrollPaneSyn.setViewportView(getJListSyn());
		}
		return jScrollPaneSyn;
	}

	private JList getJListSyn() {
		if (jListSyn == null) {
			ListModel jListSynModel = new DefaultComboBoxModel(new String[] {});
			jListSyn = new JList();
			jListSyn.setModel(jListSynModel);
			jListSyn.addMouseListener(new MouseListener() {

				public void mouseClicked(MouseEvent arg0) {
					sellectSyn();
				}

				public void mouseEntered(MouseEvent arg0) {
				}

				public void mouseExited(MouseEvent arg0) {
				}

				public void mousePressed(MouseEvent arg0) {
				}

				public void mouseReleased(MouseEvent arg0) {
				}
			});
		}
		return jListSyn;
	}
	
	
	

	protected void sellectSyn() {
		String value = jListSyn.getSelectedValue().toString();
		if(value!=null)
		{
			jTextPaneSynEdit.setText(value);
			jButtonSynEdit.setEnabled(true);
		}

		
	}

	private JPanel getJPanelDicContentOperations() {
		if (jPanelDicContentOperations == null) {
			jPanelDicContentOperations = new JPanel();
			jPanelDicContentOperations.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""),"Dictionary Update Class", TitledBorder.LEADING,TitledBorder.TOP));
			GridBagLayout jPanelDicContentOperationsLayout = new GridBagLayout();
			jPanelDicContentOperationsLayout.rowWeights = new double[] {0.0};
			jPanelDicContentOperationsLayout.rowHeights = new int[] {20};
			jPanelDicContentOperationsLayout.columnWeights = new double[] {0.8, 0.0 };
			jPanelDicContentOperationsLayout.columnWidths = new int[] { 7, 7 };
			jPanelDicContentOperations.setLayout(jPanelDicContentOperationsLayout);
			jPanelDicContentOperations.add(getJTextPaneContentEdit(),new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,0), 0, 0));
			jPanelDicContentOperations.add(getJButtonContentEdit(),new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,0), 0, 0));
		}
		return jPanelDicContentOperations;
	}

	private JTextPane getJTextPaneAddContentClassToDic() {
		if (jTextPaneAddContentClassToDic == null) {
			jTextPaneAddContentClassToDic = new JTextPane();
			jTextPaneAddContentClassToDic.setMinimumSize(new Dimension(60,30));
		}
		return jTextPaneAddContentClassToDic;
	}

	private JButton getJButtonAddContentClass() {
		if (jButtonAddContentClass == null) {
			jButtonAddContentClass = new JButton();
			jButtonAddContentClass.setText("Add Class");
			jButtonAddContentClass.setMinimumSize(new Dimension(60,25));
			jButtonAddContentClass.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					addClassContent();
				}
			});
		}
		return jButtonAddContentClass;
	}

	protected void addClassContent() {
		String newContent = jTextPaneAddContentClassToDic.getText();
		if (!newContent.equals("")) {
			((DefaultComboBoxModel) jListDicContent.getModel()).addElement(newContent);
			jTextPaneAddContentClassToDic.setText("");
			int element = dictionary.addElementClass(newContent);
			if (!(element == -1)) {
				dictionary.addResourceContent(newContent);
				classes.put(element, newContent);
				lineClassID.put(jListDicContent.getModel().getSize()- 1,element);
				jTextFieldNumberOfClasses.setText(String.valueOf(classes.size()));
				jTextPaneAddContentClassToDic.setText("");
				jListDicContent.updateUI();
				new ShowMessagePopup("Class Content Add !!!");
			}
		}
		else
		{
			Workbench.getInstance().warn("Class Enpty");
		}
	}

	private JPanel getJPanelSynOperation() {
		if (jPanelSynOperation == null) {
			jPanelSynOperation = new JPanel();
			jPanelSynOperation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""),"Dictionary Synonym Update", TitledBorder.LEADING,TitledBorder.TOP));
			GridBagLayout jPanelDicContentOperationsLayout = new GridBagLayout();
			jPanelDicContentOperationsLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelDicContentOperationsLayout.rowHeights = new int[] {20, 7, 7};
			jPanelDicContentOperationsLayout.columnWeights = new double[] {0.8, 0.0 };
			jPanelDicContentOperationsLayout.columnWidths = new int[] { 7, 7 };
			jPanelSynOperation.setLayout(jPanelDicContentOperationsLayout);
			jPanelSynOperation.add(getJTextPaneSynEdit(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSynOperation.add(getJButtonSynEdit(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelSynOperation;
	}

	private JTextPane getJTextPaneAddSyn() {
		if (jTextPaneAddSyn == null) {
			jTextPaneAddSyn = new JTextPane();
			jTextPaneAddSyn.setMinimumSize(new Dimension(60,30));
		}
		return jTextPaneAddSyn;
	}

	private JButton getJButtonAddSyn() {
		if (jButtonAddSyn == null) {
			jButtonAddSyn = new JButton();
			jButtonAddSyn.setText("Add Syn");
			jButtonAddSyn.setMinimumSize(new Dimension(60,25));
			jButtonAddSyn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					addSyn();
				}
			});
		}
		return jButtonAddSyn;
	}

	protected void addSyn() {
		int lineTerm = jListTerms.getSelectedIndex();
		if (lineTerm >= 0) {
			String newSyn = jTextPaneAddSyn.getText();
			if (!newSyn.equals("")) {	
				jTextPaneAddTerm.setText("");
				int termID = this.lineTerm.get(lineTerm);
				if(this.dictionary.addSynomns(new ResourceElement(termID,newSyn, 0, ""))) {
					((DefaultComboBoxModel) jListSyn.getModel()).addElement(newSyn);
					jTextFieldSyn.setText(String.valueOf(++numberSyn));
					new ShowMessagePopup("Synonym Added !!!");
				}
				jListSyn.updateUI();
			}
			else
			{
				Workbench.getInstance().warn("Empty Synonym Insert");
			}
			jTextPaneAddSyn.setText("");
			
		} else 
		{
			Workbench.getInstance().warn("Please Select Term");
		}
	}

	private JPanel getJPanelTermOperation() {
		if (jPanelTermOperation == null) {
			jPanelTermOperation = new JPanel();
			jPanelTermOperation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Dictionary Terms Update",TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelTermOperationLayout = new GridBagLayout();
			jPanelTermOperationLayout.rowWeights = new double[] {0.1, 0.1, 0.0};
			jPanelTermOperationLayout.rowHeights = new int[] {20, 7, 7};
			jPanelTermOperationLayout.columnWeights = new double[] { 0.1, 0.8,0.0 };
			jPanelTermOperationLayout.columnWidths = new int[] { 20, 7, 7 };
			jPanelTermOperation.setLayout(jPanelTermOperationLayout);
			jPanelTermOperation.add(getJTextPaneSearch(), new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermOperation.add(getJLabelSerach(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermOperation.add(getJCheckBoxCaseSensitive(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermOperation.add(getJTextPaneTermEdit(),new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,0), 0, 0));
			jPanelTermOperation.add(getJButtonTermEdit(),new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,0), 0, 0));
		}
		return jPanelTermOperation;
	}

	private JTextPane getJTextPaneAddTerm() {
		if (jTextPaneAddTerm == null) {
			jTextPaneAddTerm = new JTextPane();
			jTextPaneAddTerm.setMinimumSize(new Dimension(60,30));
		}
		return jTextPaneAddTerm;
	}

	private JButton getJButtonAddTerm() {
		if (jButtonAddTerm == null) {
			jButtonAddTerm = new JButton();
			jButtonAddTerm.setText("Add Term");
			jButtonAddTerm.setMinimumSize(new Dimension(60,25));
			jButtonAddTerm.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					addTerm();
				}
			});
		}
		return jButtonAddTerm;
	}

	protected void addTerm() {
		int lineClass = jListDicContent.getSelectedIndices()[0];
		if (lineClass >= 0) {
			String newtERMS = jTextPaneAddTerm.getText();
			if (!newtERMS.equals("")) {
				((DefaultComboBoxModel) jListTerms.getModel()).addElement(newtERMS);
				jTextPaneAddTerm.setText("");
				int classID = lineClassID.get(lineClass);
				if (this.dictionary.addElement(new ResourceElement(-1,newtERMS, classID, ""))) {
					int nextElem = HelpDatabase.getNextInsertTableID(dictionary.getDb(), "resource_elements");
					terms.put(newtERMS, nextElem - 1);
					lineTerm.put(jListTerms.getModel().getSize()-1, nextElem-1);
					jListTerms.updateUI();
					jTextFieldTerms.setText(String.valueOf(++numberTerms));
					new ShowMessagePopup("Term Added !!!");
				}
			}
			else
			{
				Workbench.getInstance().warn("Empty Term Insert");
			}
			jTextPaneAddTerm.setText("");
		} else {
			Workbench.getInstance().warn("You must select a Class first");
		}
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

		for (int i = 0; i < jListTerms.getModel().getSize(); i++) {

			if (((String) jListTerms.getModel().getElementAt(i)).toLowerCase().contains(text.toLowerCase()))
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

		for (int i = 0; i < jListTerms.getModel().getSize(); i++) {

			if (((String) jListTerms.getModel().getElementAt(i)).contains(text))
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

		this.jListTerms.setSelectionMode(selectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.jListTerms.setSelectionModel(selectionModel);
		if (selectionModel.isSelectionEmpty()&& (jTextPaneSearch.getText().compareTo("") != 0)) {
			jTextPaneSearch.setForeground(new java.awt.Color(255, 0, 0));
			jTextPaneSearch.setBackground(new java.awt.Color(174, 174, 174));
		} else {
			this.jTextPaneSearch.setForeground(Color.BLACK);
			this.jTextPaneSearch.setBackground(Color.WHITE);
		}
		if (rows.size() > 0) {

			this.jListTerms.scrollRectToVisible(jListTerms.getCellBounds(rows.get(0), rows.get(0)));
		}
	}

	private JLabel getJLabelSerach() {
		if (jLabelSerach == null) {
			jLabelSerach = new JLabel();
			jLabelSerach.setText("Search");
			
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

	private JTextPane getJTextPaneContentEdit() {
		if (jTextPaneContentEdit == null) {
			jTextPaneContentEdit = new JTextPane();
			jTextPaneContentEdit.setMinimumSize(new Dimension(60,30));
		}
		return jTextPaneContentEdit;
	}

	private JButton getJButtonContentEdit() {
		if (jButtonContentEdit == null) {
			jButtonContentEdit = new JButton();
			jButtonContentEdit.setText("Update");
			jButtonContentEdit.setMinimumSize(new Dimension(60,40));
			jButtonContentEdit.setEnabled(false);
			jButtonContentEdit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					editContent();
				}
			});
		}
		return jButtonContentEdit;
	}

	protected void editContent() {
		
	
		String editContent = jTextPaneContentEdit.getText();
		if(!editContent.equals(""))
		{
			int contetID = getClassID();
			Connection con = this.dictionary.getDb().getConnection();

			String editDicsql = "UPDATE classes "+
			"SET class=?"+
			"WHERE idclasses=? ";
			
			PreparedStatement editDic;
			try {
				editDic = con.prepareStatement(editDicsql);
				editDic.setString(1,editContent);
				editDic.setInt(2,contetID);
				editDic.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			int selectedIndex = jListDicContent.getSelectedIndex();
			((DefaultComboBoxModel) jListDicContent.getModel()).insertElementAt(editContent,selectedIndex);
			((DefaultComboBoxModel) jListDicContent.getModel()).removeElementAt(selectedIndex+1);
			classes.put(contetID, editContent);
			new ShowMessagePopup("Class Content Update !!!");
			jListDicContent.updateUI();
		}
		else
		{
			Workbench.getInstance().warn("Class Content Empty");
		}
	}

	private JTextPane getJTextPaneTermEdit() {
		if (jTextPaneTermEdit == null) {
			jTextPaneTermEdit = new JTextPane();
			jTextPaneTermEdit.setMinimumSize(new Dimension(60,30));
		}
		return jTextPaneTermEdit;
	}

	private JButton getJButtonTermEdit() {
		if (jButtonTermEdit == null) {
			jButtonTermEdit = new JButton();
			jButtonTermEdit.setText("Update");
			jButtonTermEdit.setMinimumSize(new Dimension(60,40));
			jButtonTermEdit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					editTerm();
				}
			});
		}
		return jButtonTermEdit;
	}

	protected void editTerm() {
		String newEditTerm = jTextPaneTermEdit.getText();
		if(!newEditTerm.equals(""))
		{
			int termID = getTermIDselected();
			int classID = getClassID();
			getTermEdit(termID, classID, newEditTerm);
			int selectedIndex = jListTerms.getSelectedIndex();
			((DefaultComboBoxModel) jListTerms.getModel()).insertElementAt(newEditTerm,selectedIndex);
			((DefaultComboBoxModel) jListTerms.getModel()).removeElementAt(selectedIndex+1);
			jListTerms.updateUI();
			new ShowMessagePopup("Term Update !!!");
		}
		else
		{
			Workbench.getInstance().warn("Term Update Empty");
		}

	}

	private void getTermEdit(int termID, int classID, String newEditTerm) {
		Connection con = this.dictionary.getDb().getConnection();
		
		String editTerm = "UPDATE resource_elements "+
					 "SET element=?"+
					 "WHERE idresource_elements=? AND resources_idresources=? AND classes_idclasses=? ";
			
		PreparedStatement editDic;
		try {
			editDic = con.prepareStatement(editTerm);
			editDic.setString(1,newEditTerm);
			editDic.setInt(2,termID);
			editDic.setInt(3,dictionary.getId());
			editDic.setInt(4,classID);
			editDic.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void editSynInDB(int termID, String element, String newEditTerm) {
		Connection con = this.dictionary.getDb().getConnection();
		
		String editSyn = "UPDATE synonyms "+
					 "SET synonym=?"+
					 "WHERE resource_elements_idresource_elements=? AND synonym=?";
			
		PreparedStatement editDic;
		try {
			editDic = con.prepareStatement(editSyn);
			editDic.setString(1,newEditTerm);
			editDic.setInt(2,termID);
			editDic.setString(3,element);
			editDic.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private int getTermIDselected() {
		int selectedTermLine = jListTerms.getSelectedIndex();
		int termID = lineTerm.get(selectedTermLine);
		return termID;
	}

	private JTextPane getJTextPaneSynEdit() {
		if (jTextPaneSynEdit == null) {
			jTextPaneSynEdit = new JTextPane();
			jTextPaneSynEdit.setMinimumSize(new Dimension(60,30));
		}
		return jTextPaneSynEdit;
	}

	private JButton getJButtonSynEdit() {
		if (jButtonSynEdit == null) {
			jButtonSynEdit = new JButton();
			jButtonSynEdit.setText("Update");
			jButtonSynEdit.setMinimumSize(new Dimension(60,40));
			jButtonSynEdit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					editSyn();
				}
			});
		}
		return jButtonSynEdit;
	}

	protected void editSyn() {
		String newEditTerm = jTextPaneSynEdit.getText();
		if(!newEditTerm.equals(""))
		{
			int termID = getTermIDselected();
			String element = jListSyn.getSelectedValue().toString();
			editSynInDB(termID, element, newEditTerm);
			int selectedIndex = jListSyn.getSelectedIndex();
			((DefaultComboBoxModel) jListSyn.getModel()).insertElementAt(newEditTerm,selectedIndex);
			((DefaultComboBoxModel) jListSyn.getModel()).removeElementAt(selectedIndex+1);
			jListSyn.updateUI();
			new ShowMessagePopup("Synonym Update !!!");
		}
		else
		{
			Workbench.getInstance().warn("Synonym Update Empty");
		}
		
	}
	
	private JPanel getJPanelTermsAndSyn() {
		if(jPanelTermsAndSyn == null) {
			jPanelTermsAndSyn = new JPanel();
			GridBagLayout jPanelTermsAndSynLayout = new GridBagLayout();
			jPanelTermsAndSynLayout.rowWeights = new double[] {0.1, 0.05};
			jPanelTermsAndSynLayout.rowHeights = new int[] {7, 7};
			jPanelTermsAndSynLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelTermsAndSynLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelTermsAndSyn.setLayout(jPanelTermsAndSynLayout);
			jPanelTermsAndSyn.add(getJScrollPaneTerms(), new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermsAndSyn.add(getJScrollPaneSyn(), new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelTermsAndSyn;
	}
	
	private JPanel getJPanelDicStats() {
		if(jPanelDicStats == null) {
			jPanelDicStats = new JPanel();
			//jPanelDicStats.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Dictionary Statitics",TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelDicStatsLayout = new GridBagLayout();
			jPanelDicStatsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelDicStatsLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelDicStatsLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.1};
			jPanelDicStatsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelDicStats.setLayout(jPanelDicStatsLayout);
			jPanelDicStats.add(getJLabelTerms(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJLabelSyn(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJTextFieldTerms(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJTextFieldSyn(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJLabelAveregeSynForTerm(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJTextFieldAverageTermsForSyn(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJLabelClassesContent(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJTextFieldNumberOfClasses(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJScrollPaneDicContentStats(), new GridBagConstraints(2, 0, 2, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelDicStats;
	}
	
	private JLabel getJLabelTerms() {
		if(jLabelTerms == null) {
			jLabelTerms = new JLabel();
			jLabelTerms.setText("Terms");
		}
		return jLabelTerms;
	}
	
	private JLabel getJLabelSyn() {
		if(jLabelSyn == null) {
			jLabelSyn = new JLabel();
			jLabelSyn.setText("Synonyms");
		}
		return jLabelSyn;
	}
	
	private JTextField getJTextFieldTerms() {
		if(jTextFieldTerms == null) {
			jTextFieldTerms = new JTextField();
			jTextFieldTerms.setMinimumSize(new Dimension(60,25));
			numberTerms = dictionary.getnumberTerms();
			jTextFieldTerms.setText(String.valueOf(numberTerms));
			jTextFieldTerms.setEditable(false);
		}
		return jTextFieldTerms;
	}
	
	private JTextField getJTextFieldSyn() {
		if(jTextFieldSyn == null) {
			jTextFieldSyn = new JTextField();
			jTextFieldSyn.setMinimumSize(new Dimension(60,25));
			jTextFieldSyn.setEditable(false);
			numberSyn = dictionary.getNumberSynonyms();
			jTextFieldSyn.setText(String.valueOf(numberSyn));
		}
		return jTextFieldSyn;
	}
	
	private JLabel getJLabelAveregeSynForTerm() {
		if(jLabelAveregeSynForTerm == null) {
			jLabelAveregeSynForTerm = new JLabel();
			jLabelAveregeSynForTerm.setText("Average Term/Syn");
		}
		return jLabelAveregeSynForTerm;
	}
	
	private JTextField getJTextFieldAverageTermsForSyn() {
		if(jTextFieldAverageTermsForSyn == null) {
			jTextFieldAverageTermsForSyn = new JTextField();
			jTextFieldAverageTermsForSyn.setMinimumSize(new Dimension(60,25));
			jTextFieldAverageTermsForSyn.setEditable(false);
			float averageTermsSyn = (float) numberSyn /(float) numberTerms ;
			jTextFieldAverageTermsForSyn.setText(dec.format(averageTermsSyn));	
		}
		return jTextFieldAverageTermsForSyn;
	}
	
	private JLabel getJLabelClassesContent() {
		if(jLabelClassesContent == null) {
			jLabelClassesContent = new JLabel();
			jLabelClassesContent.setText("Number of classes :");
		}
		return jLabelClassesContent;
	}
	
	private JTextField getJTextFieldNumberOfClasses() {
		if(jTextFieldNumberOfClasses == null) {
			jTextFieldNumberOfClasses = new JTextField();
			jTextFieldNumberOfClasses.setMinimumSize(new Dimension(60,25));
			jTextFieldNumberOfClasses.setEditable(false);
			jTextFieldNumberOfClasses.setText(String.valueOf(classes.size()));
		}
		return jTextFieldNumberOfClasses;
	}
	
	private JScrollPane getJScrollPaneDicContentStats() {
		if(jScrollPaneDicContentStats == null) {
			jScrollPaneDicContentStats = new JScrollPane();
			jScrollPaneDicContentStats.setViewportView(getJTableDicContent());
		}
		return jScrollPaneDicContentStats;
	}

	private JTable getJTableDicContent() {
		if(jTableDicContent == null) {
			TableModel jTableDicContentModel = getTableContenStatsModel();
			jTableDicContent = new JTable(){
				private static final long serialVersionUID = -4090662450740771673L;

				public boolean isCellEditable(int x,int y)
				{
					return false;
				}
			};
			jTableDicContent.setModel(jTableDicContentModel);
		}
		return jTableDicContent;
	}
	
	private TableModel getTableContenStatsModel()
	{
		String[] columns = new String[] {"Class", "Terms","Synonyms"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);	
		Object[] data;
		for(int classeID:classes.keySet() )
		{
			
			data = dictionary.getClassContentStats(classeID);
			data[0] = classes.get(classeID);
			tableModel.addRow(data);
		}
		
		return tableModel;
	}
	
	private void populateStats() {
		if(dictionary!=null)
		{
			jTextFieldTerms.setText(String.valueOf(dictionary.getnumberTerms()));
			jTextFieldSyn.setText(String.valueOf(dictionary.getNumberSynonyms()));
			float averageTermsSyn = (float)dictionary.getnumberTerms() / (float)dictionary.getNumberSynonyms() ;
			jTextFieldAverageTermsForSyn.setText(dec.format(averageTermsSyn));	
			jTextFieldNumberOfClasses.setText(String.valueOf(dictionary.getClassContent().size()));
			jTableDicContent.setModel(getTableContenStatsModel());
			jPanelDicStats.updateUI();
		}
		
	}
	
	private JPanel getJPanelAddClassDic() {
		if(jPanelAddClassDic == null) {
			jPanelAddClassDic = new JPanel();
			jPanelAddClassDic.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""),"New Dictionary Class", TitledBorder.LEADING,TitledBorder.TOP));
			GridBagLayout jPanelAddClassDicLayout = new GridBagLayout();
			jPanelAddClassDicLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelAddClassDicLayout.rowHeights = new int[] {7, 7, 7};
			jPanelAddClassDicLayout.columnWeights = new double[] {0.1, 0.0};
			jPanelAddClassDicLayout.columnWidths = new int[] {7, 7};
			jPanelAddClassDic.setLayout(jPanelAddClassDicLayout);
			jPanelAddClassDic.add(getJButtonAddContentClass(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelAddClassDic.add(getJTextPaneAddContentClassToDic(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelAddClassDic;
	}
	
	private JPanel getJPanelAddTerm() {
		if(jPanelAddTerm == null) {
			jPanelAddTerm = new JPanel();
			jPanelAddTerm.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""),"New Dictionary Term", TitledBorder.LEADING,TitledBorder.TOP));

			GridBagLayout jPanelAddTermLayout = new GridBagLayout();
			jPanelAddTermLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelAddTermLayout.rowHeights = new int[] {7, 7, 7};
			jPanelAddTermLayout.columnWeights = new double[] {0.1, 0.0};
			jPanelAddTermLayout.columnWidths = new int[] {7, 7};
			jPanelAddTerm.setLayout(jPanelAddTermLayout);
			jPanelAddTerm.add(getJButtonAddTerm(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelAddTerm.add(getJTextPaneAddTerm(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelAddTerm;
	}
	
	private JPanel getJPanelSynAdd() {
		if(jPanelSynAdd == null) {
			jPanelSynAdd = new JPanel();
			jPanelSynAdd.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""),"New Term Synonym", TitledBorder.LEADING,TitledBorder.TOP));
			GridBagLayout jPanelSynAddLayout = new GridBagLayout();
			jPanelSynAddLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelSynAddLayout.rowHeights = new int[] {7, 7, 7};
			jPanelSynAddLayout.columnWeights = new double[] {0.1, 0.0};
			jPanelSynAddLayout.columnWidths = new int[] {7, 7};
			jPanelSynAdd.setLayout(jPanelSynAddLayout);
			jPanelSynAdd.add(getJButtonAddSyn(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSynAdd.add(getJTextPaneAddSyn(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelSynAdd;
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"Dictionary_Management_Content");
				}
			});
		}
		return jButtonHelp;
	}

}
