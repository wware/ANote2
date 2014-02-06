package pt.uminho.generic.genericpanel.filechooser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import es.uvigo.ei.aibench.workbench.Workbench;

/**
 * 
 * @author pmaia
 *
 */
public class FileChooserAibench extends JPanel {

	private static final long serialVersionUID = 1L;
	public final static String CHOOSE_FILE_ACTION_COMMAND = "chooseFile";
	final static JFileChooser fileChooser = new JFileChooser(new File("."));
	
	public boolean allowMultipleSelection = false;
	protected int returnVal;
	
	protected JButton button;
	protected JTextField field;
	
	public FileChooserAibench(boolean allowMultipleSelection){
		super();
		button = new JButton(new ImageIcon(getClass().getClassLoader().getResource("icons2/fileopen.png")));
		field = new JTextField();
		button.setActionCommand(CHOOSE_FILE_ACTION_COMMAND);
		field.setActionCommand(CHOOSE_FILE_ACTION_COMMAND);
		this.allowMultipleSelection = allowMultipleSelection;
		initGUI();
	}
	
	public FileChooserAibench(){
		this(false);
	}
	
	public void addListeners(ActionListener listener){
		this.button.addActionListener(listener);
		this.field.addActionListener(listener);
	}
	
	private void initGUI(){
		
		fileChooser.setMultiSelectionEnabled(allowMultipleSelection);
		
		this.setLayout(new BorderLayout());
		
		this.field.setEditable(false);
		this.field.setPreferredSize(new Dimension(150, field.getPreferredSize().height));
		this.button.setToolTipText("Browse the local filesystem to select a file");
		
		this.add(this.field, BorderLayout.CENTER);
		this.add(this.button, BorderLayout.EAST);
		
		this.field.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				showFileChooser();				
			}
		});
		
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				showFileChooser();
			}
		});
			
	}

	private void showFileChooser() {
		int option = fileChooser.showDialog(Workbench.getInstance().getMainFrame(), "Select");
		if (option == JFileChooser.APPROVE_OPTION) {
			File selected = fileChooser.getSelectedFile();
			if (selected != null)
				this.field.setText(selected.getPath());
		}		
		returnVal = option;
	}

	public boolean isAllowMultipleSelection() {
		return allowMultipleSelection;
	}

	public void setAllowMultipleSelection(boolean allowMultipleSelection) {
		this.allowMultipleSelection = allowMultipleSelection;
	}

	public File getSelectedFile(){
		return fileChooser.getSelectedFile();
	}
	
	public File[] getSelectedFiles(){
		if(allowMultipleSelection)
			return fileChooser.getSelectedFiles();
		else return new File[]{fileChooser.getSelectedFile()};
	}

	public int getReturnVal() {
		return returnVal;
	}

	public void setReturnVal(int returnVal) {
		this.returnVal = returnVal;
	}
}
