package es.uvigo.ei.pdfDownloader;

import java.util.Observable;

/*import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;*/

//clase principal para la descarga de pdf's de art�culos
//@Operation(description="Retrieve pdf files asociated to PMIDs")
public class PubMedDownloader extends Observable{

	private String [] pubid;

	//@Port(direction=Direction.INPUT, name="PubMed Ids", order=-1)
	public void setPubMedId(String [] pubid){
		try{
			this.pubid=pubid;
			this.download(null);
		}catch(NullPointerException e){
			e.printStackTrace();
		}
	}
	
	public void setPubMedId(String [] pubid, PDFDownloadHandler handler){
		try{
			this.pubid=pubid;
			this.download(handler);
		}catch(NullPointerException e){
			e.printStackTrace();
		}
	}

	private void download(PDFDownloadHandler handler) {
		for(int i=0;i<pubid.length;i++)
		{
			String id=pubid[i];
			if (handler != null) handler.info("Inicio descarga " + id);
//			System.out.println(id);
//			System.out.println("Inicio descarga "+id);
//			DatosArticulo art=new DatosArticulo(id);
		  	/*boolean pdf_encontrado=*/WebConnectionConsola.buscarPDF(id, handler);
		  	
////		  	System.out.println("Fin descarga "+id);
//		  	if (pdf_encontrado) {
//		  		System.out.println("[Ok]");
//		  	}
//		    else
//		    {
//		      System.out.println("[Failed]");
//		     // Workbench.getInstance().error("Associated PDF can not be downloaded ");
//		    }
//		    System.out.println();
		}
	}
//	@Progress
	public WebConnectionConsola.MyProgress getProgress(){
		return WebConnectionConsola.getProgress();
	}
}