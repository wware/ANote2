package pt.uminho.generic.genericpanel.corpus;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewInputGUI;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;

public abstract class CorpusPanel extends DialogGenericViewInputGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox jComboBoxCorpus;
	private JPanel corpusSelection;
	
	public CorpusPanel()
	{
		super();
	}
	
	public CorpusPanel(String title)
	{
		super(title);
	}

	public JPanel getCorpusSelectionPanel() {
		if(corpusSelection==null)
		{
			corpusSelection = new JPanel();
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1};
			thisLayout.rowHeights = new int[] {7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			corpusSelection.setLayout(thisLayout);
			corpusSelection.setBorder(BorderFactory.createTitledBorder(null, "Corpus", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
			{
				jComboBoxCorpus = new JComboBox(getAvailableCorpusOnClipBoard());
				jComboBoxCorpus.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent arg0) {
						try {
							changeComboBoxCorpus();
						} catch (SQLException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						} catch (DatabaseLoadDriverException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						}
					}
				});
				corpusSelection.add(jComboBoxCorpus, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 15, 10, 15), 0, 0));
			}
		}
		return corpusSelection;
	}
	
	public ICorpus getSelectedCorpus()
	{
		return (ICorpus)((DefaultComboBoxModel) jComboBoxCorpus.getModel()).getSelectedItem();
	}

	protected abstract void changeComboBoxCorpus() throws SQLException, DatabaseLoadDriverException;

	public abstract DefaultComboBoxModel getAvailableCorpusOnClipBoard();

}
