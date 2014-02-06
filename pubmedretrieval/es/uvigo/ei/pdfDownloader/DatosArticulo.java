package es.uvigo.ei.pdfDownloader;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.httpclient.HttpException;
import org.xml.sax.SAXException;

//import es.uvigo.ei.sing.PMID2LinkOut;

//datos de un art�culo de entrada para el algoritmo de b�squeda del pdf
public class DatosArticulo
{
  private String pmid; //pmid del art�culo
  private String [] enlacesPDF; //url's iniciales a partir de las que buscar el pdf
  private TrozoArticulo trozo_texto; //abstract o t�tulo del art�culo
  
  //obtiene los datos del art�culo a partir de su pmid
  public DatosArticulo(String id) throws HttpException, XPathExpressionException, IOException, SAXException, ParserConfigurationException 
  {
//	System.out.println("1");
	pmid=id;
	//obtiene las urls con los atributos "full-text online", "full-text PDF" o "author manuscript"
	//(son las que pueden llevar al pdf de un art�culo), haciendo uso de HTTPClient
	enlacesPDF=AccesoEUtilsHTTPClient.getLinksWithHTTPClient(id);
//	System.out.println("2");
//	enlacesPDF=PMID2LinkOut.getLinks(id);
//	if(enlacesPDF==null || enlacesPDF.length==0)
//	{
//	  System.out.println("No se han encontrado url's sobre las que iniciar la b�squeda para el art�culo con pmid "+pmid);
//	  System.out.println("Puede ser debido a que el servidor de las eUtils del NCBI est� ca�do, deber�a intentarlo m�s tarde");
//	}
	trozo_texto=AccesoEUtilsHTTPClient.getAbstractOrTitleWithHTTPClient(id);
  }
  
  //indica si se dispone del abstract o t�tulo del art�culo a descargar
  public boolean tieneTrozoTexto() {
	return (trozo_texto!=null && trozo_texto.getTexto()!=null);
  }
  
  //devuelve el abstract o el t�tulo del art�culo (si no se dispone del abstract)
  public TrozoArticulo getTrozo_texto() {
	return trozo_texto;
  }
  
  //devuelve el pmid del art�culo
  public String getPmid() {
	return pmid;
  }
  
  //devuelve las url's iniciales a partir de las que buscar el pdf
  public String[] getEnlacesPDF() {
	return enlacesPDF;
  }
}