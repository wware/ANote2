package pt.uminho.anote2.aibench.resources.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.resources.datatypes.Resources;
import pt.uminho.anote2.aibench.resources.gui.dictionaryview.DictionaryViewAddClassGUI;
import pt.uminho.anote2.aibench.resources.gui.dictionaryview.DictionaryViewAddExternaIDGUI;
import pt.uminho.anote2.aibench.resources.gui.dictionaryview.DictionaryViewAddSynonymGUI;
import pt.uminho.anote2.aibench.resources.gui.dictionaryview.DictionaryViewAddTermGUI;
import pt.uminho.anote2.aibench.resources.gui.dictionaryview.DictionaryViewEditClassGUI;
import pt.uminho.anote2.aibench.resources.gui.dictionaryview.DictionaryViewEditSynonymGUI;
import pt.uminho.anote2.aibench.resources.gui.dictionaryview.DictionaryViewEditTermGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfoSmall;
import pt.uminho.anote2.datastructures.utils.conf.OtherConfigurations;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import pt.uminho.generic.genericpanel.view.JPanelWait;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;

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
	private JTextField jTextPaneSearch;
	private JPanel jPanelTermsAndSyn;
	private JList jListDicContent;
	private JPanelWait jPanelSearch;
	private JPanel jPanelSearchandInfoPanel;
	private JComboBox jComboBoxExternalID;
	private JPanel jPanelExternalID;
	private JButton jButtonHelp;
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
	private DictionaryAibench dictionary;
	private SortedMap<String, Integer> terms; // Term, termID
	private Map<Integer, Integer> lineTerm; // line, TermID
	private Map<Integer, Integer> termIDLine; // line, TermID
	private JCheckBox jCheckBoxSynonymsSearch;
	private JButton jButtonSearchResulsBack;
	private JButton jButtonSearchResultsNext;
	private JLabel jLabelSearchResultInfo;
	private JPanel jPanelSearchResults;
	private JButton jButtonSearchButton;
	private JCheckBox jCheckBoxMatchWholeWordOnly;
	private Map<Integer,Set<String>> termIDSynonyms;
	private JPanel jPanelClassContent;
	private JPopupMenu jPopupMenuClassContent;
	private JPopupMenu jPopupMenuSynonyms;
	private JPopupMenu jPopupMenuTerm;
	private JLabel jLabelAveregeSynForTerm;
	private int numberSyn;
	private int numberTerms;
	DecimalFormat dec = new DecimalFormat("0.00");
	private SortedMap<Integer, String> classes; // classeID,classe
	private Map<Integer, Integer> lineClassID; // lineNumber/classID
	private List<Integer> rowsMatching;
	private int selectedIndex;
	private boolean removeClass = false;

	public DictionaryView(DictionaryAibench dictionary) {
		this.dictionary = dictionary;
		try {
			classes = Resources.getResourceContentClasses(dictionary.getID());
			termIDSynonyms = new HashMap<Integer, Set<String>>();
			initGUI();
			this.dictionary.addObserver(this);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}

	}

	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1};
			thisLayout.rowHeights = new int[] {700};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			{
				jPanelContent = new JPanel();
				GridBagLayout jPanelContentLayout = new GridBagLayout();
				jPanelContent.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""),"Dictionary Content", TitledBorder.LEADING,TitledBorder.TOP));
				this.add(jPanelContent, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelContentLayout.rowWeights = new double[] {0.5, 0.3, 0.1, 0.1};
				jPanelContentLayout.rowHeights = new int[] {7, 7, 7, 7};
				jPanelContentLayout.columnWeights = new double[] {0.3, 0.7};
				jPanelContentLayout.columnWidths = new int[] {7, 7};
				jPanelContent.setLayout(jPanelContentLayout);
				jPanelContent.add(getJPanelClassContent(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jPanelContent.add(getJPanelTermsAndSyn(), new GridBagConstraints(1, 0, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelContent.add(getJPanelExternalID(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelContent.add(getJPanelSearchandInfoPanel(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelContent.add(getJPanelSearch(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelContent.add(getDictionaryStatistics(), new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jPanelContent.add(getJButtonHelp(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				GridBagLayout jPanelOperationsLayout = new GridBagLayout();
				jPanelOperationsLayout.rowWeights = new double[] { 0.1, 0.1,0.1 };
				jPanelOperationsLayout.rowHeights = new int[] { 7, 7, 7 };
				jPanelOperationsLayout.columnWeights = new double[] {0.1};
				jPanelOperationsLayout.columnWidths = new int[] {7};
			}
		}
	}

	private JPanel getDictionaryStatistics() throws SQLException, DatabaseLoadDriverException {
		if(jPanelDicInfo==null)
		{
			jPanelDicInfo = new JPanel();
			GridBagLayout jPanelDicInfoLayout = new GridBagLayout();
			jPanelDicInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""),"Information and "+GlobalTextInfoSmall.statistics, TitledBorder.LEADING,TitledBorder.TOP));
			jPanelDicInfoLayout.rowWeights = new double[] {0.1};
			jPanelDicInfoLayout.rowHeights = new int[] {7};
			jPanelDicInfoLayout.columnWeights = new double[] {0.025, 0.1};
			jPanelDicInfoLayout.columnWidths = new int[] {7, 7};
			jPanelDicInfo.setLayout(jPanelDicInfoLayout);
			jPanelDicInfo.add(getJPanelDicStats(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicInfo.add(getJScrollPaneDicContentStats(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelDicInfo;
	}

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
		try {
			classes = Resources.getResourceContentClasses(dictionary.getID());
			DefaultComboBoxModel jTableDicContentModel = fillModelPubList();
			if(removeClass)
			{
				cleanGUI(jTableDicContentModel);
				removeClass = false;
			}
			else if(jListDicContent.getSelectedIndices().length>0)
			{
				int index = jListDicContent.getSelectedIndex();
				jListDicContent.setModel(jTableDicContentModel);
				jListDicContent.setSelectedIndex(index);
				jListDicContent.updateUI();
			}
			else
			{
				cleanGUI(jTableDicContentModel);
			}
			jPanelSearchResults.setVisible(false);
			populateStats();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
		
	}

	private void cleanGUI(DefaultComboBoxModel jTableDicContentModel) {
		jListDicContent.setModel(jTableDicContentModel);
		jListDicContent.updateUI();
		ListModel jListTermsModel = new DefaultComboBoxModel();
		jListTerms.setModel(jListTermsModel);
		ListModel jListTermsModel2 = new DefaultComboBoxModel();
		jListSyn.setModel(jListTermsModel2);
		jListSyn.updateUI();
		jListTerms.updateUI();
	}

	private JList getJListClassContent() {
		if (jListDicContent == null) {
			jListDicContent = new JList();
			jListDicContent.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
			setComponentPopupMenu(jListDicContent, getJPopupMenuClassContent());
			DefaultComboBoxModel jTableDicContentModel = fillModelPubList();
			jListDicContent.setModel(jTableDicContentModel);
			jListDicContent.addMouseListener(new MouseListener() {

				public void mouseClicked(MouseEvent arg0) {
					try {
						changeTerms();
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}

				public void mouseEntered(MouseEvent arg0) {}

				public void mouseExited(MouseEvent arg0) {}

				public void mousePressed(MouseEvent arg0) {}

				public void mouseReleased(MouseEvent arg0) {}
			});
			jListDicContent.addKeyListener(new KeyListener() {

				public void keyTyped(KeyEvent arg0) {	}

				public void keyReleased(KeyEvent arg0) {	}

				public void keyPressed(KeyEvent arg0) {
					try {
						changeTerms();
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
		}
		return jListDicContent;
	}

	protected void changeTerms() throws SQLException, DatabaseLoadDriverException {

		terms = new TreeMap<String, Integer>();
		lineTerm = new TreeMap<Integer, Integer>();
		termIDLine = new HashMap<Integer, Integer>();
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
			termIDLine.put(terms.get(termValue), i);
			i++;
		}
		ListModel jListTermsModel = new DefaultComboBoxModel(listTerms);
		jListTerms.setModel(jListTermsModel);
		ListModel jListTermsModel2 = new DefaultComboBoxModel();
		jListSyn.setModel(jListTermsModel2);
		jListSyn.updateUI();
		jListTerms.updateUI();
		jPanelSearchResults.setVisible(false);
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
			jScrollPaneClassContent.setPreferredSize(new java.awt.Dimension(44, 21));
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
			jListTerms.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
			setComponentPopupMenu(jListTerms, getTermPopupMenu());
			jListTerms.setModel(jListTermsModel);
			jListTerms.addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent arg0) {
					try {
						changeSyn();
						changeExternalID();

					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}

				public void mouseEntered(MouseEvent arg0) {}

				public void mouseExited(MouseEvent arg0) {}

				public void mousePressed(MouseEvent arg0) {}

				public void mouseReleased(MouseEvent arg0) {}
			});
			jListTerms.addKeyListener(new KeyListener() {
				
				public void keyTyped(KeyEvent e) {}
				
				public void keyReleased(KeyEvent e) {				}
				
				public void keyPressed(KeyEvent e) {
					try {

						changeSyn();
						changeExternalID();		
					} catch (SQLException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					} catch (DatabaseLoadDriverException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					}
				}
			});
		    // Attach a mouse motion adapter to let us know the mouse is over an item and to show the tip.
			jListTerms.addMouseMotionListener( new MouseMotionAdapter() {
				public void mouseMoved( MouseEvent e) {
					JList theList = (JList) e.getSource();
					int index = theList.locationToIndex(e.getPoint());
					if (index > -1) {
						IResourceElementSet<IResourceElement> synElems;
						try {
							synElems = dictionary.getTermSynomns(lineTerm.get(index));
							List<IExternalID> externalIDs = dictionary.getexternalIDandSorceIDandSource(lineTerm.get(index));
							if(synElems.size() + externalIDs.size() > 0)
							{
								String html = new String("<html><center>");
								if(synElems.size() > 0)
								{
									html = html + "<b><font size=\"10px\">Synonyms:</b><br/>";
									Set<IResourceElement> elem = synElems.getElements();
									for(IResourceElement el:elem)
									{
										html = html + "<font size=\"9px\">"+el.getTerm()+"<br/>";
									}
									html = html + "<br/>";
								}
								if(externalIDs.size()>0)
								{
									html = html + "<b><font size=\"10px\">External Database Id's:</b><br/>";
									for(IExternalID externalID:externalIDs)
									{
										html = html + "<font size=\"9px\">"+externalID.getExternalID()+" <b> ( "+externalID.getSource()+")</b><br/>";
									}
									html = html + "<br/>";
								}
								theList.setToolTipText(html+"</center><html>");
							}
							else
							{
								theList.setToolTipText("<html>No information available.<html>");

							}
						} catch (SQLException e1) {
							TreatExceptionForAIbench.treatExcepion(e1);
						} catch (DatabaseLoadDriverException e1) {
							TreatExceptionForAIbench.treatExcepion(e1);

						}

					}
				}
			});

		}
		return jListTerms;
	}

	protected void changeSyn() throws SQLException, DatabaseLoadDriverException {
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
			}
		}
	}
	
	private void changeExternalID() throws SQLException, DatabaseLoadDriverException {
		if (jListTerms.getModel().getSize() > 0) {
			int jlistTermsIndex = jListTerms.getSelectedIndex();
			if (jlistTermsIndex != -1) {
				int termID = lineTerm.get(jlistTermsIndex);
				List<IExternalID> list = dictionary.getexternalIDandSorceIDandSource(termID);
				DefaultComboBoxModel amodel = new DefaultComboBoxModel();
				for(IExternalID id:list)
					amodel.addElement(id.getExternalID()+" ("+id.getSource()+")");
				jComboBoxExternalID.setModel(amodel);
			}
			else
			{
				DefaultComboBoxModel amodel = new DefaultComboBoxModel();
				jComboBoxExternalID.setModel(amodel);
			}
		}
		else
		{
			DefaultComboBoxModel amodel = new DefaultComboBoxModel();
			jComboBoxExternalID.setModel(amodel);
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
			jListSyn.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
			setComponentPopupMenu(jListSyn, getJPopupMenuSynonyms());
			jListSyn.setModel(jListSynModel);		
		}
		return jListSyn;
	}


	private JTextField getJTextPaneSearch() {
		if (jTextPaneSearch == null) {
			jTextPaneSearch = new JTextField();
			jTextPaneSearch.setMinimumSize(new Dimension(60,30));
			jTextPaneSearch.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED), null));
		}
		return jTextPaneSearch;
	}
	
	
	private JCheckBox getJCheckBoxCaseSensitive() {
		if (jCheckBoxCaseSensitive == null) {
			jCheckBoxCaseSensitive = new JCheckBox();
			jCheckBoxCaseSensitive.setSelected(true);
			jCheckBoxCaseSensitive.setText("Match Case");
		}
		return jCheckBoxCaseSensitive;
	}
	
	private JPanel getJPanelTermsAndSyn() {
		if(jPanelTermsAndSyn == null) {
			jPanelTermsAndSyn = new JPanel();
			GridBagLayout jPanelTermsAndSynLayout = new GridBagLayout();
			jPanelTermsAndSynLayout.rowWeights = new double[] {0.65, 0.35};
			jPanelTermsAndSynLayout.rowHeights = new int[] {7, 7};
			jPanelTermsAndSynLayout.columnWeights = new double[] {0.1};
			jPanelTermsAndSynLayout.columnWidths = new int[] {7};
			jPanelTermsAndSyn.setLayout(jPanelTermsAndSynLayout);
			jPanelTermsAndSyn.add(getJScrollPaneTerms(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermsAndSyn.add(getJScrollPaneSyn(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));		}
		return jPanelTermsAndSyn;
	}
	
	private JPanel getJPanelDicStats() throws SQLException, DatabaseLoadDriverException {
		if(jPanelDicStats == null) {
			jPanelDicStats = new JPanel();
			GridBagLayout jPanelDicStatsLayout = new GridBagLayout();
			jPanelDicStatsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelDicStatsLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelDicStatsLayout.columnWeights = new double[] {0.0, 0.05};
			jPanelDicStatsLayout.columnWidths = new int[] {7, 7};
			jPanelDicStats.setLayout(jPanelDicStatsLayout);
			jPanelDicStats.add(getJLabelTerms(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJLabelSyn(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJTextFieldTerms(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJTextFieldSyn(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJLabelAveregeSynForTerm(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJTextFieldAverageTermsForSyn(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJLabelClassesContent(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJTextFieldNumberOfClasses(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
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
	
	private JTextField getJTextFieldTerms() throws SQLException, DatabaseLoadDriverException {
		if(jTextFieldTerms == null) {
			jTextFieldTerms = new JTextField();
			numberTerms = Resources.getnumberTerms(dictionary.getID());
			jTextFieldTerms.setText(String.valueOf(numberTerms));
			jTextFieldTerms.setEditable(false);
		}
		return jTextFieldTerms;
	}
	
	private JTextField getJTextFieldSyn() throws DatabaseLoadDriverException, SQLException {
		if(jTextFieldSyn == null) {
			jTextFieldSyn = new JTextField();
			jTextFieldSyn.setMinimumSize(new Dimension(60,25));
			jTextFieldSyn.setEditable(false);
			numberSyn = Resources.getNumberSynonyms(dictionary.getID());
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
	
	private JScrollPane getJScrollPaneDicContentStats() throws SQLException, DatabaseLoadDriverException {
		if(jScrollPaneDicContentStats == null) {
			jScrollPaneDicContentStats = new JScrollPane();
			jScrollPaneDicContentStats.setViewportView(getJTableDicContent());
		}
		return jScrollPaneDicContentStats;
	}

	private JTable getJTableDicContent() throws SQLException, DatabaseLoadDriverException {
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
	
	private TableModel getTableContenStatsModel() throws SQLException, DatabaseLoadDriverException
	{
		String[] columns = new String[] {"Class", "Terms","Synonyms"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);	
		Object[] data;
		for(int classeID:classes.keySet() )
		{		
			data = Resources.getClassContentStats(dictionary.getID(),classeID);
			data[0] = classes.get(classeID);
			tableModel.addRow(data);
		}		
		return tableModel;
	}
	
	private void populateStats() throws SQLException, DatabaseLoadDriverException {
		if(dictionary!=null)
		{
			int terms = Resources.getnumberTerms(dictionary.getID());
			int synN = Resources.getNumberSynonyms(dictionary.getID());
			jTextFieldTerms.setText(String.valueOf(terms));
			jTextFieldSyn.setText(String.valueOf(synN));
			float averageTermsSyn = (float) synN / (float) terms ;
			jTextFieldAverageTermsForSyn.setText(dec.format(averageTermsSyn));	
			jTextFieldNumberOfClasses.setText(String.valueOf(dictionary.getClassContent().size()));
			jTableDicContent.setModel(getTableContenStatsModel());
			jPanelDicStats.updateUI();
		}
	}

	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						Help.internetAccess(GlobalOptions.wikiGeneralLink+"Dictionary_Management_Content");
					} catch (IOException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					}
				}
			});
		}
		return jButtonHelp;
	}
	
	private JPanel getJPanelExternalID() {
		if(jPanelExternalID == null) {
			jPanelExternalID = new JPanel();
			GridBagLayout jPanelExternalIDLayout = new GridBagLayout();
			jPanelExternalID.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""),"External ID (Source)", TitledBorder.LEADING,TitledBorder.TOP));
			jPanelExternalIDLayout.rowWeights = new double[] {0.1};
			jPanelExternalIDLayout.rowHeights = new int[] {7};
			jPanelExternalIDLayout.columnWeights = new double[] {0.1};
			jPanelExternalIDLayout.columnWidths = new int[] {7};
			jPanelExternalID.setLayout(jPanelExternalIDLayout);
			jPanelExternalID.add(getJTextPaneExternalID(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		}
		return jPanelExternalID;
	}
	
	private JComboBox getJTextPaneExternalID() {
		if(jComboBoxExternalID == null) {
			jComboBoxExternalID = new JComboBox();
			jComboBoxExternalID.setBackground(Color.WHITE);
			jComboBoxExternalID.setPreferredSize(new java.awt.Dimension(175, 23));
		}
		return jComboBoxExternalID;
	}
	
	private JPanel getJPanelSearchandInfoPanel() {
		if(jPanelSearchandInfoPanel == null) {
			jPanelSearchandInfoPanel = new JPanel();
			GridBagLayout jPanelSearchandInfoPanelLayout = new GridBagLayout();
			jPanelSearchandInfoPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelSearchandInfoPanelLayout.rowHeights = new int[] {7, 20, 20};
			jPanelSearchandInfoPanelLayout.columnWeights = new double[] {0.1, 0.0, 0.0};
			jPanelSearchandInfoPanelLayout.columnWidths = new int[] {7, 7, 7};
			jPanelSearchandInfoPanel.setLayout(jPanelSearchandInfoPanelLayout);
			jPanelSearchandInfoPanel.setBorder(BorderFactory.createTitledBorder("Search"));
			jPanelSearchandInfoPanel.add(getJTextPaneSearch(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSearchandInfoPanel.add(getJCheckBoxCaseSensitive(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
			jPanelSearchandInfoPanel.add(getJCheckBoxMatchWholeWordOnly(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
			jPanelSearchandInfoPanel.add(getJButtonSearchButton(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			jPanelSearchandInfoPanel.add(getJPanelSearchResults(), new GridBagConstraints(0, 1, 2, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSearchandInfoPanel.add(getJCheckBoxSynonymsSearch(), new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
		}
		return jPanelSearchandInfoPanel;
	}

	private void removeElement(int termID, String elem) throws SQLException, DatabaseLoadDriverException {
		if(termIDSynonyms.containsKey(termID))
			termIDSynonyms.get(termID).remove(elem);
		dictionary.removeElementSynonyms(termID, elem);
		jPanelSearchResults.setVisible(false);
	}
	
	private int getTermIDselected() {
		int selectedTermLine = jListTerms.getSelectedIndex();
		if(selectedTermLine!=-1)
		{
			int termID = lineTerm.get(selectedTermLine);
			return termID;
		}
		else
		{
			return -1;
		}
	}

	private void removeAllSynonymsElements(int termID) throws SQLException, DatabaseLoadDriverException {
		termIDSynonyms.remove(termID);
		dictionary.removeElementAllSynonyms(termID);
	}
	
	private JPopupMenu getJPopupMenuClassContent() {
		if(jPopupMenuClassContent == null) {
			jPopupMenuClassContent = getClassContentPopupMenu();
		}
		return jPopupMenuClassContent;
	}
	
	private JPopupMenu getJPopupMenuSynonyms() {
		if(jPopupMenuSynonyms == null) {
			jPopupMenuSynonyms = getSynonymsPopupMenu();
		}
		return jPopupMenuSynonyms;
	}
	
	private JPopupMenu getSynonymsPopupMenu(){
		if(jPopupMenuSynonyms==null)
		{
			jPopupMenuSynonyms = new JPopupMenu("Synonyms Options");		

			JMenuItem item = new JMenuItem("Add New Synonym");
			item.setBackground(Color.decode("#F0F0F0"));
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					addNewSynonym(evt);
				}
			});
			jPopupMenuSynonyms.add(item);
			
			item = new JMenuItem("Edit Synonym");
			item.setBackground(Color.decode("#F0F0F0"));
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					editSynonym(evt);
				}
			});
			jPopupMenuSynonyms.add(item);

			item = new JMenuItem("Remove Synonym");
			item.setBackground(Color.decode("#F0F0F0"));
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					try {
						removeSynonym(evt);
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
			jPopupMenuSynonyms.add(item);
			
			item = new JMenuItem("removel All Synonyms");
			item.setBackground(Color.decode("#F0F0F0"));
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					try {
						removeAllSynonyms(evt);
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
			jPopupMenuSynonyms.add(item);
			item.setText("Removel All Synonyms");

		}
		return jPopupMenuSynonyms;
	}
	
	protected void removeAllSynonyms(ActionEvent evt) throws SQLException, DatabaseLoadDriverException {
		if(jListSyn.getModel().getSize()>0)
		{
			int termID = getTermIDselected();
			if(termID!=-1)
			{
				removeAllSynonymsElements(termID);
				jListSyn.setModel(new DefaultComboBoxModel());
				jListSyn.updateUI();
				dictionary.notifyViewObservers();	
				new ShowMessagePopup("All Synonym are Removed .");
			}
			else
			{
				Workbench.getInstance().warn(GlobalTextInfoSmall.dictionaryViewSelectTerm);
			}
		}
		else
		{
			Workbench.getInstance().warn(GlobalTextInfoSmall.dictionaryViewNoElementsToRemove);

		}	
	}

	protected void removeSynonym(ActionEvent evt) throws SQLException, DatabaseLoadDriverException {
		if(jListSyn.getModel().getSize()>0)
		{
			int termID = getTermIDselected();
			if(termID!=-1)
			{
				int synIdex = jListSyn.getSelectedIndex();
				if(synIdex!=-1)
				{
					String elem = (String) jListSyn.getSelectedValue();
					removeElement(termID,elem.toString());
					((DefaultComboBoxModel) jListSyn.getModel()).removeElement(elem);
					dictionary.notifyViewObservers();
					new ShowMessagePopup(" Synonym Removed .");
				}
				else
				{
					Workbench.getInstance().warn(GlobalTextInfoSmall.dictionaryViewSelectSynonym);
				}
			}
			else
			{
				Workbench.getInstance().warn(GlobalTextInfoSmall.dictionaryViewSelectTerm);
			}
		}
		else
		{
			Workbench.getInstance().warn(GlobalTextInfoSmall.dictionaryViewNoElementsToRemove);

		}	
	}

	protected void editSynonym(ActionEvent evt) {
		int termID = getTermIDselected();
		if(termID!=-1)
		{
			int synIdex = jListSyn.getSelectedIndex();
			if(synIdex!=-1)
			{
				String oldSyn = (String) jListSyn.getSelectedValue();
				String term = (String) jListTerms.getSelectedValue();
				new DictionaryViewEditSynonymGUI(dictionary, this.jListTerms, this.jListSyn,this.termIDSynonyms,oldSyn, termID, term);
			}
			else
			{
				Workbench.getInstance().warn(GlobalTextInfoSmall.dictionaryViewSelectSynonym);
			}
		}
		else
		{
			Workbench.getInstance().warn(GlobalTextInfoSmall.dictionaryViewSelectTerm);
		}
	}

	protected void addNewSynonym(ActionEvent evt) {
		int termID = getTermIDselected();
		if(termID!=-1)
		{
			String term = (String) jListTerms.getSelectedValue();
			new DictionaryViewAddSynonymGUI(dictionary, this.jListTerms, this.jListSyn,this.termIDSynonyms, termID, term);
		}
		else
		{
			Workbench.getInstance().warn(GlobalTextInfoSmall.dictionaryViewSelectTerm);
		}
	}

	private JPopupMenu getClassContentPopupMenu(){
		if(jPopupMenuClassContent==null)
		{
			jPopupMenuClassContent = new JPopupMenu("Class Options");		

			JMenuItem item = new JMenuItem("Add New Class");
			item.setBackground(Color.decode("#F0F0F0"));
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					addNewClass(evt);
				}
			});
			jPopupMenuClassContent.add(item);

			item = new JMenuItem("Add New Term");
			item.setBackground(Color.decode("#F0F0F0"));
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					addNewTerm(evt);
				}
			});
			jPopupMenuClassContent.add(item);
			
			item = new JMenuItem("Edit Class");
			item.setBackground(Color.decode("#F0F0F0"));
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					editClass(evt);
				}
			});
			jPopupMenuClassContent.add(item);
			item = new JMenuItem("Remove Class (All Class elements)");
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					removeAllTermsFromClass(evt);
				}
			});
			jPopupMenuClassContent.add(item);

		}
		return jPopupMenuClassContent;
	}
	
	protected void removeAllTermsFromClass(ActionEvent evt) {
		int classID = getClassID();
		if(classID!=-1)
		{
			ParamSpec[] para = new ParamSpec[]{ 
					new ParamSpec("dictionary",DictionaryAibench.class,dictionary,null),
					new ParamSpec("classID",Integer.class,classID,null),
				};

			for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations())
			{
				if (def.getID().equals("operations.dicremovetermsbyclass"))
				{
					removeClass = true;
					Workbench.getInstance().executeOperation(def,para);
					jListTerms.setModel(new DefaultComboBoxModel());
					jListSyn.setModel(new DefaultComboBoxModel());
					jListTerms.updateUI();
					jListSyn.updateUI();
					jComboBoxExternalID.setModel(new DefaultComboBoxModel());
					jComboBoxExternalID.updateUI();
					jPanelSearchResults.setVisible(false);

				}
			}
		}
		else
		{
			Workbench.getInstance().warn(GlobalTextInfoSmall.dictionaryViewSelectClass);
		}	
	}

	protected void editClass(ActionEvent evt) {
		int classID = getClassID();
		if(classID!=-1)
		{
			String classe = (String) jListDicContent.getSelectedValue();
			new DictionaryViewEditClassGUI(dictionary,classes,jTableDicContent,jListDicContent,classID,classe);
		}
		else
		{
			Workbench.getInstance().warn(GlobalTextInfoSmall.dictionaryViewSelectClass);
		}	
	}

	protected void addNewTerm(ActionEvent evt) {
		int classID = getClassID();
		if(classID!=-1)
		{
			String classe = (String) jListDicContent.getSelectedValue();
			new DictionaryViewAddTermGUI(dictionary, lineTerm, terms, jListTerms, classID, classe);
		}
		else
		{
			Workbench.getInstance().warn(GlobalTextInfoSmall.dictionaryViewSelectClass);
		}
	}

	protected void addNewClass(ActionEvent evt) {
		new DictionaryViewAddClassGUI(dictionary, classes, lineClassID,jListDicContent, jTableDicContent);
	}

	private JPopupMenu getTermPopupMenu(){
		if(jPopupMenuTerm==null)
		{
			jPopupMenuTerm = new JPopupMenu("Term Options");		

			JMenuItem item = new JMenuItem("Add New Term");
			item.setBackground(Color.decode("#F0F0F0"));
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					addNewTerm(evt);
				}
			});
			jPopupMenuTerm.add(item);

			item = new JMenuItem("Add Term Synonym");
			item.setBackground(Color.decode("#F0F0F0"));
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					addNewSynonym(evt);
				}
			});
			jPopupMenuTerm.add(item);
			
			item = new JMenuItem("Add External ID");
			item.setBackground(Color.decode("#F0F0F0"));
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					addExternalID(evt);
				}
			});
			jPopupMenuTerm.add(item);
			
			item = new JMenuItem("Edit Term");
			item.setBackground(Color.decode("#F0F0F0"));
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					editTerm(evt);
				}
			});
			jPopupMenuTerm.add(item);
			
			item = new JMenuItem("Remove Term");
			item.setBackground(Color.decode("#F0F0F0"));
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					try {
						removeTerm(evt);
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
			jPopupMenuTerm.add(item);
			
			item = new JMenuItem("Remove All Synonyms");
			item.setBackground(Color.decode("#F0F0F0"));
			item.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					try {
						removeAllSynonyms(evt);
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
			jPopupMenuTerm.add(item);
		}
		return jPopupMenuTerm;
	}
	
	protected void removeTerm(ActionEvent evt) throws SQLException, DatabaseLoadDriverException {
		int termID = getTermIDselected();
		if(termID!=-1)
		{
			int classID = getClassID();
			if(classID!=-1)
			{
				String term = (String) jListTerms.getSelectedValue();
				dictionary.inactiveElement(new ResourceElement(termID, term));
				{
					int selectedIndex = jListTerms.getSelectedIndex();
					((DefaultComboBoxModel) jListTerms.getModel()).removeElementAt(selectedIndex);
					jListSyn.setModel(new DefaultComboBoxModel());
					jListTerms.updateUI();
					jListSyn.updateUI();
					jComboBoxExternalID.setModel(new DefaultComboBoxModel());
					jComboBoxExternalID.updateUI();
					changeTerms();
					dictionary.notifyViewObservers();
					new ShowMessagePopup("Term Removed.");
				}
			}
			else
			{
				Workbench.getInstance().warn(GlobalTextInfoSmall.dictionaryViewSelectClass);
				
			}
		}
		else
		{
			Workbench.getInstance().warn(GlobalTextInfoSmall.dictionaryViewSelectTerm);
		}	
	}

	protected void editTerm(ActionEvent evt) {
		int termID = getTermIDselected();
		if(termID!=-1)
		{
			int classID = getClassID();
			if(classID!=-1)
			{
				String term = (String) jListTerms.getSelectedValue();
				String classe = (String) jListDicContent.getSelectedValue();
				new DictionaryViewEditTermGUI(dictionary, jListTerms, classID, classe, termID, term);
			}
			else
			{
				Workbench.getInstance().warn(GlobalTextInfoSmall.dictionaryViewSelectClass);
				
			}
		}
		else
		{
			Workbench.getInstance().warn(GlobalTextInfoSmall.dictionaryViewSelectTerm);
		}		
	}

	protected void addExternalID(ActionEvent evt) {
		int termID = getTermIDselected();
		if(termID!=-1)
		{
			String term = (String) jListTerms.getSelectedValue();
			new DictionaryViewAddExternaIDGUI(dictionary, jComboBoxExternalID, termID,term);
		}
		else
		{
			Workbench.getInstance().warn(GlobalTextInfoSmall.dictionaryViewSelectTerm);
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
	
	private JPanel getJPanelClassContent() {
		if(jPanelClassContent == null) {
			jPanelClassContent = new JPanel();
			GridBagLayout jPanelClassContentLayout = new GridBagLayout();
			jPanelClassContent.setLayout(jPanelClassContentLayout);
			jPanelClassContent.add(getJScrollPaneClassContent(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelClassContent.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Class Content",TitledBorder.LEADING, TitledBorder.TOP));
			jPanelClassContent.setPreferredSize(new java.awt.Dimension(175, 10));
			jPanelClassContentLayout.rowWeights = new double[] {0.1};
			jPanelClassContentLayout.rowHeights = new int[] {7};
			jPanelClassContentLayout.columnWeights = new double[] {0.1};
			jPanelClassContentLayout.columnWidths = new int[] {7};

		}
		return jPanelClassContent;
	}
	
	private JCheckBox getJCheckBoxMatchWholeWordOnly() {
		if(jCheckBoxMatchWholeWordOnly == null) {
			jCheckBoxMatchWholeWordOnly = new JCheckBox();
			jCheckBoxMatchWholeWordOnly.setText("Match Whole Word Only");
		}
		return jCheckBoxMatchWholeWordOnly;
	}
	
	private JButton getJButtonSearchButton() {
		if(jButtonSearchButton == null) {
			jButtonSearchButton = new JButton();
			jButtonSearchButton.setText("Search");
			jButtonSearchButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					operationDictionarySearch();
				}
			});
		}
		return jButtonSearchButton;
	}
	
	private void operationDictionarySearch() {
		String stringToSearch = jTextPaneSearch.getText();
		if(stringToSearch.equals(""))
		{
			Workbench.getInstance().warn("Please select expression to search");
			jPanelSearchResults.setVisible(false);
		}
		else
		{
			ParamSpec[] paramsSpec = new ParamSpec[]{
					new ParamSpec("view", DictionaryView.class,this, null)
			};

			for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
				if (def.getID().equals("operations.dictionarysearch")){	
					Workbench.getInstance().executeOperation(def, paramsSpec);
					return;
				}
			}
		}
	}

	public void proceedSearch(TimeLeftProgress progress) throws SQLException, DatabaseLoadDriverException {
		boolean mactchSensitive = jCheckBoxCaseSensitive.isSelected();
		boolean mactchWholeWordOnly = jCheckBoxMatchWholeWordOnly.isSelected();
		boolean searchSynonyms = jCheckBoxSynonymsSearch.isSelected();
		String stringToSearch = jTextPaneSearch.getText();
		{
			List<Integer> rowsMatching = searchInJList(stringToSearch,mactchSensitive,mactchWholeWordOnly,searchSynonyms,progress);
			if(rowsMatching.size() == 0)
			{
				Workbench.getInstance().warn("No results found");
				jPanelSearchResults.setVisible(false);
			}
			else
			{
				setTableChangesSearch(rowsMatching);
				fillSearchResultsPane(rowsMatching);
			}
			progress.setProgress(1);
		}
	}
	
	private void fillSearchResultsPane(List<Integer> rowsMatching) {
		jPanelSearchResults.setVisible(true);
		this.rowsMatching = rowsMatching;
		this.selectedIndex = 0;
		if(rowsMatching.size() == 1)
		{
			jLabelSearchResultInfo.setText("1/1");
			jButtonSearchResulsBack.setVisible(false);
			jButtonSearchResultsNext.setVisible(false);

		}
		else
		{
			jButtonSearchResulsBack.setVisible(false);
			jLabelSearchResultInfo.setText("1/"+rowsMatching.size());
			jButtonSearchResultsNext.setVisible(true);
		}
	}

	private List<Integer> searchInJList(String stringToSearch,boolean mactchSensitive, boolean mactchWholeWordOnly, boolean searchSynonyms, TimeLeftProgress progress) throws DatabaseLoadDriverException {
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		int total = jListTerms.getModel().getSize();
		SortedSet<Integer> rows = new TreeSet<Integer>();
		if(!mactchSensitive && !mactchWholeWordOnly)
		{
			for (int i = 0; i < total ; i++) {

				if (((String) jListTerms.getModel().getElementAt(i)).toLowerCase().contains(stringToSearch.toLowerCase()))
					rows.add(i);
			}
		}
		else if(mactchSensitive && !mactchWholeWordOnly)
		{
			for (int i = 0; i < total; i++) {

				if (((String) jListTerms.getModel().getElementAt(i)).contains(stringToSearch))
				{
					rows.add(i);
				}
			}

		}
		else if(!mactchSensitive && mactchWholeWordOnly)
		{
			for (int i = 0; i < total; i++) {

				if (((String) jListTerms.getModel().getElementAt(i)).equalsIgnoreCase(stringToSearch))
				{
					rows.add(i);
				}
			}
		}
		else
		{
			for (int i = 0; i < total; i++) {

				if (((String) jListTerms.getModel().getElementAt(i)).equals(stringToSearch))
				{
					rows.add(i);
				}
			}
		}
		if(searchSynonyms)
		{
			Set<Integer> synonymLines = findIntoSynonym(stringToSearch,mactchSensitive,mactchWholeWordOnly);
			rows.addAll(synonymLines);
		}
		return new ArrayList<Integer>(rows);
	}

	private Set<Integer> findIntoSynonym(String stringToSearch,boolean mactchSensitive, boolean mactchWholeWordOnly) throws DatabaseLoadDriverException {
		String query = new String();
		Set<Integer> lisT = new HashSet<Integer>();
		if(jListDicContent.getSelectedIndex() == -1)
			return lisT;
		try {
			PreparedStatement ps;
			if(!mactchSensitive && !mactchWholeWordOnly)
			{
				query = QueriesResources.findPartialMathingInDictionariesNoMatchCaseAndNoWholeWord;
				ps = GlobalOptions.database.getConnection().prepareStatement(query);
				ps.setString(3,"%" + stringToSearch + "%");
				
			}
			else if(mactchSensitive && !mactchWholeWordOnly)
			{
				query = QueriesResources.findPartialMathingInDictionariesCaseAndNoWholeWord;
				ps = GlobalOptions.database.getConnection().prepareStatement(query);
				ps.setString(3,"%" + stringToSearch + "%");

			}
			else if(!mactchSensitive && mactchWholeWordOnly)
			{
				query = QueriesResources.findPartialMathingInDictionariesNoMatchCaseAndNoWholeWord;
				ps = GlobalOptions.database.getConnection().prepareStatement(query);
				ps.setString(3,stringToSearch);
			}
			else
			{
				query = QueriesResources.findPartialMathingInDictionaries;
				ps = GlobalOptions.database.getConnection().prepareStatement(query);
				ps.setString(3,stringToSearch );
			}
			ps.setInt(1, this.dictionary.getID());
			ps.setInt(2,lineClassID.get(jListDicContent.getSelectedIndex()));
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				lisT.add(this.termIDLine.get(rs.getInt(1)));
			}
			rs.close();
			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lisT;
	}

	protected void memoryAndProgress(TimeLeftProgress progress,long start,int step, int total) {
		System.out.println((GlobalOptions.decimalformat.format(((double) step / (double) total) * 100) + " %..."));
		System.out.println((Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory())/(1024*1024) + " MB ");
		long end;
		long differtim;
		end = GregorianCalendar.getInstance().getTimeInMillis();
		differtim = end-start;
		progress.setTime(differtim, step, total);
		progress.setProgress((float) step / (float) total);
	}

	@SuppressWarnings("static-access")
	private void setTableChangesSearch(List<Integer> rows) throws SQLException, DatabaseLoadDriverException {
		
		int row = 0;
		DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();
		for (Integer r : rows) {
			row = r.intValue();
			selectionModel .addSelectionInterval(row, row);
		}

		this.jListTerms.setSelectionMode(selectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.jListTerms.setSelectionModel(selectionModel);
		if (rows.size() > 0) {
			this.jListTerms.scrollRectToVisible(jListTerms.getCellBounds(rows.get(0), rows.get(0)));
			changeSyn();
			changeExternalID();
		}
	}


	private JPanel getJPanelSearchResults() {
		if(jPanelSearchResults == null) {
			jPanelSearchResults = new JPanel();
			GridBagLayout jPanelSearchResultsLayout = new GridBagLayout();
			jPanelSearchResults.setBorder(BorderFactory.createTitledBorder("Results"));
			jPanelSearchResultsLayout.rowWeights = new double[] {0.1};
			jPanelSearchResultsLayout.rowHeights = new int[] {7};
			jPanelSearchResultsLayout.columnWeights = new double[] {0.1, 0.0, 0.1};
			jPanelSearchResultsLayout.columnWidths = new int[] {7, 7, 7};
			jPanelSearchResults.setLayout(jPanelSearchResultsLayout);
			jPanelSearchResults.add(getJLabelSearchResultInfo(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
			jPanelSearchResults.add(getJButtonSearchResultsNext(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSearchResults.add(getJButtonSearchResulysBack(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSearchResults.setVisible(false);
		}
		return jPanelSearchResults;
	}
	
	private JLabel getJLabelSearchResultInfo() {
		if(jLabelSearchResultInfo == null) {
			jLabelSearchResultInfo = new JLabel();
			jLabelSearchResultInfo.setText("0/0");
		}
		return jLabelSearchResultInfo;
	}
	
	private JButton getJButtonSearchResultsNext() {
		if(jButtonSearchResultsNext == null) {
			jButtonSearchResultsNext = new JButton();
			jButtonSearchResultsNext.setText("Next");
			ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/forward.png"));
			jButtonSearchResultsNext.setIcon(icon);
			jButtonSearchResultsNext.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						goNextTerm();
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
		}
		return jButtonSearchResultsNext;
	}
	
	protected void goNextTerm() throws SQLException, DatabaseLoadDriverException {
		jButtonSearchResulsBack.setVisible(true);
		selectedIndex ++;
		DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();
		selectionModel.addSelectionInterval(rowsMatching.get(selectedIndex), rowsMatching.get(selectedIndex));
		jLabelSearchResultInfo.setText((selectedIndex+1)+"/"+rowsMatching.size());
		this.jListTerms.setSelectionMode(selectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.jListTerms.setSelectionModel(selectionModel);
		this.jListTerms.scrollRectToVisible(jListTerms.getCellBounds(rowsMatching.get(selectedIndex), rowsMatching.get(selectedIndex)));
		changeSyn();
		changeExternalID();
		if(selectedIndex+1 >= rowsMatching.size())
		{
			jButtonSearchResultsNext.setVisible(false);
		}
	}

	private JButton getJButtonSearchResulysBack() {
		if(jButtonSearchResulsBack == null) {
			jButtonSearchResulsBack = new JButton();
			jButtonSearchResulsBack.setText("Back");
			ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/back.png"));
			jButtonSearchResulsBack.setIcon(icon);
			jButtonSearchResulsBack.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						goBackTerm();
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
		}
		return jButtonSearchResulsBack;
	}
	
	protected void goBackTerm() throws SQLException, DatabaseLoadDriverException {
		jButtonSearchResultsNext.setVisible(true);
		selectedIndex --;
		DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();
		selectionModel .addSelectionInterval(rowsMatching.get(selectedIndex), rowsMatching.get(selectedIndex));
		jLabelSearchResultInfo.setText((selectedIndex+1)+"/"+rowsMatching.size());
		this.jListTerms.setSelectionMode(selectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.jListTerms.setSelectionModel(selectionModel);
		this.jListTerms.scrollRectToVisible(jListTerms.getCellBounds(rowsMatching.get(selectedIndex), rowsMatching.get(selectedIndex)));
		changeSyn();
		changeExternalID();
		if(selectedIndex == 0)
		{
			jButtonSearchResulsBack.setVisible(false);
		}
	}

	private JCheckBox getJCheckBoxSynonymsSearch() {
		if(jCheckBoxSynonymsSearch == null) {
			jCheckBoxSynonymsSearch = new JCheckBox();
			jCheckBoxSynonymsSearch.setText("Synonyms Search");
			jCheckBoxSynonymsSearch.setSelected(OtherConfigurations.getDictionaryViewSearchForSynonyms());
		}
		return jCheckBoxSynonymsSearch;
	}
	
	private JPanelWait getJPanelSearch() {
		if(jPanelSearch == null) {
			jPanelSearch = new JPanelWait();
			jPanelSearch.setVisible(false);
		}
		return jPanelSearch;
	}

	public void searchingOptionON()
	{
		jPanelSearch.setVisible(true);
	}
	
	public void searchingOptionOFF()
	{
		jPanelSearch.setVisible(false);
	}

}
