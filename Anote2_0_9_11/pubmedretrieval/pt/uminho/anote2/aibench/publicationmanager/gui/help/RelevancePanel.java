package pt.uminho.anote2.aibench.publicationmanager.gui.help;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.core.document.RelevanceType;


public class RelevancePanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RelevanceType relevance;
	private ButtonGroup buttonGroup;
	private JButton relevanceButton1;
	private JButton withoutRelevance;
	private JButton relevanceButton2;
	private JButton relevanceButton3;
	private IPublication publication;
	private QueryInformationRetrievalExtension query;

	public RelevancePanel(QueryInformationRetrievalExtension query, IPublication publication) throws SQLException, DatabaseLoadDriverException
	{
		this.query=query;
		this.publication = publication;
		initGUI();
		this.setSize(80, 30);
		refreshRelevanceButtons(query.getDocumentRelevanceType(publication.getID()));
		this.setOpaque(false);
	}
	
	public RelevancePanel(QueryInformationRetrievalExtension query)
	{
		this.query=query;
		initGUI();
		this.setSize(80, 30);
		this.setOpaque(false);
		relevance = RelevanceType.none;
		refreshRelevanceButtons(relevance);
	}
	

	private void initGUI() {
		buttonGroup = new ButtonGroup();
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1};
		thisLayout.rowHeights = new int[] {7};
		thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		thisLayout.columnWidths = new int[] {7, 7, 7, 7};
		this.setLayout(thisLayout);
		{
			withoutRelevance = new JButton();
			buttonGroup.add(withoutRelevance);
			withoutRelevance.setRolloverEnabled(true);
			withoutRelevance.setBorderPainted(false);
			withoutRelevance.setSize(18, 22);
			withoutRelevance.setContentAreaFilled(false);
			withoutRelevance.setToolTipText("Without Relevance");
			withoutRelevance.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					try {
						relevanceButtonPerformed(RelevanceType.none);
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (DatabaseLoadDriverException e) {
						e.printStackTrace();
					}
				}
			});
			this.add(withoutRelevance, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		}
		{
			relevanceButton1 = new JButton();
			buttonGroup.add(relevanceButton1);
			relevanceButton1.setRolloverEnabled(true);
			relevanceButton1.setBorderPainted(false);
			relevanceButton1.setSize(18, 22);
			relevanceButton1.setContentAreaFilled(false);
			relevanceButton1.setToolTipText("Irrelevant");
			relevanceButton1.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					try {
						relevanceButtonPerformed(RelevanceType.irrelevant);
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (DatabaseLoadDriverException e) {
						e.printStackTrace();
					}
				}
			});	
			this.add(relevanceButton1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		{
			relevanceButton2 = new JButton();
			buttonGroup.add(relevanceButton2);
			relevanceButton2.setRolloverEnabled(true);
			relevanceButton2.setBorderPainted(false);
			relevanceButton2.setSize(18, 22);
			relevanceButton2.setContentAreaFilled(false);
			relevanceButton2.setToolTipText("Related");
			relevanceButton2.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					try {
						relevanceButtonPerformed(RelevanceType.related);
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (DatabaseLoadDriverException e) {
						e.printStackTrace();
					}
				}
			});
			this.add(relevanceButton2, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		{
			relevanceButton3 = new JButton();
			buttonGroup.add(relevanceButton3);
			relevanceButton3.setRolloverEnabled(true);
			relevanceButton3.setBorderPainted(false);
			relevanceButton3.setSize(18, 22);
			relevanceButton3.setContentAreaFilled(false);
			relevanceButton3.setToolTipText("Relevant");
			relevanceButton3.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					try {
						relevanceButtonPerformed(RelevanceType.relevant);
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (DatabaseLoadDriverException e) {
						e.printStackTrace();
					}
				}
			});
			this.add(relevanceButton3, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
	}


	private void refreshRelevanceButtons(RelevanceType relevance){		
		if(relevance == RelevanceType.none)
		{
			this.withoutRelevance.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.withoutRelevance.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton1.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.relevanceButton2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton2.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.relevanceButton3.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton3.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
		}
		else if(relevance == RelevanceType.irrelevant)
		{
			this.withoutRelevance.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.withoutRelevance.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.relevanceButton1.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton2.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.relevanceButton3.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton3.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
		}
		else if(relevance == RelevanceType.related)
		{
			this.withoutRelevance.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.withoutRelevance.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.relevanceButton1.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.relevanceButton2.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton3.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton3.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
		}
		else
		{
			this.withoutRelevance.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.withoutRelevance.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.relevanceButton1.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.relevanceButton2.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton3.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.relevanceButton3.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
		}	
	}


	public RelevanceType getRelevance() {
		return relevance;
	}

	public void setRelevance(RelevanceType relevance) {
		this.relevance = relevance;
	}
	
	private void relevanceButtonPerformed(RelevanceType newRelevance) throws SQLException, DatabaseLoadDriverException{
		if(publication!=null)
		{
			query.updateRelevance(publication, newRelevance);
		}
		refreshRelevanceButtons(newRelevance);
	}

}
