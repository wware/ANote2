package pt.uminho.anote2.aibench.resources.gui.loader;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.process.IE.io.export.DefaultDelimiterValue;
import pt.uminho.anote2.process.IE.io.export.Delimiter;
import pt.uminho.anote2.process.IE.io.export.TextDelimiter;
import pt.uminho.generic.csvloaders.DelimiterSelectionPanel;
import pt.uminho.generic.csvloaders.tabTable.FileTablePanel;

public class HeadedTableConfigurationPanel extends JPanel implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private JTabbedPane configTabPane;
	// private static final String extraInfoId = "Extra Info";
	protected DelimiterSelectionPanel genericSelectionPane;

	protected FileTablePanel genericLoaderPane;

	protected List<String> genericFileLines;
	protected Set<String> genericHeaders;

	private int numberOfLines = 20;

	// SGC - Create a Panel with empty tables
	public HeadedTableConfigurationPanel(boolean advanced,Set<String> genericHeaders) throws Exception {
		super();
		this.genericHeaders = genericHeaders;
		this.genericFileLines = new ArrayList<String>();
		initGUI(advanced);
	}
	
	public HeadedTableConfigurationPanel(boolean advanced,String filePath,Set<String> genericHeaders)
			throws Exception {
		super();
		this.genericHeaders = genericHeaders;
		setFile(filePath);
		initGUI(advanced);
	}

	private void initGUI(boolean advanced) {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.0, 1.0};
			thisLayout.rowHeights = new int[] {0, 0, 7};
			thisLayout.columnWeights = new double[] { 1.0 };
			thisLayout.columnWidths = new int[] { 7 };
			this.setLayout(thisLayout);
			genericSelectionPane = new DelimiterSelectionPanel(advanced);
			genericSelectionPane.setBorder(BorderFactory.createTitledBorder("General Delimiter"));
			this.add(genericSelectionPane, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));

			genericSelectionPane.addListener(this, "update");
			genericSelectionPane.addKeyListener(this, "update");

			configTabPane = new JTabbedPane();
			this.add(configTabPane, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));
			insertPanels();
			configTabPane.addTab("File", genericLoaderPane);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	// SGC - insert the panel of metabolites and reactions
	// in ReacDatabaseHeadedTableConfigPanel this method is Override
	public void insertPanels() {
		genericLoaderPane = new FileTablePanel(genericFileLines,genericHeaders);
	}
	
	private void editFilterPAnel() throws Exception
	{
		genericLoaderPane.setLines(genericFileLines);
		genericLoaderPane.createTable();
	}

	public void actionPerformed(ActionEvent e) {
		Delimiter d;
		TextDelimiter t;
		String command = e.getActionCommand();
		try {
			if (command.equalsIgnoreCase("update")) {
				d = genericSelectionPane.getGeneralDelimiter();
				genericLoaderPane.setGeneralSeparator(d);
				genericLoaderPane.createTable();
			}
		} catch (Exception ex) {
			TreatExceptionForAIbench.treatExcepion(ex);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyReleased(KeyEvent e) {
		try {
			if (genericSelectionPane.myEquals(e.getSource())) {
				Delimiter d = genericSelectionPane.getGeneralDelimiter();
				genericLoaderPane.setGeneralSeparator(d);
				genericLoaderPane.createTable();
			}
		} catch (Exception ex) {
			TreatExceptionForAIbench.treatExcepion(ex);
 		}
	}

	public Map<String, Integer> getGenericHeaders() {
		return genericLoaderPane.getHeaders();
	}

	public Delimiter getGeneralDelimiter() {
		return this.genericSelectionPane.getGeneralDelimiter();
	}
	
	public Delimiter getSynonymsDelimiter()
	{
		return this.genericSelectionPane.getSynonymsDelimiter();
	}
	
	public Delimiter getExternalIDsDelimiter()
	{
		return this.genericSelectionPane.getExternalIDDelimiter();
	}
	
	public Delimiter getExternalIDSourceDelimiter()
	{
		return this.genericSelectionPane.getExternalIDSourceDelimiter();
	}
	
	public TextDelimiter getTextDelimiter()
	{
		return this.genericSelectionPane.gettextDelimiter();
	}
	
	public DefaultDelimiterValue getDefaultValue()
	{
		return this.genericSelectionPane.getDefaultValue();
	}

	public boolean getMetHasHeaders() {
		return genericLoaderPane.getHasHeaders();
	}

	public boolean hasSpecificSize() {
		return true;
	}

	public int getXdimension() {
		return 800;
	}

	public int getYdimension() {
		return 600;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void setGenericHeaders(Set<String> genericHeaders) {
		this.genericHeaders = genericHeaders;
	}
	
	public void setFile(String filePath) throws Exception {
		File f = new File(filePath);
		if (f != null && !f.exists())
			throw new Exception("The file " + f.getAbsolutePath()
					+ " does not exist!");
		this.genericFileLines = new ArrayList<String>();
		FileInputStream in = new FileInputStream(filePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		int i = 0;
		while ((strLine = br.readLine()) != null && i < numberOfLines) {
			genericFileLines.add(strLine);
			i++;
		}
		editFilterPAnel();
	}
	
	public boolean getHasHeaders()
	{
		return genericLoaderPane.getHasHeaders();
	}
	
//	/**
//	 * Auto-generated main method to display this JPanel inside a new JFrame.
//	 * 
//	 * @throws Exception
//	 */
//	public static void main(String[] args) throws Exception {
//		JFrame frame = new JFrame();
//		String metfile = "files/resources/dictionaries/csv/csvsmallfile.csv";
//		try {
//			frame.getContentPane().add(new HeadedTableConfigurationPanel(metfile));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//		frame.pack();
//		frame.setVisible(true);
//	}

}
