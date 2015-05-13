package Base.client;

import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JFrame;

public class Client
{
    public static void main(String[] args) throws UnknownHostException,
            IOException
    {        
        //initialize setup gui
        MainFrame mainFrame = new MainFrame();
        mainFrame.setSize(500, 500);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        
        mainFrame.switchToChatView();
    }
}
