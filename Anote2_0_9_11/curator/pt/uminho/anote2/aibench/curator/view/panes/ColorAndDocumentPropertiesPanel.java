package pt.uminho.anote2.aibench.curator.view.panes;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pt.uminho.anote2.aibench.corpus.datatypes.NERDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.datatypes.REDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.structures.CorporaPropertiesGUI;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;


public class ColorAndDocumentPropertiesPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private NERDocumentAnnotation doc;
	private DocumentPropertiesPanel documentPanel;
	private JScrollPane colorPanel;

	
	public ColorAndDocumentPropertiesPanel(REDocumentAnnotation doc) throws SQLException, DatabaseLoadDriverException
	{
		super();
		this.doc = doc;
		initGUI();
		initColorPanel();
	}
	
	public ColorAndDocumentPropertiesPanel(NERDocumentAnnotation doc) throws SQLException, DatabaseLoadDriverException
	{
		super();
		this.doc = doc;
		initGUI();
	}
	
	private void initColorPanel() {
		this.add(CorporaPropertiesGUI.getjPanelVerbColors(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
	}

	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1};
			thisLayout.rowHeights = new int[] {7};
			thisLayout.columnWeights = new double[] {0.1, 0.0, 0.025};
			thisLayout.columnWidths = new int[] {7, 7, 7};
			this.setLayout(thisLayout);
			{
				colorPanel = CorporaPropertiesGUI.getjScrollPaneClassColor();
				this.add(colorPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				documentPanel = new DocumentPropertiesPanel(doc);
				this.add(documentPanel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}		
	}
	
	public void updateColorPanel() throws SQLException, DatabaseLoadDriverException
	{
		this.remove(colorPanel);
		colorPanel = CorporaPropertiesGUI.getjScrollPaneClassColor();
		this.add(colorPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	

//	
}
