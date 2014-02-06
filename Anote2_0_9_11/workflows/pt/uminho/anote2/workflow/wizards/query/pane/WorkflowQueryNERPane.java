package pt.uminho.anote2.workflow.wizards.query.pane;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.process.IE.ner.INERConfiguration;
import pt.uminho.anote2.workflow.gui.wizard.panes.ANERPaneWizard;
import pt.uminho.anote2.workflow.settings.query.WorkflowQueryDefaulSettings;

public class WorkflowQueryNERPane extends ANERPaneWizard{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public WorkflowQueryNERPane() throws SQLException, DatabaseLoadDriverException
	{
		super(getDefaultSettingskey());
	}
	
	public WorkflowQueryNERPane(INERConfiguration conf) throws SQLException, DatabaseLoadDriverException
	{
		super(conf);
	}

	private static List<String> getDefaultSettingskey() {
		List<String> list = new ArrayList<String>();
		list.add(WorkflowQueryDefaulSettings.USE_PARTIAL_MATCH_WITH_DICTIONARIES);
		list.add(WorkflowQueryDefaulSettings.CASE_SENSITIVE);
		list.add(WorkflowQueryDefaulSettings.PRE_PROCESSING);
		list.add(WorkflowQueryDefaulSettings.NORMALIZATION);
		list.add(WorkflowQueryDefaulSettings.LEXICAL_RESOURCE_STOPWORDS_ID);
		list.add(WorkflowQueryDefaulSettings.RULES_RESOURCE_ID);
		return list;
	}

	@Override
	protected void defaultSetting() {
		/** this not working correctly ... when have two NER we need two check */
//		jComboBoxNERProcesses.setSelectedItem(PropertiesManager.getPManager().getProperty(WorkflowGeneralDefaulSettings.NER));
//		changeMainPanel();
		
	}


}
