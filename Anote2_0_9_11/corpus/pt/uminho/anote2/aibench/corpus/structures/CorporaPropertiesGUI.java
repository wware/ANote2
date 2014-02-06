package pt.uminho.anote2.aibench.corpus.structures;

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
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.corpus.gui.ChooseClassColorGUI;
import pt.uminho.anote2.aibench.corpus.gui.ChooseVerbBackgroundColorGUI;
import pt.uminho.anote2.aibench.corpus.gui.ChooseVerbColorGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.conf.OtherConfigurations;

public class CorporaPropertiesGUI {
	
	public static synchronized JPanel getjPanelVerbColors() {
			JPanel jPanelVerbColor = new JPanel();
			GridBagLayout jPanelVerbColorLayout = new GridBagLayout();
			jPanelVerbColor.setBorder(BorderFactory.createTitledBorder(null, "Verb (Clue)", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
			jPanelVerbColorLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelVerbColorLayout.rowHeights = new int[] {7, 7, 20, 7};
			jPanelVerbColorLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelVerbColorLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelVerbColor.setLayout(jPanelVerbColorLayout);
			jPanelVerbColor.add(getJButtonVerbs(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelVerbColor.add(getJLabelVerbs(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelVerbColor.add(getJButtonVerbColorBackground(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelVerbColor.add(getJLabelVerbColorBAckground(), new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			return jPanelVerbColor;
	}
	

	
	private static JButton getJButtonVerbs() {
		JButton jButtonVerbs = new JButton();
		jButtonVerbs.setBackground(Color.decode(OtherConfigurations.getVerbColor()));
		jButtonVerbs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButtonVerbsActionPerformed(evt);
			}
		});
		return jButtonVerbs;
	}
	
	private static JLabel getJLabelVerbs() {
		JLabel jLabelVerbs = new JLabel();
		jLabelVerbs.setText("Verb");
		return jLabelVerbs;
	}
	
	private static void jButtonVerbsActionPerformed(ActionEvent evt) {
		final String colorOld = OtherConfigurations.getVerbColor();
		new ChooseVerbColorGUI(colorOld);
	}

	private static JButton getJButtonVerbColorBackground() {
		JButton jButtonVerbColorBackground = new JButton();
		jButtonVerbColorBackground.setBackground(Color.decode(OtherConfigurations.getVerbColorBackGround()));
		jButtonVerbColorBackground.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButtonVerbColorBackgroundActionPerformed(evt);
			}
		});
		return jButtonVerbColorBackground;
	}
	
	private static JLabel getJLabelVerbColorBAckground() {
		JLabel jLabelVerbColorBAckground = new JLabel();
		jLabelVerbColorBAckground.setText("BackGround");
		return jLabelVerbColorBAckground;
	}
	
	private static void jButtonVerbColorBackgroundActionPerformed(ActionEvent evt) {
		final String colorOld = OtherConfigurations.getVerbColorBackGround();
		new ChooseVerbBackgroundColorGUI(colorOld);
	}


	private static void insertButton(JPanel tasksColorPanel) throws SQLException, DatabaseLoadDriverException{

		int column=1;
		int line=0;
		String color;
		Map<Integer, String> test = ClassProperties.getClassIDClass();
		for(int classID: ClassProperties.getClassIDClass().keySet())
		{
			final int classIDNEw = classID;
			color = CorporaProperties.getCorporaClassColor(classID).getColor();
			
			if(!color.equals("none"))
			{
				final JButton b = new JButton();
				b.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent evt) {
						try {
							changeColor(classIDNEw, b);
						} catch (SQLException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						} catch (DatabaseLoadDriverException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						}
					}
				});
				b.setBorderPainted(false);
				b.setBackground(Color.decode(color));
				b.setOpaque(true);
				tasksColorPanel.add(b, new GridBagConstraints(column, line, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));		
				JLabel l = new JLabel(ClassProperties.getClassIDClass().get(classIDNEw));
				tasksColorPanel.add(l, new GridBagConstraints(column+1, line, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
				
				if(++line==3)
				{
					line=0;
					column+=2;
				}
			}
		}
	}
	
	
	private static void changeColor(int classID, Component cmp) throws SQLException, DatabaseLoadDriverException{
		final Component button = cmp;
		final int classIDNew = classID;
		final String colorNow = CorporaProperties.getCorporaClassColor(classID).getColor();
		final ChooseClassColorGUI color = new ChooseClassColorGUI(classIDNew, colorNow);
		color.addWindowListener(
				new WindowListener(){
			public void windowActivated(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) {}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}
									
			public void windowClosed(WindowEvent arg0) {
				if(!color.getNewColor().equals(colorNow))
				{
					try {
						button.setBackground(Color.decode(CorporaProperties.getCorporaClassColor(classIDNew).getColor()));
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			}
		});
		
	}

	public static synchronized JScrollPane getjScrollPaneClassColor() throws SQLException, DatabaseLoadDriverException {
		JScrollPane jScrollPaneClassColor = new JScrollPane();
		jScrollPaneClassColor.setViewportView(getTasksColorPanel());
		return jScrollPaneClassColor;
	}

	private static JPanel getTasksColorPanel() throws SQLException, DatabaseLoadDriverException {
		JPanel tasksColorPanel = new JPanel();
		GridBagLayout tasksColorPanelLayout = new GridBagLayout();
		tasksColorPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
		tasksColorPanelLayout.rowHeights = new int[] {7, 7, 7};
		tasksColorPanelLayout.columnWeights = new double[] {0.0, 0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0, 0.1};
		tasksColorPanelLayout.columnWidths = new int[] {18, 30, 10, 30, 10, 30, 10, 30, 10, 30, 10};
		tasksColorPanel.setLayout(tasksColorPanelLayout);
		tasksColorPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		insertButton(tasksColorPanel);
		return tasksColorPanel;
	}
	
	
}
