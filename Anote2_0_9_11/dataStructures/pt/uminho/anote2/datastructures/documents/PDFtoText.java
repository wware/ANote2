package pt.uminho.anote2.datastructures.documents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDDocumentCatalog;
import org.pdfbox.pdmodel.PDPage;
import org.pdfbox.pdmodel.PDResources;
import org.pdfbox.util.PDFTextStripper;

import pt.uminho.anote2.core.document.IPDFtoTXT;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;

public class PDFtoText implements IPDFtoTXT{

	public PDFtoText()
	{
		
	}

	public String convertPDFDocument(String url) {
		return pdfToTxt(url);
	}
	
	private String pdfToTxt(String url) {
		
		String text = new String();
		try {
			File file = new File(url);
			PDFParser parser = new PDFParser(new FileInputStream(file));
			parser.parse();
			PDDocument doc = parser.getPDDocument();
			
		    PDDocumentCatalog catalog = doc.getDocumentCatalog();
		    
		    for (Object pageObj :  catalog.getAllPages()) {
		    	PDPage page = (PDPage) pageObj;
		    	PDResources resources = page.findResources();
		    	resources.getImages().clear();
		    }
			PDFTextStripper stripper = new PDFTextStripper();
			text = stripper.getText(doc);
			text = NormalizationForm.removeOffsetProblemSituation(text);	
			doc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}


	
}



	
