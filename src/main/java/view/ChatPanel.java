package view;

import network.NetworkObject;
import network.PlayerClient;
import start.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.*;

public class ChatPanel extends JPanel {
    private ActionPlayerPanel actionPlayerPanel;
    private JTextPane chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private ActionListener sender;

    public ChatPanel(ActionPlayerPanel actionPlayerPanel) {
        this.actionPlayerPanel = actionPlayerPanel;

        setLayout(new BorderLayout());

        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setOpaque(false);
        //chatArea.setLineWrap(true); // Activation du retour automatique à la ligne
        //chatArea.setWrapStyleWord(true); // Séparation des mots pour le retour à la ligne
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        inputPanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("Envoyer");

        sender = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlayerClient playerClient = ((PlayerClient) actionPlayerPanel.getApp().getPlayer());
                if (Main.hasServer()) {
                        try {
                            int id = playerClient.getId();

                            String pName = playerClient.getName();

                            NetworkObject object = new NetworkObject(NetworkObject.TypeObject.ChatMessage,
                                    pName + ": " + messageField.getText(), id);

                            playerClient.getOut().writeUnshared(object);
                            playerClient.getOut().flush();
                        } catch (Exception exception) {
                            exception.getStackTrace();
                        }
                } else {
                    sendMessage();
                }
                flushChat();
            }
        };

        sendButton.addActionListener(sender);

        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);
    }

    public ActionListener getSender() {
        return sender;
    }

    /**
     * Removes all text from the type bar.
     */
    private void flushChat() {
        messageField.setText("");
    }

    /**
     * Sends the string located in the type bar and performs a flush.
     * @see ChatPanel#flushChat
     */
    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            String pName = actionPlayerPanel.getApp().getGame().getCurrentPlayer().getName();
            appendToPane(chatArea, pName + ": " + message + "\n", null);
            flushChat();
        }
    }
    public void addMessage(String message) {
        appendToPane(chatArea, message + "\n", null);
    }

    /**
     * Adds a message to the bottom of the chat area.
     * @param tp The chat area
     * @param msg The message
     * @param attr Attributes of the message, often color
     */
    private void appendToPane(JTextPane tp, String msg, AttributeSet attr) {
        Document doc = tp.getDocument();
        try {
            // Ajoute le texte à la fin du document
            doc.insertString(doc.getLength(), msg, attr);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

}
