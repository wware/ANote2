package pt.uminho.anote2.aibench.corpus.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.utils.conf.Configuration;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class ClassEditGUI extends DialogGenericView{


	private static final long serialVersionUID = 1L;
	private JPanel tasksColorPanel;
	private Map<Integer,String> classIDclass;
	private Map<String,Integer> classclassID;
	private Map<Integer,String> classIDcolor;
	private IDatabase database;
	private JScrollPane colorsPanel;

	public ClassEditGUI() throws SQLException
	{
		super("Class Color Change");
		classIDclass = new HashMap<Integer, String>();
		classclassID = new HashMap<String, Integer>();
		classIDcolor = new HashMap<Integer, String>();
		initGUI();
		getDatabase();
		initClassesAndColors();
		insertButtons();
		this.setSize(600,400);
	}
	
	
	private void initClassesAndColors() throws SQLException {
		String color = new String();		
		classIDclass = ResourcesHelp.getClassIDClassOnDatabase(database);
		for(Integer classID:classIDclass.keySet())
		{
			classclassID.put(classIDclass.get(classID), classID);
			color = Corpora.getColorForClass(classID, database);
			classIDcolor.put(classID, color);
		}
	}
	
	private void getDatabase()
	{
		ArrayList<String> elements = new ArrayList<String>();
		elements.add("DB-Host");
		elements.add("DB-Port");
		elements.add("DB-Schema");
		elements.add("DB-User");
		elements.add("DB-Pwd");
		ArrayList<String> data = Configuration.getElementByXMLFile("conf/settings.conf",elements);
		database = new MySQLDatabase(data.get(0),data.get(1),data.get(2),data.get(3),data.get(4));
	}


	private void initGUI() {
		GridBagLayout thisLayout = new GridBagLayout();	
		thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.0};
		thisLayout.rowHeights = new int[] {7, 7, 7, 7};
		thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		thisLayout.columnWidths = new int[] {7, 7, 7, 7};
		this.setLayout(thisLayout);
		colorsPanel = new JScrollPane();
		colorsPanel.setViewportView(getTasksColorPanel());
		getContentPane().add(colorsPanel, new GridBagConstraints(0, 0, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getButtonsPanel(), new GridBagConstraints(0, 3, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}


	
	private JPanel getTasksColorPanel() {
		if (tasksColorPanel == null) {
			tasksColorPanel = new JPanel();
			GridBagLayout tasksColorPanelLayout = new GridBagLayout();
			tasksColorPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			tasksColorPanelLayout.rowHeights = new int[] {7, 7, 7};
			tasksColorPanelLayout.columnWeights = new double[] {0.0, 0.0, 0.1, 0.0, 0.1, 0.0, 0.1};
			tasksColorPanelLayout.columnWidths = new int[] {18, 30, 10, 30, 10, 30, 10};
			tasksColorPanel.setLayout(tasksColorPanelLayout);
			tasksColorPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		}
		return tasksColorPanel;
	}
	
	private void insertButtons(){

		int column=1, line=0;
		String color;
		
		for(final String classe:classclassID.keySet())
		{
			int classID = classclassID.get(classe);
			color = classIDcolor.get(classID);
			if(!color.equals("none"))
			{
				final JButton b = new JButton();
				b.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent evt) {
						changeColor(classe,String.valueOf(b.getBackground()), b);
					}
				});
				b.setSize(30, 20);
				b.setBackground(Color.decode(color));
				tasksColorPanel.add(b, new GridBagConstraints(column, line, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				
				JLabel l = new JLabel(classe);
				tasksColorPanel.add(l, new GridBagConstraints(column+1, line, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
				
				if(++line==3)
				{
					line=0;
					column+=2;
				}
			}
		}
	}
	
	private void changeColor(String classe,String color, Component cmp){
		final Component button = cmp;
		final String colorChange = color;
		int classID = classclassID.get(classe);
		final ChooseColorGUI gui = new ChooseColorGUI(classID,classe,database);
		gui.addWindowListener(new WindowListener(){
			
			public void windowActivated(WindowEvent arg0) {}		
			public void windowClosing(WindowEvent arg0) {}	
			public void windowDeactivated(WindowEvent arg0) {}	
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}					
			public void windowClosed(WindowEvent arg0) 
			{
				String newColor = gui.getColor();
				if(newColor.equals(colorChange))
				{
					
				}
				else
				{
					button.setBackground(Color.decode(newColor));
					tasksColorPanel.updateUI();
				}
			}		
		});
	}


	protected void okButtonAction() {
		finish();
	}

}
