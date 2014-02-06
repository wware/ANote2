package pt.uminho.anote2.aibench.publicationmanager.gui.help;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Properties;

import javax.swing.JPanel;

import pt.uminho.anote2.aibench.publicationmanager.gui.panes.PubmedSearchAdvancePanel;
import pt.uminho.anote2.datastructures.process.ir.configuration.IRSearchConfiguration;
import pt.uminho.anote2.process.IR.IIRSearchConfiguration;
import es.uvigo.ei.aibench.workbench.Workbench;



public class PubmedSearchOptionsPane extends APubmedSeach{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PubmedSearchAdvancePanel jPanelAdvancePAnel;
	
	public PubmedSearchOptionsPane()
	{
		initGUI();
	}

	private void initGUI() {
		GridBagLayout fieldsPanelLayout = new GridBagLayout();
		fieldsPanelLayout.rowWeights = new double[] {0.1};
		fieldsPanelLayout.rowHeights = new int[] {20};
		fieldsPanelLayout.columnWeights = new double[] {0.1};
		fieldsPanelLayout.columnWidths = new int[] {7};
		this.setLayout(fieldsPanelLayout);
		{
			this.add(getJPanelAdvancePAnel(), new GridBagConstraints(-1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}		
	}
	

	@Override
	public boolean validateOptions() {
		if(jPanelAdvancePAnel.getOrganism().length() == 0 && jPanelAdvancePAnel.getKeyWords().length() == 0)
		{
			Workbench.getInstance().warn("Please insert keywords or an organism");
			return false;
		}
		if(!jPanelAdvancePAnel.getToDate().equals("present") && Integer.decode(jPanelAdvancePAnel.getToDate())<Integer.decode(jPanelAdvancePAnel.getToDate()))
		{
			Workbench.getInstance().warn("Please select a valid date range.");
			return false;
		}
		return true;
	}

	@Override
	public IIRSearchConfiguration getConfiguration() {
		if(validateOptions())
		{
			Properties properties = new Properties();
			if(!jPanelAdvancePAnel.getAuthors().equals(""))
			{
				properties.put("authors",jPanelAdvancePAnel.getAuthors());
			}
			if(!jPanelAdvancePAnel.getJournal().equals(""))
			{
				properties.put("journal",jPanelAdvancePAnel.getJournal());
			}
			properties.put("fromDate",jPanelAdvancePAnel.getFromDate());
			if(!jPanelAdvancePAnel.getToDate().equals("present"))
			{
				properties.put("toDate",jPanelAdvancePAnel.getToDate());
			}
			if(!jPanelAdvancePAnel.getArticleType().equals("All"))
			{
				properties.put("articletype",jPanelAdvancePAnel.getArticleType());
			}
			if(jPanelAdvancePAnel.selectedJCheckBoxAll())
			{
				
			}
			else
			{
				if(jPanelAdvancePAnel.selectjCheckBoxAvailableAbstract())
				{
					properties.put("articleDetails","abstract");
					
				}
				else if(jPanelAdvancePAnel.selectJCheckBoxFreeFullText())
				{
					properties.put("articleDetails","freefulltext");
				
				}
				else
				{
					properties.put("articleDetails","fulltext");
						
				}
			}
			if(jPanelAdvancePAnel.selectJCheckBoxAllDocuments())
			{
				
			}
			else
			{
				if(jPanelAdvancePAnel.selectJCheckPubmed())
				{
					if(jPanelAdvancePAnel.selectJCheckBoxMedline())
					{
						properties.put("ArticleSource","medpmc");
					}
					else
					{
						properties.put("ArticleSource","pmc");
					}
				}
				else
				{
					properties.put("ArticleSource","med");
				}
			}
			String keywords = jPanelAdvancePAnel.getKeyWords();
			String organism = jPanelAdvancePAnel.getOrganism();
			return new IRSearchConfiguration(keywords, organism, properties);
		}	
		return null;
	}
	
	private JPanel getJPanelAdvancePAnel() {
		if(jPanelAdvancePAnel == null) {
			jPanelAdvancePAnel = new PubmedSearchAdvancePanel();
		}
		return jPanelAdvancePAnel;
	}

	@Override
	public void updateInfo(IIRSearchConfiguration pubmedSearchConfigutration) {
		jPanelAdvancePAnel.updateFields(pubmedSearchConfigutration);	
	}

}
