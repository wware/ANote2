package es.uvigo.ei.pdfDownloader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

//clase usada para obtener el texto de un fichero pdf
public class TryPDFBox {
	public static String getTextoPDF(String archivo) {
		return TryPDFBox.getTextoPDF(archivo, null);
	}
	
	// obtiene el texto contenido en un fichero pdf dada la ruta en la
	// que se encuentra almacenado localmente
	public static String getTextoPDF(String archivo, PDFDownloadHandler handler) {
		String text = null;
		try {
			FileInputStream in = new FileInputStream(archivo);
			PDFParser parser = new PDFParser(in);
			parser.parse();
			PDDocument doc = parser.getPDDocument();
			if (doc != null) {
				PDFTextStripper stripper = new PDFTextStripper();
				text = stripper.getText(doc);
				doc.close();
			} else {
				if (handler != null)
					handler.error("Error, el archivo no es un PDF");
//				System.err.println("Error, el archivo no es un PDF");
			}
			in.close();
		} catch (FileNotFoundException fnfe) {
			if (handler != null)
				handler.error("No se ha encontrado el fichero " + archivo, fnfe);
			text = null;
//			System.err.println("No se ha encontrado el fichero " + archivo);
		} catch (IOException ioe) {
			if (handler != null)
				handler.error(ioe);
			text = null;
//			System.err.println("Error de E/S");
//			ioe.printStackTrace();
		} catch(Exception cl){
			if (handler != null)
				handler.error(cl);
		}

		return text;
	}
}