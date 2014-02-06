package pt.uminho.anote2.aibench.publicationmanager.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.core.document.IPublication;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
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
public class AddFileView extends JDialog{
	
	private static final long serialVersionUID = 1L;
	private JTextField jTextFieldHost;
	private FileChooserExtensionPdf jFileChooserAddFile;
	private JPanel jPanelFileChoser;
	private JLabel jLabel1;

	private IPublication pub;
	private QueryInformationRetrievalExtension query;

	public AddFileView(IPublication pub,QueryInformationRetrievalExtension query) {
		super(Workbench.getInstance().getMainFrame());
		this.pub=pub;
		this.query=query;
		initGUI();
		Utilities.centerOnOwner(this);
		this.setSize(500, 400);
		this.setModal(true);
		this.setVisible(true);
	}


	private void initGUI() {
		
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {12, 7, 20, 7};
			thisLayout.columnWeights = new double[] {0.0, 0.0};
			thisLayout.columnWidths = new int[] {147, 149};
			getContentPane().setLayout(thisLayout);
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel1.setText("File Identification :");
			}
			{
				jTextFieldHost = new JTextField();
				getContentPane().add(jTextFieldHost, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jTextFieldHost.setText(this.pub.getOtherID());
				jTextFieldHost.setEditable(false);
			}
			{
				jPanelFileChoser = new JPanel();
				getContentPane().add(jPanelFileChoser, new GridBagConstraints(0, 2, 2, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					jFileChooserAddFile = new FileChooserExtensionPdf(rootPaneCheckingEnabled);
					jPanelFileChoser.add(jFileChooserAddFile, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
			}
		}
	}

	protected void okButtonAction() {
		
		File file = jFileChooserAddFile.getSelectedFile();
		if(jFileChooserAddFile.accept(file))
		{
			ParamSpec[] paramsSpec = new ParamSpec[]{ 
					new ParamSpec("Publication",IPublication.class,this.pub, null),
					new ParamSpec("Query",QueryInformationRetrievalExtension.class,this.query, null),
					new ParamSpec("File",File.class,file, null)
			};
			
			for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
				if (def.getID().equals("operations.addfile")){			
					Workbench.getInstance().executeOperation(def, paramsSpec);
					this.setVisible(false);
					this.dispose();
					this.setModal(false);
					return;
				}
			}

		}
		else
		{
			Workbench.getInstance().warn("File extension incorrect...\n Please Select a pdf File");
		}
		

	}
	
	

}
