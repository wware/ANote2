package pt.uminho.anote2.aibench.corpus.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.datastructures.database.queries.corpora.QueriesCorpora;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfo;
import pt.uminho.generic.genericpanel.database.DataBaseDelete;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class RemoveCorpusGUI extends DataBaseDelete{

	private JPanel jPanelInformation;
	private JTextField jTextFieldID;
	private JTextField jTextFieldName;
	private JTextField jTextFieldProcesses;
	private JTextField jTextFieldTextType;
	private JLabel jLabelEntityAnnotations;
	private JLabel jLabelProcessType;
	private JLabel jLabelProcessName;
	private JLabel jLabelProcessID;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ICorpus corpus;
	

	public RemoveCorpusGUI(ICorpus corpus) throws SQLException, DatabaseLoadDriverException {
		super(GlobalTextInfo.databasedeleteCorpusInfo);
		this.corpus = corpus;
		completeFields();
		this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);	
		Utilities.centerOnOwner(this);
		this.setTitle("Remove Corpus "+corpus.getDescription()+"("+corpus.getID()+") from Database");
		this.setModal(true);
		this.setVisible(true);
	}

	private void completeFields() throws SQLException, DatabaseLoadDriverException {
		jTextFieldID.setText(String.valueOf(corpus.getID()));
		jTextFieldName.setText(corpus.getDescription());
		jTextFieldProcesses.setText(String.valueOf(getCorpusAssosiated()));
		jTextFieldTextType.setText(corpus.getProperties().getProperty(GlobalNames.textType));	
	}

	private int getCorpusAssosiated() throws SQLException, DatabaseLoadDriverException {
		int result = 0;
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesCorpora.selectCountCorpusProcesses);
		ps.setInt(1,corpus.getID());
		ResultSet rs = ps.executeQuery();
		if(rs.next())
		{
			result = rs.getInt(1);
		}
		rs.close();
		ps.close();
		return result;
	}

	@Override
	protected void okButtonAction() {
		removeFromClipboard();
		try {
			removeFromDatabase();
		} catch (SQLException e) {
			new ShowMessagePopup("Corpus Deleted Fail");
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Corpus Deleted Fail");
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		}
		((Corpus) corpus).getCorpora().notifyViewObservers();
		finish();
		new ShowMessagePopup("Corpus Deleted From the Database");
	}

	private void removeFromDatabase() throws SQLException, DatabaseLoadDriverException {
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesCorpora.inactiveCorpus);
		ps.setInt(1,corpus.getID());
		ps.execute();
		ps.close();
	}

	private void removeFromClipboard() {
		int id = corpus.getID();
		List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(Corpus.class);
		for(ClipboardItem item:cl)
		{
			Corpus corpus = (Corpus) item.getUserData();
			if(corpus.getID()==id)
			{
				List<ClipboardItem> co = Core.getInstance().getClipboard().getItemsByClass(Corpora.class);
				
				Core.getInstance().getClipboard().removeClipboardItem(item);
				corpus.getCorpora().getCorpusSet().remove(corpus);
				break;
			}
		}
	}

	@Override
	public JPanel getDetailPanel() {
		if(jPanelInformation == null) {
			jPanelInformation = new JPanel();
			jPanelInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Corpus Information", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelinformationLayout = new GridBagLayout();
			jPanelinformationLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelinformationLayout.rowHeights = new int[] {7, 7, 7, 20, 20, 7};
			jPanelinformationLayout.columnWeights = new double[] {0.1, 0.1};
			jPanelinformationLayout.columnWidths = new int[] {7, 7};
			jPanelInformation.setLayout(jPanelinformationLayout);
			{
				jLabelProcessID = new JLabel();
				jPanelInformation.add(jLabelProcessID, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelProcessID.setText("Corpus ID : ");
			}
			{
				jLabelProcessName = new JLabel();
				jPanelInformation.add(jLabelProcessName, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelProcessName.setText("Name : ");
			}
			{
				jLabelProcessType = new JLabel();
				jPanelInformation.add(jLabelProcessType, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelProcessType.setText("Processes : ");
			}
			{
				jLabelEntityAnnotations = new JLabel();
				jPanelInformation.add(jLabelEntityAnnotations, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelEntityAnnotations.setText("Text Type :");
			}
			{
				jTextFieldID = new JTextField();
				jPanelInformation.add(jTextFieldID, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextFieldName = new JTextField();
				jPanelInformation.add(jTextFieldName, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextFieldProcesses = new JTextField();
				jPanelInformation.add(jTextFieldProcesses, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextFieldTextType = new JTextField();
				jPanelInformation.add(jTextFieldTextType, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}

		}
		return jPanelInformation;
	}

	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink + "Corpora_Remove_Corpus";
	}
	
	

}
