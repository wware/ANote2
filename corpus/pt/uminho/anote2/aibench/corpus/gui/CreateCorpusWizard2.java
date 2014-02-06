package pt.uminho.anote2.aibench.corpus.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.ListModel;

import pt.uminho.anote2.aibench.corpus.structures.QueryInfornationRetrieval;
import pt.uminho.anote2.aibench.utils.conf.Configuration;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesIRProcess;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesProcess;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class CreateCorpusWizard2 extends WizartStandard{
	
	private static final long serialVersionUID = 1L;

	private JPanel jPanelUpperPanel;
	private JList jListQueries;
	private JPanel jPanelButtonsMovePanel;
	private JButton jButtonAdd;
	private JButton jButtonRemove;
	private JButton jButtonUpAll;
	private JButton jButtonAddAll;
	private JList jListSelectedQueries;
	private JLabel jLabelSelectQueries;
	private JPanel jPanelBarPanel;
	private JPanel jPanelPresentationPAnel;
	
	private IDatabase database;
	private List<QueryInfornationRetrieval> queries;


	public CreateCorpusWizard2(int sizeH, int sizeV,List<Object> param) {
		super(sizeH,sizeV,param);
		getDatabase();
		initGUI();
		this.setTitle("Create a Corpus Step 2/4");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}

	private void initGUI() {
		setEnableDoneButton(false);
		{
			jPanelUpperPanel = new JPanel();
			GridBagLayout jPanelUpperPanelLayout = new GridBagLayout();
			jPanelUpperPanelLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1};
			jPanelUpperPanelLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelUpperPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelUpperPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelUpperPanel.setLayout(jPanelUpperPanelLayout);
			{
				jPanelPresentationPAnel = new JPanel();
				GridBagLayout jPanelPresentationPAnelLayout = new GridBagLayout();
				jPanelUpperPanel.add(jPanelPresentationPAnel, new GridBagConstraints(0, 1, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelPresentationPAnelLayout.rowWeights = new double[] {0.0, 0.1, 0.0, 0.1};
				jPanelPresentationPAnelLayout.rowHeights = new int[] {7, 7, 7, 7};
				jPanelPresentationPAnelLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.0};
				jPanelPresentationPAnelLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelPresentationPAnel.setLayout(jPanelPresentationPAnelLayout);
				{
					jLabelSelectQueries = new JLabel();
					jPanelPresentationPAnel.add(jLabelSelectQueries, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelSelectQueries.setText("Select Queries");
				}
				{

					jListQueries = new JList();
					jListQueries.setModel(getQueries());
					jPanelPresentationPAnel.add(jListQueries, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

				}
				{
					ListModel jListSelectedQueriesModel = 
						new DefaultComboBoxModel(
								new String[] {});
					jListSelectedQueries = new JList();
					jPanelPresentationPAnel.add(jListSelectedQueries, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jListSelectedQueries.setModel(jListSelectedQueriesModel);
				}
				{
					jPanelButtonsMovePanel = new JPanel();
					GridBagLayout jPanelButtonsMovePanelLayout = new GridBagLayout();
					jPanelPresentationPAnel.add(jPanelButtonsMovePanel, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelButtonsMovePanelLayout.rowWeights = new double[] {0.1};
					jPanelButtonsMovePanelLayout.rowHeights = new int[] {7};
					jPanelButtonsMovePanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
					jPanelButtonsMovePanelLayout.columnWidths = new int[] {7, 7, 20, 20, 7, 7};
					jPanelButtonsMovePanel.setLayout(jPanelButtonsMovePanelLayout);
					{
						jButtonAdd = new JButton();
						jPanelButtonsMovePanel.add(jButtonAdd, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jButtonAdd.setText("Add");
						jButtonAdd.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/down.png")));
						jButtonAdd.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								add();
							}
						});
					}
					{
						jButtonAddAll = new JButton();
						jPanelButtonsMovePanel.add(jButtonAddAll, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jButtonAddAll.setText("Add All");
						jButtonAddAll.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/downall.png")));
						jButtonAddAll.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								addAll();
							}
						});
					}
					{
						jButtonUpAll = new JButton();
						jPanelButtonsMovePanel.add(jButtonUpAll, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jButtonUpAll.setText("Remove All");
						jButtonUpAll.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/upall.png")));
						jButtonUpAll.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								removeAlll();
							}
						});
					}
					{
						jButtonRemove = new JButton();
						jPanelButtonsMovePanel.add(jButtonRemove, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jButtonRemove.setText("Remove");
						jButtonRemove.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/up.png")));
						jButtonRemove.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								remove();
							}
						});
					}
				}
			}
			{
				jPanelBarPanel = new JPanel();
				GridBagLayout jPanelBarPanelLayout = new GridBagLayout();
				jPanelUpperPanel.add(jPanelBarPanel, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelBarPanelLayout.rowWeights = new double[] {0.1};
				jPanelBarPanelLayout.rowHeights = new int[] {7};
				jPanelBarPanelLayout.columnWeights = new double[] {0.1, 0.1};
				jPanelBarPanelLayout.columnWidths = new int[] {20, 7};
			}
		}
		getJScrollPaneUpPanel().setViewportView(jPanelUpperPanel);
	}
	
	protected void removeAlll() {
		int size = ((DefaultComboBoxModel) jListSelectedQueries.getModel()).getSize();
		for(int i=0;i<size;i++)
		{
			Object obj = ((DefaultComboBoxModel) jListSelectedQueries.getModel()).getElementAt(i);
			((DefaultComboBoxModel) jListQueries.getModel()).addElement(obj);
		}
		((DefaultComboBoxModel) jListSelectedQueries.getModel()).removeAllElements();
		updateGUI();
	}
	
	protected void remove() {
		
		Object[] objects = jListSelectedQueries.getSelectedValues();
		for(Object obj:objects)
		{
			((DefaultComboBoxModel) jListSelectedQueries.getModel()).removeElement(obj);
			((DefaultComboBoxModel) jListQueries.getModel()).addElement(obj);
		}
		updateGUI();
	}

	protected void addAll() {
		int size = ((DefaultComboBoxModel) jListQueries.getModel()).getSize();
		for(int i=0;i<size;i++)
		{
			Object obj = ((DefaultComboBoxModel) jListQueries.getModel()).getElementAt(i);
			((DefaultComboBoxModel) jListSelectedQueries.getModel()).addElement(obj);
		}
		((DefaultComboBoxModel) jListQueries.getModel()).removeAllElements();
		updateGUI();
		
	}

	protected void add() {
		Object[] objects = jListQueries.getSelectedValues();
		for(Object obj:objects)
		{
			((DefaultComboBoxModel) jListSelectedQueries.getModel()).addElement(obj);
			((DefaultComboBoxModel) jListQueries.getModel()).removeElement(obj);
		}
		updateGUI();
	}
	
	private void updateGUI()
	{
		jListSelectedQueries.updateUI();
		jListQueries.updateUI();
	}

	private DefaultComboBoxModel getQueries()
	{
		try {
			return fillModelQuerysTable("");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new DefaultComboBoxModel();
	}
	
	private  DefaultComboBoxModel fillModelQuerysTable(String order) throws SQLException{

		DefaultComboBoxModel list = new DefaultComboBoxModel();
		
		Properties prop;
		ResultSet rs2 = null;
		String queryDB = QueriesProcess.selectQueriesPubmed;
		queryDB = queryDB+order;	
		PreparedStatement statement = database.getConnection().prepareStatement(queryDB);
		PreparedStatement statement2 = database.getConnection().prepareStatement(QueriesIRProcess.selectQueryProperties);
		ResultSet rs = statement.executeQuery();	
		queries = new ArrayList<QueryInfornationRetrieval>();
		while(rs.next())
		{	
			prop = new Properties();
			statement2.setInt(1,rs.getInt(1));
			rs2 = statement2.executeQuery();
			while(rs2.next())
			{
				prop.put(rs2.getString(1),rs2.getString(2));
			}
			QueryInfornationRetrieval query = new QueryInfornationRetrieval(
					rs.getInt(1),
					rs.getDate(2),
					rs.getString(3),
					rs.getString(4),
					rs.getInt(5),
					rs.getInt(6),
					0,
					prop);	
			queries.add(query);
			rs2.close();
		}
		if(rs!=null)
		{
			rs.close();
		}
		for(QueryInfornationRetrieval res: queries)
		{
			list.addElement(res);
		}
		return list;
	}
	
	private void getDatabase()
	{
		ArrayList<String> elements = new ArrayList<String>();
		elements.add("DB-Host");
		elements.add("DB-Port");
		elements.add("DB-Schema");
		elements.add("DB-User");
		elements.add("DB-Pwd");
		ArrayList<String> data = Configuration.getElementByXMLFile("conf/settings.conf",elements);
		database = new MySQLDatabase(data.get(0),data.get(1),data.get(2),data.get(3),data.get(4));
	}

	
	public void done() {}

	public void goBack() {
		this.setVisible(false);
		new CreateCorpusWizard2(600,400,new ArrayList<Object>());
	}

	public void goNext() {
	
		int size = ((DefaultComboBoxModel) jListSelectedQueries.getModel()).getSize();
		if(size==0)
		{
			Workbench.getInstance().warn("Please select query(ies)");
		}
		else
		{
			this.setVisible(false);
			List<QueryInfornationRetrieval> queries = new ArrayList<QueryInfornationRetrieval>();
			for(int i=0;i<size;i++)
			{
				Object obj = ((DefaultComboBoxModel) jListSelectedQueries.getModel()).getElementAt(i);
				queries.add((QueryInfornationRetrieval)obj);
			}
			List<Object> objs = getParam();
			objs.add(queries);
			new CreateCorpusWizard3(800,600,objs);
		}
	}

	public static void main(String[] args) throws IOException{
		new CreateCorpusWizard2(600,400,null);
	}

}
