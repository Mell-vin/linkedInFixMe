package com.router;

import com.broker.*;
import com.market.*;
import com.core.*;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConn {
    private static ServerSocket brokerSS;
    private static ServerSocket MarketSS;
    private static Socket brokerSocket;
    private static Socket MarketSocket;
    private CoreLogic core = new CoreLogic();
    private static final int BROKER_PORT = 5000;
    private static final int MARKET_PORT = 5001;
    private static ArrayList<BrokerHandler> brokerList = new ArrayList<>();
    public static MarketHandler marketHandler = null;
    private static ExecutorService marketPool = Executors.newFixedThreadPool(1);
    private static ExecutorService brokerPool = Executors.newFixedThreadPool(4);

    public static void marketConn(int MARKET_PORT) {
        try {
            MarketSS = new ServerSocket(MARKET_PORT);
        } catch (IOException e) {
            System.out.println("[Market] Some connection error Occurred(''- )...");
            e.printStackTrace();
        }
        System.out.println("Market is open and online. Happy trading");
    }

    public static void brokerConn(int BROKER_PORT) {
        try {
            brokerSS = new ServerSocket(BROKER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startBrokerConn(int brokerPort) {
        brokerConn(brokerPort);
        while (true) {
            try {
                brokerSocket = brokerSS.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("[Server] New broker connection established... Verifying...");
            System.out.println("_____________________________________________________");
            try {
                BrokerHandler brokerThread = new BrokerHandler(brokerSocket, CoreLogic.brokerIDseed,
                        CoreLogic.hashGenerator(CoreLogic.brokerSeed, CoreLogic.brokerIDseed), marketHandler);
                brokerList.add(brokerThread);
                brokerPool.execute(brokerThread);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void startMarketConn(int marketPort) {
        marketConn(marketPort);
        while (true) {
            try {
                MarketSocket = MarketSS.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("[Server] New market connection established... Verifying...\n");
            try {
                marketHandler = new MarketHandler(MarketSocket, 200000);
                marketPool.execute(marketHandler);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {

        Thread t1, t2;
        t1 = new Thread(new Runnable() {
            public void run() {
                ServerConn.startBrokerConn(BROKER_PORT);
            }
        });
        t2 = new Thread(new Runnable() {
            public void run() {
                ServerConn.startMarketConn(MARKET_PORT);
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
}
