package pt.uminho.generic.defaultdialog;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import es.uvigo.ei.aibench.workbench.Workbench;

/**
 * DynamicModelSimulator
 * Created By
 * User: ptiago
 * Date: Mar 10, 2009
 * Time: 12:37:45 PM
 */
public class DefaultDialog {

    public static void  showErrorMessageDialog(String message){
        JFrame mainFrame = Workbench.getInstance().getMainFrame();
        JOptionPane.showMessageDialog(mainFrame,message,"Error",JOptionPane.ERROR_MESSAGE);
    }
}
