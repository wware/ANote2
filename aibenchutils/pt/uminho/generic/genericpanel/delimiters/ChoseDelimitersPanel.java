package pt.uminho.generic.genericpanel.delimiters;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import pt.uminho.anote2.aibench.utils.conf.Delimiter;

public class ChoseDelimitersPanel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	
	private JRadioButton commabutton;
	private JRadioButton tabbutton;
	private JTextField userdefinedText;
	private JRadioButton userdefinedButton;
	private JRadioButton wspacebutton;
	private ButtonGroup buttonGroup;
	
	
	
	private static String RADIO_BUTTON_ACTIVED = "RadioButtonSelected";
	
	public ChoseDelimitersPanel(){
		initGUI();
		addRadioButtonAction(this);
	}

	private void initGUI() {
		try {
			{
				
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.columnWidths = new int[] {7, 7, 7, 7, 7};
				thisLayout.rowWeights = new double[] {0.1};
				thisLayout.rowHeights = new int[] {7};
				thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.0};
				this.setLayout(thisLayout);
				this.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
				{
					commabutton = new JRadioButton();
					this.add(commabutton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					commabutton.setText("Comma");
					commabutton.setActionCommand(RADIO_BUTTON_ACTIVED);
				}
				{
					tabbutton = new JRadioButton();
					this.add(tabbutton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					tabbutton.setText("Tab");
					commabutton.setActionCommand(RADIO_BUTTON_ACTIVED);
				}
				{
					wspacebutton = new JRadioButton();
					this.add(wspacebutton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					wspacebutton.setText("w.space");
					commabutton.setActionCommand(RADIO_BUTTON_ACTIVED);
				}
				{
					userdefinedButton = new JRadioButton();
					this.add(userdefinedButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					userdefinedButton.setText("user defined");
					commabutton.setActionCommand(RADIO_BUTTON_ACTIVED);
				}
				{
					userdefinedText = new JTextField();
					this.add(userdefinedText, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					userdefinedText.setPreferredSize(new java.awt.Dimension(30, 22));
					userdefinedText.setSize(30, 22);
					userdefinedText.setEnabled(false);
				}
			}
			buttonGroup = new ButtonGroup();
			buttonGroup.add(commabutton);
			buttonGroup.add(tabbutton);
			buttonGroup.add(userdefinedButton);
			buttonGroup.add(wspacebutton);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(userdefinedButton.isSelected()){
			userdefinedText.setEnabled(true);
		}else
			userdefinedText.setEnabled(false);
	}
	
	public void setDefaulValue(int index){
		
		JRadioButton teste = null;
		if(index == 0)
			teste = commabutton;
		else if (index == 1) teste = tabbutton;
		else if (index == 2) teste = wspacebutton;
		else teste = userdefinedButton;
		
		teste.setSelected(true);
	}
	
	public Delimiter getDelimiter(){
		
		Delimiter teste = Delimiter.USER;
		
		if(commabutton.isSelected())
			return Delimiter.COMMA;
		else if(tabbutton.isSelected())
			return Delimiter.TAB;
		else if(wspacebutton.isSelected())
			return Delimiter.WHITE_SPACE;
		else{
			teste.setUserDelimiter(userdefinedText.getText().trim());
			return teste;
		}
	}
	
	public void enabledAll(boolean b){
		this.commabutton.setEnabled(b);
		this.tabbutton.setEnabled(b);
		this.wspacebutton.setEnabled(b);
		this.userdefinedButton.setEnabled(b);
		//this.userdefinedText.setEnabled(b);
	}
	
	public void addRadioButtonAction(ActionListener actionListener){
		commabutton.addActionListener(actionListener);
		tabbutton.addActionListener(actionListener);
		wspacebutton.addActionListener(actionListener);
		userdefinedButton.addActionListener(actionListener);
		
	 }

}
