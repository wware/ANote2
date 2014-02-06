package pt.uminho.anote2.aibench.resources.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.resources.datatypes.RulesAibench;
import pt.uminho.anote2.aibench.utils.exceptions.MissingDatatypesException;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewInputGUI;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.resource.IResourceElement;

import com.stevesoft.pat.Regex;

import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class EditRuleGUI extends DialogGenericViewInputGUI{

	private static final long serialVersionUID = 4736901599859897970L;
	
	private JTextField test1TextArea;
	private JLabel jLabelNewRuleFormat;
	private JTextPane jTextPaneOldExpression;
	private JLabel jLabelFreeText;
	private JLabel jLabelClass;
	private JTextField jTextPaneNewClass;
	private JPanel correctPanel;
	private JComboBox taskComboBox;
	private JButton testButton;
	private JEditorPane test2TextArea;
	private JScrollPane test2ScrollPane;
	private JScrollPane test1ScrollPane;
	private JPanel testPanel;
	private JTextField reg_expTextField;
	private JPanel freeTextPanel;
	private RulesAibench rules;
	private IResourceElement ruleElement;
	
	public EditRuleGUI(RulesAibench rules,IResourceElement ruleElement) throws MissingDatatypesException{
		super("Edit Rule");
		this.rules=rules;
		this.ruleElement=ruleElement;
		initGUI();
		populateGUI();
		this.setSize(GlobalOptions.generalWidth,GlobalOptions.generalHeight);
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	
	private void populateGUI() {
		jTextPaneOldExpression.setEditable(false);
		jTextPaneOldExpression.setBackground(Color.WHITE);
		reg_expTextField.setText(ruleElement.getTerm());
		jTextPaneOldExpression.setText(ruleElement.getTerm());
		taskComboBox.setSelectedItem(ClassProperties.getClassIDClass().get(ruleElement.getTermClassID()));
	}

	private void initGUI() throws MissingDatatypesException{

		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1, 0.0, 0.4, 0.0};
		thisLayout.rowHeights = new int[] {7, 7, 100, 7};
		thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.0};
		thisLayout.columnWidths = new int[] {7, 7, 7, 7};
		getContentPane().setLayout(thisLayout);		
		{
			getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 2), 0, 0));
			getContentPane().add(getFreeTextPanel(), new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 0, 0), 0, 0));
			getContentPane().add(getTestPanel(), new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 0, 2), 0, 0));
			getContentPane().add(getCorrectPanel(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(6, 2, 0, 2), 0, 0));
		}
	}
	
	public void finish() {
		this.setVisible(false);
		this.setModal(false);
		this.dispose();		
	}

	public void onValidationError(Throwable arg0) {
		Workbench.getInstance().error(arg0);		
	}
	
	/**
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 * 
	 */
	protected void edit(String classe) throws SQLException, DatabaseLoadDriverException {
		int termID = ruleElement.getID();
		int classID = ClassProperties.getClassClassID().get(classe);
			PreparedStatement editDic = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.updateResourceElement2);
			editDic.setNString(1,reg_expTextField.getText());
			editDic.setInt(2,classID);
			editDic.setInt(3,termID);
			editDic.setInt(4,rules.getID());
			editDic.execute();
			editDic.close();
	}

	private JTextField getReg_expTextField() {
		if(reg_expTextField == null) {
			reg_expTextField = new JTextField();
			reg_expTextField.setForeground(Color.BLUE);
		}
		return reg_expTextField;
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
	
	private JTextField getTest1TextArea() {
		if(test1TextArea == null) {
			test1TextArea = new JTextField();
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
	
	private void testRegExp() {
		String text = this.test1TextArea.getText();
		String rexp = this.reg_expTextField.getText();
		if(text.length() == 0)
		{
			Workbench.getInstance().warn("Please enter the text for testing");
		}
		else if(rexp.length() == 0)
		{
			Workbench.getInstance().warn("Please enter rule text for testing");
		}
		else
		{
			Pattern p = Pattern.compile("("+rexp+")");
			Matcher m = p.matcher(text);
			String color = "#FF0000"; //((ANoteProperty) taskComboBox.getSelectedItem()).getColor();
			int step = 0;
			int errorTag = 36;
			while(m.find())
			{
				int groupCount = m.groupCount();
				if(groupCount > 1)
				{
					for(int i=2;i<=groupCount;i++)
					{
						if(m.start(i)>0 && m.end(i) > 0)
						{
							text = text.substring(0, m.start(i)+step) 
									+ "<FONT COLOR=\"" + color + "\"><b>"+
									text.substring(m.start(i)+step,m.end(i)+step)+
									"</b></FONT>"+ text.substring(m.end(i)+step);
							step += errorTag ;
						}
					}
				}
				else
				{
					Regex r = new Regex(rexp,"<FONT COLOR=" + color + "><b>$&</b></FONT>");
					text = r.replaceAll(text);
				}
			}
			test2TextArea.setText(text);
		}
	}

	private JComboBox getTaskComboBox() {
		if(taskComboBox == null) {
			DefaultComboBoxModel taskComboBoxModel = new DefaultComboBoxModel();
			for(String value:ClassProperties.getClassIDClass().values())
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

	private JPanel getCorrectPanel() {
		if(correctPanel == null) {
			correctPanel = new JPanel();
			GridBagLayout correctPanelLayout = new GridBagLayout();
			correctPanelLayout.rowWeights = new double[] {0.0, 0.0, 0.1, 0.1};
			correctPanelLayout.rowHeights = new int[] {7, 7, 7, 7};
			correctPanelLayout.columnWeights = new double[] {0.1};
			correctPanelLayout.columnWidths = new int[] {7};
			correctPanel.setLayout(correctPanelLayout);
		}
		return correctPanel;
	}
	
	private JTextField getJTextPaneNewClass() {
		if(jTextPaneNewClass == null) {
			jTextPaneNewClass = new JTextField();
		}
		return jTextPaneNewClass;
	}
	
	private JLabel getJLabelClass() {
		if(jLabelClass == null) {
			jLabelClass = new JLabel();
			jLabelClass.setText("New Class :");
		}
		return jLabelClass;
	}
	

	private JPanel getFreeTextPanel() {
		if(freeTextPanel == null) {
			freeTextPanel = new JPanel();
			freeTextPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Edit Rule", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout symbolsPanelLayout = new GridBagLayout();
			symbolsPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.0};
			symbolsPanelLayout.rowHeights = new int[] {7, 7, 7};
			symbolsPanelLayout.columnWeights = new double[] {0.05, 0.1, 0.1, 0.1};
			symbolsPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
			freeTextPanel.setLayout(symbolsPanelLayout);
			freeTextPanel.setPreferredSize(new java.awt.Dimension(456, 135));
			freeTextPanel.add(getJLabelFreeText(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			freeTextPanel.add(getJTextPane(), new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			freeTextPanel.add(getTaskComboBox(), new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			freeTextPanel.add(getJTextPaneNewClass(), new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			freeTextPanel.add(getJLabelClass(), new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			freeTextPanel.add(getReg_expTextField(), new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			freeTextPanel.add(getJLabelNewRuleFormat(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return freeTextPanel;
	}
	
	private JLabel getJLabelFreeText() {
		if(jLabelFreeText == null) {
			jLabelFreeText = new JLabel();
			jLabelFreeText.setText("Old Rule ( Expression) :");
		}
		return jLabelFreeText;
	}
	
	private JTextPane getJTextPane() {
		if(jTextPaneOldExpression == null) {
			jTextPaneOldExpression = new JTextPane();
			jTextPaneOldExpression.addKeyListener(new KeyListener() {
				
				public void keyTyped(KeyEvent e) {}
				
				public void keyReleased(KeyEvent e) {
					changeText();
				}
				
				public void keyPressed(KeyEvent e) {
					changeText();
				}
			});
		}
		return jTextPaneOldExpression;
	}

	protected void changeText() {
		reg_expTextField.setText(jTextPaneOldExpression.getText());
		
	}
	
	private JLabel getJLabelNewRuleFormat() {
		if(jLabelNewRuleFormat == null) {
			jLabelNewRuleFormat = new JLabel();
			jLabelNewRuleFormat.setText("New Rule ( Expression) :");
		}
		return jLabelNewRuleFormat;
	}

	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void okButtonAction() {
		try {

			if(reg_expTextField.getText().equals(""))
			{
				Workbench.getInstance().warn("Rule Expression is Empty");
			}
			else
			{
				if(taskComboBox.getSelectedItem().toString().equals("New Class"))
				{
					if(jTextPaneNewClass.getText().equals(""))
					{
						Workbench.getInstance().warn("Class is empty");
					}
					else
					{
						rules.addResourceContent(jTextPaneNewClass.getText());

						edit(jTextPaneNewClass.getText());
						rules.notifyViewObservers();
						new ShowMessagePopup("Rule Edit");
						finish();	
					}
				}
				else
				{
					if(reg_expTextField.getText().equals(jTextPaneOldExpression.getText()) && ruleElement.getTermClassID() == rules.addElementClass(taskComboBox.getSelectedItem().toString()))
					{
						Workbench.getInstance().warn("Rule don´t have changes");
					}
					else
					{
						rules.addResourceContent(taskComboBox.getSelectedItem().toString());
						edit(taskComboBox.getSelectedItem().toString());
						rules.notifyViewObservers();
						new ShowMessagePopup("Rule Updated");
						finish();
					}
				}
			}
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		}

	}

	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"RuleSet_Edit_Rule";
	}

}
