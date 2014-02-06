package pt.uminho.anote2.aibench.publicationmanager.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.publicationmanager.dataStructures.RelevanceChanged;
import pt.uminho.anote2.aibench.publicationmanager.dataStructures.RelevanceData;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.documents.Publication;
import pt.uminho.anote2.datastructures.utils.MathUtils;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
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
/**
 * 
 * AibenchGUI for Change Relevance of a Query
 *
 */
public class RelevanceGUI extends JDialog {

	private static final long serialVersionUID = -2641509452511390921L;
	private JPanel relevancePanel;
	private ButtonGroup buttonGroup1;
	private JButton relevanceButton1;
	private JButton relevanceButton2;
	private JButton relevanceButton3;
	private JList keywordsList;
	private JButton cancelButton;

	private IPublication pubResume;
	private ArrayList<RelevanceData> queryKeywords;
	private RelevanceChanged rel_changed;
	private IDatabase db;
	private JButton jButtonHelp;

	public RelevanceGUI(IDatabase db,IPublication pubResume, ArrayList<RelevanceData> queryKeywords, RelevanceChanged rel_changed){
		super(Workbench.getInstance().getMainFrame());
		this.pubResume = pubResume;
		this.db=db;
		this.queryKeywords = queryKeywords;
		this.rel_changed = rel_changed;
		this.setVisible(true);
		this.setTitle("Publication - " + this.pubResume.getOtherID());
		
		//this.setPreferredSize(new java.awt.Dimension(800, 500));
		//this.setMinimumSize(new java.awt.Dimension(800,200));
		this.setSize(900,600);
		Utilities.centerOnOwner(this);
		this.setModal(true);
		
		initGUI();
	}

	private void initGUI() {
		try {
			{
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.0, 0.1};
				thisLayout.rowHeights = new int[] {63, 20};
				thisLayout.columnWeights = new double[] {0.1, 0.0};
				thisLayout.columnWidths = new int[] {7, 200};
				getContentPane().setLayout(thisLayout);
				{
					{
						buttonGroup1 = new ButtonGroup();
					}
					relevancePanel = new JPanel();
					getContentPane().add(relevancePanel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(6, 6, 6, 6), 0, 0));
					relevancePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Relevance", TitledBorder.LEADING, TitledBorder.TOP));
					{
						relevanceButton1 = new JButton();
						relevancePanel.add(relevanceButton1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						buttonGroup1.add(relevanceButton1);
						relevanceButton1.setRolloverEnabled(true);
						relevanceButton1.setBorderPainted(false);
						relevanceButton1.setSize(18, 22);
						relevanceButton1.setContentAreaFilled(false);
						relevanceButton1.setToolTipText("Irrelevant");
						relevanceButton1.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent arg0) {
								relevanceButtonPerformed("irrelevant");
							}
						});
					}
					
