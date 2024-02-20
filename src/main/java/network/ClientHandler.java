package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream input = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String text;
            do {
                text = reader.readLine();
                writer.println("Message reçu : " + text); // Echo le message reçu au client
            } while (!text.equals("bye"));

            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Erreur I/O : " + e.getMessage());
        }
    }

}
