package pt.uminho.generic.components.gradientpanel.topgradientpanel;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import pt.uminho.generic.components.gradientpanel.GradientGUI;

/**
 * DynamicModelSimulator
 * Created By
 * User: ptiago
 * Date: Mar 6, 2009
 * Time: 6:02:59 PM
 */
public class TextInformationPanel extends GradientGUI {
	
   
	private static final long serialVersionUID = 1L;
    protected Font titleFont;
    protected Color titleColor;
    protected Font additionalInformationFont;
    protected Color additionalInformationColor;
    protected JLabel titleLabel;

    public TextInformationPanel(String title, Font titleFont, Color titleColor, Font additionalInformationFont, Color additionalInformationColor, String additionalInformation) {
        super();
        titleLabel = new JLabel(title);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(titleColor);
        this.additionalInformationFont = additionalInformationFont;
        this.additionalInformationColor = additionalInformationColor;
        this.additionalInformation = additionalInformation;
        initGUI();
    }

    protected String additionalInformation;
    
    public TextInformationPanel() {
        super();
        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Impact", 1, 30));
        titleLabel.setForeground(Color.WHITE);
        initGUI();
    }
    
    public TextInformationPanel(String title) {
        super();
        titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Impact", 1, 30));
        titleLabel.setForeground(Color.WHITE);
        initGUI();
    }
    
    protected void initGUI() {
        setLayout(new BorderLayout());
        add(titleLabel,BorderLayout.CENTER);
    }

	public String getTitle() {
		return titleLabel.getText();
	}

	public void setTitle(String title) {
		titleLabel.setText(title);
	}

	public Font getTitleFont() {
		return titleLabel.getFont();
	}

	public void setTitleFont(Font titleFont) {
		titleLabel.setFont(titleFont);
	}

	public Color getTitleColor() {
		return titleLabel.getForeground();
	}

	public void setTitleColor(Color titleColor) {
		titleLabel.setForeground(titleColor);
	}
    
    


}
