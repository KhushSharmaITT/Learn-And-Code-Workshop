package com.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) throws IOException {
    	ExecutorService executor = Executors.newFixedThreadPool(ServerConfiguration.TOTAL_CONNECTIONS);

        try(final ServerSocket listenerSocket = new ServerSocket(ServerConfiguration.PORT)){
            System.out.println("[SERVER] waiting for client on port" + ServerConfiguration.PORT);

            while(true) {
            	Socket clientSocket = listenerSocket.accept();
            	executor.submit(new ClientThread(clientSocket));
            }
        }catch(IOException issue){
        	issue.printStackTrace();
        }finally {
        	executor.shutdown();
        }
            //final Thread serverThread = new Thread(new ServerThread(listenerSocket.accept()));
            //serverThread.start();
        // Close connection
        //listenerSocket.close();
    }
}

