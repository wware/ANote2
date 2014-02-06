package pt.uminho.generic.csvloaders;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import pt.uminho.anote2.aibench.utils.session.SessionPropertykeys;
import pt.uminho.anote2.aibench.utils.session.SessionSettings;
import pt.uminho.anote2.process.IE.io.export.DefaultDelimiterValue;
import pt.uminho.anote2.process.IE.io.export.Delimiter;
import pt.uminho.anote2.process.IE.io.export.TextDelimiter;

public class DelimiterSelectionPanel extends javax.swing.JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField jTextFieldDefaultValue;
	private JLabel jLabelExternalIDSource;
	private JLabel jLabelExternalIdsDelimiter;
	private JLabel jLabelSynonymDelimiter;
	private JComboBox jComboBoxDefaultValue;
	private JLabel jLabelDefaultValue;
	private JLabel jLabelTextDelimiter;
	private JLabel jLabelGeneralDelimiter;
	private JTextField jTextFieldUserTextDelimiter;
	private JTextField jTextFieldUserDilimiter;
	private JTextField jTextFieldExtenalIDSourceUserDelimiter;
	private JTextField jTextFieldExternalIDUserDelimiter;
	private JTextField jTextFieldSynonymsUserDelimiter;
	private JComboBox jComboBoxExternalIDSourceDelimiter;
	private JComboBox jComboBoxExternalIDDelimiter;
	private JComboBox jComboBoxSynonymDelimiter;
	private JComboBox jComboGeneralDilimiter;
	private JComboBox jComboBoxTextDelimiter;
	private boolean advancedOptions;
	
	public DelimiterSelectionPanel(boolean advancedOptions) {
		super();
		this.advancedOptions = advancedOptions;
		initGUI();
		changeDefaults();
	}
	
	private void changeDefaults() {
		String settingsDefaultGeneralDelimiterValue = SessionSettings.getSessionSettings().getSessionProperty(SessionPropertykeys.generalDelimiter);
		if(settingsDefaultGeneralDelimiterValue!=null)
		{
			Delimiter delimiter = Delimiter.getDelimiterByString(settingsDefaultGeneralDelimiterValue);
			if(delimiter!=null)
			{
				jComboGeneralDilimiter.setSelectedItem(delimiter);
			}
		}
		String settingsDefaultTextDelimiterValue = SessionSettings.getSessionSettings().getSessionProperty(SessionPropertykeys.generaltextDelimiter);
		if(settingsDefaultTextDelimiterValue!=null)
		{
			TextDelimiter textdelimiter = TextDelimiter.getDelimiterByString(settingsDefaultTextDelimiterValue);
			if(textdelimiter!=null)
			{
				jComboBoxTextDelimiter.setSelectedItem(textdelimiter);
			}
		}
		String settingsDefaultValue = SessionSettings.getSessionSettings().getSessionProperty(SessionPropertykeys.generalDefaultValueDelimiter);
		if(settingsDefaultValue!=null)
		{
			DefaultDelimiterValue defaultvalue = DefaultDelimiterValue.getDelimiterByString(settingsDefaultValue);
			if(defaultvalue!=null)
			{
				jComboBoxDefaultValue.setSelectedItem(defaultvalue);
			}
		}
		if(advancedOptions)
		{
			String sessionSynonymsDelimiter = SessionSettings.getSessionSettings().getSessionProperty(SessionPropertykeys.sessionSynonymsDelimiter);
			if(sessionSynonymsDelimiter!=null)
			{
				Delimiter defaultvalue = Delimiter.getDelimiterByString(sessionSynonymsDelimiter);
				if(defaultvalue!=null)
				{
					jComboBoxSynonymDelimiter.setSelectedItem(defaultvalue);
				}
			}
			String sessionExternalIDsDelimiter = SessionSettings.getSessionSettings().getSessionProperty(SessionPropertykeys.sessionExternalIDsDelimiter);
			if(sessionExternalIDsDelimiter!=null)
			{
				Delimiter defaultvalue = Delimiter.getDelimiterByString(sessionExternalIDsDelimiter);
				if(defaultvalue!=null)
				{
					jComboBoxExternalIDDelimiter.setSelectedItem(defaultvalue);
				}
			}
			String sessionExternalIDSourceDelimiter = SessionSettings.getSessionSettings().getSessionProperty(SessionPropertykeys.sessionExternalIDSourceDelimiter);
			if(sessionExternalIDSourceDelimiter!=null)
			{
				Delimiter defaultvalue = Delimiter.getDelimiterByString(sessionExternalIDSourceDelimiter);
				if(defaultvalue!=null)
				{
					jComboBoxExternalIDSourceDelimiter.setSelectedItem(defaultvalue);
				}
			}
		}
	}

	public void setGeneralDelimiter(Delimiter d){
		jComboGeneralDilimiter.setSelectedItem(d);
	}
	
	private void initGUI() {
		try {
			{
				this.setBorder(BorderFactory.createTitledBorder("Delimiter"));
				GridBagLayout thisLayout = new GridBagLayout();
				this.setPreferredSize(new java.awt.Dimension(689, 315));
				thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.0, 0.0, 0.0};
				thisLayout.rowHeights = new int[] {7, 20, 20, 20, 7, 20};
				thisLayout.columnWeights = new double[] {0.0, 0.1, 0.1};
				thisLayout.columnWidths = new int[] {7, 20, 7};
				this.setLayout(thisLayout);
				this.add(getComboBoxGeneralDilimiter(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
				this.add(getJTextFieldUserDilimiter(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
				this.add(getJTextFieldUserTextDelimiter(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
				this.add(getJLabelGeneralDelimiter(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
				this.add(getJComboBoxTextDelimiter(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
				this.add(getJLabelTextDelimiter(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
				this.add(getJLabelDefaultValue(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
				this.add(getJTextFieldDefaultValue(), new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
				this.add(getJComboBoxDefaultValue(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
				if(advancedOptions)
				{
					this.add(getJLabelSynonymDelimiter(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
					this.add(getJLabelExternalIdsDelimiter(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
					this.add(getJLabelExternalIDSource(), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
					this.add(getJComboBoxSynonymDelimiter(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
					this.add(getJComboBoxExternalIDDelimiter(), new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
					this.add(getJComboBoxExternalIDSourceDelimiter(), new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
					this.add(getJTextFieldSynonymsUserDelimiter(), new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
					this.add(getJTextFieldExternalIDUserDelimiter(), new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
					this.add(getJTextFieldExtenalIDSourceUserDelimiter(), new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));

				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Component getComboBoxGeneralDilimiter() {
		if(jComboGeneralDilimiter==null)
		{
			jComboGeneralDilimiter = new JComboBox(Delimiter.values());
			jComboGeneralDilimiter.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					openText(arg0);
				}
			});
		}
		return jComboGeneralDilimiter;
	}
	
	protected void openText(ActionEvent arg0) {
		Delimiter delimiter = (Delimiter) jComboGeneralDilimiter.getSelectedItem();
		if(delimiter.equals(Delimiter.USER))
		{
			jTextFieldUserDilimiter.setEnabled(true);
		}
		else
		{
			jTextFieldUserDilimiter.setEnabled(false);
		}
		TextDelimiter textDelimiter = (TextDelimiter) jComboBoxTextDelimiter.getSelectedItem();
		if(textDelimiter.equals(TextDelimiter.USER))
		{
			jTextFieldUserTextDelimiter.setEnabled(true);
		}
		else
		{
			jTextFieldUserTextDelimiter.setEnabled(false);
		}
		DefaultDelimiterValue defaultValue = (DefaultDelimiterValue) jComboBoxDefaultValue.getSelectedItem();
		if(defaultValue.equals(DefaultDelimiterValue.USER))
		{
			jTextFieldDefaultValue.setEnabled(true);
		}
		else
		{
			jTextFieldDefaultValue.setEnabled(false);
		}
		if(advancedOptions)
		{
			Delimiter synonymsDelimiter = (Delimiter) jComboBoxSynonymDelimiter.getSelectedItem();
			if(synonymsDelimiter.equals(Delimiter.USER))
			{
				jTextFieldSynonymsUserDelimiter.setEnabled(true);
			}
			else
			{
				jTextFieldSynonymsUserDelimiter.setEnabled(false);
			}
			Delimiter externalIDDelimiter = (Delimiter) jComboBoxExternalIDDelimiter.getSelectedItem();
			if(externalIDDelimiter.equals(Delimiter.USER))
			{
				jTextFieldExternalIDUserDelimiter.setEnabled(true);
			}
			else
			{
				jTextFieldExternalIDUserDelimiter.setEnabled(false);
			}
			Delimiter externalIDSourceDelimiter = (Delimiter) jComboBoxExternalIDSourceDelimiter.getSelectedItem();
			if(externalIDSourceDelimiter.equals(Delimiter.USER))
			{
				jTextFieldExtenalIDSourceUserDelimiter.setEnabled(true);
			}
			else
			{
				jTextFieldExtenalIDSourceUserDelimiter.setEnabled(false);
			}
		}
	}

	public Delimiter getGeneralDelimiter(){
		Delimiter delimiter = (Delimiter) jComboGeneralDilimiter.getSelectedItem();
		if(delimiter.equals(Delimiter.USER))
		{
			delimiter.setUserDelimiter(jTextFieldUserDilimiter.getText());
		}
		return delimiter;	
	}
	
	public Delimiter getSynonymsDelimiter(){
		Delimiter delimiter = (Delimiter) jComboBoxSynonymDelimiter.getSelectedItem();
		if(delimiter.equals(Delimiter.USER))
		{
			delimiter.setUserDelimiter(jTextFieldSynonymsUserDelimiter.getText());
		}
		return delimiter;	
	}
	
	public Delimiter getExternalIDDelimiter(){
		Delimiter delimiter = (Delimiter) jComboBoxExternalIDDelimiter.getSelectedItem();
		if(delimiter.equals(Delimiter.USER))
		{
			delimiter.setUserDelimiter(jTextFieldExternalIDUserDelimiter.getText());
		}
		return delimiter;	
	}
	
	public Delimiter getExternalIDSourceDelimiter(){
		Delimiter delimiter = (Delimiter) jComboBoxExternalIDSourceDelimiter.getSelectedItem();
		if(delimiter.equals(Delimiter.USER))
		{
			delimiter.setUserDelimiter(jTextFieldExtenalIDSourceUserDelimiter.getText());
		}
		return delimiter;	
	}
	
	public TextDelimiter gettextDelimiter(){
		TextDelimiter textDelimiter = (TextDelimiter) jComboBoxTextDelimiter.getSelectedItem();
		if(textDelimiter.equals(TextDelimiter.USER))
		{
			textDelimiter.setUserDelimiter(jTextFieldUserTextDelimiter.getText());
		}
		return textDelimiter;	
	}
	
	public DefaultDelimiterValue getDefaultValue(){
		DefaultDelimiterValue textDelimiter = (DefaultDelimiterValue) jComboBoxDefaultValue.getSelectedItem();
		if(textDelimiter.equals(TextDelimiter.USER))
		{
			textDelimiter.setUserDelimiter(jTextFieldDefaultValue.getText());
		}
		return textDelimiter;	
	}
	
	public void addKeyListener(KeyListener kl, String command){
		jComboGeneralDilimiter.addKeyListener(kl);
		jComboGeneralDilimiter.setActionCommand(command);
		jTextFieldUserDilimiter.addKeyListener(kl);
		jTextFieldUserDilimiter.setActionCommand(command);
	}
	
	public void addListener(ActionListener l, String command){
		jComboGeneralDilimiter.setActionCommand(command);
		jComboGeneralDilimiter.addActionListener(l);
		jTextFieldUserDilimiter.setActionCommand(command);
		jTextFieldUserDilimiter.addActionListener(l);
	}
	
	private JTextField getJTextFieldUserDilimiter() {
		if(jTextFieldUserDilimiter == null) {
			jTextFieldUserDilimiter = new JTextField();
			jTextFieldUserDilimiter.setEnabled(false);
		}
		return jTextFieldUserDilimiter;
	}
	
	public boolean myEquals(Object object){
		return jTextFieldUserDilimiter.equals(object);
	}

	private JComboBox getJComboBoxTextDelimiter() {
		if(jComboBoxTextDelimiter == null) {
			jComboBoxTextDelimiter = new JComboBox(TextDelimiter.values());
			jComboBoxTextDelimiter.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					openText(arg0);
				}
			});
		}
		return jComboBoxTextDelimiter;
	}
	
	private JTextField getJTextFieldUserTextDelimiter() {
		if(jTextFieldUserTextDelimiter == null) {
			jTextFieldUserTextDelimiter = new JTextField();
			jTextFieldUserTextDelimiter.setEnabled(false);

		}
		return jTextFieldUserTextDelimiter;
	}
	
	private JLabel getJLabelGeneralDelimiter() {
		if(jLabelGeneralDelimiter == null) {
			jLabelGeneralDelimiter = new JLabel();
			jLabelGeneralDelimiter.setText("General :");
		}
		return jLabelGeneralDelimiter;
	}
	
	private JLabel getJLabelTextDelimiter() {
		if(jLabelTextDelimiter == null) {
			jLabelTextDelimiter = new JLabel();
			jLabelTextDelimiter.setText("Text :");
		}
		return jLabelTextDelimiter;
	}
	
	private JLabel getJLabelDefaultValue() {
		if(jLabelDefaultValue == null) {
			jLabelDefaultValue = new JLabel();
			jLabelDefaultValue.setText("Default Value :");
		}
		return jLabelDefaultValue;
	}
	
	private JTextField getJTextFieldDefaultValue() {
		if(jTextFieldDefaultValue == null) {
			jTextFieldDefaultValue = new JTextField();
			jTextFieldDefaultValue.setEnabled(false);

		}
		return jTextFieldDefaultValue;
	}
	
	private JComboBox getJComboBoxDefaultValue() {
		if(jComboBoxDefaultValue == null) {
			jComboBoxDefaultValue = new JComboBox(DefaultDelimiterValue.values());
			jComboBoxDefaultValue.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					openText(arg0);
				}
			});
		}
		return jComboBoxDefaultValue;
	}
	
	private JLabel getJLabelSynonymDelimiter() {
		if(jLabelSynonymDelimiter == null) {
			jLabelSynonymDelimiter = new JLabel();
			jLabelSynonymDelimiter.setText("Synonyms :");
		}
		return jLabelSynonymDelimiter;
	}
	
	private JLabel getJLabelExternalIdsDelimiter() {
		if(jLabelExternalIdsDelimiter == null) {
			jLabelExternalIdsDelimiter = new JLabel();
			jLabelExternalIdsDelimiter.setText("ExternalIDs :");
		}
		return jLabelExternalIdsDelimiter;
	}
	
	private JLabel getJLabelExternalIDSource() {
		if(jLabelExternalIDSource == null) {
			jLabelExternalIDSource = new JLabel();
			jLabelExternalIDSource.setText("ExternalID/Source :");
		}
		return jLabelExternalIDSource;
	}
	
	private JComboBox getJComboBoxSynonymDelimiter() {
		if(jComboBoxSynonymDelimiter == null) {
			jComboBoxSynonymDelimiter = new JComboBox(Delimiter.values());
			jComboBoxSynonymDelimiter.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					openText(arg0);
				}
			});
		}
		return jComboBoxSynonymDelimiter;
	}
	
	private JComboBox getJComboBoxExternalIDDelimiter() {
		if(jComboBoxExternalIDDelimiter == null) {
			jComboBoxExternalIDDelimiter = new JComboBox(Delimiter.values());
			jComboBoxExternalIDDelimiter.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					openText(arg0);
				}
			});
		}
		return jComboBoxExternalIDDelimiter;
	}
	
	private JComboBox getJComboBoxExternalIDSourceDelimiter() {
		if(jComboBoxExternalIDSourceDelimiter == null) {
			jComboBoxExternalIDSourceDelimiter = new JComboBox(Delimiter.values());
			jComboBoxExternalIDSourceDelimiter.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					openText(arg0);
				}
			});
		}
		return jComboBoxExternalIDSourceDelimiter;
	}
	
	private JTextField getJTextFieldSynonymsUserDelimiter() {
		if(jTextFieldSynonymsUserDelimiter == null) {
			jTextFieldSynonymsUserDelimiter = new JTextField();
		}
		return jTextFieldSynonymsUserDelimiter;
	}
	
	private JTextField getJTextFieldExternalIDUserDelimiter() {
		if(jTextFieldExternalIDUserDelimiter == null) {
			jTextFieldExternalIDUserDelimiter = new JTextField();
		}
		return jTextFieldExternalIDUserDelimiter;
	}
	
	private JTextField getJTextFieldExtenalIDSourceUserDelimiter() {
		if(jTextFieldExtenalIDSourceUserDelimiter == null) {
			jTextFieldExtenalIDSourceUserDelimiter = new JTextField();
		}
		return jTextFieldExtenalIDSourceUserDelimiter;
	}

}
