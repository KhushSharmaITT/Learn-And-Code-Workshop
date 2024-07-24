package com.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.Hashtable;

import org.json.simple.parser.ParseException;

import com.action.Action;
import com.client.ClientPayloadService;
import com.exception.DataSerializationException;
import com.exception.InvalidArgumentException;
import com.exception.InvalidOperationException;
import com.factory.DataSerializerFactory;
import com.factory.UserActionFactory;
import com.utility.ProtocolConstant;
import com.utility.SocketHelper;
import com.utility.core.CommunicationProtocol;
import com.utility.core.DataSerializer;

public class ServerThread implements Runnable{

	private Socket clientSocket;
    private SocketHelper socketHelper;
    private static final boolean close = false;

	public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.socketHelper = SocketHelper.getInstance();
    }

	@Override
	public void run() {
		try {
            System.out.println("[SERVER] client connected");
            while(true) {
                if(close) {
                    clientSocket.close();
                    break;
                }
                readClientRequest();
            }
        } catch(InvalidArgumentException|DataSerializationException|ParseException|IOException issue) {
            System.err.println("Something went wrong: "+issue.getLocalizedMessage());
        }
		catch(Exception issue) {
			System.err.println("Something went wrong: "+issue.getLocalizedMessage());
		}

	}

	private void readClientRequest() throws IOException, ParseException, DataSerializationException, InvalidArgumentException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidOperationException {
		final BufferedReader clientInput = socketHelper.getReader(clientSocket);
        final String protocolFormat = clientInput.readLine().split("=")[1].trim();
        final StringBuilder protocolBody = new StringBuilder();
        while(clientInput.ready()) {
        		protocolBody.append(clientInput.readLine());
        }
        final DataSerializer serializer = DataSerializerFactory.getInstance(protocolFormat);
        final CommunicationProtocol requestProtocol = serializer.deserialize(protocolBody.toString());
        processClientRequest(requestProtocol);
    }

	private void processClientRequest(CommunicationProtocol requestProtocol) throws InvalidArgumentException, DataSerializationException, IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidOperationException {
		final String requestActionName = requestProtocol.getHeaders().get(ProtocolConstant.ACTION_NAME);
        final Action action = UserActionFactory.getInstance(requestActionName);
        final String data = requestProtocol.getData()+"="+requestActionName;
        CommunicationProtocol responseProtocol = null;
        try {
        	final String responseData = action.handleAction(data);
            System.out.println("in client thread");
            responseProtocol = prepareSuccessResponse(responseData);
        } catch (Exception issue) {
            System.out.println("Data Issue : "+issue.getLocalizedMessage());
            responseProtocol = prepareErrorResponse(issue.getLocalizedMessage());
        }
        sendResponseToClient(responseProtocol);
    }

	private CommunicationProtocol prepareErrorResponse(String errorMessage) {
		 final ClientPayloadService clientService = new ClientPayloadService(prepareErrorResponseDetails());
		 final CommunicationProtocol responseProtocol = clientService.createResponseCommunicationProtocol(errorMessage);
	     responseProtocol.setStatus(ProtocolConstant.FAILURE);
	     responseProtocol.setStatusCode(ProtocolConstant.ERROR_CODE);
	     responseProtocol.setErrorMessage(errorMessage);
	     return responseProtocol;
    }

	private CommunicationProtocol prepareSuccessResponse(String responseData) {
		 final String actualResponseData  = responseData.split("=")[0].trim();
		 final ClientPayloadService clientService = new ClientPayloadService(prepareResponseDetails(responseData));
		 final CommunicationProtocol responseProtocol = clientService.createResponseCommunicationProtocol(actualResponseData);
	     responseProtocol.setStatus(ProtocolConstant.SUCCESS);
	     responseProtocol.setStatusCode(ProtocolConstant.SUCCESS_CODE);
	     return responseProtocol;
	}
	private Hashtable<String, String> prepareResponseDetails(String responseData) {
		final String responseActionName  = responseData.split("=")[1].trim();
        final Hashtable<String, String> responseDetails = new Hashtable<>();
        responseDetails.put(ProtocolConstant.ACTION_NAME, responseActionName);
        responseDetails.put(ProtocolConstant.PROTOCOL_FORMAT, ProtocolConstant.JSON);
        return responseDetails;
    }

	private Hashtable<String, String> prepareErrorResponseDetails() {
        final Hashtable<String, String> responseDetails = new Hashtable<>();
        responseDetails.put("actionName", "error");
        responseDetails.put(ProtocolConstant.PROTOCOL_FORMAT, ProtocolConstant.JSON);
        return responseDetails; 
    }

	private void sendResponseToClient(CommunicationProtocol responseProtocol) throws IOException, DataSerializationException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final DataSerializer serializer = DataSerializerFactory.getInstance(ProtocolConstant.JSON);
        final PrintWriter socketWriter = socketHelper.getWriter(clientSocket);
        socketWriter.write("type=json\n");
        socketWriter.write(serializer.serialize(responseProtocol) + "\n");
        socketWriter.flush();
    }
}
