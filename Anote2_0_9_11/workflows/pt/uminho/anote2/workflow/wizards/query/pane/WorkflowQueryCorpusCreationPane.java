package pt.uminho.anote2.workflow.wizards.query.pane;

import pt.uminho.anote2.core.corpora.ICorpusCreateConfiguration;
import pt.uminho.anote2.core.document.CorpusTextType;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.workflow.gui.wizard.panes.ACorpusPaneWizard;
import pt.uminho.anote2.workflow.settings.query.WorkflowQueryDefaulSettings;

public class WorkflowQueryCorpusCreationPane extends ACorpusPaneWizard{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public WorkflowQueryCorpusCreationPane(boolean selectTextType) {
		super(selectTextType);
	}

	@Override
	protected void defaultSettings() {
		jTextFieldName.setText(PropertiesManager.getPManager().getProperty(WorkflowQueryDefaulSettings.CORPUS_NAME).toString());
		if(selectTextType)
		{
			CorpusTextType corpusType = ((CorpusTextType) PropertiesManager.getPManager().getProperty(WorkflowQueryDefaulSettings.CORPUS_TYPE));
			if(corpusType.equals(CorpusTextType.Abstract))
			{
				jRadioButtonAbstract.setSelected(true);
			}
			else if(corpusType.equals(CorpusTextType.FullText))
			{
				jRadioButtonFullText.setSelected(true);
				changeJournalRetrievalOption();
				jCheckBoxRetrievalPDFs.setSelected(Boolean.parseBoolean(PropertiesManager.getPManager().getProperty(WorkflowQueryDefaulSettings.CORPUS_RETRIEVAL_PDF).toString()));
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
