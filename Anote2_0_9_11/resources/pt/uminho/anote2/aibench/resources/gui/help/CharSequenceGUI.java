package pt.uminho.anote2.aibench.resources.gui.help;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.resources.gui.CreateRuleGUI;

import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class CharSequenceGUI extends JDialog {

	private static final long serialVersionUID = -2815075841907054416L;
	private JButton okButton;
	private JButton cancelButton;
	private JScrollPane charactersScrollPane;
	private JButton undoButton;
	private JTextField sequenceTextField;
	private JLabel sequenceLabel;
	private JPanel sequencePanel;
	private JCheckBox capsCheckBox;
	private JPanel charactersPanel;
	private JPanel buttonsPanel;
	
	private Stack<String> stack;
	private CreateRuleGUI parent_gui;

	public CharSequenceGUI(CreateRuleGUI p){
		super(p);
		this.setTitle("Character Sequence");
		this.parent_gui = p;
		initGUI();
		addButtons();
		this.stack = new Stack<String>();
		this.setSize(450,350);
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	
	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			getContentPane().setLayout(thisLayout);
			{
				buttonsPanel = new JPanel();
				buttonsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "", TitledBorder.LEADING, TitledBorder.TOP));
				GridBagLayout buttonsPanelLayout = new GridBagLayout();
				getContentPane().add(buttonsPanel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
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
				charactersScrollPane = new JScrollPane();
				getContentPane().add(charactersScrollPane, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					charactersPanel = new JPanel();
					GridBagLayout charactersPanelLayout = new GridBagLayout();
					charactersScrollPane.setViewportView(charactersPanel);
					charactersPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
					charactersPanelLayout.rowHeights = new int[] {7, 7, 7, 7};
					charactersPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
					charactersPanelLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
					charactersPanel.setLayout(charactersPanelLayout);
					{
						capsCheckBox = new JCheckBox();
						charactersPanel.add(capsCheckBox, new GridBagConstraints(8, 2, 2, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						capsCheckBox.setText("Caps");
					}
				}
			}
			{
				sequencePanel = new JPanel();
				GridBagLayout sequencePanelLayout = new GridBagLayout();
				getContentPane().add(sequencePanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				sequencePanelLayout.rowWeights = new double[] {0.1};
				sequencePanelLayout.rowHeights = new int[] {7};
				sequencePanelLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
				sequencePanelLayout.columnWidths = new int[] {7, 7, 7};
				sequencePanel.setLayout(sequencePanelLayout);
				{
					sequenceLabel = new JLabel();
					sequencePanel.add(sequenceLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(3, 2, 3, 2), 0, 0));
					sequenceLabel.setText("Sequence:");
				}
				{
					sequenceTextField = new JTextField();
					sequenceTextField.setEditable(false);
					sequenceTextField.setForeground(Color.BLUE);
					sequencePanel.add(sequenceTextField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 2, 3, 2), 0, 0));
				}
				{
					undoButton = new JButton();
					sequencePanel.add(undoButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(3, 2, 3, 2), 0, 0));
					undoButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/undo.png")));
					undoButton.setToolTipText("Undo");
					undoButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e) {
							undo();
						}
					});
					this.undoButton.setEnabled(false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addButtons(){
		int col, line=0;
		
		for(int i=0; i<=25; i++)
		{	
			final JButton button = new JButton();
			final char character = (char)(97+i);
			button.setText(String.valueOf(character));
			button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					if(capsCheckBox.isSelected())
						addChar((char)(((int) character) - 32));
					else
						addChar(character);
				}
			});
			
			line = (int) i/10;
			col = i%10;
			charactersPanel.add(button, new GridBagConstraints(col, line, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		
		int l=line+1;
		
		for(int i=0; i<=9; i++)
		{	
			final JButton button = new JButton();
			final char character = (char)(48+i);
			button.setText(String.valueOf(character));
			button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					addChar(character);	
				}
			});
			
			line = l + (int) i/10;
			col = i%10;
			charactersPanel.add(button, new GridBagConstraints(col, line, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
	}
	
	private void addChar(char c){
		String sequence = this.sequenceTextField.getText();
		this.stack.push(sequence);
		sequence = sequence + c;
		this.sequenceTextField.setText(sequence);
		if(!this.undoButton.isEnabled())
			this.undoButton.setEnabled(true);
	}
	
	private void undo(){
		String sequence = this.stack.pop();
		this.sequenceTextField.setText(sequence);
		if(stack.isEmpty())
			this.undoButton.setEnabled(false);
	}
	
	private void okButtonPerformed(){
		finish();
		this.parent_gui.addSequence(this.sequenceTextField.getText());
	}

	private void finish() {
		this.setVisible(false);
		this.dispose();		
	}
}
