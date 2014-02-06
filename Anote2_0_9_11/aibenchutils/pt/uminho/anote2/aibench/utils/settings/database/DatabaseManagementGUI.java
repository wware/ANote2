package pt.uminho.anote2.aibench.utils.settings.database;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import pt.uminho.anote2.aibench.utils.exceptions.treat.ExceptionMessageDialog;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.core.database.DataBaseTypeEnum;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.DatabaseArchive;
import pt.uminho.anote2.datastructures.database.DatabaseFactory;
import pt.uminho.anote2.datastructures.database.DatabaseManager;
import pt.uminho.anote2.datastructures.database.startdatabase.StartDatabase;
import pt.uminho.anote2.datastructures.settings.database.DatabaseDefaultSettings;
import pt.uminho.anote2.datastructures.settings.database.help.TreatDatabaseException;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManagerException;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class DatabaseManagementGUI extends JDialog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton jButtonExitORRestart;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JPanel jPanelDatabaseManagement;
	private JPanel jPanelDatabaseAndAddNewDatabseConnection;
	private JPanel jPanelCurrentDatabaseSettings;
	private JPanel jPanelAddNewDatabase;
	private JComboBox jComboBoxDatabaseTypes;
	private JLabel jLabelHost;
	private JTextField jTextFieldhost;
	private JLabel jLabelPort;
	private JTextField jTextFieldPort;
	private JLabel jLabelSchema;
	private JTextField jTextFieldSchema;
	private JLabel jLabelUser;
	private JTextField jTextFieldUser;
	private JLabel jLabelPassword;
	private JPasswordField jPasswordFieldPwd;
	private JPanel jPanelConfirm;
	private JCheckBox jCheckBoxSelectAsPrimaryDatabase;
	private JButton jButtonAddDatabase;
	private JLabel jLabelCurrentDatabaseType;
	private JTextField jTextFieldCurrentDatabaseType;
	private JLabel jLabelCurrentDatabaseHost;
	private JTextField jTextFieldCurrentDatabaseHost;
	private JLabel jLabelCurrentDatabasePort;
	private JTextField jTextFieldCurrentDatabasePort;
	private JLabel jLabelCuurentDatabaseSchema;
	private JTextField jTextFieldCurrentDatabaseSchema;
	private JLabel jLabelCurrentDatabaseUser;
	private JTextField jTextFieldCurrentDatabaseUser;
	private JLabel jLabelCurrentDatabasePwd;
	private JPasswordField jPasswordFieldCurrentDatabasePwd;
	private JPanel jPanelDatabaseArchive;
	private JScrollPane jScrollPaneDatabaseArchive;
	private JTable jTabelArchive;
	private DatabaseArchive databaseArchive;
	private JButton jButtonHelp;
	private JButton jButtonTryReconnect;
	private JLabel jLabelDatabaseType;
	private IDatabase currentDatabase;
	private JPanel jPanelButtons;

	public DatabaseManagementGUI()
	{
		super(Workbench.getInstance().getMainFrame());
		initGUI();
		fillSettingsArchive();
		this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle("Database Connection");		
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}

	public DatabaseManagementGUI(Exception e)
	{
		super(Workbench.getInstance().getMainFrame());
		ExceptionMessageDialog.showMessageDialog(Workbench.getInstance().getMainFrame(), e);
		initGUI();
		fillSettings();
		putRecooncetButton();
		this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle("Database Connection");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	
	private void fillSettings() {
		fillSettingsArchive();
		IDatabase database = ((DatabaseManager) PropertiesManager.getPManager().getProperty(DatabaseDefaultSettings.DATABASE)).getDatabase();
		if(database!=null && !(database instanceof StartDatabase))
			fillCurrentDatabase(database);
	}

	private void fillSettingsArchive() {
		this.databaseArchive = (DatabaseArchive) PropertiesManager.getPManager().getProperty(DatabaseDefaultSettings.DATABASE_ARCHIVE);
		if(databaseArchive==null)
			databaseArchive = new DatabaseArchive();
		builtDatabaseArchiveTable();
	}
	
	private void fillCurrentDatabase(IDatabase database) {
		currentDatabase = database;
		jTextFieldCurrentDatabaseType.setText(database.getDataBaseType().toString());
		jTextFieldCurrentDatabaseHost.setText(database.getHost());
		jTextFieldCurrentDatabasePort.setText(database.getPort());
		jTextFieldCurrentDatabaseSchema.setText(database.getSchema());
		jTextFieldCurrentDatabaseUser.setText(database.getUser());
		jPasswordFieldCurrentDatabasePwd.setText(database.getPwd());
	}
	
	private void builtDatabaseArchiveTable() {
		String[] columns = new String[] {"Database Type","Host", "Port", "Schema","User","Remove","Load"};
		DefaultTableModel tableModel = new DefaultTableModel()
		{
			private static final long serialVersionUID = 1L;
			
			
			public Class<?> getColumnClass(int columnIndex){
				return String.class;
			}
			
			public boolean isCellEditable(int row, int col) {  
				if (col == 5 || col == 6) {  
					return true;  
				} else {  
					return false;  
				}         
			} 

		};	
		tableModel.setColumnIdentifiers(columns);	
		this.jTabelArchive.setModel(tableModel);
		for(IDatabase db: this.databaseArchive.getDatabaseArchive())
		{
			addTableRow(db);
		}
		completeTable(this.jTabelArchive);
	}
	
	private void addTableRow(IDatabase databaseAdded) {
		Object[] obj = new Object[5];
		obj[0] = databaseAdded.getDataBaseType().toString();
		obj[1] = databaseAdded.getHost();
		obj[2] = databaseAdded.getPort();
		obj[3] = databaseAdded.getSchema();
		obj[4] = databaseAdded.getUser();
		((DefaultTableModel) this.jTabelArchive.getModel()).addRow(obj);
	}
	
	private void completeTable(JTable jtable) {
		jtable.getColumnModel().getColumn(6).setMaxWidth(50);
		jtable.getColumnModel().getColumn(6).setMinWidth(50);
		jtable.getColumnModel().getColumn(6).setPreferredWidth(50);
		jtable.getColumnModel().getColumn(5).setMaxWidth(50);
		jtable.getColumnModel().getColumn(5).setMinWidth(50);
		jtable.getColumnModel().getColumn(5).setPreferredWidth(50);
		jtable.setRowHeight(20);
		jtable.getColumn("Remove").setCellRenderer(new ButtonTableDBRenderer());
		jtable.getColumn("Remove").setCellEditor(new ButtonTableDBCellEditor());
		jtable.getColumn("Load").setCellRenderer(new ButtonLoadResourceRenderer());
		jtable.getColumn("Load").setCellEditor(new ButtonLoadResourceCellEditor());
	}
	
	class ButtonTableDBRenderer  extends JButton implements TableCellRenderer{

		private static final long serialVersionUID = 6770443805813243771L;

		public ButtonTableDBRenderer() {
			super();
			this.setText("-");

		}

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			this.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					removeDatabaseFromArchive();
				}
			});
			return this;
		}
	}
	
	class ButtonTableDBCellEditor extends DefaultCellEditor
	{
		private static final long serialVersionUID = 1L;

		protected JButton button;

		  private String label = "-";

		  public ButtonTableDBCellEditor() {
		    super(new JCheckBox());
		    button = new JButton();
		    button.setOpaque(true);
		    button.setText(label);
		    button.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	 removeDatabaseFromArchive();
		      }
		    });
		  }

		  public Component getTableCellEditorComponent(JTable table, Object value,
		      boolean isSelected, int row, int column) {
		    if (isSelected) {
		      button.setForeground(table.getSelectionForeground());
		      button.setBackground(table.getSelectionBackground());
		    } else {
		      button.setForeground(table.getForeground());
		      button.setBackground(table.getBackground());
		    }
		    return button;
		  }

		  public Object getCellEditorValue() {
		    return super.getCellEditorValue();
		  }
	}

	protected void removeDatabaseFromArchive() {
		int selectedRow = jTabelArchive.getSelectedRow();
		this.databaseArchive.removedatabase(selectedRow);
		((DefaultTableModel) jTabelArchive.getModel()).removeRow(selectedRow);
	}
	
	class ButtonLoadResourceRenderer  extends JButton implements TableCellRenderer{

		private static final long serialVersionUID = 6770443805813243771L;

		public ButtonLoadResourceRenderer() {
			super();
			this.setText("+");
		}

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			
			this.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					defineDatabaseAsPrimary();
				}
			});
			return this;
		}
	}
	
	class ButtonLoadResourceCellEditor extends DefaultCellEditor
	{
		private static final long serialVersionUID = 1L;

		protected JButton button;

		  private String label = "+";

		  public ButtonLoadResourceCellEditor() {
		    super(new JCheckBox());
		    button = new JButton();
		    button.setOpaque(true);
		    button.setText(label);
		    button.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
				defineDatabaseAsPrimary();
		      }
		    });
		  }

		  public Component getTableCellEditorComponent(JTable table, Object value,
		      boolean isSelected, int row, int column) {
		    if (isSelected) {
		      button.setForeground(table.getSelectionForeground());
		      button.setBackground(table.getSelectionBackground());
		    } else {
		      button.setForeground(table.getForeground());
		      button.setBackground(table.getBackground());
		    }

		    return button;
		  }

		  public Object getCellEditorValue() {
		    return super.getCellEditorValue();
		  }
	}
	
	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.0};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			getContentPane().setLayout(thisLayout);
			{
				jPanelButtons = new JPanel();
				GridBagLayout jPanelButtonsLayout = new GridBagLayout();
				getContentPane().add(jPanelButtons, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jPanelButtonsLayout.rowWeights = new double[] {0.1};
				jPanelButtonsLayout.rowHeights = new int[] {7};
				jPanelButtonsLayout.columnWeights = new double[] {0.0, 0.1};
				jPanelButtonsLayout.columnWidths = new int[] {7, 7};
				jPanelButtons.setLayout(jPanelButtonsLayout);
				{
					jButtonExitORRestart = new JButton();
					jPanelButtons.add(jButtonExitORRestart, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelButtons.add(getJButtonHelp(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
				jButtonExitORRestart.setText("Exit @Note2");
				jButtonExitORRestart.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						System.exit(0);
					}
				});
			}
			{
				jPanelDatabaseManagement = new JPanel();
				GridBagLayout jPanelDatabaseManagementLayout = new GridBagLayout();
				getContentPane().add(jPanelDatabaseManagement, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelDatabaseManagementLayout.rowWeights = new double[] {0.1, 0.1};
				jPanelDatabaseManagementLayout.rowHeights = new int[] {7, 7};
				jPanelDatabaseManagementLayout.columnWeights = new double[] {0.1};
				jPanelDatabaseManagementLayout.columnWidths = new int[] {7};
				jPanelDatabaseManagement.setLayout(jPanelDatabaseManagementLayout);
				{
					jPanelDatabaseManagement.add(getJPanelDatabaseAndAddNewDatabseConnection(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
				{
					jPanelDatabaseManagement.add(getJPanelDatabaseArchive(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
			}
		}		
	}
	
	private JPanel getJPanelDatabaseAndAddNewDatabseConnection() {
		if(jPanelDatabaseAndAddNewDatabseConnection == null) {
			jPanelDatabaseAndAddNewDatabseConnection = new JPanel();
			GridBagLayout jPanelDatabaseAndAddNewDatabseConnectionLayout = new GridBagLayout();
			jPanelDatabaseAndAddNewDatabseConnectionLayout.rowWeights = new double[] {0.1};
			jPanelDatabaseAndAddNewDatabseConnectionLayout.rowHeights = new int[] {7};
			jPanelDatabaseAndAddNewDatabseConnectionLayout.columnWeights = new double[] {0.5, 0.5};
			jPanelDatabaseAndAddNewDatabseConnectionLayout.columnWidths = new int[] {7, 7};
			jPanelDatabaseAndAddNewDatabseConnection.setLayout(jPanelDatabaseAndAddNewDatabseConnectionLayout);
			jPanelDatabaseAndAddNewDatabseConnection.add(getJPanelActualDatabaseSettings(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDatabaseAndAddNewDatabseConnection.add(getJPanelAddNewDatabase(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelDatabaseAndAddNewDatabseConnection;
	}
	
	private JPanel getJPanelActualDatabaseSettings() {
		if(jPanelCurrentDatabaseSettings == null) {
			jPanelCurrentDatabaseSettings = new JPanel();
			GridBagLayout jPanelCurrentDatabaseSettingsLayout = new GridBagLayout();
			jPanelCurrentDatabaseSettings.setBorder(BorderFactory.createTitledBorder("Current Database"));
			jPanelCurrentDatabaseSettingsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelCurrentDatabaseSettingsLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7, 20};
			jPanelCurrentDatabaseSettingsLayout.columnWeights = new double[] {0.15, 0.75};
			jPanelCurrentDatabaseSettingsLayout.columnWidths = new int[] {7, 7};
			jPanelCurrentDatabaseSettings.setLayout(jPanelCurrentDatabaseSettingsLayout);
			jPanelCurrentDatabaseSettings.add(getJLabelCurrentDatabaseType(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelCurrentDatabaseSettings.add(getJTextFieldDatabaseType(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
			jPanelCurrentDatabaseSettings.add(getJLabel1(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelCurrentDatabaseSettings.add(getJTextFieldCurrentHost(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
			jPanelCurrentDatabaseSettings.add(getJLabelCurrentDatabasePort(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelCurrentDatabaseSettings.add(getJTextFieldCurrentDatabasePort(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
			jPanelCurrentDatabaseSettings.add(getJLabelCuurentDatabaseSchema(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelCurrentDatabaseSettings.add(getJTextFieldCurrentDatabaseSchema(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
			jPanelCurrentDatabaseSettings.add(getJLabelCurrentDatabaseUser(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelCurrentDatabaseSettings.add(getJTextFieldCurrentDatabaseUser(), new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
			jPanelCurrentDatabaseSettings.add(getJLabelCurrentDatabasePwd(), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelCurrentDatabaseSettings.add(getJPasswordFieldCurrentDatabasePwd(), new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
			jPanelCurrentDatabaseSettings.add(getJButtonTryReconnect(), new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5), 0, 0));
		}
		return jPanelCurrentDatabaseSettings;
	}
	
	private JPanel getJPanelAddNewDatabase() {
		if(jPanelAddNewDatabase == null) {
			jPanelAddNewDatabase = new JPanel();
			GridBagLayout jPanelAddNewDatabaseLayout = new GridBagLayout();
			jPanelAddNewDatabase.setBorder(BorderFactory.createTitledBorder("Add New Database Connection"));
			jPanelAddNewDatabaseLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelAddNewDatabaseLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7, 20, 7};
			jPanelAddNewDatabaseLayout.columnWeights = new double[] {0.25, 0.75};
			jPanelAddNewDatabaseLayout.columnWidths = new int[] {7, 7};
			jPanelAddNewDatabase.setLayout(jPanelAddNewDatabaseLayout);
			jPanelAddNewDatabase.add(getJLabelDatabaseType(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelAddNewDatabase.add(getJComboBoxDatabaseTypes(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
			jPanelAddNewDatabase.add(getJLabelHost(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelAddNewDatabase.add(getJTextFieldhost(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
			jPanelAddNewDatabase.add(getJLabelPort(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelAddNewDatabase.add(getJTextFieldPort(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
			jPanelAddNewDatabase.add(getJLabelSchema(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelAddNewDatabase.add(getJTextFieldSchema(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
			jPanelAddNewDatabase.add(getJLabelUser(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelAddNewDatabase.add(getJTextFieldUser(), new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
			jPanelAddNewDatabase.add(getJLabelPassword(), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelAddNewDatabase.add(getJPasswordFieldPwd(), new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
			jPanelAddNewDatabase.add(getJPanelConfirm(), new GridBagConstraints(0, 6, 2, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelAddNewDatabase;
	}
	
	private JLabel getJLabelDatabaseType() {
		if(jLabelDatabaseType == null) {
			jLabelDatabaseType = new JLabel();
			jLabelDatabaseType.setText("Database Type :");
		}
		return jLabelDatabaseType;
	}
	
	private JComboBox getJComboBoxDatabaseTypes() {
		if(jComboBoxDatabaseTypes == null) {
			ComboBoxModel jComboBoxDatabaseTypesModel = 
					new DefaultComboBoxModel();
			for(DataBaseTypeEnum dbType :DataBaseTypeEnum.values())
			{
				((DefaultComboBoxModel)jComboBoxDatabaseTypesModel).addElement(dbType);
			}
			jComboBoxDatabaseTypes = new JComboBox();
			jComboBoxDatabaseTypes.setModel(jComboBoxDatabaseTypesModel);
		}
		return jComboBoxDatabaseTypes;
	}
	
	private JLabel getJLabelHost() {
		if(jLabelHost == null) {
			jLabelHost = new JLabel();
			jLabelHost.setText("Host :");
		}
		return jLabelHost;
	}
	
	private JTextField getJTextFieldhost() {
		if(jTextFieldhost == null) {
			jTextFieldhost = new JTextField();
			jTextFieldhost.setText("localhost");
		}
		return jTextFieldhost;
	}
	
	private JLabel getJLabelPort() {
		if(jLabelPort == null) {
			jLabelPort = new JLabel();
			jLabelPort.setText("Port :");
		}
		return jLabelPort;
	}
	
	private JTextField getJTextFieldPort() {
		if(jTextFieldPort == null) {
			jTextFieldPort = new JTextField();
			jTextFieldPort.setText("3306");
		}
		return jTextFieldPort;
	}
	
	private JLabel getJLabelSchema() {
		if(jLabelSchema == null) {
			jLabelSchema = new JLabel();
			jLabelSchema.setText("Schema :");
		}
		return jLabelSchema;
	}
	
	private JTextField getJTextFieldSchema() {
		if(jTextFieldSchema == null) {
			jTextFieldSchema = new JTextField();
			jTextFieldSchema.setText("anote_db");
		}
		return jTextFieldSchema;
	}
	
	private JLabel getJLabelUser() {
		if(jLabelUser == null) {
			jLabelUser = new JLabel();
			jLabelUser.setText("User :");
		}
		return jLabelUser;
	}
	
	private JTextField getJTextFieldUser() {
		if(jTextFieldUser == null) {
			jTextFieldUser = new JTextField();
			jTextFieldUser.setText("root");
		}
		return jTextFieldUser;
	}
	
	private JLabel getJLabelPassword() {
		if(jLabelPassword == null) {
			jLabelPassword = new JLabel();
			jLabelPassword.setText("Password :");
		}
		return jLabelPassword;
	}
	
	private JPasswordField getJPasswordFieldPwd() {
		if(jPasswordFieldPwd == null) {
			jPasswordFieldPwd = new JPasswordField();
		}
		return jPasswordFieldPwd;
	}
	
	private JPanel getJPanelConfirm() {
		if(jPanelConfirm == null) {
			jPanelConfirm = new JPanel();
			GridBagLayout jPanelConfirmLayout = new GridBagLayout();
			jPanelConfirmLayout.rowWeights = new double[] {0.1, 0.1};
			jPanelConfirmLayout.rowHeights = new int[] {7, 7};
			jPanelConfirmLayout.columnWeights = new double[] {0.1};
			jPanelConfirmLayout.columnWidths = new int[] {7};
			jPanelConfirm.setLayout(jPanelConfirmLayout);
			jPanelConfirm.add(getJCheckBoxSelectAsPrimaryDatabase(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));
			jPanelConfirm.add(getJButtonAddDatabase(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));
		}
		return jPanelConfirm;
	}
	
	private JCheckBox getJCheckBoxSelectAsPrimaryDatabase() {
		if(jCheckBoxSelectAsPrimaryDatabase == null) {
			jCheckBoxSelectAsPrimaryDatabase = new JCheckBox();
			jCheckBoxSelectAsPrimaryDatabase.setText("Use as Primary Database");
			jCheckBoxSelectAsPrimaryDatabase.setSelected(true);
		}
		return jCheckBoxSelectAsPrimaryDatabase;
	}
	
	private JButton getJButtonAddDatabase() {
		if(jButtonAddDatabase == null) {
			jButtonAddDatabase = new JButton();
			jButtonAddDatabase.setText("Add Database to Archive");
			jButtonAddDatabase.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					addDatabase();
				}
			});
		}
		return jButtonAddDatabase;
	}
	
	protected void addDatabase() {
		DataBaseTypeEnum databaseType = (DataBaseTypeEnum) jComboBoxDatabaseTypes.getSelectedItem();
		String host = jTextFieldhost.getText();
		String port = jTextFieldPort.getText();
		String schema = jTextFieldSchema.getText();
		String user = jTextFieldUser.getText();
		String pwd = String.valueOf(jPasswordFieldPwd.getPassword());
		IDatabase databaseAdded = DatabaseFactory.createDatabase(databaseType, host, port, schema, user, pwd);
		try {
			databaseAdded.openConnection();
			if(databaseAdded.getConnection()!=null)
			{
				addDBToSystem(databaseAdded);
			}
		} catch (DatabaseLoadDriverException e) {
			TreatDatabaseException.treat(databaseAdded,e);
			return;
		} catch (SQLException e) {
			if(TreatDatabaseException.treat(databaseAdded,e))
			{
				addDBToSystem(databaseAdded);
			}
			else
			{
				return;
			}
		}
		
	}

	private void addDBToSystem(IDatabase databaseAdded) {
		this.databaseArchive.addDatabaseToArchive(databaseAdded);
		addTableRow(databaseAdded);
		if(jCheckBoxSelectAsPrimaryDatabase.isSelected())
		{
			fillCurrentDatabase(databaseAdded);
			this.currentDatabase = databaseAdded;
			changeExitButtonToRestart();
		}
	} 
	
	private JLabel getJLabelCurrentDatabaseType() {
		if(jLabelCurrentDatabaseType == null) {
			jLabelCurrentDatabaseType = new JLabel();
			jLabelCurrentDatabaseType.setText("Database Type :");
		}
		return jLabelCurrentDatabaseType;
	}
	
	private JTextField getJTextFieldDatabaseType() {
		if(jTextFieldCurrentDatabaseType == null) {
			jTextFieldCurrentDatabaseType = new JTextField();
			jTextFieldCurrentDatabaseType.setEditable(false);
		}
		return jTextFieldCurrentDatabaseType;
	}
	
	private JLabel getJLabel1() {
		if(jLabelCurrentDatabaseHost == null) {
			jLabelCurrentDatabaseHost = new JLabel();
			jLabelCurrentDatabaseHost.setText("Host :");
		}
		return jLabelCurrentDatabaseHost;
	}
	
	private JTextField getJTextFieldCurrentHost() {
		if(jTextFieldCurrentDatabaseHost == null) {
			jTextFieldCurrentDatabaseHost = new JTextField();
			jTextFieldCurrentDatabaseHost.setEditable(false);
		}
		return jTextFieldCurrentDatabaseHost;
	}
	
	private JLabel getJLabelCurrentDatabasePort() {
		if(jLabelCurrentDatabasePort == null) {
			jLabelCurrentDatabasePort = new JLabel();
			jLabelCurrentDatabasePort.setText("Port :");
		}
		return jLabelCurrentDatabasePort;
	}
	
	private JTextField getJTextFieldCurrentDatabasePort() {
		if(jTextFieldCurrentDatabasePort == null) {
			jTextFieldCurrentDatabasePort = new JTextField();
			jTextFieldCurrentDatabasePort.setEditable(false);
		}
		return jTextFieldCurrentDatabasePort;
	}
	
	private JLabel getJLabelCuurentDatabaseSchema() {
		if(jLabelCuurentDatabaseSchema == null) {
			jLabelCuurentDatabaseSchema = new JLabel();
			jLabelCuurentDatabaseSchema.setText("Schema :");
		}
		return jLabelCuurentDatabaseSchema;
	}
	
	private JTextField getJTextFieldCurrentDatabaseSchema() {
		if(jTextFieldCurrentDatabaseSchema == null) {
			jTextFieldCurrentDatabaseSchema = new JTextField();
			jTextFieldCurrentDatabaseSchema.setEditable(false);
		}
		return jTextFieldCurrentDatabaseSchema;
	}
	
	private JLabel getJLabelCurrentDatabaseUser() {
		if(jLabelCurrentDatabaseUser == null) {
			jLabelCurrentDatabaseUser = new JLabel();
			jLabelCurrentDatabaseUser.setText("User :");
		}
		return jLabelCurrentDatabaseUser;
	}
	
	private JTextField getJTextFieldCurrentDatabaseUser() {
		if(jTextFieldCurrentDatabaseUser == null) {
			jTextFieldCurrentDatabaseUser = new JTextField();
			jTextFieldCurrentDatabaseUser.setEditable(false);
		}
		return jTextFieldCurrentDatabaseUser;
	}
	
	private JLabel getJLabelCurrentDatabasePwd() {
		if(jLabelCurrentDatabasePwd == null) {
			jLabelCurrentDatabasePwd = new JLabel();
			jLabelCurrentDatabasePwd.setText("Password :");
		}
		return jLabelCurrentDatabasePwd;
	}
	
	private JPasswordField getJPasswordFieldCurrentDatabasePwd() {
		if(jPasswordFieldCurrentDatabasePwd == null) {
			jPasswordFieldCurrentDatabasePwd = new JPasswordField();
			jPasswordFieldCurrentDatabasePwd.setEditable(false);
		}
		return jPasswordFieldCurrentDatabasePwd;
	}
	
	private JPanel getJPanelDatabaseArchive() {
		if(jPanelDatabaseArchive == null) {
			jPanelDatabaseArchive = new JPanel();
			GridBagLayout jPanelDatabaseArchiveLayout = new GridBagLayout();
			jPanelDatabaseArchive.setBorder(BorderFactory.createTitledBorder("Database Archive"));
			jPanelDatabaseArchiveLayout.rowWeights = new double[] {0.1};
			jPanelDatabaseArchiveLayout.rowHeights = new int[] {7};
			jPanelDatabaseArchiveLayout.columnWeights = new double[] {0.1};
			jPanelDatabaseArchiveLayout.columnWidths = new int[] {7};
			jPanelDatabaseArchive.setLayout(jPanelDatabaseArchiveLayout);
			jPanelDatabaseArchive.setSize(450, 100);
			jPanelDatabaseArchive.add(getJScrollPaneDatabaseArchive(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelDatabaseArchive;
	}
	
	private JScrollPane getJScrollPaneDatabaseArchive() {
		if(jScrollPaneDatabaseArchive == null) {
			jScrollPaneDatabaseArchive = new JScrollPane();
			jScrollPaneDatabaseArchive.setSize(450, 100);
			jScrollPaneDatabaseArchive.setViewportView(getJPanelArchive());
		}
		return jScrollPaneDatabaseArchive;
	}
	
	private JTable getJPanelArchive() {
		if(jTabelArchive == null) {
			jTabelArchive = new JTable();
		}
		return jTabelArchive;
	}
	
	protected void defineDatabaseAsPrimary(){
		int selectedRow = jTabelArchive.getSelectedRow();
		IDatabase databaseToPrimary = this.databaseArchive.getDatabaseArchive().get(selectedRow);
		try {
			databaseToPrimary.getConnection();
			fillCurrentDatabase(databaseToPrimary);
			this.currentDatabase = databaseToPrimary;
			changeExitButtonToRestart();
		} catch (DatabaseLoadDriverException e) {
			TreatDatabaseException.treat(databaseToPrimary, e);
		} catch (SQLException e) {
			if(TreatDatabaseException.treat(databaseToPrimary, e))
			{
				try {
					databaseToPrimary.getConnection();
				} catch (DatabaseLoadDriverException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				fillCurrentDatabase(databaseToPrimary);
				this.currentDatabase = databaseToPrimary;
				changeExitButtonToRestart();
			}
		}
	}
	
	private void changeExitButtonToRestart() {
		jButtonExitORRestart.setText("Save and Restart @Note2");
		jButtonExitORRestart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				restart();
			}
		});
	}

	protected void restart() {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(DatabaseDefaultSettings.DATABASE_ARCHIVE, databaseArchive);
		props.put(DatabaseDefaultSettings.DATABASE, new DatabaseManager(currentDatabase));
		
		try {
			PropertiesManager.getPManager().getNodes().get("General.Database").setProperties(props);
			PropertiesManager.getPManager().saveNodes();
		} catch (PropertiesManagerException e) {
			e.printStackTrace();
		}					
		System.exit(10);
	}

	private JButton getJButtonTryReconnect() {
		if(jButtonTryReconnect == null) {
			jButtonTryReconnect = new JButton();
			jButtonTryReconnect.setText("Try Reconnect");
			jButtonTryReconnect.setVisible(false);
			if(currentDatabase!=null)
			{
				putRecooncetButton();
			}
		}
		return jButtonTryReconnect;
	}

	private void putRecooncetButton() {
		jButtonTryReconnect.setVisible(true);
		jButtonTryReconnect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tryReconnect();
			}
		});
	}

	protected void tryReconnect() {
		try {
			currentDatabase.getConnection();
			changeExitButtonToRestart();
		} catch (DatabaseLoadDriverException e) {
			TreatDatabaseException.treat(currentDatabase, e);
		} catch (SQLException e) {
			TreatDatabaseException.treat(currentDatabase, e);
		}
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						Help.internetAccess(GlobalOptions.wikiGeneralLink+"Starting_to_use_@Note");
					} catch (IOException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
		}
		return jButtonHelp;
	}

}

