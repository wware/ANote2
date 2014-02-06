package pt.uminho.generic.genericresult;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 * DynamicModelSimulator
 * Created By
 * User: ptiago
 * Date: Mar 6, 2009
 * Time: 2:27:37 PM
 */
public class GenericResultGUI extends JPanel implements ActionListener{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel mainPanel;
    protected List<ButtonPanelData> buttonPanelList;
    protected ButtonPanelData currentButtonPanel;
    protected JSplitPane splitPane;

    public GenericResultGUI(JPanel mainPanel,List<ButtonPanelData> buttonPanelList){
        this.mainPanel = mainPanel;
        this.buttonPanelList = buttonPanelList;
        currentButtonPanel = null;
        initGUI();
    }

    protected void initGUI() {
        setLayout(new BorderLayout());
        Border mainPanelBorder = BorderFactory.createLoweredBevelBorder();
        Border toolbarBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        addToolBarButtons(toolbar);
        mainPanel.setBorder(mainPanelBorder);
        add(mainPanel,BorderLayout.CENTER);
        toolbar.setBorder(toolbarBorder);
        add(toolbar,BorderLayout.SOUTH);
    }

    protected void addToolBarButtons(JToolBar toolbar){
        for(ButtonPanelData buttonPanelData:buttonPanelList){
            JButton button  = buttonPanelData.getButton();
            toolbar.add(button);
            button.addActionListener(this);
        }

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String actionCommand = actionEvent.getActionCommand();
        ButtonPanelData buttonPanelData = getButtonPanelData(actionCommand);

        if(currentButtonPanel != null){
            JButton currentButton = currentButtonPanel.getButton();
            String currentButtonActionCommand = currentButton.getActionCommand();
            if(actionCommand.equals(currentButtonActionCommand))
                currentButtonPanel = null;
        }else
             currentButtonPanel = buttonPanelData;

        buildMainPanel();
        updateUI();
    }

    private void buildMainPanel() {
        remove(mainPanel);

        if(splitPane != null)
                remove(splitPane);

        if(currentButtonPanel != null){
            JPanel buttonPanel = currentButtonPanel.getPanel();
            splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,mainPanel,buttonPanel);
            splitPane.setDividerLocation(750);
            splitPane.setOneTouchExpandable(true);
            add(splitPane,BorderLayout.CENTER);
        }else
            add(mainPanel,BorderLayout.CENTER);
    }

    private ButtonPanelData getButtonPanelData(String actionCommand) {
        for(ButtonPanelData buttonPanelData:buttonPanelList){
            JButton button = buttonPanelData.getButton();
            String buttonActionCommand = button.getActionCommand();
            if(buttonActionCommand.equals(actionCommand))
                return buttonPanelData;
                
        }

        return null;
    }


}
