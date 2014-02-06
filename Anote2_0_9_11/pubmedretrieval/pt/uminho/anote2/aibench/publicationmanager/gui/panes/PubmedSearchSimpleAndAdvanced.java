package pt.uminho.anote2.aibench.publicationmanager.gui.panes;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Properties;

import javax.swing.JTabbedPane;

import pt.uminho.anote2.aibench.publicationmanager.gui.help.APubmedSeach;
import pt.uminho.anote2.datastructures.process.ir.configuration.IRSearchConfiguration;
import pt.uminho.anote2.process.IR.IIRSearchConfiguration;
import es.uvigo.ei.aibench.workbench.Workbench;

public class PubmedSearchSimpleAndAdvanced extends APubmedSeach{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTabbedPane jTabbedPaneMain;
	private PubmedSearchAdvancePanel jPanelAdvance;
	private PubmedSearchSimplePane jPanelBasic;

	public PubmedSearchSimpleAndAdvanced()
	{
		initGUI();
	}
	
	
	
	
	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(424, 317));
			thisLayout.rowWeights = new double[] {0.1};
			thisLayout.rowHeights = new int[] {7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			{
				jTabbedPaneMain = new JTabbedPane();
				this.add(jTabbedPaneMain, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					jPanelBasic = new PubmedSearchSimplePane();
					jTabbedPaneMain.addTab("Basic", null, jPanelBasic, null);
				}
				{
					jPanelAdvance = new PubmedSearchAdvancePanel();
					jTabbedPaneMain.addTab("Advanced", null, jPanelAdvance, null);
				}
			}
		}

	}



	@Override
	public boolean validateOptions() {
		if(jPanelBasic.isShowing())
		{
			if(jPanelBasic.getOrganism().length() == 0 && jPanelBasic.getKeyWords().length() == 0)
			{
				Workbench.getInstance().warn("Please insert keywords or an organism");
				return false;
			}
		}
		else
		{
			if(jPanelAdvance.getOrganism().length() == 0 && jPanelAdvance.getKeyWords().length() == 0)
			{
				Workbench.getInstance().warn("Please insert keywords or an organism");
				return false;
			}
			if(!jPanelAdvance.getToDate().equals("present") && Integer.decode(jPanelAdvance.getToDate())<Integer.decode(jPanelAdvance.getFromDate()))
			{
				Workbench.getInstance().warn("Please select a valid date range.");
				return false;
			}
		}
		return true;
	}

	@Override
	public IIRSearchConfiguration getConfiguration() {
		if(validateOptions())
		{
			if(jPanelBasic.isShowing())
			{
				Properties properties = new Properties();
				String fromDate = "1900";
				properties.put("fromDate",fromDate);
				properties.put("articleDetails","abstract");
				String keywords = jPanelBasic.getKeyWords();
				String organism = jPanelBasic.getOrganism();
				return new IRSearchConfiguration(keywords, organism, properties);
			}
			else
			{
				Properties properties = new Properties();
				if(!jPanelAdvance.getAuthors().isEmpty())
				{
					properties.put("authors",jPanelAdvance.getAuthors());
				}
				if(!jPanelAdvance.getJournal().isEmpty())
				{
					properties.put("journal",jPanelAdvance.getJournal());
				}
				properties.put("fromDate",jPanelAdvance.getFromDate());
				if(!jPanelAdvance.getToDate().equals("present"))
				{
					properties.put("toDate",jPanelAdvance.getToDate());
				}
				if(!jPanelAdvance.getArticleType().equals("All"))
				{
					properties.put("articletype",jPanelAdvance.getArticleType());
				}
				if(jPanelAdvance.selectedJCheckBoxAll())
				{
					
				}
				else
				{
					if(jPanelAdvance.selectjCheckBoxAvailableAbstract())
					{
						properties.put("articleDetails","abstract");
						
					}
					else if(jPanelAdvance.selectJCheckBoxFreeFullText())
					{
						properties.put("articleDetails","freefulltext");
					
					}
					else
					{
						properties.put("articleDetails","fulltext");
							
					}
				}
				if(jPanelAdvance.selectJCheckBoxAllDocuments())
				{
					
				}
				else
				{
					if(jPanelAdvance.selectJCheckPubmed())
					{
						if(jPanelAdvance.selectJCheckBoxMedline())
						{
							properties.put("ArticleSource","medpmc");
						}
						else
						{
							properties.put("ArticleSource","med");
						}
					}
					else
					{
						properties.put("ArticleSource","pmc");
					}
				}
				String keywords = jPanelAdvance.getKeyWords();
				String organism = jPanelAdvance.getOrganism();
				return new IRSearchConfiguration(keywords, organism, properties);
			}
			
		}	
		return null;
	}




	public void updateInfo(IIRSearchConfiguration pubmedSearchConfigutration) {
		if(pubmedSearchConfigutration.getProperties().size()==2)
		{
			jPanelBasic.updateFields(pubmedSearchConfigutration);
			jTabbedPaneMain.setSelectedComponent(jPanelBasic);
		}
		else
		{
			jPanelAdvance.updateFields(pubmedSearchConfigutration);
			jTabbedPaneMain.setSelectedComponent(jPanelAdvance);
		}
	}

}
