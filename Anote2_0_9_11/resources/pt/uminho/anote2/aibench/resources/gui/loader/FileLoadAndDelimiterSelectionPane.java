package pt.uminho.anote2.aibench.resources.gui.loader;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JPanel;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.datastructures.exceptions.LoaderFileException;
import pt.uminho.anote2.datastructures.utils.generic.CSVFileConfigurations;
import pt.uminho.generic.genericpanel.filechooser.FileChooserPane;

public abstract class FileLoadAndDelimiterSelectionPane extends JPanel{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected FileChooserPane fileChooserPane;
	protected HeadedTableConfigurationPanel delemiterSelection;
	
	public FileLoadAndDelimiterSelectionPane(boolean advanced,Set<String> headers)  throws Exception
	{
		initGUI(advanced,headers);
	}
	
	private void initGUI(boolean advanced,Set<String> headers) throws Exception  {
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.0, 0.1, 0.0};
		thisLayout.rowHeights = new int[] {7, 7, 7};
		thisLayout.columnWeights = new double[] {0.1};
		thisLayout.columnWidths = new int[] {7};
		this.setLayout(thisLayout);
		this.add(getChoseFile(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getJPanelMainInfo(advanced,headers), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	private Component getJPanelMainInfo(boolean advanced,Set<String> headers) throws Exception {
		if(delemiterSelection == null)
		{
			delemiterSelection = new HeadedTableConfigurationPanel(advanced,headers);
		}
		return delemiterSelection;
	}

	private Component getChoseFile() {
		if(fileChooserPane==null)
		{
			fileChooserPane = new FileChooserPane();
			fileChooserPane.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(fileChooserPane.selectDirectory())
					{
						try {
							delemiterSelection.setFile(fileChooserPane.getSelectedFile().getAbsolutePath());
						} catch (Exception e) {
							TreatExceptionForAIbench.treatExcepion(e);
						}
					}	
				}
			});
		}
		return fileChooserPane;
	}
	
	public abstract CSVFileConfigurations getconfigurations();
	
	public abstract void validateSettings() throws LoaderFileException;
	
	public String getFile()
	{
		if(fileChooserPane.getSelectedFile()==null)
			return null;
		return fileChooserPane.getSelectedFile().getAbsolutePath();
	}
	
	public void setHeaderStrings(Set<String> headers)
	{
		delemiterSelection.setGenericHeaders(headers);
	}
	
}
