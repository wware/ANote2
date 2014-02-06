package es.uvigo.ei.pdfDownloader;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.httpclient.HttpException;
import org.xml.sax.SAXException;

//import es.uvigo.ei.sing.PMID2LinkOut;

public class DatosArticulo
{
  private String pmid; //pmid del articulo
  private String [] enlacesPDF; //url's iniciales a partir de las que buscar el pdf
  private TrozoArticulo trozo_texto; //abstract o t�tulo del articulo
  
  //obtiene los datos del articulo a partir de su pmid
  public DatosArticulo(String id) throws HttpException, XPathExpressionException, IOException, SAXException, ParserConfigurationException 
  {
	pmid=id;
	enlacesPDF=AccesoEUtilsHTTPClient.getLinksWithHTTPClient(id);
	trozo_texto=AccesoEUtilsHTTPClient.getAbstractOrTitleWithHTTPClient(id);
  }
  
  public boolean tieneTrozoTexto() {
	return (trozo_texto!=null && trozo_texto.getTexto()!=null);
  }
  
  public TrozoArticulo getTrozo_texto() {
	return trozo_texto;
  }
  
  public String getPmid() {
	return pmid;
  }
  
  public String[] getEnlacesPDF() {
	return enlacesPDF;
  }
}