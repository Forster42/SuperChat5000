/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Base.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Max Schiedermeier
 */
public final class ChatPanel extends JPanel
{

    //attributes for the chat history
    private final JScrollPane historyScrollPane;
    private final DefaultStyledDocument document;
    private final Style style;
    private final JTextPane chatLog;

    //the chat bar below
    private final JTextField chatSend;

    //stores the currently set username
    private String username;
    private final ArrayList<String> history = new ArrayList<>();
    private ListIterator<String> iter = null;

    public ChatPanel(Color bgColor)
    {
        //get username from system
        username = System.getProperty("user.name");

        //final Container topLayer = getContentPane();
        setLayout(new BorderLayout());
        //setBackground(bgColor);

        //add chat history window + scrollbar
        document = new DefaultStyledDocument();
        chatLog = new JTextPane(document);
        //chatLog.setBackground(bgColor);
        StyleContext context = new StyleContext();
        style = context.addStyle("test", null);
        StyleConstants.setForeground(style, Color.BLUE);
        chatLog.setEditable(false);
        historyScrollPane = new JScrollPane(chatLog, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(historyScrollPane, BorderLayout.CENTER);

        chatSend = new JTextField();
        //chatSend.setBackground(bgColor);
        add(chatSend, BorderLayout.SOUTH);

        chatSend.addKeyListener(new KeyboardListener());
        writeLog("For internet server-ip, check:");
        writeLog("http://students.fim.uni-passau.de/~schieder/gateway");
        chatSend.setText("connect:192.168.1.8");

        ConnectionManager.getInstance().setOutputWindow(this);
    }

    private String getEntry()
    {
        return chatSend.getText();
    }

    private void clearEntryField()
    {
        chatSend.setText("");
    }

    private void setEntry(String entry)
    {
        chatSend.setText(entry);
    }

    public void writeLog(String logText)
    {
        //actually write message
        extendHistory(logText, "#000000", true);
    }

    public void extendHistory(String addon, String color, boolean centered)
    {
        //set font color
        StyleConstants.setForeground(style, Color.decode(color));

        try {
            document.insertString(document.getLength(), "\n" + addon, style);
        }
        catch (BadLocationException ex) {
            Logger.getLogger(ChatPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        chatLog.setDocument(document);

        //set font alignment
        StyledDocument doc = chatLog.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, (centered ? StyleConstants.ALIGN_CENTER : StyleConstants.ALIGN_LEFT));
        doc.setParagraphAttributes(doc.getLength(), addon.length(), center, false);

        //automatically scroll down so the most recently added message is shown
        chatLog.setCaretPosition(chatLog.getDocument().getLength());
    }

    private class KeyboardListener implements KeyListener
    {

        @Override
        public void keyTyped(KeyEvent ke)
        {
        }

        @Override
        public void keyPressed(KeyEvent ke)
        {
            //arrow up [38] / arrow down [40] -> iterate through command history
            if (ke.getKeyCode() == 38) {
                if (iter == null) {
                    iter = history.listIterator(history.size());
                    setEntry("");
                }
                if (iter.hasPrevious()) {
                    setEntry(iter.previous());
                }
            }
            else {
                iter = null;
            }

            //arrow down [40]
            //esc [27] -> deconnect if connected
            if (ke.getKeyCode() == 27 && ConnectionManager.getInstance().isConnected()) {
                try {
                    ConnectionManager.getInstance().disconnect();
                    writeLog("Disconnected");
                }
                catch (IOException ex) {
                    throw new RuntimeException("Unable to disconnect");
                }
            }

            //get message and send when enter (keycode 10) was typed
            else if (ke.getKeyCode() == 10) {

                //extract message from field and store message in history-collection
                String message = getEntry();
                history.add(message);

                //check if typed text was an internal command
                if (message.startsWith("connect:") && !ConnectionManager.getInstance().isConnected()) {

                    //connect to server if not yet connected
                    String ip = message.split(":")[1].trim();
                    try {
                        ConnectionManager.getInstance().connect(ip);
                        writeLog("Connected to \"" + ip + "\" - ESC to disconnect.");
                    }
                    catch (IOException ex) {
                        writeLog("Unable to connect to \"" + ip + "\"");
                        return;
                    }
                }
                else if (message.startsWith("setname:")) {

                    username = message.split(":")[1].trim();
                }
                else {
                    ConnectionManager.getInstance().sendMessage(username + ": ", message);
                }

                //clear input field
                clearEntryField();
            }
        }

        @Override
        public void keyReleased(KeyEvent ke)
        {
        }

    }

    /**
     * Extracts the messages sender prelude, uses a hash function to generate a
     * unique color value for that prelude. This way all chat participants (most
     * likely) have different colors and have the same colors on all clients.
     *
     * @return Hex representation of the Color derived from the Hash as String
     */
    public static String getPreludeColor(String message)
    {
    	/*
    	 if_not[ColoredNames]
    	 return "#000000";
    	 else[ColoredNames]
         String prelude = message.split(": ")[0];
         return String.format("#%X", prelude.hashCode()).substring(0, 6);
         end[ColoredNames]
        */
    }

}
