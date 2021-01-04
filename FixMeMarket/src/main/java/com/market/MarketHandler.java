package com.market;

import com.core.*;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MarketHandler implements Runnable {
    private static Socket market;
    private BufferedReader in;
    private PrintWriter out;
    private final int marketID;
    private final String marketHash;

    public MarketHandler(Socket marketSocket, int ID) throws IOException {
        market = marketSocket;
        marketID = ID;
        marketHash = CoreLogic.hashGenerator(CoreLogic.marketSeed, marketID);
        in = new BufferedReader(new InputStreamReader(market.getInputStream()));
        out = new PrintWriter(market.getOutputStream(), true);
    }

    public PrintWriter getOut() {
        return this.out;
    }

    public void run() {
        String msg = "Verified\n[Market: " + this.marketID + " ] is online.\n\n";
        System.out.println(msg);
        try {
            while (true) {
                String request = in.readLine();
                if (request.contains("exit")) {
                    out.println("okay. bye");
                    break;
                } else {
                    System.out.println(request);
                }
            }
        } catch (IOException e) {
            System.err.println("[Market: " + this.marketID + " ] has disconnected.\n");
            e.getStackTrace();
            try {
                out.close();
                in.close();
            } catch (IOException e1) {
                System.err.println("[Market: " + this.marketID + " ] is offline.\n");
                e1.getStackTrace();
            }
        }
    }
}
