package pt.uminho.anote2.datastructures.documents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

import pt.uminho.anote2.core.document.IPDFtoTXT;

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
			PDFParser parser = new PDFParser(new FileInputStream(new File(url)));
			parser.parse();
			PDDocument doc = parser.getPDDocument();
			PDFTextStripper stripper = new PDFTextStripper();
			text = stripper.getText(doc);
			doc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
}
