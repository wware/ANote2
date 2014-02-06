package pt.uminho.anote2.aibench.curator.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.curator.menu.externallinks.ExternalLinksMenu;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewOkButtonOnly;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.utils.MenuItem;
import pt.uminho.anote2.datastructures.utils.Source;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class ChangeCuratorExtenalLinksMenu extends DialogGenericViewOkButtonOnly implements InputGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton jButtonAddSourceLinkage;
	private JCheckBox jCheckBoxWarningMessages;
	private JPanel jPanelCheckBox;
	private JButton jButtonUpdateName;
	private JButton jButtonRemoveSecondaryLinks;
	private JButton jButtonRemovePrimaryLinks;
	private JTextArea jTextAreaURL;
	private JButton jButtonIconShow;
	private JTextField jTextFieldName;
	private JTextField jTextFieldID;
	private JLabel jLabelID;
	private JLabel jLabelmenuName;
	private JLabel jLabelIcon;
	private JLabel jLabelURL;
	private JPanel jPanelLinkgeneralInfo;
	private JList jListSourceLingage;
	private JScrollPane jScrollPaneSourceMenuLinkage;
	private JButton jButtonRemoveSourceLinkage;
	private JPanel jPanelSecundaryLinks;
	private JPanel jPanelSourceLinkage;
	private JScrollPane jScrollPaneSecondaryLinks;
	private JScrollPane jScrollPaneJPrimaryLinks;
	private JList jListSecondaryLinks;
	private JButton jButtonaDDSecondaryLinks;
	private JList jListPrimaryLinks;
	private JButton jButtonAddPrimaryLinks;
	private JPanel jPanelMenuLinkSelectedDetails;
	private JPanel jPanelPrimaryLinks;
	private JPanel jPanelUpperPanel;
	private Map<Integer,MenuItem> menuIDMenuItem;
	private List<MenuItem> primaryMenuItems;
	private Map<Integer,Source> sourceIDSource;
	private JFileChooser filePath;

	public ChangeCuratorExtenalLinksMenu()
	{
		super("Curator Menu Link-out Manager");
		try {
			getMenuItems();
			getSources();
			initGUI();	
			this.setModal(true);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		}
	}

	private void getSources() throws SQLException, DatabaseLoadDriverException {
		sourceIDSource = new HashMap<Integer, Source>();
		List<Source> sources;
		sources = HelpDatabase.getAllAvailableSources();
		for(Source source:sources)
		{
			sourceIDSource.put(source.getSourceID(), source);
		}
	}

	private void initGUI() {
		
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {1.0, 0.0, 0.0};
		thisLayout.rowHeights = new int[] {7, 7, 7};
		thisLayout.columnWeights = new double[] {1.0};
		thisLayout.columnWidths = new int[] {7};
		getContentPane().setLayout(thisLayout);
		{
			getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		{
			filePath = new JFileChooser(); 	
			filePath.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jPanelUpperPanel = new JPanel();
			GridLayout jPanelUpperPanelLayout = new GridLayout(1, 3);
			jPanelUpperPanelLayout.setColumns(3);
			jPanelUpperPanelLayout.setHgap(5);
			jPanelUpperPanelLayout.setVgap(5);
			getContentPane().add(jPanelUpperPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			getContentPane().add(getJPanelCheckBox(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelUpperPanel.setLayout(jPanelUpperPanelLayout);
			{
				jPanelPrimaryLinks = new JPanel();
				GridBagLayout jPanelPrimaryLinksLayout = new GridBagLayout();
				jPanelUpperPanel.add(jPanelPrimaryLinks);
				jPanelPrimaryLinksLayout.rowWeights = new double[] {1, 0.0};
				jPanelPrimaryLinksLayout.rowHeights = new int[] {7, 7};
				jPanelPrimaryLinksLayout.columnWeights = new double[] {0.0, 1};
				jPanelPrimaryLinksLayout.columnWidths = new int[] {7, 7};
				jPanelPrimaryLinks.setLayout(jPanelPrimaryLinksLayout);
				jPanelPrimaryLinks.setBorder(BorderFactory.createTitledBorder(null, "Primary links", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
				{
					jPanelPrimaryLinks.add(getJButtonAddPrimaryLinks(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				}
				{
					jScrollPaneJPrimaryLinks = new JScrollPane();
					jPanelPrimaryLinks.add(jScrollPaneJPrimaryLinks, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					{

						jScrollPaneJPrimaryLinks.setViewportView(getJListPrimaryMenuItems());
					}
				}
				{
					jPanelPrimaryLinks.add(getJButtonRemovePrimaryLinks(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
				}
			}
			{
				jPanelSecundaryLinks = new JPanel();
				GridBagLayout jPanelSecundaryLinksLayout = new GridBagLayout();
				jPanelUpperPanel.add(jPanelSecundaryLinks);
				jPanelSecundaryLinksLayout.rowWeights = new double[] {1, 0.0};
				jPanelSecundaryLinksLayout.rowHeights = new int[] {7, 7};
				jPanelSecundaryLinksLayout.columnWeights = new double[] {0.0, 1};
				jPanelSecundaryLinksLayout.columnWidths = new int[] {7, 7};
				jPanelSecundaryLinks.setLayout(jPanelSecundaryLinksLayout);
				jPanelSecundaryLinks.setBorder(BorderFactory.createTitledBorder("Secondary Links"));
				jPanelSecundaryLinks.setOpaque(false);
				{

					jPanelSecundaryLinks.add(getJButtonAddSecondaryLinks(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

				}
				{
					jScrollPaneSecondaryLinks = new JScrollPane();
					jPanelSecundaryLinks.add(jScrollPaneSecondaryLinks, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					{
						jScrollPaneSecondaryLinks.setViewportView(getJListSecondaryMenuItems());
					}
				}
				{
					jPanelSecundaryLinks.add(getJButtonRemoveSecondaryLinks(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
				}
			}
			{
				jPanelMenuLinkSelectedDetails = new JPanel();
				GridLayout jPanelMenuLinkSelectedDetailsLayout = new GridLayout(2, 2);
				jPanelMenuLinkSelectedDetailsLayout.setColumns(2);
				jPanelMenuLinkSelectedDetailsLayout.setHgap(5);
				jPanelMenuLinkSelectedDetailsLayout.setVgap(5);
				jPanelMenuLinkSelectedDetailsLayout.setRows(2);
				jPanelUpperPanel.add(jPanelMenuLinkSelectedDetails);
				jPanelMenuLinkSelectedDetails.setBorder(BorderFactory.createTitledBorder("Link Info and Updates"));
				{
					jPanelLinkgeneralInfo = new JPanel();
					GridBagLayout jPanelLinkgeneralInfoLayout = new GridBagLayout();
					jPanelMenuLinkSelectedDetails.add(jPanelLinkgeneralInfo);
					jPanelLinkgeneralInfoLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
					jPanelLinkgeneralInfoLayout.rowHeights = new int[] {7, 7, 7, 7};
					jPanelLinkgeneralInfoLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.0};
					jPanelLinkgeneralInfoLayout.columnWidths = new int[] {7, 7, 7, 7};
					jPanelLinkgeneralInfo.setLayout(jPanelLinkgeneralInfoLayout);
					{
						jLabelURL = new JLabel();
						jPanelLinkgeneralInfo.add(jLabelURL, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
						jLabelURL.setText("URL :");
					}
					{
						jLabelIcon = new JLabel();
						jPanelLinkgeneralInfo.add(jLabelIcon, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
						jLabelIcon.setText("Icon :");
					}
					{
						jLabelmenuName = new JLabel();
						jPanelLinkgeneralInfo.add(jLabelmenuName, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
						jLabelmenuName.setText("Name :");
					}
					{
						jLabelID = new JLabel();
						jPanelLinkgeneralInfo.add(jLabelID, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
						jLabelID.setText("ID :");
					}
					{
						jTextFieldID = new JTextField();
						jPanelLinkgeneralInfo.add(jTextFieldID, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jTextFieldID.setEditable(false);
					}
					{
						jTextFieldName = new JTextField();
						jPanelLinkgeneralInfo.add(jTextFieldName, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					}
					{
						jButtonIconShow = new JButton();
						jPanelLinkgeneralInfo.add(jButtonIconShow, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jButtonIconShow.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent arg0) {
								changeImageGUI();
							}
						});
					}
					{
						
						
						jTextAreaURL = new JTextArea();
						jTextAreaURL.setAutoscrolls(true);
						jTextAreaURL.setLineWrap(true);
						jTextAreaURL.setWrapStyleWord(true);
						jPanelLinkgeneralInfo.add(jTextAreaURL, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					}
					{
						jButtonUpdateName = new JButton();
						jPanelLinkgeneralInfo.add(jButtonUpdateName, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
						jButtonUpdateName.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Refresh.png")));
						jButtonUpdateName.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								try {
									jButtonUpdateActionPerformed(evt);
								} catch (SQLException e) {
									TreatExceptionForAIbench.treatExcepion(e);
								} catch (InterruptedException e) {
									TreatExceptionForAIbench.treatExcepion(e);
								} catch (IOException e) {
									TreatExceptionForAIbench.treatExcepion(e);
								} catch (DatabaseLoadDriverException e) {
									TreatExceptionForAIbench.treatExcepion(e);
								}
							}
						});
					}
				}
				jPanelMenuLinkSelectedDetails.setLayout(jPanelMenuLinkSelectedDetailsLayout);
				{
					jPanelSourceLinkage = new JPanel();
					GridBagLayout jPanelSourceLinkageLayout = new GridBagLayout();
					jPanelMenuLinkSelectedDetails.add(jPanelSourceLinkage);
					jPanelSourceLinkageLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.0};
					jPanelSourceLinkageLayout.rowHeights = new int[] {7, 7, 7, 7};
					jPanelSourceLinkageLayout.columnWeights = new double[] {0.0, 0.1};
					jPanelSourceLinkageLayout.columnWidths = new int[] {7, 20};
					jPanelSourceLinkage.setLayout(jPanelSourceLinkageLayout);
					jPanelSourceLinkage.setBorder(BorderFactory.createTitledBorder(null, "Source Linkage", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
					{
						jButtonAddSourceLinkage = new JButton();
						jPanelSourceLinkage.add(jButtonAddSourceLinkage, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jButtonAddSourceLinkage.setText("+");
						jButtonAddSourceLinkage.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jButtonAddSourceLinkageActionPerformed(evt);
							}
						});
					}
					{
						jButtonRemoveSourceLinkage = new JButton();
						jPanelSourceLinkage.add(jButtonRemoveSourceLinkage, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
						jButtonRemoveSourceLinkage.setText("-");
						jButtonRemoveSourceLinkage.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								try {
									jButtonRemoveSourceLinkageActionPerformed(evt);
								} catch (SQLException e) {
									TreatExceptionForAIbench.treatExcepion(e);
								} catch (DatabaseLoadDriverException e) {
									TreatExceptionForAIbench.treatExcepion(e);
								}
							}
						});
					}
					{
						jScrollPaneSourceMenuLinkage = new JScrollPane();
						jPanelSourceLinkage.add(jScrollPaneSourceMenuLinkage, new GridBagConstraints(0, 0, 2, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						{							 
							jScrollPaneSourceMenuLinkage.setViewportView(getJListLinkageSourceMenuItems());
						}
					}
				}
				{
					
				}
			}
		}
	}
	
	protected void changeImageGUI() {
		int rel = filePath.showOpenDialog(new JFrame());
		if(rel == filePath.CANCEL_OPTION)
		{

		}
		else
		{
			File save_path = filePath.getSelectedFile();
			if(save_path.isFile())
			{
				ImageIcon icon = new ImageIcon(save_path.getAbsolutePath());
				jButtonIconShow.setIcon(icon);
			}
			else
			{
				Workbench.getInstance().warn("Please Select a image File");
			}
		}
	}

	private JButton getJButtonRemovePrimaryLinks() {
		if(jButtonRemovePrimaryLinks==null)
		{
			jButtonRemovePrimaryLinks = new JButton();
			jButtonRemovePrimaryLinks.setText("-");
			jButtonRemovePrimaryLinks.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						jButtonRemovePrimaryLinkActionPerformed(evt);
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
		}
		return jButtonRemovePrimaryLinks;
	}
	
	protected void jButtonRemovePrimaryLinkActionPerformed(ActionEvent evt) throws SQLException, DatabaseLoadDriverException {
		MenuItem menu = (MenuItem) jListPrimaryLinks.getSelectedValue();
		if( menu!=null)
		{
			jListSecondaryLinks.setModel(new DefaultComboBoxModel());
			for(MenuItem subitem:menu.getSubitems())
			{
				HelpDatabase.removeMenuLink(subitem);
			}
			HelpDatabase.removeMenuLink(menu);
			((DefaultComboBoxModel) jListPrimaryLinks.getModel()).removeElement(menu);
			menuIDMenuItem.remove(menu);
			primaryMenuItems.remove(menu);
			cleanInfoPanel();
			jCheckBoxWarningMessages.setSelected(true);
		}
	}
	
	private JButton getJButtonAddPrimaryLinks() {
		if(jButtonAddPrimaryLinks==null)
		{
			jButtonAddPrimaryLinks = new JButton();
			jButtonAddPrimaryLinks.setText("+");
			jButtonAddPrimaryLinks.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						jButtonAddPrimaryLinkActionPerformed(evt);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
		}
		return jButtonAddPrimaryLinks;
	}
	
	protected void jButtonAddPrimaryLinkActionPerformed(ActionEvent evt) throws DatabaseLoadDriverException, SQLException {
			int nextID = HelpDatabase.getNextInsertTableID(GlobalTablesName.menulinks);
			MenuItem newItem = new MenuItem(nextID, "New Menu", new ImageIcon(), "", new ArrayList<MenuItem>(), 1, new HashSet<Integer>());
			HelpDatabase.addMenuLink(newItem);
			menuIDMenuItem.put(nextID, newItem);
			primaryMenuItems.add(newItem);
			((DefaultComboBoxModel) jListPrimaryLinks.getModel()).addElement(newItem);
			jCheckBoxWarningMessages.setSelected(true);
	}
	
	private JButton getJButtonRemoveSecondaryLinks() {
		if(jButtonRemoveSecondaryLinks==null)
		{
			jButtonRemoveSecondaryLinks = new JButton();
			jButtonRemoveSecondaryLinks.setText("-");
			jButtonRemoveSecondaryLinks.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						jButtonRemoveSecondaryLinkActionPerformed(evt);
					} catch (SQLException e) {					
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
		}
		return jButtonRemoveSecondaryLinks;
	}
	
	protected void jButtonRemoveSecondaryLinkActionPerformed(ActionEvent evt) throws SQLException, DatabaseLoadDriverException {
		MenuItem menu = (MenuItem) jListPrimaryLinks.getSelectedValue();
		MenuItem menuItem = (MenuItem) jListSecondaryLinks.getSelectedValue();
		if(menuItem!=null && menu!=null)
		{
				HelpDatabase.removeMenuLink(menuItem);
				((DefaultComboBoxModel) jListSecondaryLinks.getModel()).removeElement(menuItem);
				menu.getSubitems().remove(menuItem);
				menuIDMenuItem.remove(menuItem);
				cleanInfoPanel();
				jCheckBoxWarningMessages.setSelected(true);
		}
	}

	private void cleanInfoPanel() {
		jTextFieldID.setText("");
		jTextAreaURL.setText("");
		jTextFieldName.setText("");
		jButtonIconShow.setIcon(new ImageIcon());
		jListSourceLingage.setModel(new DefaultComboBoxModel());
	}

	private JButton getJButtonAddSecondaryLinks() {
		if(jButtonaDDSecondaryLinks==null)
		{
			jButtonaDDSecondaryLinks = new JButton();
			jButtonaDDSecondaryLinks.setText("+");
			jButtonaDDSecondaryLinks.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						jButtonAddSecondaryLinkActionPerformed(evt);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
		}
		return jButtonaDDSecondaryLinks;
	}

	protected void jButtonAddSecondaryLinkActionPerformed(ActionEvent evt) throws DatabaseLoadDriverException, SQLException {
		MenuItem menuItem = (MenuItem) jListPrimaryLinks.getSelectedValue();
		if(menuItem!=null)
		{
				int nextID = HelpDatabase.getNextInsertTableID(GlobalTablesName.menulinks);
				MenuItem newItem = new MenuItem(nextID, "New Secondary Menu", new ImageIcon(), "", new ArrayList<MenuItem>(), 2, new HashSet<Integer>());
				HelpDatabase.addMenuLink(newItem);
				HelpDatabase.addMenuSubMenuLinkage(menuItem,newItem);
				menuItem.getSubitems().add(newItem);
				menuIDMenuItem.put(nextID, newItem);
				((DefaultComboBoxModel) jListSecondaryLinks.getModel()).addElement(newItem);
				jCheckBoxWarningMessages.setSelected(true);
			
		}
	}

	protected void jButtonAddSourceLinkageActionPerformed(ActionEvent evt) {
		if(jTextFieldID.getText()!=null && jTextFieldID.getText().length()>0)
		{
			int id = Integer.valueOf(jTextFieldID.getText());
			MenuItem item = menuIDMenuItem.get(id);	
			new SelectSourceGUI(sourceIDSource,item,jListSourceLingage);
			jCheckBoxWarningMessages.setSelected(true);
		}
	}

	protected void jButtonRemoveSourceLinkageActionPerformed(ActionEvent evt) throws SQLException, DatabaseLoadDriverException {
		if(jTextFieldID.getText()!=null && jTextFieldID.getText().length()>0)
		{
			Object[] objects = jListSourceLingage.getSelectedValues();
			List<Source> sources = new ArrayList<Source>();
			for(Object source:objects)
			{
				sources.add((Source) source);
			}
			if(sources!=null && sources.size()>0)
			{
				int id = Integer.valueOf(jTextFieldID.getText());
				MenuItem item = menuIDMenuItem.get(id);				
				HelpDatabase.removeMenuItemSourceLinkage(item,sources);
				for(Source source:sources)
				{
					item.getSourcesLinkage().remove(source.getSourceID());
					((DefaultComboBoxModel) jListSourceLingage.getModel()).removeElement(source);
				}
				jCheckBoxWarningMessages.setSelected(true);
			}
		}
	}

	private JList getJListLinkageSourceMenuItems() {
		if(jListSourceLingage==null)
		{
			jListSourceLingage = new JList();
		}
		return jListSourceLingage;
	}

	


	private JList getJListPrimaryMenuItems() {
		if(jListPrimaryLinks==null)
		{
			jListPrimaryLinks = new JList();
			ListModel jListPrimaryLinksModel = getListModel();
			jListPrimaryLinks.setModel(jListPrimaryLinksModel);
			jListPrimaryLinks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jListPrimaryLinks.addMouseListener(new MouseListener() {
				
				public void mouseReleased(MouseEvent arg0) {}
				public void mousePressed(MouseEvent arg0) { updatePrimaryMenuSelection();}
				public void mouseExited(MouseEvent arg0) {}
				public void mouseEntered(MouseEvent arg0) {}
				public void mouseClicked(MouseEvent arg0) {}
			});
			jListPrimaryLinks.addKeyListener(new KeyListener() {
				public void keyTyped(KeyEvent arg0) {}
				public void keyReleased(KeyEvent arg0) {}
				public void keyPressed(KeyEvent arg0) {updatePrimaryMenuSelection();}
			});
		}
		return jListPrimaryLinks;
	}

	private JList getJListSecondaryMenuItems() {
		if(jListSecondaryLinks==null)
		{
			jListSecondaryLinks = new JList();
			jListSecondaryLinks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jListSecondaryLinks.addMouseListener(new MouseListener() {
				
				public void mouseReleased(MouseEvent arg0) {}
				public void mousePressed(MouseEvent arg0) { updateSecondaryMenuSelection();}
				public void mouseExited(MouseEvent arg0) {}
				public void mouseEntered(MouseEvent arg0) {}
				public void mouseClicked(MouseEvent arg0) {}
			});
			jListSecondaryLinks.addKeyListener(new KeyListener() {
				public void keyTyped(KeyEvent arg0) {}
				public void keyReleased(KeyEvent arg0) {}
				public void keyPressed(KeyEvent arg0) {updateSecondaryMenuSelection();}
			});
		}
		return jListSecondaryLinks;
	}

	protected void updateSecondaryMenuSelection() {
		MenuItem item = (MenuItem) jListSecondaryLinks.getSelectedValue();
		if(item!=null)
		{
			updateInfo(item);
			sourceLinkageUpdate(item);
		}
	}



	private void updateInfo(MenuItem item) {
		if(item.getId()!=0)
			jTextFieldID.setText(String.valueOf(item.getId()));
		else
			jTextFieldID.setText("");
		if(item.getName()!=null)
			jTextFieldName.setText(item.getName());
		else
			jTextFieldName.setText("");
		if(item.getUrl()!=null)
			jTextAreaURL.setText(item.getUrl());
		else
			jTextAreaURL.setText("");
		if(item.getIcon()!=null)
			jButtonIconShow.setIcon(item.getIcon());
		else
			jButtonIconShow.setIcon(null);
	}



	protected void updatePrimaryMenuSelection() {
		MenuItem menuItem = (MenuItem) jListPrimaryLinks.getSelectedValue();
		if(menuItem!=null)
		{
			List<MenuItem> subItems = menuItem.getSubitems();	
			DefaultComboBoxModel model = new DefaultComboBoxModel();
			for(MenuItem item:subItems)
			{
				model.addElement(item);
			}
			jListSecondaryLinks.setModel(model);
			updateInfo(menuItem);
			sourceLinkageUpdate(menuItem);
		}
	}



	private void sourceLinkageUpdate(MenuItem menuItem) {
		Set<Integer> sources = menuItem.getSourcesLinkage();
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for(Integer id:sources)
		{
			model.addElement(sourceIDSource.get(id));
		}
		jListSourceLingage.setModel(model);
	}

	private ListModel getListModel() {
		DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
		for(MenuItem menuItem:primaryMenuItems)
		{
			comboModel.addElement(menuItem);
		}
		return comboModel;
	}



	private void getMenuItems() throws SQLException, DatabaseLoadDriverException {
			List<MenuItem> meuItems = ExternalLinksMenu.getMenuLinksDatabase();
			primaryMenuItems = meuItems;
			menuIDMenuItem = new HashMap<Integer, MenuItem>();
			for(MenuItem menuItem:meuItems)
			{
				menuIDMenuItem.put(menuItem.getId(), menuItem);
				for(MenuItem menuItem2:menuItem.subitems)
				{
					menuIDMenuItem.put(menuItem2.getId(), menuItem2);
				}
			}
	}
	

	


	private void jButtonUpdateActionPerformed(ActionEvent evt) throws SQLException, InterruptedException, IOException, DatabaseLoadDriverException {
		if(jTextFieldID.getText()!=null && jTextFieldID.getText().length()>0)
		{
			if(jTextFieldName.getText()!=null && jTextFieldName.getText().length()>0)
			{
				int id = Integer.valueOf(jTextFieldID.getText());
				String name = jTextFieldName.getText();
				String url = jTextAreaURL.getText();
				MenuItem item = menuIDMenuItem.get(id);
				item.setUrl(url);
				item.setName(name);
				ImageIcon icon = (ImageIcon) jButtonIconShow.getIcon();
				if(icon == null || icon.getImage() == null)
					icon = null;
				item.setIcon(icon);
				jCheckBoxWarningMessages.setSelected(true);
				HelpDatabase.updateMenuItem(item);
			}
			else
			{
				Workbench.getInstance().warn("Please select a Menu Link-out Name");
			}
		}
		else
		{
			Workbench.getInstance().warn("Please select a Menu Link-out");
		}
	}
	
	
	protected void okButtonAction() {
		if(jCheckBoxWarningMessages.isSelected())
		{
			String urlFalt = testURLNullORLink();
			String sourceFault = testSourcesLinkOut();
			if(urlFalt.length() > 0 && sourceFault.length() > 0)
			{
				Workbench.getInstance().warn(urlFalt + sourceFault);
			}
			else if(urlFalt.length() > 0)
			{
				Workbench.getInstance().warn(urlFalt);
			}
			else if(sourceFault.length() > 0)
			{
				Workbench.getInstance().warn(sourceFault);
			}
			else
			{
				closeView();
			}
			jCheckBoxWarningMessages.setSelected(false);
		}
		else
		{
			closeView();
		}	
	}

	private void closeView() {
		this.setModal(false);
		this.setVisible(false);
		this.dispose();
	}
	
	public String testSourcesLinkOut()
	{
		String result = new String();
		for(MenuItem item : primaryMenuItems)
		{
			if(item.getSourcesLinkage().size() == 0)
			{
				result = result + item.getName()+ " does not have sources associated"+"\n";
			}
			if(item.haveSubMenuItems())
			{
				for(MenuItem subItem : item.getSubitems())
				{
					if(subItem.getUrl()== null || subItem.getUrl().equals(""))
					{
						result = result + "Subitem "+subItem.getName()+ " from "+item.getName()+ " does not have sources associated"+"\n";
					}
				}
			}
		}
		return result;
	}
	
	public String testURLNullORLink()
	{
		String result = new String();
		for(MenuItem item : primaryMenuItems)
		{
			if(item.getUrl()== null || item.getUrl().equals(""))
			{

				result = result + item.getName()+ " does not have link-out URL"+"\n";
			}
			else if(!item.getUrl().contains("#"))
			{
				result = result + item.getName()+ " URL does not contain a # character. Please press the Info button for more details."+"\n";
			}
			if(item.haveSubMenuItems())
			{
				for(MenuItem subItem : item.getSubitems())
				{
					if(subItem.getUrl()== null || subItem.getUrl().equals(""))
					{
						result = result + "Subitem "+subItem.getName()+ " from "+item.getName()+ " does not have link-out URL"+"\n";

					}
					else if(!subItem.getUrl().contains("#"))
					{
						result = result + "Subitem "+subItem.getName()+ " from "+item.getName()+" URL does not contain a # character. Please press the Info button for more details."+"\n";
					}
				}
			}
		}
		return result;
	}

	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);
		Utilities.centerOnOwner(this);
		this.setVisible(true);
		
	}

	@Override
	public void onValidationError(Throwable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Linkout_Management";
	}
	
	private JPanel getJPanelCheckBox() {
		if(jPanelCheckBox == null) {
			jPanelCheckBox = new JPanel();
			GridBagLayout jPanelCheckBoxLayout = new GridBagLayout();
			jPanelCheckBoxLayout.rowWeights = new double[] {0.1};
			jPanelCheckBoxLayout.rowHeights = new int[] {7};
			jPanelCheckBoxLayout.columnWeights = new double[] {0.1};
			jPanelCheckBoxLayout.columnWidths = new int[] {7};
			jPanelCheckBox.setLayout(jPanelCheckBoxLayout);
			jPanelCheckBox.add(getJCheckBoxWarningMessages(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		}
		return jPanelCheckBox;
	}
	
	private JCheckBox getJCheckBoxWarningMessages() {
		if(jCheckBoxWarningMessages == null) {
			jCheckBoxWarningMessages = new JCheckBox();
			jCheckBoxWarningMessages.setText("Enable warning messages");
			jCheckBoxWarningMessages.setSelected(true);
		}
		return jCheckBoxWarningMessages;
	}

}
