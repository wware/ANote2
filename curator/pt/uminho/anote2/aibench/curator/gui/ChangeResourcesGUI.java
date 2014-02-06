package pt.uminho.anote2.aibench.curator.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERDocumentAnnotation;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class ChangeResourcesGUI extends DialogGenericView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList jListAvailableLookuptables;
	private JButton jButtonChangeLookuptable;
	private JButton jButtonNewLookuptable;
	private JList jList1Lookuptable;
	private JPanel jPanelSelectedDic;
	private JPanel jPanelAvailableDics;
	private NERDocumentAnnotation doc;
	private IResource<IResourceElement> resource;
	private boolean isResourceChange = false;


	public ChangeResourcesGUI(NERDocumentAnnotation doc,IResource<IResourceElement> resource)
	{
		this.doc=doc;
		this.resource=resource;
		this.setSize(600,400);
		try {
			initGUI();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setTitle("Change Resource");
		Utilities.centerOnOwner(this);
		this.setVisible(true);
		this.setModal(true);
	}

	private void initGUI() throws SQLException {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.025, 0.1, 0.0};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7, 20};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			getContentPane().setLayout(thisLayout);
			{
				jPanelAvailableDics = new JPanel();
				jPanelAvailableDics.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Available Dictionaries", TitledBorder.LEADING, TitledBorder.TOP));

				GridBagLayout jPanelAvailableDicsLayout = new GridBagLayout();
				getContentPane().add(jPanelAvailableDics, new GridBagConstraints(0, 0, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelAvailableDicsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelAvailableDicsLayout.rowHeights = new int[] {7, 7, 7, 7};
				jPanelAvailableDicsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelAvailableDicsLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelAvailableDics.setLayout(jPanelAvailableDicsLayout);
				{
					ListModel jListAvailableDicsModel = getLookupTables();
					jListAvailableLookuptables = new JList();
					jPanelAvailableDics.add(jListAvailableLookuptables, new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jListAvailableLookuptables.setModel(jListAvailableDicsModel);
				}
			}
			{
				jPanelSelectedDic = new JPanel();
				jPanelSelectedDic.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Selected Dictionary", TitledBorder.LEADING, TitledBorder.TOP));
				GridBagLayout jPanelSelectedDicLayout = new GridBagLayout();
				getContentPane().add(jPanelSelectedDic, new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				getContentPane().add(getJButtonNewLookuptable(), new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				getContentPane().add(getJButtonChangeDic(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 4, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelSelectedDicLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelSelectedDicLayout.rowHeights = new int[] {7, 7, 7, 7};
				jPanelSelectedDicLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelSelectedDicLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelSelectedDic.setLayout(jPanelSelectedDicLayout);
				{
					ListModel jList1DicModel = initResource();
					jList1Lookuptable = new JList();
					jPanelSelectedDic.add(jList1Lookuptable, new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jList1Lookuptable.setModel(jList1DicModel);
				}
			}
		}

	}
	


	private DefaultComboBoxModel getLookupTables() throws SQLException{

		DefaultComboBoxModel model = new DefaultComboBoxModel();
		IDatabase db = ((Corpus) doc.getCorpus()).getCorpora().getDb();
		Statement statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(QueriesResources.selectAllresources);
		int id;
		String type,name,note;
		while(rs.next())
		{
			id = rs.getInt(1);
			type = rs.getString(2);
			name = rs.getString(3);
			note = rs.getString(4);
			if(type.equals("lookuptable"))
			{
				model.addElement(new Dictionary(db, id, name, note));
			}
		}
		rs.close();
		return model;
	}
	
	@SuppressWarnings("unchecked")
	private void changeResourceSelected()
	{
		resource = (IResource<IResourceElement>) jListAvailableLookuptables.getSelectedValue();
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement(resource);
		jList1Lookuptable.setModel(model);
		jList1Lookuptable.updateUI();
		isResourceChange=true;
	}
	
	private ListModel initResource() {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		if(resource==null)
		{
			
		}
		else
		{
			model.addElement(resource);
		}
		return model;
	}
	
	private void changeDicResource() throws SQLException
	{
		jListAvailableLookuptables.setModel(getLookupTables());
		jListAvailableLookuptables.updateUI();
	}

	private JButton getJButtonNewLookuptable() {
		if(jButtonNewLookuptable == null) {
			jButtonNewLookuptable = new JButton();
			jButtonNewLookuptable.setText("New LookupTable");
			jButtonNewLookuptable.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					new CreateLookuptable(doc).addWindowListener(new WindowListener(){

						public void windowActivated(WindowEvent arg0) {}
						
						public void windowClosing(WindowEvent arg0) {}
						
						public void windowDeactivated(WindowEvent arg0) {}
						
						public void windowDeiconified(WindowEvent arg0) {}
						
						public void windowIconified(WindowEvent arg0) {}
						
						public void windowOpened(WindowEvent arg0) {}
						
						public void windowClosed(WindowEvent arg0) {
							try {
								changeDicResource();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						
					});
				}
			});
		}
		return jButtonNewLookuptable;
	}
	
	private JButton getJButtonChangeDic() {
		if(jButtonChangeLookuptable == null) {
			jButtonChangeLookuptable = new JButton();
			jButtonChangeLookuptable.setText("Change");
			jButtonChangeLookuptable.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					changeResourceSelected();
				}
			});
		}
		return jButtonChangeLookuptable;
	}
	
	public IResource<IResourceElement> getResource() {
		return resource;
	}

	public boolean isResourceChange() {
		return isResourceChange;
	}

	protected void okButtonAction() {
		this.setVisible(false);
		this.setModal(false);
		this.dispose();
	}
}