					{
						relevanceButton2 = new JButton();
						relevancePanel.add(relevanceButton2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						buttonGroup1.add(relevanceButton2);
						relevanceButton2.setRolloverEnabled(true);
						relevanceButton2.setBorderPainted(false);
						relevanceButton2.setSize(18, 22);
						relevanceButton2.setContentAreaFilled(false);
						relevanceButton2.setToolTipText("Related");
						relevanceButton2.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent arg0) {
								relevanceButtonPerformed("related");
							}
						});
					}
					
					{
						relevanceButton3 = new JButton();
						relevancePanel.add(relevanceButton3, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						buttonGroup1.add(relevanceButton3);
						relevanceButton3.setRolloverEnabled(true);
						relevanceButton3.setBorderPainted(false);
						relevanceButton3.setSize(18, 22);
						relevanceButton3.setContentAreaFilled(false);
						relevanceButton3.setToolTipText("Relevant");
						relevanceButton3.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent arg0) {
								relevanceButtonPerformed("relevant");
							}
						});
					}
				}
				{
					ListModel keywordsListModel = new DefaultComboBoxModel();
										
					keywordsList = new JList();
					getContentPane().add(keywordsList, new GridBagConstraints(0, 0, 2, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0));
					getContentPane().add(getCancelButton(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
					getContentPane().add(getJButtonHelp(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					keywordsList.setModel(keywordsListModel);
					keywordsList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Query / Keywords", TitledBorder.LEADING, TitledBorder.TOP));
					keywordsList.setBackground(new java.awt.Color(243,243,243));
					keywordsList.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent evt) {
							refreshRelevanceButtons();
						}
					});
					refreshListModel();

				}
				
				{
					this.setVisible(true);
					this.setTitle("Publication - " + this.pubResume.getOtherID());
					
					//this.setPreferredSize(new java.awt.Dimension(800, 500));
					//this.setMinimumSize(new java.awt.Dimension(800,200));
					this.setSize(900,600);
					Utilities.centerOnOwner(this);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void relevanceButtonPerformed(String relevance){
		
		int index = this.keywordsList.getSelectedIndex();
				
		if(index==-1)
			return;
		List<RelevanceData> list = new ArrayList<RelevanceData>();
					
		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
			if (def.getID().equals("operations.selectrelevance"))
			{
				if(index==this.queryKeywords.size())
				{
					for(RelevanceData pair: this.queryKeywords)
					{
						pair.setPreviousRelevance(pair.getRelevance());
						list.add(pair);
						pair.setRelevance(relevance);
					}
					if(this.rel_changed!=null)
						this.rel_changed.setNew_relevance(relevance);
				}
				else
				{
					int idquery = this.queryKeywords.get(index).getIdQuery();
					
					RelevanceData data = this.queryKeywords.get(index);
					data.setPreviousRelevance(data.getRelevance());
					list.add(data);
					data.setRelevance(relevance);
					if(rel_changed!=null)
						if(idquery==rel_changed.getIdquery())
							this.rel_changed.setNew_relevance(relevance);
				}
				
				ParamSpec[] paramsSpec = new ParamSpec[]{
						/** DB */
						new ParamSpec("database",IDatabase.class,this.db,null),
						new ParamSpec("publication",Publication.class,this.pubResume,null),
						new ParamSpec("listchangedata",List.class,list,null),
					};
				Core.getInstance().executeOperation(def, null, paramsSpec);			
				refreshListModel();
				this.finish();	
				return;
			}
		}	
	}
	
	private void refreshListModel(){
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		
		for(RelevanceData pair: this.queryKeywords)
			model.addElement(pair);
		model.addElement("All Queries");
		this.keywordsList.setModel(model);
	}
	
	public void finish() {
		this.setEnabled(false);
		this.setVisible(false);
		this.dispose();		
	}
	
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Close");
			cancelButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/cancel.png")));
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					finish();
				}
			});
		}
		return cancelButton;
	}
	
		
	private void refreshRelevanceButtons(){
		int index = this.keywordsList.getSelectedIndex();
		String relevance;
		if(index==this.queryKeywords.size())
			relevance = getAverageRelevance();
		else
			relevance = this.queryKeywords.get(index).getRelevance();

			if(relevance == null)
			{
				this.relevanceButton1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
				this.relevanceButton1.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
				this.relevanceButton2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
				this.relevanceButton2.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
				this.relevanceButton3.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
				this.relevanceButton3.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			}
			else if(relevance.compareTo("irrelevant")==0)
			{
				this.relevanceButton1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
				this.relevanceButton1.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
				this.relevanceButton2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
				this.relevanceButton2.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
				this.relevanceButton3.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
				this.relevanceButton3.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			}
			else if(relevance.compareTo("related")==0)
			{
				this.relevanceButton1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
				this.relevanceButton1.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
				this.relevanceButton2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
				this.relevanceButton2.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
				this.relevanceButton3.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
				this.relevanceButton3.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			}
			else
			{
				this.relevanceButton1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
				this.relevanceButton1.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
				this.relevanceButton2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
				this.relevanceButton2.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
				this.relevanceButton3.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
				this.relevanceButton3.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			}	
		}
	
	private String getAverageRelevance(){
	
		if(this.queryKeywords.size()==0)
			return null;
		
		boolean notVoted = true;
		
		int[] relevances = new int[this.queryKeywords.size()];
				
		for(int i=0;i<this.queryKeywords.size();i++)
		{
			if(this.queryKeywords.get(i).getRelevance()!=null)
			{	
				notVoted=false;
				if(this.queryKeywords.get(i).getRelevance().compareTo("irrelevant")==0)
					relevances[i]=1;
				else if(this.queryKeywords.get(i).getRelevance().compareTo("related")==0)
					relevances[i]=2;
				else
					relevances[i]=3;
			}
		}
		
		if(notVoted) // There is no relevation voted for the publication to any query
			return null;
		
		int relevance = MathUtils.getAverage(relevances);

		switch(relevance){
			case 1:
				return "irrelevant";
			case 2:
				return "related";
			default:
				return "relevant";
		}
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"Change_Relevance_Document");
				}
			});
		}
		return jButtonHelp;
	}

}
