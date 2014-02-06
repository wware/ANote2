package pt.uminho.anote2.aibench.curator.menu.externallinks;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;

import pt.uminho.anote2.aibench.corpus.datatypes.NERDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.datatypes.NERSchema;
import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.curator.datastructures.document.CuratorDocument;
import pt.uminho.anote2.aibench.curator.settings.CuratorDefaultSettings;
import pt.uminho.anote2.aibench.curator.view.panes.PanelTextEditor;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.core.annotation.IAnnotation;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.ExternalID;
import pt.uminho.anote2.datastructures.database.queries.corpora.QueriesExternalLinks;
import pt.uminho.anote2.datastructures.utils.MathUtils;
import pt.uminho.anote2.datastructures.utils.MenuItem;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import es.uvigo.ei.aibench.workbench.Workbench;

public class ExternalLinksMenu extends JMenu{

	private static final long serialVersionUID = 5348597845929126655L;
	
	private JEditorPane editPanel;
	private CuratorDocument curatorDocument;
	private Map<JMenuItem,Set<Integer>> menuItem;


	public ExternalLinksMenu(JEditorPane editPanel,CuratorDocument curatorDocument) throws SQLException, DatabaseLoadDriverException{	
		super(GlobalNames.menuLinkOut);
		this.curatorDocument = curatorDocument;
		this.editPanel = editPanel;	
		this.menuItem = new HashMap<JMenuItem, Set<Integer>>();
		createMEnu();
		onlytermexternalDatabases();
	}



