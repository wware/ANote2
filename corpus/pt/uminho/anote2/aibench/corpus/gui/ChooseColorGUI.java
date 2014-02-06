package pt.uminho.anote2.aibench.corpus.gui;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;

import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.queries.QueriesGeneral;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class ChooseColorGUI extends JDialog {

	private static final long serialVersionUID = -2815075841907054416L;
	private JColorChooser jColorChooser1;
	private JButton okButton;
	private JButton cancelButton;
	private JPanel buttonsPanel;
	
	private int classeID;
	private String color;
	private IDatabase db;

	public ChooseColorGUI(int classeID, String color,IDatabase db){
		super(Workbench.getInstance().getMainFrame());
		this.color = color;
		this.classeID = classeID;
		this.db=db;
		initGUI();
	}
	
	private void initGUI() {
		try {
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
							okButtonPerformed();
						}
					});
				}
			}
			{
				this.setSize(650, 450);
				this.setVisible(true);
				Utilities.centerOnOwner(this);
				this.setTitle("Change Class Color");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void okButtonPerformed(){
		Color c = this.jColorChooser1.getColor();
		
		if(!c.equals(Color.white) && !c.equals(Color.black))
		{						
			try{
				String color = "#" + Integer.toHexString(c.getRGB());
				color = color.replace("#ff", "#");
				updateColorOnDatabase(color);
				this.color=color;
				new ShowMessagePopup("Class Color Change !!!");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		finish();		
	}

	public String getColor() {
		return color;
	}

	private void updateColorOnDatabase(String color) {

		Connection con = db.getConnection();
		if(con==null)
		{
			
		}
		else
		{
			try {
				PreparedStatement updatecolorPS = con.prepareStatement(QueriesGeneral.updateColor);
				updatecolorPS.setString(1, color);
				updatecolorPS.setInt(2, classeID);
				updatecolorPS.execute();
				new ShowMessagePopup("Class Color Change !!!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void finish() {
		this.setVisible(false);
		this.dispose();		
	}
}
