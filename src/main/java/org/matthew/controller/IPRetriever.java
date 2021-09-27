package org.matthew.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class IPRetriever {

    private static final List<String> ipFinders = Arrays.asList("https://checkip.amazonaws.com/", "https://ipv4.icanhazip.com/",
            "https://myexternalip.com/raw", "https://ipecho.net/plain",
            "https://bot.whatismyipaddress.com/", "https://www.trackip.net/ip");

    public static String getIPAddress() throws IOException {

        String ip = null;

        for(String ipFinder: ipFinders) {
            URL myIP = new URL(ipFinder);
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(myIP.openStream()));
                ip = in.readLine();
                break;
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if(ip==null) {
            ip = InetAddress.getLocalHost().getHostAddress();
        }
        return ip;
    }
}
