package pt.uminho.anote2.aibench.corpus.gui;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.datastructures.exceptions.ColorRestrictionException;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public abstract class ChooseColorGUI extends JDialog {

	private static final long serialVersionUID = -2815075841907054416L;
	private JColorChooser jColorChooser1;
	private JButton okButton;
	private JButton cancelButton;
	private JPanel buttonsPanel;
	private String color;
	private String newColor;
	private int classID;

	public ChooseColorGUI(String title,String color,int classID){
		super(Workbench.getInstance().getMainFrame());
		this.classID = classID;
		this.color = color;
		this.newColor = color;
		initGUI();
		this.setSize(650, 450);
		Utilities.centerOnOwner(this);
		this.setTitle(title);
		this.setModal(true);
		this.setVisible(true);
	}
	
	private void initGUI() {
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
		thisLayout.rowHeights = new int[] {7, 7, 7};
		thisLayout.columnWeights = new double[] {0.1};
		thisLayout.columnWidths = new int[] {7};
		getContentPane().setLayout(thisLayout);
		{
			jColorChooser1 = new JColorChooser();
			getContentPane().add(jColorChooser1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));		
		}
		{
			buttonsPanel = new JPanel();
			GridBagLayout buttonsPanelLayout = new GridBagLayout();
			getContentPane().add(buttonsPanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
			buttonsPanelLayout.rowWeights = new double[] {0.1};
			buttonsPanelLayout.rowHeights = new int[] {7};
			buttonsPanelLayout.columnWeights = new double[] {0.1, 0.1};
			buttonsPanelLayout.columnWidths = new int[] {7, 7};
			buttonsPanel.setLayout(buttonsPanelLayout);

			{
				cancelButton = new JButton();
				buttonsPanel.add(cancelButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 0, 3), 0, 0));
				cancelButton.setText("Cancel");
				cancelButton.setIcon(new ImageIcon(getClass()
						.getClassLoader().getResource("icons/cancel.png")));
				cancelButton.addActionListener(new ActionListener(){

					public void actionPerformed(ActionEvent arg0) {
						finish();
					}
				});
			}
			{
				okButton = new JButton();
				buttonsPanel.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 0, 3), 0, 0));
				okButton.setText("OK");
				okButton.setIcon(new ImageIcon(getClass().getClassLoader()
						.getResource("icons/apply.png")));
				okButton.addActionListener(new ActionListener(){

					public void actionPerformed(ActionEvent arg0) {
						Color c = jColorChooser1.getColor();
						String color = "#" + Integer.toHexString(c.getRGB());
						newColor = color.replace("#ff", "#");
						try {
							colorRestrition(c);
						} catch (ColorRestrictionException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						}
					}
				});
			}
		}

	}

	
	protected void colorRestrition(Color c) throws ColorRestrictionException {
		if(!c.equals(Color.white))
		{						
			okButtonPerformed(classID,newColor);
		}
		else
		{
			throw new ColorRestrictionException("white");
		}
	}
	
	public String getOldColor() {
		return color;
	}

	abstract void okButtonPerformed(int classID,String newColor) throws ColorRestrictionException;

	public String getNewColor() {
		return newColor;
	}



	protected void finish() {
		this.setModal(false);
		this.setVisible(false);
		this.dispose();		
	}
}
