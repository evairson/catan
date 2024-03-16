package view;

import network.NetworkObject;
import network.PlayerClient;
import start.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatPanel extends JPanel {
    private ActionPlayerPanel actionPlayerPanel;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;

    public ChatPanel(ActionPlayerPanel actionPlayerPanel) {
        this.actionPlayerPanel = actionPlayerPanel;

        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setOpaque(false);
        chatArea.setLineWrap(true); // Activation du retour automatique à la ligne
        chatArea.setWrapStyleWord(true); // Séparation des mots pour le retour à la ligne
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        inputPanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("Envoyer");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlayerClient playerClient = actionPlayerPanel.getApp().getPlayer();
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
        });
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);
    }

    private void flushChat() {
        messageField.setText("");
    }
    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            String pName = actionPlayerPanel.getApp().getGame().getCurrentPlayer().getName();
            chatArea.append(pName + ": " + message + "\n");
            flushChat();
        }
    }
    public void addMessage(String message) {
        chatArea.append(message + "\n");
    }

}
