package pt.uminho.anote2.aibench.curator.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pt.uminho.anote2.aibench.utils.gui.Help;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class OpenUrlGUI extends JDialog{
	
	private static final long serialVersionUID = -1619557498896659153L;
	
	private JLabel urlLabel;
	private JPanel buttonsPanel;
	private JButton cancelButton;
	private JButton executeButton;
	private JTextField urlTextField;

	public OpenUrlGUI() {
		super(Workbench.getInstance().getMainFrame());
		initGUI();
		Image img = new ImageIcon(getClass().getClassLoader().getResource("icons/search_bio.png")).getImage();
		this.setIconImage(img);
	}
	
	private void initGUI() {
		try {
			{
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.0, 0.0};
				thisLayout.rowHeights = new int[] {41, 26};
				thisLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.1};
				thisLayout.columnWidths = new int[] {67, 217, 80, 7};
				getContentPane().setLayout(thisLayout);
			}			
			
			{
				urlTextField = new JTextField();
				getContentPane().add(urlTextField, new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0, 0));
			}
			{
				urlLabel = new JLabel();
				getContentPane().add(urlLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 10, 0, 0), 0, 0));
				urlLabel.setText("URL:");
			}
			{
				buttonsPanel = new JPanel();
				GridBagLayout buttonsPanelLayout = new GridBagLayout();
				getContentPane().add(buttonsPanel, new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 5, 10), 0, 0));
				buttonsPanelLayout.rowWeights = new double[] {0.1};
				buttonsPanelLayout.rowHeights = new int[] {7};
				buttonsPanelLayout.columnWeights = new double[] {0.1, 0.1};
				buttonsPanelLayout.columnWidths = new int[] {7, 7};
				buttonsPanel.setLayout(buttonsPanelLayout);
				{
					cancelButton = new JButton();
					buttonsPanel.add(cancelButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 1), 0, 0));
					cancelButton.setText("Cancel");
					cancelButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/cancel.png")));
					cancelButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							cancelButtonPerformed(evt);
						}
					});
				}
				{
					executeButton = new JButton();
					buttonsPanel.add(executeButton, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 1, 0, 0), 0, 0));
					executeButton.setText("Go");
					executeButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/apply.png")));
					executeButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							addButtonActionPerformed(evt);
						}
					});
				}
			}
			{
				this.setSize(374, 140);
				Utilities.centerOnOwner(this);
				this.setResizable(false);
				this.setTitle("Open URL");
				this.setVisible(true);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}

	
	private void addButtonActionPerformed(ActionEvent evt){
		if(this.urlTextField.getText().compareTo("")!=0)
		{
			Help.internetAcess(this.urlTextField.getText());
//			for (OperationDefinition def : Core.getInstance().getOperations())
//			{
//				if (def.getID().equals("operations.openbrowser"))
//				{
//					ParamSpec[] paramsSpec = new ParamSpec[]{ 
//							new ParamSpec("url", String.class, this.urlTextField.getText(), null)
//						};
//					Core.getInstance().executeOperation(def, null, paramsSpec);
//				}
//			}
			finish();
		}
		else
		{
			Workbench.getInstance().warn("Please insert a url!");
		}
	}
	
	private void cancelButtonPerformed(ActionEvent evt){
		this.finish();
	}
	
	public void finish() {
		this.setVisible(false);
		this.dispose();		
	}
}
