package com.market;

import com.core.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class MarkerConn {
    private static final String SERVERHOST = "localhost";
    private static final int PORT = 5001;
    CoreLogic core  = new CoreLogic();

    public static int guardianAngel(String msg) {
        String message = msg;
        String[] components = message.split(",");
        final String hash = components[2];
        String[] getID = components[0].split(":");
        int ID = Integer.parseInt(getID[1]);
        if (hash.equals(CoreLogic.hashGenerator(CoreLogic.brokerSeed, ID))) {
            return 1;
        }
        return 0;
    }

    public static void MsgRedact(String msg) {
        String message = msg;
        String[] components = message.split(",");
        Random rand = new Random();
        final String hash = components[2];
        String[] getID = components[0].split(":");
        final int ID = Integer.parseInt(getID[1]);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (rand.nextInt(3) == 2) {
            if (components[1].contains("buy")) {
                System.out.println(components[0] + "buy was successful");
            } else if (components[1].contains("sell")) {
                System.out.println(components[0] + "sell was successful");
            } else {
                System.out.println("Something isnt right");
            }
        } else {
            System.out.println(components[0] + "Transaction rejected " + hash);
        }
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVERHOST, PORT);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            System.out.println("> : ");

            // if (!command.equals("buy")) {
            // out.println("[market]: Okay");
            // } else {
            // out.println("Choose an instrument to buy " + command);
            // }
            String serverRes = input.readLine();
            if (guardianAngel(serverRes) == 1) {
                MsgRedact(serverRes);
            } else {
                out.println("This broker is not verified");
            }
        }
    }
}
