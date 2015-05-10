/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.awt.Color;
import java.awt.Container;
import java.awt.HeadlessException;
import javax.swing.JFrame;

/**
 *
 * @author Max Schiedermeier
 */
public class MainFrame extends JFrame {

    private final Container topLayer = getContentPane();
    private final Color backgroundColor = Color.WHITE;
    
    public MainFrame() throws HeadlessException
    {

    }
    
    public void switchToChatView()
    {
        ChatPanel chatPanel = new ChatPanel(backgroundColor);
        topLayer.removeAll();
        topLayer.add(chatPanel);
        setVisible(true);
    }
}
