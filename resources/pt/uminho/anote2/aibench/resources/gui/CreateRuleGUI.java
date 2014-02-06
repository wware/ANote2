package pt.uminho.anote2.aibench.resources.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.resources.datatypes.Resources;
import pt.uminho.anote2.aibench.resources.datatypes.RulesAibench;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.exceptions.MissingDatatypesException;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.core.database.IDatabase;

import com.stevesoft.pat.Regex;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


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
public class CreateRuleGUI extends JDialog implements InputGUI{

	private static final long serialVersionUID = 4736901599859897970L;
	
	private JButton cancelButton;
	private JButton okButton;
	private JPanel buttonsPanel;
	
	private ParamsReceiver paramsRec = null;	
	private JPanel symbolsPanel;
	private JTextArea test1TextArea;
	private JButton jButtonHelp;
	private JComboBox jComboBoxRuleSet;
	private JPanel jPanelRuleSet;
	private JLabel jLabelClass;
	private JTextPane jTextPaneNewClass;
	private JPanel correctPanel;
	private JButton opcionalButton;
	private JPanel projectPanel;
	private JComboBox taskComboBox;
	private JButton charactersButton;
	private JButton ifenButton;
	private JButton spaceButton;
	private JButton testButton;
	private JEditorPane test2TextArea;
	private JScrollPane test2ScrollPane;
	private JScrollPane test1ScrollPane;
	private JPanel testPanel;
	private JButton clearButton;
	private JButton undoButton;
	private JButton zeroOrMoreButton;
	private JTextField reg_expTextField;
	private JButton oneOrMoreButton;
	private JButton digitsButton;
	private JButton capsCharButton;
	private JButton lowCharButton;

	private Stack<String> history;

	private List<JButton> qt_buttons;
	private boolean quantified=true;
	private Map<Integer,String> idclassesClasses;
	private RulesAibench rules;
	
	
	
	public CreateRuleGUI() throws MissingDatatypesException{
		super(Workbench.getInstance().getMainFrame());
		this.setTitle("Create a Rule");
		confirm();
		this.qt_buttons = new ArrayList<JButton>(); 		
		initGUI();
		setButtonActions();
		setQuantButtonsEnabled(false);
		history = new Stack<String>();
	}
	
	private void confirm() {
		List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(RulesAibench.class);
		if(items==null||items.size()==0)
		{
			Workbench.getInstance().warn("No RuleSet on clipboard");
			return;
		}
	}
	
