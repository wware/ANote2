package pt.uminho.anote2.aibench.corpus.view;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;

import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;


public class RESchemaRelationsResumeStats extends JPanel{

	private static final long serialVersionUID = 6168787669685545702L;
	private JTextField jTextFieldNumberOfVerbsAssociationWhitRelations;
	private JTextField jTextFieldNumberOfRelations;
	private JTextField jTextFieldDiecetionallyBoth;
	private JLabel jLabelDirectionallyBoth;
	private JTextField jTextFieldCardinallityyOther;
	private JLabel jLabelCardinallityOther;
	private JTextField jTextFieldDirectionallyUnknown;
	private JLabel jLabelDirectionallyUnknown;
	private JTextField jTextFieldPolarityUnknown;
	private JTextField jTextFieldConditional;
	private JLabel jLabelConditional;
	private JTextField jTextFieldMAnyMany;
	private JTextField jTextFieldMAnyOne;
	private JTextField jTextFieldManyZero;
	private JTextField jTextFieldZeroMany;
	private JTextField jTextFieldOneMany;
	private JTextField jTextFieldOneOne;
	private JLabel jLabelManyMany;
	private JLabel jLabelManyOne;
	private JLabel jLabelManyZero;
	private JLabel jLabelZeroMany;
	private JLabel jLabel1OneMany;
	private JLabel jLabel1OneOne;
	private JTextField jTextFieldInderect;
	private JTextField jTextFieldDirect;
	private JLabel jLabelInderect;
	private JLabel jLabelDirect;
	private JTextField jTextFieldNegative;
	private JTextField jTextFieldPositive;
	private JLabel jLabelNegative;
	private JLabel jLabelPositive;
	private JPanel jPanelDirectionality;
	private JPanel jPanelCardinality;
	private JPanel jPanelPolarity;
	private JScrollPane jScrollPaneDirecionaly;
	private JScrollPane jScrollPaneCardinality;
	private JScrollPane jScrollPanePolarity;
	private JPanel jPanel3Stats;
	private JTextField jTextFieldNumberOfDocs;
	private JLabel jLabelNumberOfDocs;
	private JScrollPane jScrollPaneTop30Verbs;
	private JLabel jLabelNumberOfRelations;
	private JLabel jLabelClues;
	private JLabel Unknown;
	private JButton jButtonHelp;
	private JList top30List;	
	private Map<String,Integer> verdIDcount;
	private DecimalFormat round3 = new DecimalFormat("0.000");
	private DecimalFormat round2 = new DecimalFormat("0.00");
	
	/**
	 * 0 - Negativos
	 * 1 - Condiconais
	 * 2 - Positivos
	 * 3 - Not defined
	 */
	private int[] polarity;
	/**
	 * 0 - LeftToRight
	 * 1 - RightToLeft
	 * 2 - not defined
	 */
	private int[] directionality;
	/**
	 * 0 - One to One
	 * 1 - One to Many
	 * 2 - Zero to Many
	 * 3 - Many to Zero
	 * 4 - Many to One
	 * 5 - Many to Many
	 * 6 - Other
	 */
	private int[] cardinality;
	private String[] top30_terms;
	private RESchema reprocess;

