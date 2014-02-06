package pt.uminho.generic.genericresult.methodinformation;

import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * DynamicModelSimulator
 * Created By
 * User: ptiago
 * Date: Mar 6, 2009
 * Time: 3:08:57 PM
 */
public class GenericMethodInformationGUI extends JPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String methodDescription;


    public GenericMethodInformationGUI(String methodDescription){
        this.methodDescription = methodDescription;
        initGUI();
    }

    protected void initGUI() {
        setLayout(new FlowLayout());
        JLabel methodLabel = new JLabel("Method Description: ");
        Font methodLabelFont = methodLabel.getFont();
        methodLabel.setFont(methodLabelFont.deriveFont(methodLabelFont.getStyle() ^ Font.BOLD));
        add(methodLabel);

        JLabel methodDescriptionLabel = new JLabel(methodDescription);
        add(methodDescriptionLabel);
    }
    
}
