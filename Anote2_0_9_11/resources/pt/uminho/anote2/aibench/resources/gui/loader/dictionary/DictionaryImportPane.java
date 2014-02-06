package pt.uminho.anote2.aibench.resources.gui.loader.dictionary;

import java.util.HashMap;
import java.util.Map;

import pt.uminho.anote2.aibench.resources.gui.loader.FileLoadAndDelimiterSelectionPane;
import pt.uminho.anote2.datastructures.exceptions.LoaderFileException;
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.csv.ColumnNames;
import pt.uminho.anote2.datastructures.utils.generic.CSVFileConfigurations;
import pt.uminho.anote2.datastructures.utils.generic.ColumnDelemiterDefaultValue;
import pt.uminho.anote2.datastructures.utils.generic.ColumnParameters;
import pt.uminho.anote2.process.IE.io.export.DefaultDelimiterValue;
import pt.uminho.anote2.process.IE.io.export.Delimiter;
import pt.uminho.anote2.process.IE.io.export.TextDelimiter;
import pt.uminho.generic.csvloaders.tabTable.GenericSCSVColumnsNAmes;

public class DictionaryImportPane extends FileLoadAndDelimiterSelectionPane{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DictionaryImportPane()
			throws Exception {
		super(true, GenericSCSVColumnsNAmes.All);
	}

	@Override
	public CSVFileConfigurations getconfigurations() {
		Map<String, ColumnParameters> columnNameColumnParameters = new HashMap<String, ColumnParameters>();
		Map<String, Integer> headers = delemiterSelection.getGenericHeaders();	
		DefaultDelimiterValue defaultDelimiter = delemiterSelection.getDefaultValue();
		ColumnParameters value = new ColumnParameters(headers.get(ColumnNames.term),Delimiter.USER, defaultDelimiter);
		columnNameColumnParameters.put(ColumnNames.term, value);
		ColumnParameters classe = new ColumnParameters(headers.get(ColumnNames.classe),Delimiter.USER, defaultDelimiter);
		columnNameColumnParameters.put(ColumnNames.classe, classe);
		Delimiter synonymDelimiter = delemiterSelection.getSynonymsDelimiter();
		if(headers.containsKey(ColumnNames.synonyms))
		{
			ColumnParameters synonymsValue = new ColumnParameters(headers.get(ColumnNames.synonyms),synonymDelimiter , defaultDelimiter);
			columnNameColumnParameters.put(ColumnNames.synonyms, synonymsValue);
		}
		if(headers.containsKey(ColumnNames.externalID))
		{
			Delimiter externalIDDelimiter = delemiterSelection.getExternalIDsDelimiter();
			Delimiter externalSourceDelimiter = delemiterSelection.getExternalIDSourceDelimiter();
			ColumnParameters externalIdColumn = new ColumnParameters(headers.get(ColumnNames.externalID),externalIDDelimiter,defaultDelimiter, externalSourceDelimiter);
			columnNameColumnParameters.put(ColumnNames.externalID, externalIdColumn);
		}
		ColumnDelemiterDefaultValue columsDelemiterDefaultValue = new ColumnDelemiterDefaultValue(columnNameColumnParameters);
		CSVFileConfigurations csv = new CSVFileConfigurations(delemiterSelection.getGeneralDelimiter(),
				delemiterSelection.getTextDelimiter(),
				delemiterSelection.getDefaultValue(),
				columsDelemiterDefaultValue,
				delemiterSelection.getHasHeaders());
		return csv;
	}

	@Override
	public void validateSettings() throws LoaderFileException {
		if(getFile().isEmpty())
		{
			throw new LoaderFileException("Please select file...");
		}
		Map<String, Integer> headers = delemiterSelection.getGenericHeaders();		
		if(!headers.containsKey(ColumnNames.term))
		{
			throw new LoaderFileException("Please select Term Column...");
		}
		if(!headers.containsKey(ColumnNames.classe))
		{
			throw new LoaderFileException("Please select Class Column...");
		}
//		if(!headers.containsKey(ColumnNames.synonyms))
//		{
//			throw new LoaderFileException("Please select Synonym Column...");
//		}
//		if(!headers.containsKey(ColumnNames.externalID))
//		{
//			throw new LoaderFileException("Please select ExternalID Column...");
//		}
		Delimiter generalDelimiter = delemiterSelection.getGeneralDelimiter();
		if(generalDelimiter.equals(Delimiter.USER) && generalDelimiter.getValue().isEmpty())
		{
			throw new LoaderFileException("Please define General Delimiter...");
		}
		TextDelimiter textDelimiter = delemiterSelection.getTextDelimiter();	
		if(textDelimiter.equals(TextDelimiter.USER) && textDelimiter.getValue().isEmpty())
		{
			throw new LoaderFileException("Please define Text Delimiter...");
		}
		Delimiter synonymDelimiter = delemiterSelection.getSynonymsDelimiter();
		if(headers.containsKey(ColumnNames.synonyms) && synonymDelimiter.equals(Delimiter.USER) && synonymDelimiter.getValue().isEmpty())
		{
			throw new LoaderFileException("Please define Synonym Delimiter...");
		}
		Delimiter externalIDDelimiter = delemiterSelection.getExternalIDsDelimiter();
		if(headers.containsKey(ColumnNames.externalID) && externalIDDelimiter.equals(Delimiter.USER) && externalIDDelimiter.getValue().isEmpty())
		{
			throw new LoaderFileException("Please define ExternalID Delimiter...");
		}
		Delimiter externalSourceDelimiter = delemiterSelection.getExternalIDSourceDelimiter();
		if(headers.containsKey(ColumnNames.externalID) && externalSourceDelimiter.equals(Delimiter.USER) && externalSourceDelimiter.getValue().isEmpty())
		{
			throw new LoaderFileException("Please define ExternalID/Source Delimiter...");
		}
		DefaultDelimiterValue defaultValue = delemiterSelection.getDefaultValue();
		if(defaultValue.equals(DefaultDelimiterValue.USER) && defaultValue.getValue().isEmpty())
		{
			throw new LoaderFileException("Please define Default Delimiter...");
		}
		if(generalDelimiter.getValue().equals(textDelimiter.getValue()))
		{
			throw new LoaderFileException("General Delimiter and Text Delimiter can not be the same...");
		}
		if(headers.containsKey(ColumnNames.synonyms) && generalDelimiter.getValue().equals(synonymDelimiter.getValue()))
		{
			throw new LoaderFileException("General Delimiter and Synonyms Delimiter cannot be the same...");
		}
		if(headers.containsKey(ColumnNames.externalID) && generalDelimiter.getValue().equals(externalIDDelimiter.getValue()))
		{
			throw new LoaderFileException("General Delimiter and ExternalID Delimiter cannot be the same...");
		}
		if(headers.containsKey(ColumnNames.externalID) && generalDelimiter.getValue().equals(externalSourceDelimiter.getValue()))
		{
			throw new LoaderFileException("General Delimiter and ExternalID/Source Delimiter cannot be the same...");
		}
		if(headers.containsKey(ColumnNames.externalID) && externalIDDelimiter.getValue().equals(externalSourceDelimiter.getValue()))
		{
			throw new LoaderFileException("ExternalID Delimiter and Source ExternalID cannot be the same...");
		}	
	}

}
