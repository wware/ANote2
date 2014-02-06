package pt.uminho.anote2.aibench.corpus.operations;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Properties;

import pt.uminho.anote2.aibench.corpus.cytoscape.CytoscapeOpen;
import pt.uminho.anote2.aibench.corpus.gui.report.NetworkExportReportGUI;
import pt.uminho.anote2.aibench.corpus.io.xgmml.REProcessToNetworkExtension;
import pt.uminho.anote2.aibench.corpus.settings.cytoscape.CytoscapeDefaultSettings;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.processes.ie.io.exporter.INetworkExportReport;
import pt.uminho.anote2.datastructures.exceptions.InvalidDirectoryException;
import pt.uminho.anote2.datastructures.exceptions.UnknownOperationSystemException;
import pt.uminho.anote2.datastructures.process.io.export.XGMMLNetworkExport;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.process.IE.IRESchema;
import pt.uminho.anote2.process.IE.network.IEdge;
import pt.uminho.anote2.process.IE.network.INetwork;
import pt.uminho.anote2.process.IE.network.INode;
import pt.uminho.anote2.process.IE.re.export.configuration.IREToNetworkConfiguration;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;

@Operation(enabled=false)
public class ExportRESchemaToXGMMLOperation {

	
	private IRESchema reProcess;
	private IREToNetworkConfiguration configuration;
	private REProcessToNetworkExtension process;
	private TimeLeftProgress progress = new TimeLeftProgress("Export RE Process to Cytoscape (XGMML) File");
	private boolean cancel = false;
	
	@Cancel(cancelInBackground=true)
	public void cancel()
	{
		if(process!=null)
		{
			process.setcancel();
			cancel = true;
		}
	}
	@Progress
	public TimeLeftProgress getProgress(){
		return progress ;
	}
	
	
	@Port(name="process",direction=Direction.INPUT,order=1)
	public void setREProcess(IRESchema reProcess)
	{
		this.reProcess=reProcess;
	}
	
	@Port(name="configurations",direction=Direction.INPUT,order=2)
	public void setConfiguration(IREToNetworkConfiguration configuration)
	{
		this.configuration=configuration;
	}
	
	@Port(name="file",direction=Direction.INPUT,order=3)
	public void setfile(File file)
	{
		System.gc();
		process = new REProcessToNetworkExtension(progress);
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		INetwork<INode, IEdge> network;
		try {
			network = process.getNetwork(reProcess , configuration);
			XGMMLNetworkExport exporter = new XGMMLNetworkExport(file , network);
			if(!cancel)
			{
				INetworkExportReport report = exporter.export();
				if(report.isFinishing())
				{
					long endTime = GregorianCalendar.getInstance().getTimeInMillis();
					report.setTime(endTime - startTime);
					new NetworkExportReportGUI(report);
					new ShowMessagePopup("REProcess Export Successfully");
					if(this.configuration.getREToNetworkConfigurationAutoOpen()!= null && 
							this.configuration.getREToNetworkConfigurationAutoOpen().autoOpen())
					{
						Properties properties = new Properties();
						properties.put(CytoscapeOpen.fileProperty, file.getAbsolutePath());		
						String cytoscapePath = PropertiesManager.getPManager().getProperty(CytoscapeDefaultSettings.CYTOSCAPE_DIRECTORY).toString();
						CytoscapeOpen.launch(cytoscapePath, properties);
					}
				}
				else
					new ShowMessagePopup("REProcess Export Fail");
			}
			else
			{
				new ShowMessagePopup("REProcess Export Cancel");
			}
		} catch (SQLException e) {
			new ShowMessagePopup("REProcess Export Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("REProcess Export Fail");
			e.printStackTrace();
		} catch (UnknownOperationSystemException e) {
			new ShowMessagePopup("Auto Open Cytoscape Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (InvalidDirectoryException e) {
			new ShowMessagePopup("Auto Open Cytoscape Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (IOException e) {
			new ShowMessagePopup("Auto Open Cytoscape Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		}


	}
	
}
