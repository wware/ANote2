package pt.uminho.anote2.aibench.utils.gui;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class AboutGUI extends DialogGenericViewOkButtonOnly implements InputGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel jLabelImage;
	private JPanel imagePanel;

	public AboutGUI()
	{
		super("About @Note2 ...");
		initGUI();
		this.setModal(true);
	}
	
	private void initGUI() {
		{
			GridBagLayout jPanelImageLayout = new GridBagLayout();
			jPanelImageLayout.rowWeights = new double[] {0.1};
			jPanelImageLayout.rowHeights = new int[] {7};
			jPanelImageLayout.columnWeights = new double[] {0.1};
			jPanelImageLayout.columnWidths = new int[] {7};
			getContentPane().setLayout(jPanelImageLayout);
			{
				imagePanel = new JPanel();
				imagePanel.setBackground(Color.WHITE);
				GridBagLayout imagePanelLayout = new GridBagLayout();
				getContentPane().add(imagePanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				imagePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				imagePanelLayout.rowWeights = new double[] {0.01, 0.1, 0.2};
				imagePanelLayout.rowHeights = new int[] {7, 7, 7};
				imagePanelLayout.columnWeights = new double[] {0.01, 0.1, 0.01};
				imagePanelLayout.columnWidths = new int[] {7, 7, 7};
				imagePanel.setLayout(imagePanelLayout);
				{
					jLabelImage = new JLabel();
					jLabelImage.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/anote_splash.png")));
					imagePanel.add(jLabelImage, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
				{
					imagePanel.add(getButtonsPanel(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
			}
		}

	}

	protected void okButtonAction() {
		finish();
	}

	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		this.setSize(710, 350);
		Utilities.centerOnOwner(this);
		this.setVisible(true);
	}

	public void onValidationError(Throwable arg0) {
		
	}

	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink;
	}

}
