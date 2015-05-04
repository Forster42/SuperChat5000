/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import static java.awt.image.ImageObserver.WIDTH;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Max Schiedermeier
 */
public class SetupPanel extends JPanel{

    /*
    private final JButton serverButton;
    private final JButton clientButton;
    private final JTextField ipField;
    private final JLabel status;*/
    
    public SetupPanel(Color background)
    {        
        /*
        setBackground(background);
        setLayout(new BoxLayout(this, WIDTH));
        
        JLabel welcome = new JLabel("***SuperChat 5000***");
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(welcome);
        
        add(Box.createRigidArea(new Dimension(0, 10)));
        //JLabel ip = new JLabel(Chat.ip);
        //ip.setAlignmentX(Component.CENTER_ALIGNMENT);
        //add(ip);
        //JLabel hostname = new JLabel(Chat.hostname);
        //hostname.setAlignmentX(Component.CENTER_ALIGNMENT);
        //add(hostname);
        
        add(Box.createRigidArea(new Dimension(0, 100)));           
        
        serverButton = new JButton("Launch Server");
        serverButton.setBackground(background);
        serverButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(serverButton);
        
        add(Box.createRigidArea(new Dimension(0, 100)));
        
        JPanel ipPanel = new JPanel(new BorderLayout());
        ipPanel.setBackground(background);
        ipField = new JTextField("Client Ready");
        ipField.setBackground(background);
        ipField.setHorizontalAlignment(JTextField.CENTER);
        ipPanel.add(ipField, BorderLayout.SOUTH);
        add(ipPanel);
        
        add(Box.createRigidArea(new Dimension(0, 20)));
        
        clientButton = new JButton("Connect to IP");
        clientButton.setBackground(background);
        clientButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(clientButton);
        
        add(Box.createRigidArea(new Dimension(0, 20)));
        status = new JLabel(" ");
        status.setForeground(Color.red);
        status.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(status);
        
        add(Box.createRigidArea(new Dimension(0, 80))); 
        
        JLabel disclaimer = new JLabel("FiftyMaex / Mastermilz / T-Dog");
        disclaimer.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(disclaimer);   
                */
    }
    
    /*
    public void addServerListener(ActionListener serverListener)
    {
        serverButton.addActionListener(serverListener);
    }

    public void addClientListener(ActionListener clientListener)
    {
        clientButton.addActionListener(clientListener);
    }

    public String getIPFieldContent()
    {
        return ipField.getText();
    }
    
    public void flashHostWarning()
    {
        Thread noRoute = new Flash(status, "No route to host!");
        noRoute.start();
    }
    
    private class Flash extends Thread
    {
        private JLabel field;
        private String message;
        
        Flash(JLabel field, String message)
        {
            this.field = field;
            this.message = message;
        }

        @Override
        public void run()
        {
            try
            {
                field.setText(message);
                Thread.sleep(3000);
                field.setText(" ");
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(SetupPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
    }*/
}
