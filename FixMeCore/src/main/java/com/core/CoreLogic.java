package com.core;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Random;
import java.math.BigInteger;
import java.util.concurrent.locks.Lock;
import java.security.MessageDigest;
import java.util.concurrent.locks.ReentrantLock;

public class CoreLogic {
	private static final String[] instruments = { "Bitcoin", "Ethereum", "Litcoin", "Ripple", "Tether" };
    private static final String[] abbr = { "BTC", "ETH", "LTC", "XRP", "USDT" };
    private static int[] prices = { 10000, 8000, 12000, 6000, 500 };
    private static String[] FIXMsg = { "35=D", "50=", "30=", "55=", "40=1" };
    private static String Stock;
    private static Random rand = new Random();
    private static int[] priceChanges = { rand.nextInt(300), rand.nextInt(300), rand.nextInt(300), rand.nextInt(300),
            rand.nextInt(300) };
    private static int num;
    private static String itemID;
    private static String NoS; // number of shares
    private static int Nos;
    public static int brokerIDseed = 100000;
    public static String brokerSeed = "erbko!@3YenO*&Y&_+-*/";
    public static int marketIDseed = 200000;
    public static String marketSeed = "tremak!@3YenO*&Y&_+-*/";
    private static Lock lock = new ReentrantLock();

    public static String hashGenerator(String seed, int ID) {
        String tmp = seed + ID;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(tmp.getBytes());
            BigInteger num = new BigInteger(1, hash);
            tmp = num.toString(16);
            while (tmp.length() < 32) {
                tmp = "0" + tmp;
            }
            if (ID == brokerIDseed)
                setBrokerIDseed();
            else
                setMarketIDseed();
            return tmp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void PrintList(PrintWriter out) {
        String msg = "FixMe CryptoCurrencies: \n" + "1) " + instruments[0] + " Price: R" + (prices[0] + priceChanges[0])
                + " (" + abbr[0] + ")\n" + "2) " + instruments[1] + " Price: R" + (prices[1] + priceChanges[1]) + "("
                + abbr[1] + ")\n" + "3) " + instruments[2] + " Price: R" + (prices[2] + priceChanges[2]) + "(" + abbr[2]
                + ")\n" + "4) " + instruments[3] + " Price: R" + (prices[3] + priceChanges[3]) + "(" + abbr[3] + ")\n";
        System.out.println(msg);
    }

    public static void setMarketIDseed() {
        marketIDseed++;
    }

    public static void setBrokerIDseed() {
        brokerIDseed++;
    }

    public static int isNumeric(String str) {
        if (str == null) {
            return 0;
        }
        try {
            int n = Integer.parseInt(str);
            if (n <= 0) {
                return 0;
            } else {
                return 1;
            }
        } catch (Exception e) {
            return -2;
        }
    }

    public static String encoder(String msg) { // void saveTODB(String msg) { }
        if (msg != null) {
            String[] split = msg.split("\\|");
            String finalMsg;

            // System.out.println(split[1]);
            // System.out.println(split[2]);
            // System.out.println(split[3]);
            // System.out.println(split[4]);
            // System.out.println(split[5]);

            if (split[1].contains("buy")) {
                split[1] = FIXMsg[1] + "1";
            } else {
                split[1] = FIXMsg[1] + "0";
            }

            split[2] = FIXMsg[2] + split[2];
            split[3] = FIXMsg[3] + split[3];
            split[4] = FIXMsg[4];
            finalMsg = FIXMsg[0] + "|" + split[1] + "|" + split[2] + "|" + split[3] + "|" + split[4];
            return finalMsg;
        }
        return null;
    }

    public static String decoder(String msg) {
        if (msg != null) {
            String[] split = msg.split("|");
            String finalMsg;

            if (split[1].contains("1")) {
                split[1] = "buy";
            } else {
                split[1] = "sell";
            }

            split[2] = FIXMsg[2] + split[2];
            split[3] = FIXMsg[3] + split[3];
            split[4] = FIXMsg[4];
            finalMsg = FIXMsg[0] + "|" + split[1] + "|" + split[2] + "|" + split[3] + "|" + split[4];
            return (split[0] + " " + split[1] + " " + split[2] + " " + split[3] + " " + split[4]);
        }
        return null;
    }

    public static String buyLogic(BufferedReader in, PrintWriter out) {
        lock.lock();
        String encodingMsg = null;
        try {
            while (true) {
                System.out.println("Choose Item ID to purchase:");
                PrintList(out);
                itemID = in.readLine();
                if (isNumeric(itemID) != 1) {
                    System.out.println("Choice must be numeric.");
                } else if (isNumeric(itemID) == 1) {
                    num = Integer.parseInt(itemID);
                    if (num <= 0 || num >= 5) {
                        System.out.println("Choice must be between 1 - 4.");
                    } else {
                        Stock = abbr[num - 1];
                        break;
                    }
                }
            }

            while (true) {
                System.out.println("Choose number of shares to buy:");
                NoS = in.readLine();
                if (isNumeric(itemID) != 1) {
                    System.out.println("invalid choice:");
                } else {
                    Nos = Integer.parseInt(NoS);
                    break;
                }
            }

            int tot = prices[num - 1] + priceChanges[num - 1];
            tot = tot * Nos;
            System.out.println("you ordered to buy " + Stock + " worth R" +  tot);
            encodingMsg = "Order|buy|" + Integer.toString(Nos) + "|" + Stock + "|market|"
                    + Integer.toString((prices[num - 1] + priceChanges[num - 1]));
            // saveTransaction(encodingMsg)
            System.out.println(encodingMsg);
            System.out.println(encoder(encodingMsg));
            out.println("tnes!@#");
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            lock.unlock();
        }
        return encodingMsg;
    }

    public static String sellLogic(BufferedReader in, PrintWriter out) {
        lock.lock();
        String encodingMsg = null;
        try {
            while (true) {
                System.out.println("Choose Item ID to sell:");
                PrintList(out);
                itemID = in.readLine();
                if (isNumeric(itemID) != 1) {
                    System.out.println("Choice must be numeric.");
                } else if (isNumeric(itemID) == 1) {
                    num = Integer.parseInt(itemID);
                    if (num <= 0 || num >= 5) {
                        System.out.println("Choice must be between 1 - 4.");
                    } else {
                        Stock = abbr[num - 1];
                        break;
                    }
                }
            }

            while (true) {
                System.out.println("Choose number of shares to sell:");
                NoS = in.readLine();
                if (isNumeric(itemID) != 1) {
                    System.out.println("invalid choice:");
                } else {
                    Nos = Integer.parseInt(NoS);
                    break;
                }
            }

            int tot = prices[num - 1] + priceChanges[num - 1];
            tot = tot * Nos;

            System.out.println("you ordered to sell " + Stock + " worth R" + tot);
            encodingMsg = "Order|sell|" + Integer.toString(Nos) + "|" + Stock + "|market|"
                    + Integer.toString((prices[num - 1] + priceChanges[num - 1]));
            // saveTransaction(encodingMsg)
            System.out.println(encoder(encodingMsg));
            out.println("tnes!@#");
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            lock.unlock();
        }
        return encodingMsg;
    }

    public static void noLogic() {
        lock.lock();
        try {

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            lock.unlock();
        }
    }
}
