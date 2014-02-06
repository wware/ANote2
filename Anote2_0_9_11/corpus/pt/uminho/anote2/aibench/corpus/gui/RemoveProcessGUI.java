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

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERSchema;
import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesProcess;
import pt.uminho.anote2.datastructures.database.queries.process.Stats.NERQueriesStats;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfo;
import pt.uminho.anote2.process.IProcess;
import pt.uminho.generic.genericpanel.database.DataBaseDelete;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class RemoveProcessGUI extends DataBaseDelete{
	
	private JPanel jPanelInformation;
	private JTextField jTextFieldID;
	private JTextField jTextFieldName;
	private JTextField jTextFieldEntityAnnotations;
	private JTextField jTextFieldType;
	private JTextField jTextFieldRelationsAnnotations;
	private JLabel jLabelRelationsAnnotations;
	private JLabel jLabelEntityAnnotations;
	private JLabel jLabelProcessType;
	private JLabel jLabelProcessName;
	private JLabel jLabelProcessID;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IProcess process;
	

	public RemoveProcessGUI(IProcess process) throws SQLException, DatabaseLoadDriverException {
		super(GlobalTextInfo.databasedeleteProcessInfo);
		this.process = process;
		completePanel();
		completeFields();
		this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);	
		Utilities.centerOnOwner(this);
		this.setTitle("Remove Process "+process.getID()+"("+process.getType()+") from Database");
		this.setModal(true);
		this.setVisible(true);
	}
	
	private void completePanel() {
		if(process.getType().toLowerCase().equals(GlobalNames.re))
		{
			{
				jLabelRelationsAnnotations = new JLabel();
				jPanelInformation.add(jLabelRelationsAnnotations, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelRelationsAnnotations.setText("Relations Annotations : ");
			}
			{
				jTextFieldRelationsAnnotations = new JTextField();
				jPanelInformation.add(jTextFieldRelationsAnnotations, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		
	}

	private void completeFields() throws SQLException, DatabaseLoadDriverException {
		jTextFieldID.setText(String.valueOf(process.getID()));
		jTextFieldName.setText(((IEProcess) process).getName());
		jTextFieldType.setText(process.getType());
		jTextFieldEntityAnnotations.setText(String.valueOf(getAnnotationsCount()));
		if(process.getType().toLowerCase().equals(GlobalNames.re))
			jTextFieldRelationsAnnotations.setText(String.valueOf(getRelationsCount()));	
	}

	private int getRelationsCount() throws SQLException, DatabaseLoadDriverException {
		int result = 0;
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(NERQueriesStats.relationsAllProcessAnnottions);
		ps.setInt(1,process.getID());
		ResultSet rs = ps.executeQuery();
		if(rs.next())
		{
			result = rs.getInt(1);
		}
		rs.close();
		ps.close();
		return result;
	}

	private int getAnnotationsCount() throws SQLException, DatabaseLoadDriverException {
		int result = 0;
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(NERQueriesStats.entitiesAllProcessAnnottions);
		ps.setInt(1,process.getID());
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
			new ShowMessagePopup("Process Deleted Fail");
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		}
		IEProcess  ieprocess = (IEProcess) process;
		((Corpus) ieprocess.getCorpus()).notifyViewObserver();
		finish();
		new ShowMessagePopup("Process Deleted");
	}

	private void removeFromDatabase() throws SQLException, DatabaseLoadDriverException {
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesProcess.inactivateProcess);
		ps.setInt(1,process.getID());
		ps.execute();
		ps.close();
	}

	private void removeFromClipboard() {
		int id = process.getID();
		List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(NERSchema.class);
		for(ClipboardItem item:cl)
		{
			NERSchema nerprocess = (NERSchema) item.getUserData();
			if(nerprocess.getID()==id)
			{
				Core.getInstance().getClipboard().removeClipboardItem(item);
				((Corpus) nerprocess.getCorpus()).getIEProcesses().remove(nerprocess);
				break;
			}
		}
		cl = Core.getInstance().getClipboard().getItemsByClass(RESchema.class);
		for(ClipboardItem item:cl)
		{
			RESchema nerprocess = (RESchema) item.getUserData();
			if(nerprocess.getID()==id)
			{
				Core.getInstance().getClipboard().removeClipboardItem(item);
				((Corpus) nerprocess.getCorpus()).getIEProcesses().remove(nerprocess);
				break;
			}
		}
	}

	@Override
	public JPanel getDetailPanel() {
		if(jPanelInformation == null) {
			jPanelInformation = new JPanel();
			jPanelInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Process Information", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelinformationLayout = new GridBagLayout();
			jPanelinformationLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelinformationLayout.rowHeights = new int[] {7, 7, 7, 20, 20, 7};
			jPanelinformationLayout.columnWeights = new double[] {0.1, 0.1};
			jPanelinformationLayout.columnWidths = new int[] {7, 7};
			jPanelInformation.setLayout(jPanelinformationLayout);
			{
				jLabelProcessID = new JLabel();
				jPanelInformation.add(jLabelProcessID, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelProcessID.setText("Process ID : ");
			}
			{
				jLabelProcessName = new JLabel();
				jPanelInformation.add(jLabelProcessName, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelProcessName.setText("Name : ");
			}
			{
				jLabelProcessType = new JLabel();
				jPanelInformation.add(jLabelProcessType, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelProcessType.setText("Type : ");
			}
			{
				jLabelEntityAnnotations = new JLabel();
				jPanelInformation.add(jLabelEntityAnnotations, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelEntityAnnotations.setText("Entity Annotations :");
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
				jTextFieldType = new JTextField();
				jPanelInformation.add(jTextFieldType, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextFieldEntityAnnotations = new JTextField();
				jPanelInformation.add(jTextFieldEntityAnnotations, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}

		}
		return jPanelInformation;
	}
	
	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink + "Corpus_Remove_Process";
	}

}
