package pt.uminho.anote2.aibench.publicationmanager.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.documents.query.QueryManager;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfo;
import pt.uminho.generic.genericpanel.database.DataBaseDelete;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class RemoveQueryGUI extends DataBaseDelete {

	private JPanel jPanelInformation;
	private JLabel jLabelDate;
	private JTextField jTextFieldID;
	private JTextField jTextFieldKeywords;
	private JTextField jTextFieldDate;
	private JTextField jTextFieldAvailablePubs;
	private JTextField jTextFieldOrganism;
	private JTextField jTextFieldAvailableAbstracts;
	private JLabel jLabelAvailableAbstracts;
	private JLabel jLabelAvailablePubs;
	private JLabel jLabelOrganism;
	private JLabel jLabelQueryKeywords;
	private JLabel jLabelQueryID;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private QueryInformationRetrievalExtension queryInformationRetrievalExtension;

	public RemoveQueryGUI(QueryInformationRetrievalExtension queryInformationRetrievalExtension) {
		super(GlobalTextInfo.databasedeleteQueryInfo);
		this.queryInformationRetrievalExtension = queryInformationRetrievalExtension;
		completeFields();
		this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);	
		Utilities.centerOnOwner(this);
		this.setTitle("Remove Query "+queryInformationRetrievalExtension.getID()+" from Database");
		this.setModal(true);
		this.setVisible(true);
	}

	private void completeFields() {
		jTextFieldID.setText(String.valueOf(queryInformationRetrievalExtension.getID()));
		jTextFieldKeywords.setText(queryInformationRetrievalExtension.getKeyWords());
		jTextFieldOrganism.setText(queryInformationRetrievalExtension.getOrganism());
		jTextFieldDate.setText(String.valueOf(queryInformationRetrievalExtension.getDate()));
		jTextFieldAvailablePubs.setText(String.valueOf(queryInformationRetrievalExtension.getDocumentsRetrived()));
		jTextFieldAvailableAbstracts.setText(String.valueOf(queryInformationRetrievalExtension.getAvailable_abstracts()));	
	}

	@Override
	protected void okButtonAction() {
		removeFromClipboard();
		try {
			QueryManager.removeQueryFromDatabase(queryInformationRetrievalExtension.getID());
			queryInformationRetrievalExtension.getPubManager().notifyViewObserver();
			finish();
		} catch (SQLException e) {
			new ShowMessagePopup("Query Delete Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Query Delete Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		}
		new ShowMessagePopup("Query Delete From Database");

	}

	private void removeFromClipboard() {
		int id = queryInformationRetrievalExtension.getID();
		List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(QueryInformationRetrievalExtension.class);
		for(ClipboardItem item:cl)
		{
			QueryInformationRetrievalExtension query = (QueryInformationRetrievalExtension) item.getUserData();
			if(query.getID()==id)
			{
				Core.getInstance().getClipboard().removeClipboardItem(item);
				query.getPubManager().getQueries().remove(query);
				break;
			}
		}
	}

	@Override
	public JPanel getDetailPanel() {
		if(jPanelInformation == null) {
			jPanelInformation = new JPanel();
			jPanelInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Query Information", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelinformationLayout = new GridBagLayout();
			jPanelinformationLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelinformationLayout.rowHeights = new int[] {7, 7, 7, 20, 20, 7};
			jPanelinformationLayout.columnWeights = new double[] {0.1, 0.1};
			jPanelinformationLayout.columnWidths = new int[] {7, 7};
			jPanelInformation.setLayout(jPanelinformationLayout);
			{
				jLabelQueryID = new JLabel();
				jPanelInformation.add(jLabelQueryID, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelQueryID.setText("Query ID : ");
			}
			{
				jLabelQueryKeywords = new JLabel();
				jPanelInformation.add(jLabelQueryKeywords, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelQueryKeywords.setText("KeyWords :");
			}
			{
				jLabelOrganism = new JLabel();
				jPanelInformation.add(jLabelOrganism, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelOrganism.setText("Organism");
			}
			{
				jLabelDate = new JLabel();
				jPanelInformation.add(jLabelDate, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelDate.setText("Date : ");
			}
			{
				jLabelAvailablePubs = new JLabel();
				jPanelInformation.add(jLabelAvailablePubs, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelAvailablePubs.setText("Available Publications :");
			}
			{
				jLabelAvailableAbstracts = new JLabel();
				jPanelInformation.add(jLabelAvailableAbstracts, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelAvailableAbstracts.setText("Available Abstracts : ");
			}
			{
				jTextFieldID = new JTextField();
				jPanelInformation.add(jTextFieldID, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextFieldKeywords = new JTextField();
				jPanelInformation.add(jTextFieldKeywords, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextFieldOrganism = new JTextField();
				jPanelInformation.add(jTextFieldOrganism, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextFieldDate = new JTextField();
				jPanelInformation.add(jTextFieldDate, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextFieldAvailablePubs = new JTextField();
				jPanelInformation.add(jTextFieldAvailablePubs, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextFieldAvailableAbstracts = new JTextField();
				jPanelInformation.add(jTextFieldAvailableAbstracts, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		return jPanelInformation;
	}

	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Query_Remove";
	}

}
