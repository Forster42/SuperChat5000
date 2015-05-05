/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.*;

/**
 *
 * @author Max Schiedermeier
 */
public final class ChatPanel extends JPanel
{

    private final JTextArea chatLog;
    private final JTextField chatSend;
    private String username;

    public ChatPanel(Color bgColor)
    {
        //get username from system
        username = System.getProperty("user.name");

        //final Container topLayer = getContentPane();
        setLayout(new BorderLayout());
        setBackground(bgColor);

        chatLog = new JTextArea();
        chatLog.setBackground(bgColor);
        chatLog.setEditable(false);
        add(chatLog, BorderLayout.CENTER);

        chatSend = new JTextField();
        chatSend.setBackground(bgColor);
        add(chatSend, BorderLayout.SOUTH);

        chatSend.addKeyListener(new KeyboardListener());

        chatSend.setText("connect:192.168.1.3");

        ConnectionManager.getInstance().setOutputWindow(this);
    }

    public void sendMessage(String hostname)
    {
        StringBuilder sb = new StringBuilder(hostname);
        sb.append(chatSend.getText());
        chatSend.setText("");
        extendHistory(sb.toString());
    }

    private String getEntry()
    {
        return chatSend.getText();
    }

    private void clearEntryField()
    {
        chatSend.setText("");
    }

    public void writeLog(String logText)
    {
        //center text
        int offset = getWidth() / 2;
        offset /= 5;
        offset = offset - (logText.length() / 2);
        StringBuilder sb = new StringBuilder();
        for (offset = offset + 1; offset > 0; offset--) //+1 for the star
        {
            sb.append(" ");
        }
        sb.append("*");
        sb.append(logText);
        sb.append("*\n");
        extendHistory(sb.toString());
    }

    public void extendHistory(String addon)
    {
        //check if fits
        int linenumber = chatLog.getHeight() / 16;
        StringBuilder sb = new StringBuilder(chatLog.getText());
        sb.append("\n");
        sb.append(addon);
        String[] chatArray = sb.toString().split("\n");

        if (chatArray.length <= linenumber) {
            chatLog.setText(sb.toString());
        }
        else {
            StringBuilder matchingString = new StringBuilder();
            for (int i = chatArray.length - linenumber; i < chatArray.length; i++) {
                matchingString.append("\n");
                matchingString.append(chatArray[i]);
            }
            chatLog.setText(matchingString.toString());
        }
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
            //get message and send when enter (keycode 10) was typed
            if (ke.getKeyCode() == 10) {

                //check if typed text was an internal command
                String message = getEntry();

                if (message.startsWith("connect:") && !ConnectionManager.getInstance().isConnected()) {

                    //connect to server if not yet connected
                    String ip = message.split(":")[1].trim();
                    try {
                        ConnectionManager.getInstance().connect(ip);
                        writeLog("Connected to \"" + ip + "\"");
                    }
                    catch (IOException ex) {
                        writeLog("Unable to connect to \"" + ip + "\"");
                        return;
                    }
                }
                else if (message.equals("disconnect") && ConnectionManager.getInstance().isConnected()) {
                    try {
                        ConnectionManager.getInstance().disconnect();
                        writeLog("Disconnected");
                    }
                    catch (IOException ex) {
                        throw new RuntimeException("Unable to disconnect");
                    }
                }
                else if (message.startsWith("setname:")) {

                    username = message.split(":")[0].trim();
                }
                else {
                    ConnectionManager.getInstance().sendMessage(username + ": " + message);
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
}