	public RESchemaRelationsResumeStats(RESchema reprocess)
	{
		this.reprocess = reprocess;
		this.verdIDcount = new HashMap<String, Integer>();
		polarity = new int[4];
		directionality = new int[4];
		cardinality = new int[7];
		initGUI();
		try {
			createStats();
			fillStats();
		} catch (SQLException e) {	
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}
	
	private void fillStats() throws SQLException, DatabaseLoadDriverException
	{
		// Para os verbo
		findTop30Clues();
		fillTop30Clues();
		// Para a cardinalidade
		String pos="Na";
		String neg="Na";
		String cod="Na";
		String unknown = "Na";
		int total = polarity[0]+polarity[1]+polarity[2]+polarity[3];
		if(total!=0)
		{
			unknown = String.valueOf(round2.format((float)polarity[3]*100/(float)total)+" %");
			pos = String.valueOf(round2.format((float)polarity[2]*100/(float)total)+" %");
			neg = String.valueOf(round2.format((float)polarity[0]*100/(float)total)+" %");
			cod = String.valueOf(round2.format((float)polarity[1]*100/(float)total)+" %");
		}
		jTextFieldPositive.setText(pos);
		jTextFieldNegative.setText(neg);
		jTextFieldConditional.setText(cod);
		jTextFieldPolarityUnknown.setText(unknown);
		// Para a direcionalidade
		String lr="Na";
		String rl="Na";
		String un = "Na";
		String bt = "Na";
		total = directionality[0]+directionality[1]+directionality[2]+directionality[3];
		if(total!=0)
		{
			lr = String.valueOf(round2.format((float)directionality[0]*100/(float)total)+" %");
			rl = String.valueOf(round2.format((float)directionality[1]*100/(float)total)+" %");
			un = String.valueOf(round2.format((float)directionality[2]*100/(float)total)+" %");
			bt = String.valueOf(round2.format((float)directionality[3]*100/(float)total)+" %");
		}
		jTextFieldDirect.setText(lr);
		jTextFieldInderect.setText(rl);
		jTextFieldDirectionallyUnknown.setText(un);
		jTextFieldDiecetionallyBoth.setText(bt);
		// Para a Cardinalidade
		String oneone = "Na";
		String oneMany="Na";
		String zeroMany="Na";
		String manyZero="Na";
		String manyOne="Na";
		String manyMany="Na";
		String other="Na";
		total = cardinality[0]+cardinality[1]+cardinality[2]+cardinality[3]+cardinality[4]+cardinality[5]+cardinality[6];
		if(total!=0)
		{
			oneone = String.valueOf(round2.format((float)cardinality[0]*100/(float)total)+" %");	
			oneMany = String.valueOf(round2.format((float)cardinality[1]*100/(float)total)+" %");	
			zeroMany = String.valueOf(round2.format((float)cardinality[2]*100/(float)total)+" %");
			manyZero = String.valueOf(round2.format((float)cardinality[3]*100/(float)total)+" %");
			manyOne = String.valueOf(round2.format((float)cardinality[4]*100/(float)total)+" %");
			manyMany = String.valueOf(round2.format((float)cardinality[5]*100/(float)total)+" %");
			other = String.valueOf(round2.format((float)cardinality[6]*100/(float)total)+" %");
		}
		jTextFieldOneOne.setText(oneone);
		jTextFieldOneMany.setText(oneMany);
		jTextFieldZeroMany.setText(zeroMany);
		jTextFieldManyZero.setText(manyZero);
		jTextFieldMAnyOne.setText(manyOne);
		jTextFieldMAnyMany.setText(manyMany);
		jTextFieldCardinallityyOther.setText(other);
		jTextFieldNumberOfVerbsAssociationWhitRelations.setText(String.valueOf(verdIDcount.size()));
		jTextFieldNumberOfRelations.setText(String.valueOf(reprocess.getEventsAnnotations().size()));
		jTextFieldNumberOfDocs.setText(String.valueOf(reprocess.getCorpus().getArticlesCorpus().size()));
	}
	
	private void createStats() throws SQLException, DatabaseLoadDriverException
	{	
		for(IEventAnnotation event:reprocess.getEventsAnnotations().values())
		{
			polarity(event);
			directionaly(event);
			cardinalaty(event);
			addClue(event);
		}
	}
	
	private void addClue(IEventAnnotation event) {
		if(event.getEventClue()!=null)
		{
			if(verdIDcount.containsKey(event.getEventClue()))
			{
				int number = verdIDcount.get(event.getEventClue());
				verdIDcount.put(event.getEventClue(), ++number);
			}
			else
			{
				verdIDcount.put(event.getEventClue(),1);
			}
		}
	}

	private void polarity(IEventAnnotation event)
	{
		this.polarity[event.getEventProperties().getPolarity().databaseValue()]++;
	}
	
	private void directionaly(IEventAnnotation event)
	{
		this.directionality[event.getEventProperties().getDirectionally().databaseValue()]++;
	}
	
	private void cardinalaty(IEventAnnotation event)
	{
		int left = event.getEntitiesAtLeft().size();
		int right = event.getEntitiesAtRight().size();
		if(left>1&&right>1)
		{
			this.cardinality[5]++;
		}
		else if(left==1&&right==1)
		{
			this.cardinality[0]++;
		}
		else if(left==1&&right>1)
		{
			this.cardinality[1]++;
		}
		else if(left==0&&right>1)
		{
			this.cardinality[2]++;
		}
		else if(left>1&&right==0)
		{
			this.cardinality[3]++;
		}
		else if(left>1&&right==1)
		{
			this.cardinality[4]++;
		}
		else
		{
			this.cardinality[6]++;
		}
	}
	
	private void initGUI() {
		try {
			{
				GridBagLayout thisLayout = new GridBagLayout();
				this.setSize(837, 700);
				thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
				thisLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
				thisLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
				thisLayout.columnWidths = new int[] {51, 7, 134, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
				this.setLayout(thisLayout);
				{
					jButtonHelp = new JButton();
					this.add(jButtonHelp, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
					jButtonHelp.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							try {
								Help.internetAccess(GlobalOptions.wikiGeneralLink+"Process_Relations_Resume_Stats");
							} catch (IOException e) {
								TreatExceptionForAIbench.treatExcepion(e);
							}
						}
					});
					jButtonHelp.setEnabled(true);
					jButtonHelp.setVisible(true);
				}
				{
					jLabelClues = new JLabel();
					this.add(jLabelClues, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jLabelClues.setText("Clues Associated to Relations");
				}
				{
					jLabelNumberOfRelations = new JLabel();
					this.add(jLabelNumberOfRelations, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jLabelNumberOfRelations.setText("Number of Events (Relations)");
				}
				{
					jTextFieldNumberOfVerbsAssociationWhitRelations = new JTextField();
					this.add(jTextFieldNumberOfVerbsAssociationWhitRelations, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jTextFieldNumberOfVerbsAssociationWhitRelations.setEditable(false);
					jTextFieldNumberOfVerbsAssociationWhitRelations.setBackground(Color.WHITE);
				}	
				{
					jTextFieldNumberOfRelations = new JTextField();
					this.add(jTextFieldNumberOfRelations, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jTextFieldNumberOfRelations.setEditable(false);
					jTextFieldNumberOfRelations.setBackground(Color.WHITE);
				}
				{
					jScrollPaneTop30Verbs = new JScrollPane();
					this.add(jScrollPaneTop30Verbs, new GridBagConstraints(6, 1, 7, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jScrollPaneTop30Verbs.setBorder(BorderFactory.createTitledBorder("Top 30 Clues"));
				}
				{
					jLabelNumberOfDocs = new JLabel();
					this.add(jLabelNumberOfDocs, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jLabelNumberOfDocs.setText("Number Of Documents");
				}
				{
					jTextFieldNumberOfDocs = new JTextField();
					this.add(jTextFieldNumberOfDocs, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jTextFieldNumberOfDocs.setEditable(false);
					jTextFieldNumberOfDocs.setBackground(Color.WHITE);
				}
				{
					jPanel3Stats = new JPanel();
					GridBagLayout jPanel3StatsLayout = new GridBagLayout();
					this.add(jPanel3Stats, new GridBagConstraints(0, 5, 20, 15, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanel3StatsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
					jPanel3StatsLayout.rowHeights = new int[] {7, 7, 7, 7};
					jPanel3StatsLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
					jPanel3StatsLayout.columnWidths = new int[] {7, 7, 7};
					jPanel3Stats.setLayout(jPanel3StatsLayout);
					{
						jScrollPanePolarity = new JScrollPane();
						jScrollPanePolarity.setBorder(BorderFactory.createTitledBorder("Polarity"));
						jPanel3Stats.add(jScrollPanePolarity, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						{
							jPanelPolarity = new JPanel();
							GridBagLayout jPanelPolarityLayout = new GridBagLayout();
							jPanelPolarity.setLayout(jPanelPolarityLayout);
							jScrollPanePolarity.setViewportView(jPanelPolarity);
							{
								jLabelPositive = new JLabel();
								jPanelPolarity.add(jLabelPositive, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jLabelPositive.setText("Positive (+)");					
							}
							{
								jLabelNegative = new JLabel();
								jPanelPolarity.add(jLabelNegative, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jLabelNegative.setText("Negative (-)");
							}
							{
								jTextFieldPositive = new JTextField();
								jPanelPolarity.add(jTextFieldPositive, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jTextFieldPositive.setEditable(false);
								jTextFieldPositive.setBackground(Color.WHITE);
							}
							{
								jTextFieldNegative = new JTextField();
								jPanelPolarity.add(jTextFieldNegative, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jTextFieldNegative.setEditable(false);
								jTextFieldNegative.setBackground(Color.WHITE);	
							}
							{
								jLabelConditional = new JLabel();
								jPanelPolarity.add(jLabelConditional, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jLabelConditional.setText("Conditional (?)");
							}
							{
								jTextFieldConditional = new JTextField();
								jPanelPolarity.add(jTextFieldConditional, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jTextFieldConditional.setEditable(false);
								jTextFieldConditional.setBackground(Color.WHITE);
							}
							{
								Unknown = new JLabel();
								jPanelPolarity.add(Unknown, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								Unknown.setText("Unknown");
							}
							{
								jTextFieldPolarityUnknown = new JTextField();
								jPanelPolarity.add(jTextFieldPolarityUnknown, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jTextFieldPolarityUnknown.setText("Na");
								jTextFieldPolarityUnknown.setEditable(false);
								jTextFieldPolarityUnknown.setBackground(Color.WHITE);
							}
							jPanelPolarityLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
							jPanelPolarityLayout.rowHeights = new int[] {7, 7, 7, 7};
							jPanelPolarityLayout.columnWeights = new double[] {0.0, 0.0, 0.1, 0.1};
							jPanelPolarityLayout.columnWidths = new int[] {17, 77, 20, 7};
						}
					}
					{
						jScrollPaneCardinality = new JScrollPane();
						jScrollPaneCardinality.setBorder(BorderFactory.createTitledBorder("Cardinality"));
						jPanel3Stats.add(jScrollPaneCardinality, new GridBagConstraints(1, 0, 1, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						{
							jPanelCardinality = new JPanel();
							GridBagLayout jPanelCardinalityLayout = new GridBagLayout();
							jPanelCardinality.setLayout(jPanelCardinalityLayout);
							jScrollPaneCardinality.setViewportView(jPanelCardinality);
							{
								jLabel1OneOne = new JLabel();
								jPanelCardinality.add(jLabel1OneOne, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jLabel1OneOne.setText("One-One");
								
							}
							{
								jLabel1OneMany = new JLabel();
								jPanelCardinality.add(jLabel1OneMany, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jLabel1OneMany.setText("One-Many");
							}
							{
								jLabelZeroMany = new JLabel();
								jPanelCardinality.add(jLabelZeroMany, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jLabelZeroMany.setText("Zero-Many");
							}
							{
								jLabelManyZero = new JLabel();
								jPanelCardinality.add(jLabelManyZero, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jLabelManyZero.setText("Many-Zero");
							}
							{
								jLabelManyOne = new JLabel();
								jPanelCardinality.add(jLabelManyOne, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jLabelManyOne.setText("Many-One");
							}
							{
								jLabelManyMany = new JLabel();
								jPanelCardinality.add(jLabelManyMany, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jLabelManyMany.setText("Many-Many");
							}
							{
								jTextFieldOneOne = new JTextField();
								jPanelCardinality.add(jTextFieldOneOne, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jTextFieldOneOne.setEditable(false);
								jTextFieldOneOne.setBackground(Color.WHITE);	
							}
							{
								jTextFieldOneMany = new JTextField();
								jPanelCardinality.add(jTextFieldOneMany, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jTextFieldOneMany.setEditable(false);
								jTextFieldOneMany.setBackground(Color.WHITE);	
							}
							{
								jTextFieldZeroMany = new JTextField();
								jPanelCardinality.add(jTextFieldZeroMany, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jTextFieldZeroMany.setEditable(false);
								jTextFieldZeroMany.setBackground(Color.WHITE);	
							}
							{
								jTextFieldManyZero = new JTextField();
								jPanelCardinality.add(jTextFieldManyZero, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jTextFieldManyZero.setEditable(false);
								jTextFieldManyZero.setBackground(Color.WHITE);	
							}
							{
								jTextFieldMAnyOne = new JTextField();
								jPanelCardinality.add(jTextFieldMAnyOne, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jTextFieldMAnyOne.setEditable(false);
								jTextFieldMAnyOne.setBackground(Color.WHITE);	
							}
							{
								jTextFieldMAnyMany = new JTextField();
								jPanelCardinality.add(jTextFieldMAnyMany, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jTextFieldMAnyMany.setEditable(false);
								jTextFieldMAnyMany.setBackground(Color.WHITE);	
							}
							{
								jLabelCardinallityOther = new JLabel();
								jPanelCardinality.add(jLabelCardinallityOther, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jLabelCardinallityOther.setText("Other");
							}
							{
								jTextFieldCardinallityyOther = new JTextField();
								jPanelCardinality.add(jTextFieldCardinallityyOther, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jTextFieldCardinallityyOther.setText("Na");
								jTextFieldCardinallityyOther.setEditable(false);
							}
							jPanelCardinalityLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
							jPanelCardinalityLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7, 7, 7};
							jPanelCardinalityLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.0};
							jPanelCardinalityLayout.columnWidths = new int[] {13, 7, 7, 25};
						}
					}
					{
						jScrollPaneDirecionaly = new JScrollPane();
						jScrollPaneDirecionaly.setBorder(BorderFactory.createTitledBorder("Directionality"));
						jPanel3Stats.add(jScrollPaneDirecionaly, new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						{
							jPanelDirectionality = new JPanel();
							GridBagLayout jPanelDirectionalityLayout = new GridBagLayout();
							jPanelDirectionality.setLayout(jPanelDirectionalityLayout);
							jScrollPaneDirecionaly.setViewportView(jPanelDirectionality);
							{
								jLabelDirect = new JLabel();
								jPanelDirectionality.add(jLabelDirect, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jLabelDirect.setText("Direct (L->R)");
							}
							{
								jLabelInderect = new JLabel();
								jPanelDirectionality.add(jLabelInderect, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jLabelInderect.setText("Inderect (R->L)");
							}
							{
								jTextFieldDirect = new JTextField();
								jPanelDirectionality.add(jTextFieldDirect, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jTextFieldDirect.setEditable(false);
								jTextFieldDirect.setBackground(Color.WHITE);	
							}
							{
								jTextFieldInderect = new JTextField();
								jPanelDirectionality.add(jTextFieldInderect, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jTextFieldInderect.setEditable(false);
								jTextFieldInderect.setBackground(Color.WHITE);	
							}
							{
								jLabelDirectionallyUnknown = new JLabel();
								jPanelDirectionality.add(jLabelDirectionallyUnknown, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jLabelDirectionallyUnknown.setText("Unknown");
							}
							{
								jTextFieldDirectionallyUnknown = new JTextField();
								jPanelDirectionality.add(jTextFieldDirectionallyUnknown, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jTextFieldDirectionallyUnknown.setEditable(false);
								jTextFieldDirectionallyUnknown.setBackground(Color.WHITE);
							}
							{
								jLabelDirectionallyBoth = new JLabel();
								jPanelDirectionality.add(jLabelDirectionallyBoth, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jLabelDirectionallyBoth.setText("Both (L <-> R)");
							}
							{
								jTextFieldDiecetionallyBoth = new JTextField();
								jPanelDirectionality.add(jTextFieldDiecetionallyBoth, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jTextFieldDiecetionallyBoth.setText("Na");
								jTextFieldDiecetionallyBoth.setEditable(false);
							}
							jPanelDirectionalityLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
							jPanelDirectionalityLayout.rowHeights = new int[] {7, 7, 7, 7};
							jPanelDirectionalityLayout.columnWeights = new double[] {0.0, 0.0, 0.1, 0.1};
							jPanelDirectionalityLayout.columnWidths = new int[] {17, 112, 7, 7};
						}
					}
					{
						jButtonHelp = new JButton();
						jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
						jButtonHelp.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								try {
									Help.internetAccess(GlobalOptions.wikiGeneralLink+"Process_Relations_Resume_Stats");
								} catch (IOException e1) {
									TreatExceptionForAIbench.treatExcepion(e1);
								}
							}
						});
						jPanel3Stats.add(jButtonHelp, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					}
				}

			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void fillTop30Clues() {
		ListModel top10ListModel = new DefaultComboBoxModel(top30_terms);
		top30List = new JList();
		jScrollPaneTop30Verbs.setViewportView(top30List);
		top30List.setModel(top10ListModel);
	}
	
	public void findTop30Clues() throws SQLException, DatabaseLoadDriverException
	{
		top30_terms = new String[30];
		TreeMap<Integer,List<String>> hashwhitverbs = new TreeMap<Integer, List<String>>();
		for(String verb:verdIDcount.keySet())
		{
			int value = verdIDcount.get(verb);
			if(hashwhitverbs.containsKey(value))
			{
				hashwhitverbs.get(value).add(verb);
			}
			else
			{
				hashwhitverbs.put(value,new ArrayList<String>());
				hashwhitverbs.get(value).add(verb);
			}
		}
		int i=0;
		Iterator<Integer> itNumber = hashwhitverbs.keySet().iterator();
		List<Integer> array = new ArrayList<Integer>();
		while(itNumber.hasNext())
		{
			array.add(itNumber.next());
		}		
		for(int k=array.size()-1;k>=0;k--)
		{
			Integer number = array.get(k);
			List<String> arrayStrings = hashwhitverbs.get(number);
			for(int j=0;j<arrayStrings.size()&&i<30;j++)
			{
				top30_terms[i]=arrayStrings.get(j)+" ("+round3.format((double)number/(double)reprocess.getEventsAnnotations().size())+")";
				i++;
			}
		}
	}

}