	private void initGUI() throws MissingDatatypesException{

		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1, 0.1, 0.0, 0.1, 0.4, 0.0};
		thisLayout.rowHeights = new int[] {7, 7, 7, 20, 100, 7};
		thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.0};
		thisLayout.columnWidths = new int[] {7, 7, 7, 7};
		getContentPane().setLayout(thisLayout);
		
		{
			getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 5, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 2), 0, 0));
			getContentPane().add(getSymbolsPanel(), new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 0, 0), 0, 0));
			getContentPane().add(getReg_expTextField(), new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			getContentPane().add(getTestPanel(), new GridBagConstraints(0, 4, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 0, 2), 0, 0));
			getContentPane().add(getProjectPanel(), new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			getContentPane().add(getTaskComboBox(), new GridBagConstraints(2, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			getContentPane().add(getCorrectPanel(), new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(6, 2, 0, 2), 0, 0));
			getContentPane().add(getJTextPaneNewClass(), new GridBagConstraints(2, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			getContentPane().add(getJLabelClass(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			getContentPane().add(getJButtonHelp(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		{
			this.setSize(800,600);
			Utilities.centerOnOwner(this);
			this.setModal(true);
		}

	}
	
	
	
	public void finish() {
		this.setVisible(false);
		this.dispose();		
	}

	public void init(ParamsReceiver arg0, @SuppressWarnings("rawtypes") OperationDefinition arg1) {
		this.paramsRec = arg0;
		this.setTitle("Create Rule");
		this.setVisible(true);
	}

	public void onValidationError(Throwable arg0) {
		Workbench.getInstance().error(arg0);		
	}
	
	private JPanel getButtonsPanel() {
		if(buttonsPanel == null) {
			buttonsPanel = new JPanel();
			GridBagLayout buttonsPanelLayout = new GridBagLayout();
			buttonsPanelLayout.rowWeights = new double[] {0.1};
			buttonsPanelLayout.rowHeights = new int[] {7};
			buttonsPanelLayout.columnWeights = new double[] {0.1, 0.1};
			buttonsPanelLayout.columnWidths = new int[] {7, 7};
			buttonsPanel.setLayout(buttonsPanelLayout);
			buttonsPanel.add(getOkButton(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 1, 3, 4), 0, 0));
			buttonsPanel.add(getCancelButton(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 4, 3, 1), 0, 0));
		}
		return buttonsPanel;
	}
	
	private JButton getOkButton() {
		if(okButton == null) {
			okButton = new JButton();
			okButton.setText("Ok");
			okButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/apply.png")));
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {

					if(reg_expTextField.getText().equals(""))
					{
						Workbench.getInstance().warn("Rule Expression is Empty");
					}
					else
					{
						
						String classe = taskComboBox.getSelectedItem().toString();
						if(taskComboBox.getSelectedItem().equals("New Class"))
						{
							if(jTextPaneNewClass.getText().equals(""))
							{
								Workbench.getInstance().warn("Biological Class is empty");
								return;
							}
							else
							{
								classe=jTextPaneNewClass.getText();
							}

						}
						paramsRec.paramsIntroduced( new ParamSpec[]{
								new ParamSpec("rulesAibench", RulesAibench.class,rules, null),
								new ParamSpec("rule", String.class,reg_expTextField.getText(), null),
								new ParamSpec("ruleClass", String.class,classe, null)
						});
					}
				}
			});

		}
		return okButton;
	}
	
	private JButton getCancelButton() {
		if(cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/cancel.png")));
			cancelButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					finish();
				}
			});
		}
		return cancelButton;
	}
	
	
	private JPanel getSymbolsPanel() {
		if(symbolsPanel == null) {
			symbolsPanel = new JPanel();
			symbolsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Create Rule", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout symbolsPanelLayout = new GridBagLayout();
			symbolsPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			symbolsPanelLayout.rowHeights = new int[] {7, 7, 7};
			symbolsPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			symbolsPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
			symbolsPanel.setLayout(symbolsPanelLayout);
			symbolsPanel.setPreferredSize(new java.awt.Dimension(456, 135));
			symbolsPanel.add(getLowCharButton(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			symbolsPanel.add(getCapsCharButton(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			symbolsPanel.add(getDigitsButton(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			symbolsPanel.add(getZeroOtMoreButton(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			symbolsPanel.add(getOneOrMoreButton(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			symbolsPanel.add(getSpaceButton(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			symbolsPanel.add(getIfenButton(), new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			symbolsPanel.add(getCharactersButton(), new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			symbolsPanel.add(getOpcionalButton(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return symbolsPanel;
	}
	
	private JButton getLowCharButton() {
		if(lowCharButton == null) {
			lowCharButton = new JButton();
			lowCharButton.setText("a-z");
			lowCharButton.setToolTipText("Low Letters");
		}
		return lowCharButton;
	}
	
	private JButton getCapsCharButton() {
		if(capsCharButton == null) {
			capsCharButton = new JButton();
			capsCharButton.setText("A-Z");
			capsCharButton.setToolTipText("Caps Letters");
		}
		return capsCharButton;
	}
	
	private JButton getDigitsButton() {
		if(digitsButton == null) {
			digitsButton = new JButton();
			digitsButton.setText("0-9");
			digitsButton.setToolTipText("Digits");
		}
		return digitsButton;
	}
	
	private JButton getZeroOtMoreButton() {
		if(zeroOrMoreButton == null) {
			zeroOrMoreButton = new JButton();
			zeroOrMoreButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/zormore.png")));
			zeroOrMoreButton.setToolTipText("Zero or more");
			this.qt_buttons.add(zeroOrMoreButton);
		}
		return zeroOrMoreButton;
	}
	
	private JButton getOneOrMoreButton() {
		if(oneOrMoreButton == null) {
			oneOrMoreButton = new JButton();
			oneOrMoreButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/oneormore.png")));
			oneOrMoreButton.setToolTipText("One or more");
			this.qt_buttons.add(oneOrMoreButton);
		}
		return oneOrMoreButton;
	}
	
	private JButton getOpcionalButton() {
		if(opcionalButton == null) {
			opcionalButton = new JButton();
			opcionalButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/opt.png")));
			opcionalButton.setToolTipText("Optional");
			this.qt_buttons.add(opcionalButton);
		}
		return opcionalButton;
	}
	
	private JTextField getReg_expTextField() {
		if(reg_expTextField == null) {
			reg_expTextField = new JTextField();
			reg_expTextField.setEditable(false);
			reg_expTextField.setForeground(Color.BLUE);
		}
		return reg_expTextField;
	}
	
	private JButton getUndoButton() {
		if(undoButton == null) {
			undoButton = new JButton();
			undoButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/undo.png")));
			undoButton.setToolTipText("Undo");
		}
		return undoButton;
	}
	
	private JButton getClearButton() {
		if(clearButton == null) {
			clearButton = new JButton();
			clearButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/cancel.png")));		
			clearButton.setToolTipText("Clear");
		}
		return clearButton;
	}
	
	private JPanel getTestPanel() {
		if(testPanel == null) {
			testPanel = new JPanel();
			GridBagLayout testPanelLayout = new GridBagLayout();
			testPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			testPanelLayout.rowHeights = new int[] {7, 7, 7};
			testPanelLayout.columnWeights = new double[] {0.1, 0.0, 0.1};
			testPanelLayout.columnWidths = new int[] {7, 7, 7};
			testPanel.setLayout(testPanelLayout);
			testPanel.setBorder(BorderFactory.createTitledBorder("Test Rule"));
			testPanel.add(getTest1ScrollPane(), new GridBagConstraints(0, 0, 1, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			testPanel.add(getTest2ScrollPane(), new GridBagConstraints(2, 0, 1, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			testPanel.add(getTestButton(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return testPanel;
	}
	
	private JScrollPane getTest1ScrollPane() {
		if(test1ScrollPane == null) {
			test1ScrollPane = new JScrollPane();
			test1ScrollPane.setBorder(BorderFactory.createTitledBorder("Example Text"));
			test1ScrollPane.setViewportView(getTest1TextArea());
		}
		return test1ScrollPane;
	}
	
	private JTextArea getTest1TextArea() {
		if(test1TextArea == null) {
			test1TextArea = new JTextArea();
			test1TextArea.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
		}
		return test1TextArea;
	}
	
	private JScrollPane getTest2ScrollPane() {
		if(test2ScrollPane == null) {
			test2ScrollPane = new JScrollPane();
			test2ScrollPane.setBorder(BorderFactory.createTitledBorder("Result Text"));
			test2ScrollPane.setViewportView(getTest2TextArea());
		}
		return test2ScrollPane;
	}
	
	private JEditorPane getTest2TextArea() {
		if(test2TextArea == null) {
			test2TextArea = new JEditorPane();
			test2TextArea.setContentType("text/html");
			test2TextArea.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
			test2TextArea.setEditable(false);
		}
		return test2TextArea;
	}
	
	private JButton getTestButton() {
		if(testButton == null) {
			testButton = new JButton();
			testButton.setText("Test");
			testButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					testRegExp();				
				}
			});
		}
		return testButton;
	}
	
	private JButton getSpaceButton() {
		if(spaceButton == null) {
			spaceButton = new JButton();
			spaceButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/space_.png")));
			spaceButton.setToolTipText("Space");
		}
		return spaceButton;
	}
	
	private JButton getIfenButton() {
		if(ifenButton == null) {
			ifenButton = new JButton();
			ifenButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/ifen_.png")));
			ifenButton.setToolTipText("ifen");
		}
		return ifenButton;
	}
	
	private void setButtonActions(){
		
		clearButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				reg_expTextField.setText("");			
			}
		});
		
		undoButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				reg_expTextField.setText(history.pop());
				if(history.isEmpty())
					undoButton.setEnabled(false);

				setQuantButtonsEnabled(quantified);
				quantified = !quantified;
			}
		});
		
		zeroOrMoreButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				quantified=true;
				addToRegExp("{0,}");
				setQuantButtonsEnabled(false);
			}
		});
		
		opcionalButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				quantified=true;
				addToRegExp("?");
				setQuantButtonsEnabled(false);
			}
		});
		
		oneOrMoreButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				quantified=true;
				addToRegExp("{1,}");
				setQuantButtonsEnabled(false);
			}
		});
		
		digitsButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				addToRegExp("\\d");
				quantified=false;
				setQuantButtonsEnabled(true);
			}
		});
		
		capsCharButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				addToRegExp("[A-Z]");	
				quantified=false;
				setQuantButtonsEnabled(true);
			}
		});
		
		lowCharButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				addToRegExp("[a-z]");
				quantified=false;
				setQuantButtonsEnabled(true);
			}
		});
		
		spaceButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				addToRegExp("\\s");
				quantified=false;
				setQuantButtonsEnabled(true);
			}
		});
		
		ifenButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				addToRegExp("-");
				quantified=false;
				setQuantButtonsEnabled(true);
			}
		});
		
	}
	
	private void addToRegExp(String t){
		String reg_exp = reg_expTextField.getText();
		history.push(reg_exp);
		
		if(!quantified)
		{
			history.push(reg_exp + "{1,1}");
			t = "{1,1}" + t; 
		}
			
		reg_expTextField.setText(reg_exp + t);
		if(!undoButton.isEnabled())
			undoButton.setEnabled(true);
	}
	
	private void testRegExp() {

        Regex r = null;
        
        String text = this.test1TextArea.getText();
        String rexp = this.reg_expTextField.getText();
        
        String color = "#FF0000"; //((ANoteProperty) taskComboBox.getSelectedItem()).getColor();
        r = new Regex(rexp,"<FONT COLOR=" + color + "><b>$&</b></FONT>");
        
        String res = r.replaceAll(text);
        test2TextArea.setText(res);
	}
	
	private JButton getCharactersButton() {
		if(charactersButton == null) {
			charactersButton = new JButton();
			charactersButton.setText("...");
			charactersButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					showCharSequenceGUI();			
				}
			});
		}
		return charactersButton;
	}
	
	// Method to showCharSequenceGUI add the sequence to reg_exp field
	public void addSequence(String sequence){
		addToRegExp(sequence);
		quantified=true;
	}

	private void showCharSequenceGUI(){
		new CharSequenceGUI(this);
	}
	
	private JComboBox getTaskComboBox() {
		if(taskComboBox == null) {
			DefaultComboBoxModel taskComboBoxModel = new DefaultComboBoxModel();

			try {
				this.idclassesClasses = getClasses();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(String value:idclassesClasses.values())
			{
				taskComboBoxModel.addElement(value);
			}
			taskComboBoxModel.addElement("New Class");
			taskComboBox = new JComboBox();
			taskComboBox.setModel(taskComboBoxModel);
			taskComboBox.setSelectedItem("New Class");
			taskComboBox.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent arg0) {
					changeCombo();
				}
			});
			

		}
		return taskComboBox;
	}
	
	protected void changeCombo() {
		if(taskComboBox.getSelectedItem().equals("New Class"))
		{
			this.jTextPaneNewClass.setEditable(true);
			this.jTextPaneNewClass.setEnabled(true);
		}
		else
		{
			this.jTextPaneNewClass.setEnabled(false);
			this.jTextPaneNewClass.setEditable(false);
		}
		
	}

	private JPanel getProjectPanel() throws MissingDatatypesException{
		if(projectPanel == null) {
			projectPanel = new JPanel();
			GridBagLayout projectPanelLayout = new GridBagLayout();
			projectPanelLayout.rowWeights = new double[] {0.0};
			projectPanelLayout.rowHeights = new int[] {35};
			projectPanelLayout.columnWeights = new double[] {0.0, 0.1};
			projectPanelLayout.columnWidths = new int[] {7, 7};
			projectPanel.setLayout(projectPanelLayout);
			projectPanel.add(getJPanelRuleSet(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return projectPanel;
	}

	private void setQuantButtonsEnabled(boolean value){
		for(JButton b : this.qt_buttons)
			b.setEnabled(value);
	}

	private JPanel getCorrectPanel() {
		if(correctPanel == null) {
			correctPanel = new JPanel();
			GridBagLayout correctPanelLayout = new GridBagLayout();
			correctPanelLayout.rowWeights = new double[] {0.0, 0.0, 0.1, 0.1};
			correctPanelLayout.rowHeights = new int[] {7, 7, 7, 7};
			correctPanelLayout.columnWeights = new double[] {0.1};
			correctPanelLayout.columnWidths = new int[] {7};
			correctPanel.setLayout(correctPanelLayout);
			correctPanel.add(getUndoButton(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			correctPanel.add(getClearButton(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return correctPanel;
	}
	
	private JTextPane getJTextPaneNewClass() {
		if(jTextPaneNewClass == null) {
			jTextPaneNewClass = new JTextPane();
		}
		return jTextPaneNewClass;
	}
	
	public Map<Integer,String> getClasses() throws SQLException
	{
		List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(Resources.class);
		Resources resources = (Resources) items.get(0).getUserData();
		IDatabase db = resources.getDb();
		ResultSet rs = null;
		Map<Integer,String> classIDClass = new HashMap<Integer, String>();
		
		String q = "SELECT * FROM classes";
		
		db.openConnection();
		Connection connection = db.getConnection();
		if(connection == null)
		{
			db.closeConnection();
			return null;
		}
		else
		{
			rs = db.executeQuery(q);
			while(rs.next())
			{
				classIDClass.put(rs.getInt(1),rs.getString(2));
			}
		}
		return classIDClass;
	}
	
	private JLabel getJLabelClass() {
		if(jLabelClass == null) {
			jLabelClass = new JLabel();
			jLabelClass.setText("New Class :");
		}
		return jLabelClass;
	}
	
	private JPanel getJPanelRuleSet() throws MissingDatatypesException {
		if(jPanelRuleSet == null) {
			jPanelRuleSet = new JPanel();
			jPanelRuleSet.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Select Rules", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelRuleSetLayout = new GridBagLayout();
			jPanelRuleSetLayout.rowWeights = new double[] {0.1};
			jPanelRuleSetLayout.rowHeights = new int[] {7};
			jPanelRuleSetLayout.columnWeights = new double[] {0.1};
			jPanelRuleSetLayout.columnWidths = new int[] {7};
			jPanelRuleSet.setLayout(jPanelRuleSetLayout);
			jPanelRuleSet.add(getJComboBoxRuleSet(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelRuleSet;
	}
	
	private JComboBox getJComboBoxRuleSet() throws MissingDatatypesException {
		if(jComboBoxRuleSet == null) {
			
			ComboBoxModel jComboBoxRuleSetModel = 
				new DefaultComboBoxModel();
			jComboBoxRuleSet = new JComboBox();
			jComboBoxRuleSet.setModel(jComboBoxRuleSetModel);
			List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(RulesAibench.class);

			for (ClipboardItem item : cl) {
				jComboBoxRuleSet.addItem((RulesAibench)item.getUserData());
			}
			if (jComboBoxRuleSet.getModel().getSize() > 0)
			{
				Object obj = HelpAibench.getSelectedItem(RulesAibench.class);
				RulesAibench dicAibnech = (RulesAibench) obj;
				jComboBoxRuleSet.setSelectedItem(dicAibnech);
				rules= dicAibnech;
			}
			else
			{
				rules = (RulesAibench) jComboBoxRuleSet.getModel().getElementAt(0);
			}
			jComboBoxRuleSet.addActionListener(new ActionListener(){			
				public void actionPerformed(ActionEvent arg0) {
					changeruleSet();
				}
			});

		}
		return jComboBoxRuleSet;
	}

	protected void changeruleSet() {
		this.rules = (RulesAibench) ((ClipboardItem)jComboBoxRuleSet.getSelectedItem()).getUserData();
		
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"RulesSet_Add_Rule");
				}
			});
		}
		return jButtonHelp;
	}

}
