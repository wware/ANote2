package pt.uminho.anote2.workflow.wizards.general.panes;

import pt.uminho.anote2.core.document.CorpusTextType;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.workflow.gui.wizard.panes.ACorpusPaneWizard;
import pt.uminho.anote2.workflow.settings.general.WorkflowGeneralDefaulSettings;

public class WorkflowGeneralCorpusCreationPane extends ACorpusPaneWizard{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public WorkflowGeneralCorpusCreationPane(boolean selectTextType) {
		super(selectTextType);
	}

	@Override
	protected void defaultSettings() {
		jTextFieldName.setText(PropertiesManager.getPManager().getProperty(WorkflowGeneralDefaulSettings.CORPUS_NAME).toString());
		if(selectTextType)
		{
			CorpusTextType corpusType = ((CorpusTextType) PropertiesManager.getPManager().getProperty(WorkflowGeneralDefaulSettings.CORPUS_TYPE));
			if(corpusType.equals(CorpusTextType.Abstract))
			{
				jRadioButtonAbstract.setSelected(true);
			}
			else if(corpusType.equals(CorpusTextType.FullText))
			{
				jRadioButtonFullText.setSelected(true);
				changeJournalRetrievalOption();
				jCheckBoxRetrievalPDFs.setSelected(Boolean.parseBoolean(PropertiesManager.getPManager().getProperty(WorkflowGeneralDefaulSettings.CORPUS_RETRIEVAL_PDF).toString()));
			}
			else
			{
				jRadioButtonAbstract.setSelected(true);
			}
		}
		else
		{
			jRadioButtonAbstract.setSelected(true);
		}
	}

}
