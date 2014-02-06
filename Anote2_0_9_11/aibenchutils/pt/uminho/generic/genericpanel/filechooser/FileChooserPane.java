package pt.uminho.generic.genericpanel.filechooser;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.utils.session.SessionSettings;

public class FileChooserPane extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textFile;
	private JButton selectFile;
	protected JFileChooser fileChooser;
	
	public static final String CHOOSE_FILE = "choose file";



	public FileChooserPane()
	{
		iniiGUI();
		String homeDirectory = SessionSettings.getSessionSettings().getSearchDirectory();
		if(homeDirectory!=null)
			fileChooser.setCurrentDirectory(new File(homeDirectory));
	}
	
	private void iniiGUI() {
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		GridBagLayout filePanelLayout = new GridBagLayout();
		filePanelLayout.rowWeights = new double[] {0.1};
		filePanelLayout.rowHeights = new int[] {7};
		filePanelLayout.columnWeights = new double[] {0.1, 0.0};
		filePanelLayout.columnWidths = new int[] {7, 7};
		this.setLayout(filePanelLayout);
		this.setBorder(BorderFactory.createTitledBorder(null, "Select File", TitledBorder.LEADING, TitledBorder.BELOW_TOP));
		{
			textFile = new JTextField();
			textFile.setEditable(false);
			this.add(textFile, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			textFile.setPreferredSize(new java.awt.Dimension(6, 30));
		}
		{
			selectFile = new JButton();
			selectFile.setText("File...");
			selectFile.setPreferredSize(new java.awt.Dimension(100, 30));
			this.add(selectFile, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
	}
	
	public boolean selectDirectory() {
		int returnVal = fileChooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			if(fileChooser.getSelectedFile()==null)
				return false;
			textFile.setText(fileChooser.getSelectedFile().getAbsolutePath());
			SessionSettings.getSessionSettings().setSearchDirectory(fileChooser.getSelectedFile().getParent());
			return true;
		}
		else
			return false;
	}
	
	public void addActionListener(ActionListener l)
	{
		selectFile.addActionListener(l);
	}

	public File getSelectedFile() {
		return fileChooser.getSelectedFile();
	}

}
