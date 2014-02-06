package pt.uminho.anote2.datastructures.utils.conf;

public class GlobalTextInfo {
	
	public final static String databasedeleteQueryInfo = "If you click “Ok”, this operation will delete the selected Query and all its information. To cancel the operation, press “Cancel”.";
	
	public final static String databasedeleteProcessInfo = "If you click “Ok”, this operation will delete the selected process from the Corpus and all its information. To cancel the operation, press “Cancel”.";

	public final static String databasedeleteCorpusInfo = "If you click “Ok”, this operation will delete the selected Corpus and all processes associated with it. To cancel the operation, press “Cancel”.";

	public final static String databasedeleteResourceInfo = "If you click “Ok”, this operation will delete the selected Lexical Resource and all its information.  To cancel the operation, press “Cancel”.";

	public final static String nerSelectNewNER = "Perform a new NER process based on lexical resources (Dictionaries, Lookup Tables, Rule Sets, Ontologies) matching terms in the text. Also, you can filter results using Stop Words (Lexical Words Resource) to remove common English words and improve results using the disambiguation process.";

	public final static String nerSelectAlreadyDoneNer = "Load configuration from an existent NER process using the same lexical resources and options (stop words and disambiguation).";

	public final static String createCorpus = "Setup wizard to create a new corpus. You can select queries and filter results using different criteria.";

	public final static String configurationWizardStepOne = "This is probably the first time you run @Note2 (…) (https://sourceforge.net/projects/anote2/). "+
															"Take a few seconds to configure your system. For help visit darwin.di.uminho.pt/anote2/wiki ."+
															"Best regards from the @Note2 development team ";
	
	public final static String configurationWizardStepTwo = " You must configure a proxy for internet access. If you don’t need to use a proxy, simply click “Next”; otherwise, check the “Use proxy” option and provide proxy credentials.";

	public final static String configurationWizardStepThree = "You need to configure the MySql database credentials by filling user and password fields. ";

	public final static String configurationWizardStepFour = "You can change @Note2 settings by selecting the option “Settings” in the menu bar as shown below. Press “Ok” to finish the configuration.";

	public final static String abnerInfo = "ABNER is A Biomedical Named Entity Recogniser. It uses machine learning (linear-chain conditional random fields, CRFs) to find entities such as genes, cell types, and DNA in text. Full details of ABNER can be found at http://pages.cs.wisc.edu/ bsettles/abner/";

	public final static String chemistryTaggerInfo = "Chemistry Tagger is designed to tag a number of chemistry items in running text. Currently the tagger tags compound formulas (e.g. SO2, H2O, H2SO4 ...) ions (e.g. Fe3+, Cl-) and element names and symbols (e.g. Sodium and Na). For more details visit http://gate.ac.uk/sale/tao/splitch21.html#sec:parsers:chemistrytagger";

	public final static String pleasewait = "Processing... please wait.";

}
