package pt.uminho.generic.genericresult;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * DynamicModelSimulator
 * Created By
 * User: ptiago
 * Date: Mar 6, 2009
 * Time: 2:36:06 PM
 */
public class ButtonPanelData {
    protected JButton button;
    protected JPanel panel;

    public ButtonPanelData(JButton button, JPanel panel) {
        this.button = button;
        this.panel = panel;
    }

    

    public JButton getButton() {
        return button;
    }

    public JPanel getPanel() {
        return panel;
    }
}