	private void onlytermexternalDatabases() {
		if(Boolean.valueOf(PropertiesManager.getPManager().getProperty(CuratorDefaultSettings.ONLY_TERM_EXTERNALIDS_AVAILABLE).toString()))
		{
			this.addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseMoved( MouseEvent e) {
					try {

						JMenuItem comp = (JMenuItem) getComponentAt(e.getX(), e.getY());
						if(comp.getText().equals(GlobalNames.menuLinkOut))
						{
							String t = editPanel.getSelectedText();
							if(t!=null)
							{
								int start = editPanel.getSelectionStart() - PanelTextEditor.error;
								int end = editPanel.getSelectionEnd() 	- PanelTextEditor.error;
								AnnotationPosition position = new AnnotationPosition(start,end);
								t = t.replace(" ", "_");
								if(curatorDocument.getNERAnnotations().containsKey(position))
								{
									IAnnotation annot = curatorDocument.getNERAnnotations().get(position);
									if(annot instanceof IEntityAnnotation)
									{
										for(JMenuItem  item:menuItem.keySet())
										{
											item.setVisible(false);
										}
										IEntityAnnotation entity = (IEntityAnnotation) annot;
										List<IExternalID> externalIds = new ArrayList<IExternalID>();;
										try {
											externalIds = getTermsID(entity);
										} catch (SQLException e1) {
											TreatExceptionForAIbench.treatExcepion(e1);
										} catch (DatabaseLoadDriverException e1) {
											TreatExceptionForAIbench.treatExcepion(e1);
										}
										Set<Integer> sourcesID = new HashSet<Integer>();
										for(IExternalID extID:externalIds)
										{
											sourcesID.add(extID.getSourceID());
										}
										if(!sourcesID.isEmpty())
										{
											int numberOfNodesVisibal = 0;
											for(JMenuItem  item2:menuItem.keySet())
											{
												Set<Integer> set = menuItem.get(item2);
												if(MathUtils.interceptedSet(sourcesID, set))
												{
													item2.setVisible(true);
													numberOfNodesVisibal ++;
												}
											}
											if(numberOfNodesVisibal != 0)
											{
												for(JMenuItem  menu3: menuItem.keySet())
												{
													if(!menu3.isVisible())
													{
														MenuElement[] elems = menu3.getSubElements();
														for(int i=0;i<elems.length;i++)
														{
															if(((JMenuItem) elems[i]).isVisible())
															{
																menu3.setVisible(true);
																break;
															}
														}
													}
												}
											}

										}
									}
								}			
								else
								{
									for(JMenuItem  item:menuItem.keySet())
									{
										item.setVisible(true);
									}
								}
							}
						}
					} catch (SQLException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					} catch (DatabaseLoadDriverException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					}	
				}
			});
		}
	}



	private void createMEnu() throws SQLException, DatabaseLoadDriverException {
		this.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/internet.png")));
		List<MenuItem> items = getMenuLinksDatabase();
		for(MenuItem item :items)
		{
			if(!item.haveSubMenuItems())
			{				
				if(item.getUrl()!=null && item.getUrl().length() > 6)
				{
					JMenuItem menuItem = putMenuItem(item);
					this.add(menuItem);
					this.menuItem.put(menuItem, item.getSourcesLinkage());
				}
			}
			else
			{
				JMenu menu = putMenu(item);
				this.add(menu);
				this.menuItem.put(menu, item.getSourcesLinkage());
			}
		}
	}



	private JMenuItem putMenuItem(final MenuItem item) {
		JMenuItem menuitem;
		if(item.getIcon()!=null)
		{
			menuitem = new JMenuItem();
			menuitem.setIcon(item.getIcon());
			menuitem.setToolTipText(item.getName());
		}
		else
		{
			menuitem = new JMenuItem(item.getName());
			menuitem.setToolTipText(item.getName());
		}	
		if(item.getUrl()!=null && item.getUrl().length()> 6)
		{
			menuitem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					try {
						urlready1(item);
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (IOException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
		}
		else
		{
			menuitem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					Workbench.getInstance().warn("This menu item do not have URL");
				}
			});
		}
		return menuitem;
	}
	
	private JMenu putMenu(final MenuItem item) {
		JMenu menu;
		if(item.getIcon()!=null)
		{
			menu = new JMenu();
			menu.setIcon(item.getIcon());
			menu.setToolTipText(item.getName());
		}
		else
		{
			menu = new JMenu(item.getName());
			menu.setToolTipText(item.getName());
		}	
		if(item.getUrl()!=null && item.getUrl().length()>0)
		{
			menu.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					try {
						urlready1(item);
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (IOException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}

				}
			});
		}
		else
		{
			menu.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					Workbench.getInstance().warn("This menu item do not have URL");
				}
			});
		}
		for(MenuItem subMenu:item.getSubitems())
		{
			if(subMenu.getUrl()!=null && subMenu.getUrl().length() > 6)
			{
				JMenuItem subItem = putMenuItem(subMenu);
				menu.add(subItem);
				menuItem.put(menu,item.getSourcesLinkage());
			}
		}	
		return menu;
	}


		
		
	protected void urlready1(final MenuItem item) throws SQLException, DatabaseLoadDriverException, IOException {
		String t = editPanel.getSelectedText();
		if(t==null)
			Workbench.getInstance().warn("No text selected!");
		else
		{
			int total = 0;
			int sourceID;
			String externalID;
			int start = editPanel.getSelectionStart() - PanelTextEditor.error;
			int end = editPanel.getSelectionEnd() 	- PanelTextEditor.error;
			AnnotationPosition position = new AnnotationPosition(start,end);
			t = t.replace(" ", "_");
			if(curatorDocument.getNERAnnotations().containsKey(position))
			{
				IAnnotation annot = curatorDocument.getNERAnnotations().get(position);
				if(annot instanceof IEntityAnnotation)
				{
					IEntityAnnotation entity = (IEntityAnnotation) annot;
					List<IExternalID> externalIds = getTermsID(entity);
					for(IExternalID extID:externalIds)
					{
						sourceID = extID.getSourceID();
						if(item.getSourcesLinkage().contains(sourceID))
						{
							externalID = extID.getExternalID();
							externalID = externalID.replace(" ", "_");
							openURL(item.getUrl().replace("#", externalID));
							total ++;
						}
					}
				}
			}			
			if(total>0 && !Boolean.valueOf(PropertiesManager.getPManager().getProperty(CuratorDefaultSettings.USING_TERM_NAME_LINKOUT_SEARCH).toString()))
			{
				
			}
			else
			{// Open Free text
				openURL(item.getUrl().replace("#", t));
			}
		}
	}

	private List<IExternalID> getTermsID(IEntityAnnotation entity) throws SQLException, DatabaseLoadDriverException {
		List<IExternalID> externalLinks = new ArrayList<IExternalID>();
		int processID = -1;
		NERDocumentAnnotation nerDoc = this.curatorDocument.getAnnotatedDocument();
		if(nerDoc.getProcess() instanceof NERSchema)
		{
			processID = ((NERSchema) nerDoc.getProcess()).getCorpus().getID();
		}
		else if(nerDoc.getProcess() instanceof RESchema)
		{
			processID = ((RESchema) nerDoc.getProcess()).getCorpus().getID();
		}
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesExternalLinks.superExternalIDCurator);
		ps.setInt(1,nerDoc.getProcess().getID());
		ps.setInt(2,processID);
		ps.setInt(3,nerDoc.getID());
		ps.setInt(4,(int)entity.getStartOffset());
		ps.setInt(5,(int)entity.getEndOffset());
		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			externalLinks.add(new ExternalID(rs.getString(1),"",rs.getInt(2)));
		}
		rs.close();
		ps.close();
		return externalLinks;
	}
	
	public static List<MenuItem> getMenuLinksDatabase() throws SQLException, DatabaseLoadDriverException
	{
		List<MenuItem> menuLinks = new ArrayList<MenuItem>();
		PreparedStatement getPrimaryMenu = Configuration.getDatabase().getConnection().prepareStatement(QueriesExternalLinks.getprimaryMenu);
		PreparedStatement getPrimaryMenuSubMenus = Configuration.getDatabase().getConnection().prepareStatement(QueriesExternalLinks.getprimaryMenuSubMEnus);
		PreparedStatement getMenuLinkageSources = Configuration.getDatabase().getConnection().prepareStatement(QueriesExternalLinks.getMenuLinkageSources);
		ResultSet menuLinksRS = getPrimaryMenu.executeQuery();
		int id,level;
		String name,url;
		ImageIcon icon;
		MenuItem menuItem;
		while(menuLinksRS.next())
		{
			id = menuLinksRS.getInt(1);
			name = menuLinksRS.getString(2);
			url = menuLinksRS.getString(4);
			Blob blob = menuLinksRS.getBlob(3);
			if(blob!=null)
				icon = new ImageIcon(blob.getBytes(1L, (int) blob.length() ));
			else
				icon = null;
			level = menuLinksRS.getInt(5);
			List<MenuItem> subItems = getSubItems(id,getPrimaryMenuSubMenus,getMenuLinkageSources);
			Set<Integer> sourcesLinkage = getSourceLinkage(id,getMenuLinkageSources);
			menuItem = new MenuItem(id, name, icon, url, subItems, level, sourcesLinkage);
			menuLinks.add(menuItem);
		}
		menuLinksRS.close();
		getPrimaryMenu.close();
		getPrimaryMenuSubMenus.close();
		getMenuLinkageSources.close();
		return menuLinks;
	}
	
	private static List<MenuItem> getSubItems(int idExternalID,PreparedStatement getPrimaryMenuSubMenus, PreparedStatement getMenuLinkageSources) throws SQLException {
		List<MenuItem> ids = new ArrayList<MenuItem>();
		getPrimaryMenuSubMenus.setInt(1,idExternalID);
		ResultSet rs = getPrimaryMenuSubMenus.executeQuery();
		int id,level;
		String name,url;
		ImageIcon icon;
		MenuItem menuItem;
		while(rs.next())
		{	
			id = rs.getInt(1);
			name = rs.getString(2);
			url = rs.getString(4);
			Blob blob = rs.getBlob(3);
			if(blob!=null)
				icon = new ImageIcon(blob.getBytes(1L, (int) blob.length() ));
			else
				icon = null;
			level = rs.getInt(5);
			Set<Integer> sourcesLinkage = getSourceLinkage(id,getMenuLinkageSources);
			menuItem = new MenuItem(id, name, icon, url,  new ArrayList<MenuItem>(), level, sourcesLinkage);
			ids.add(menuItem);
		}
		return ids;
	}



	private static Set<Integer> getSourceLinkage(int id,PreparedStatement getMenuLinkageSources) throws SQLException {
		Set<Integer> sourceIDs = new HashSet<Integer>();
		getMenuLinkageSources.setInt(1,id);
		ResultSet rs = getMenuLinkageSources.executeQuery();
		while(rs.next())
		{
			sourceIDs.add(rs.getInt(1));
		}
		return sourceIDs;
	}



	/**
	 * @param url
	 * @throws IOException 
	 */
	public void openURL(String url) throws IOException{
		Help.internetAccess(url);
	}

	public JEditorPane getEditPanel() {return editPanel;}
	public void setEditPanel(JEditorPane editPanel) {this.editPanel = editPanel;}
}
