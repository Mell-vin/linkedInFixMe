package com.broker;

import com.core.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BrokerConn {
	private static final String SERVERHOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVERHOST, PORT);
        BufferedReader kbrd = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        // new Thread(server).start();

        while (true) {
            System.out.println("> : ");
            String command = kbrd.readLine();

            if (command.toLowerCase().contains("buy")) {
                out.println(CoreLogic.buyLogic(kbrd, out));
            } else if (command.toLowerCase().contains("sell")) {
                out.println(CoreLogic.sellLogic(kbrd, out));
            } else if (command.toLowerCase().equals("exit")) {
                out.println(command);
                break;
            } else {
                out.println("say something useful");
            }
            String serverRes = input.readLine(); // coming from brokerHandler
            System.out.println(serverRes);
        }
        kbrd.close();
        input.close();
        out.close();
        socket.close();
    }
}
