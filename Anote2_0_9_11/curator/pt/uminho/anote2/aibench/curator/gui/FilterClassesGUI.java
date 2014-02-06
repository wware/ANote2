package pt.uminho.anote2.aibench.curator.gui;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.datastructures.annotation.properties.AnnotationColorStyleProperty;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class FilterClassesGUI extends JDialog {

	private static final long serialVersionUID = -2815075841907054416L;

	private JButton okButton;
	private JButton cancelButton;
	private JScrollPane classesScrollPane;
	private JPanel entitiesPanel;
	private JPanel buttonsPanel;
	
	private Map<String,AnnotationColorStyleProperty> tasks;
	private List<String> ent_selected;
	private Point point;	
	
	public FilterClassesGUI(Map<String,AnnotationColorStyleProperty> tasks, Point point){
		super(Workbench.getInstance().getMainFrame());
		this.point = point;		
		this.tasks = tasks;
		this.ent_selected = new ArrayList<String>();
		for(String e: tasks.keySet())
			this.ent_selected.add(e);
		initGUI();
		this.setTitle("Filter Classes");
		this.setSize(GlobalOptions.smallWidth, GlobalOptions.smallHeight);
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
		this.setLocation(this.point);
	}
	
	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.0};
			thisLayout.rowHeights = new int[] {7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			getContentPane().setLayout(thisLayout);
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
							ent_selected=null;
							finish();
						}
					});
				}
				{
					okButton = new JButton();
					buttonsPanel.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 0, 3), 0, 0));
					okButton.setText("Apply");
					okButton.setIcon(new ImageIcon(getClass().getClassLoader()
							.getResource("icons/apply.png")));
					okButton.addActionListener(new ActionListener(){
						
						public void actionPerformed(ActionEvent arg0) {
							okButtonPerformed();
						}
					});
				}
				{
					classesScrollPane = new JScrollPane();
					buttonsPanel.add(classesScrollPane, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					classesScrollPane.setViewportView(getEntPanel());
					getContentPane().add(classesScrollPane, new GridBagConstraints(0, 0, 2, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 5, 0, 3), 0, 0));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void okButtonPerformed(){
		finish();		
	}

	private void finish() {
		this.setVisible(false);
		this.dispose();		
	}
	
	private JPanel getEntPanel() {
		if(entitiesPanel!=null)
		{
			entitiesPanel.removeAll();
			entitiesPanel.setVisible(false);
		}
		entitiesPanel = new JPanel();
		GridBagLayout entitiesPanelLayout = new GridBagLayout();
		entitiesPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		entitiesPanelLayout.rowHeights = new int[] {7, 7, 7, 7};
		entitiesPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		entitiesPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
		entitiesPanel.setLayout(entitiesPanelLayout);
		entitiesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Entities", TitledBorder.LEADING, TitledBorder.TOP));
	
		int i=0, col, line;

		for(String c : this.tasks.keySet())
		{
			String color = this.tasks.get(c).getColor();
			if(!color.equals("none"))
			{	
				final JCheckBox entcheckBox = new JCheckBox();
				entcheckBox.setText(c);
				entcheckBox.setSelected(true);
				entcheckBox.setForeground(Color.decode(color));
				entcheckBox.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						
						if(entcheckBox.isSelected())
							ent_selected.add(entcheckBox.getText());
						else
							ent_selected.remove(entcheckBox.getText());
					}
				});
				
				col = (int) i/4;
				line = i%4;
				entitiesPanel.add(entcheckBox, new GridBagConstraints(col, line, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				i++;
			}
		}
		return entitiesPanel;
	}

	public List<String> getEnt_selected() {
		return ent_selected;
	}
}
