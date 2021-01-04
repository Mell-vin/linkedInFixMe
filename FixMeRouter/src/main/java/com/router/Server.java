package com.router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Server implements Runnable {
    private Socket server;
    private BufferedReader in;
    private PrintWriter out;

    public Server(Socket sock) throws IOException {
        server = sock;
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        out = new PrintWriter(server.getOutputStream(), true);

    }

    public void run() {
        while (true) {
            String SR = null;
            try {
                SR = in.readLine();
                out.println("Server says: " + SR);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                System.out.println("Server input close err");
                e.printStackTrace();
            }
        }
    }
}
