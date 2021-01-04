package com.broker;

import com.market.*;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class BrokerHandler implements Runnable {
	private static Socket broker;
    private BufferedReader in;
    private PrintWriter out;
    private MarketHandler market = null;
    private final int brokerID;
    private final String brokerHash;

    public BrokerHandler(Socket brokerSocket, int ID, String hash, MarketHandler mk) throws IOException {
        broker = brokerSocket;
        this.market = mk;
        this.brokerHash = hash;
        this.brokerID = ID;
        in = new BufferedReader(new InputStreamReader(broker.getInputStream()));
        out = new PrintWriter(broker.getOutputStream(), true);
    }

    public void sendReq(String req) {
        String[] split = req.split("\\|");
        split[0] = Integer.toString(brokerID);
        req = split[0] + "|" + split[1] + "|" + split[2] + "|" + split[3] + "|" + split[4] + "|" + split[5] + "|"
                + (Integer.parseInt(split[2]) * Integer.parseInt(split[5]));
        // saveStuff(req);
        System.out.println(req);
        System.out.println("transacting " + req);
        if (this.market != null) {
            this.market.getOut().println("[Broker:" + brokerID + ":]," + req + "," + brokerHash);
        } else {
            System.out.println("Sorry, market communication error, try again later...");
        }
    }

    public void run() {
        String msg = "Verified\n[Broker: " + this.brokerID + " ] is online.\n\n";
        System.out.println(msg);
        try {
            while (true) {
                String request = in.readLine().toLowerCase();
                if (request.contains("buy")) {
                    sendReq(request);
                } else if (request.contains("sell")) {
                    sendReq(request);
                } else if (request.toLowerCase().equals("exit")) {
                    break;
                } else if (request.contains("tnes!@#")) {
                    out.println("transaction sent...");
                } else {
                    out.println("type buy/ sell");
                }
            }
            System.err.println("[Broker: " + this.brokerID + " ] has disconnected  .\n");
            out.close();
            in.close();
        } catch (IOException e) {
            System.err.println("[Broker: " + this.brokerID + " ] is offline.\n");
            try {
                out.close();
                in.close();
            } catch (IOException e1) {
                System.err.println("[Broker: " + this.brokerID + " ] is offlin.\n");
            }
        }
    }
}
