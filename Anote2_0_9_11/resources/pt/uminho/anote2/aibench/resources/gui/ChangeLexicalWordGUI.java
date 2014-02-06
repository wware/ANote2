package pt.uminho.anote2.aibench.resources.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.resources.datatypes.LexicalWordsAibench;
import pt.uminho.anote2.aibench.resources.views.LexicalWordsView;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class ChangeLexicalWordGUI extends DialogGenericView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean ischange = true;
	private LexicalWordsView lexicalWordsView;
	private String term;
	private JList jlistterms;
	private LexicalWordsAibench lexical;
	private JPanel jPanelTermInfo;
	private JLabel jLabelTerm;
	private JTextField jTextFieldTerm;

	public ChangeLexicalWordGUI(LexicalWordsAibench lexical ,JList jlist, String term){
		super();
		this.term = term;
		this.lexical = lexical;
		this.jlistterms = jlist;
		initGUI();
		this.setTitle("Update Lexical Word - "+term);
		this.setSize(GlobalOptions.smallWidth, GlobalOptions.smallHeight);
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}

	private void initGUI() {
		
		GridBagLayout thisLayout = new GridBagLayout();	
		thisLayout.rowWeights = new double[] {0.1,0.0};
		thisLayout.rowHeights = new int[] {7, 7};
		thisLayout.columnWeights = new double[] {0.1};
		thisLayout.columnWidths = new int[] {7};
		getContentPane().setLayout(thisLayout);
		getContentPane().add(getJPanelTermInfo(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 1, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}
	
	private JPanel getJPanelTermInfo() {
		if(jPanelTermInfo == null) {
			jPanelTermInfo = new JPanel();
			jPanelTermInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Term Update", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelTermInfoLayout = new GridBagLayout();
			jPanelTermInfoLayout.rowWeights = new double[] {0.1, 0.0};
			jPanelTermInfoLayout.rowHeights = new int[] {7, 7};
			jPanelTermInfoLayout.columnWeights = new double[] {0.1, 0.0, 0.1};
			jPanelTermInfoLayout.columnWidths = new int[] {7, 313, 7};
			jPanelTermInfo.setLayout(jPanelTermInfoLayout);
			jPanelTermInfo.add(getJLabelTerm(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelTermInfo.add(getJTextFieldTerm(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelTermInfo;
	}
	
	private JLabel getJLabelTerm() {
		if(jLabelTerm == null) {
			jLabelTerm = new JLabel();
			jLabelTerm.setText("Update Term :");
		}
		return jLabelTerm;
	}

	
	private JTextField getJTextFieldTerm() {
		if(jTextFieldTerm == null) {
			jTextFieldTerm = new JTextField();
			jTextFieldTerm.setText(term);
		}
		return jTextFieldTerm;
	}
	public boolean ischange() {
		return ischange;
	}

	@Override
	protected void okButtonAction() {
		if(jTextFieldTerm.getText() == null || jTextFieldTerm.getText().length()==0)
		{
			Workbench.getInstance().warn("New Element Can not be Empty");
		}
		else if(term.equals(jTextFieldTerm.getText()))
		{
			Workbench.getInstance().warn("The term don´t have changes");
		} else
			try {
				if(lexical.getLexicalWords().contains(jTextFieldTerm.getText()))
				{
					Workbench.getInstance().warn("The term already exists");
				}
				else
				{
					update();
				}
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			}
	}

	private void update() throws SQLException, DatabaseLoadDriverException {
		int index = jlistterms.getSelectedIndex();
		lexical.updateElement(term, jTextFieldTerm.getText());
		((DefaultListModel) jlistterms.getModel()).remove(index);
		((DefaultListModel) jlistterms.getModel()).add(index, jTextFieldTerm.getText());
		jlistterms.updateUI();
		new ShowMessagePopup("Element Update.");
		finish();
	}

	@Override
	protected String getHelpLink() {
		return null;
	}
	


}
