package pt.uminho.anote2.aibench.curator.view;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import pt.uminho.anote2.aibench.corpus.datatypes.NERDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.datatypes.REDocumentAnnotation;
import pt.uminho.anote2.aibench.curator.datastructures.document.CuratorDocument;
import pt.uminho.anote2.aibench.curator.view.panes.AnnotationLogPanel;
import pt.uminho.anote2.aibench.curator.view.panes.ColorAndDocumentPropertiesPanel;
import pt.uminho.anote2.aibench.curator.view.panes.NERCuratorPane;
import pt.uminho.anote2.aibench.curator.view.panes.RECuratorPane;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.annotation.log.AnnotationLog;
import pt.uminho.anote2.datastructures.annotation.log.AnnotationLogManagement;

public class CuratorView extends JPanel implements Observer {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSplitPane jSplitPaneMainPanelAndLog;
	private NERCuratorPane  nerCurator;
	private RECuratorPane reCurator;
	private CuratorDocument curatorDocument;
	private JPanel jPanelCuratorZoneAndColor;
	private NERDocumentAnnotation doc;
	private List<AnnotationLog> annotationLogs;
	private AnnotationLogPanel annotationLog;
	private ColorAndDocumentPropertiesPanel colorAndDocument;
	private int zoom = 12;

	public CuratorView(NERDocumentAnnotation doc){
		super();
		this.doc = doc;
		this.doc.addObserver(this);
		try {
			this.curatorDocument = new CuratorDocument(this, doc);
			inittGUI();
			initGUIJustForNER();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}

	public CuratorView(REDocumentAnnotation doc){
		super();
		this.doc = doc;
		this.doc.addObserver(this);
		try {
			this.curatorDocument = new CuratorDocument(this, doc);
			inittGUI();
			initGUIForNERandRE();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}

	}
	
	private List<AnnotationLog> getAnnotationLogs() throws SQLException, DatabaseLoadDriverException
	{
		if(annotationLogs==null)
		{
			annotationLogs = AnnotationLogManagement.getDocumentLogsInAnProcessAndCorpus(doc.getCorpus().getID(), doc.getProcess().getID(), doc.getID());
		}
		return annotationLogs;
	}
	
	public void addAnnotationLog(AnnotationLog log) throws SQLException, DatabaseLoadDriverException
	{
		AnnotationLogManagement.addDocumentLog(log);
		getAnnotationLogs().add(log);
		annotationLog.addAnnotationLogNode(log);
	}


	@Override
	public void update(Observable arg0, Object arg1) {
		if(nerCurator!=null)
		{
			nerCurator.update(arg0, arg1);
		}
		if(reCurator!=null)
		{
			reCurator.update(arg0, arg1);
		}
		if(colorAndDocument!=null)
		{
			try {
				colorAndDocument.updateColorPanel();
				colorAndDocument.updateUI();
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}
		}
	}
	
	private void inittGUI() throws SQLException, DatabaseLoadDriverException {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1};
			thisLayout.rowHeights = new int[] {7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(645, 496));
			{
				jSplitPaneMainPanelAndLog = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				jSplitPaneMainPanelAndLog.add(getJpanelCuratorZoneAndColor(), JSplitPane.TOP);
				this.add(jSplitPaneMainPanelAndLog, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				annotationLog = new AnnotationLogPanel(getAnnotationLogs());
				jSplitPaneMainPanelAndLog.add(annotationLog, JSplitPane.BOTTOM);
				jSplitPaneMainPanelAndLog.setResizeWeight(1.00);
			}
			
		}
		
	}
	
	private Component getJpanelCuratorZoneAndColor(){
		if(jPanelCuratorZoneAndColor==null)
		{
			jPanelCuratorZoneAndColor = new JPanel();
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.0};
			thisLayout.rowHeights = new int[] {7,7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			jPanelCuratorZoneAndColor.setLayout(thisLayout);
		}
			
		return jPanelCuratorZoneAndColor;
	}
	
	public NERDocumentAnnotation getAnnotatedDocument() {
		return doc;
	}



	private void initGUIJustForNER() throws SQLException, DatabaseLoadDriverException {
		nerCurator = new NERCuratorPane(curatorDocument);		
		jPanelCuratorZoneAndColor.add(nerCurator, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		colorAndDocument = new ColorAndDocumentPropertiesPanel(doc);
		jPanelCuratorZoneAndColor.add(colorAndDocument, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}
	
	private void initGUIForNERandRE() throws SQLException, DatabaseLoadDriverException {
		nerCurator = new NERCuratorPane(curatorDocument);
		reCurator = new RECuratorPane(curatorDocument);
		JTabbedPane jTabbedPaneNERCuratorAndRECurator = new JTabbedPane();
		jTabbedPaneNERCuratorAndRECurator.add("Entities",nerCurator);
		jTabbedPaneNERCuratorAndRECurator.add("Event / Relations",reCurator);
		jPanelCuratorZoneAndColor.add(jTabbedPaneNERCuratorAndRECurator, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));	
		colorAndDocument = new ColorAndDocumentPropertiesPanel((REDocumentAnnotation)doc);
		jPanelCuratorZoneAndColor.add(colorAndDocument, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(boolean zoomin) {
		if(zoomin)
		{
			this.zoom++;
		}
		else
			this.zoom--;
		if(nerCurator!=null)
		{
			nerCurator.changeZoom(zoomin);
		}
		if(reCurator!=null)
		{
			reCurator.changeZoom(zoomin);
		}
	}

}
