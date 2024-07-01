package com.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketHelper {
	private static SocketHelper socketHelper;

    public static SocketHelper getInstance() {
        if (socketHelper == null) {
            socketHelper = new SocketHelper();
        }
        return socketHelper;
    }

    private SocketHelper() {
        // To allow singelton instance
    }

    public BufferedReader getReader(Socket socket) throws IOException {
        final InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        final BufferedReader inputReader = new BufferedReader(inputStreamReader);
        return inputReader;
    }

    public OutputStreamWriter getWriter(Socket socket) throws IOException {
        final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
        return outputStreamWriter;
    }

}
