package pt.uminho.anote2.workflow.wizards.general.panes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.process.IE.ner.INERConfiguration;
import pt.uminho.anote2.workflow.gui.wizard.panes.ANERPaneWizard;
import pt.uminho.anote2.workflow.settings.general.WorkflowGeneralDefaulSettings;

public class WorkflowGeneralNERPane extends ANERPaneWizard{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public WorkflowGeneralNERPane() throws SQLException, DatabaseLoadDriverException
	{
		super(getDefaultSettingskey());
	}

	public WorkflowGeneralNERPane(INERConfiguration inerConfiguration) throws SQLException, DatabaseLoadDriverException {
		super(inerConfiguration);
	}

	private static List<String> getDefaultSettingskey() {
		List<String> list = new ArrayList<String>();
		list.add(WorkflowGeneralDefaulSettings.USE_PARTIAL_MATCH_WITH_DICTIONARIES);
		list.add(WorkflowGeneralDefaulSettings.CASE_SENSITIVE);
		list.add(WorkflowGeneralDefaulSettings.PRE_PROCESSING);
		list.add(WorkflowGeneralDefaulSettings.NORMALIZATION);
		list.add(WorkflowGeneralDefaulSettings.LEXICAL_RESOURCE_STOPWORDS_ID);
		list.add(WorkflowGeneralDefaulSettings.RULES_RESOURCE_ID);
		return list;
	}

	@Override
	protected void defaultSetting() {
		/** this not working correctly ... when have two NER we need two check */
//		jComboBoxNERProcesses.setSelectedItem(PropertiesManager.getPManager().getProperty(WorkflowGeneralDefaulSettings.NER));
//		changeMainPanel();
	}

}
