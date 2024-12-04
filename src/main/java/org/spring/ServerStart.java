package org.spring;

public class ServerStart {
    public static void main(String[] args) throws Exception {
        SimpleServer server = new SimpleServer();
        server.start();
    }
}