package com.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Hashtable;

import org.json.simple.parser.ParseException;

import com.controller.Controller;
import com.exception.DataSerializationException;
import com.exception.InvalidArgumentException;
import com.exception.InvalidDataException;
import com.exception.InvalidOperationException;
import com.exception.UserNotFoundException;
import com.factory.ControllerFactory;
import com.factory.DataSerializerFactory;
import com.utility.ActionChoiceConstant;
import com.utility.ProtocolConstant;
import com.utility.core.CommunicationProtocol;
import com.utility.core.DataSerializer;
import com.utility.core.RequestWrapper;
import com.utility.SocketHelper;
import com.payload.MenuPayloadHelper;
import com.payload.UserPayload;
import com.payload.UserPayloadHelper;

public class ClientHandler {
	private Socket client;
    private static ClientHandler handler;
    private SocketHelper socketHelper;
    private ClientInputHandler inputHandler;
    private Hashtable<String, Object> userInputs;

    public static ClientHandler getInstance() throws UnknownHostException, IOException {
        if(handler == null) {
            handler = new ClientHandler();
        }
        return handler;
    }
    
    private ClientHandler() throws UnknownHostException, IOException {
        this.inputHandler = ClientInputHandler.getInstance();
        client = new Socket("localhost", 9999);
        socketHelper = SocketHelper.getInstance();
    }

    public void startClient() throws IOException, DataSerializationException, ParseException, InvalidArgumentException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidOperationException, SQLException, UserNotFoundException {
        System.out.println("Client started");
        this.userInputs = loadInputData();
        sendRequestToServer();
        System.out.println("Waiting from server");
        readServerResponse();
    }

    private void sendRequestToServer() throws IOException, DataSerializationException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	final ClientPayloadService clientService = new ClientPayloadService(prepareRequestDetails());
    	final DataSerializer serializer = DataSerializerFactory.getInstance("json");
    	String actionName = inputHandler.getActionName();
    	CommunicationProtocol protocol = null;
    	if(actionName == ActionChoiceConstant.AUTHENTICATE_USER) {  //create a factory pattern for this also to reduce the code.(for payload)
    		final UserPayloadHelper<RequestWrapper> userPayload = new UserPayloadHelper<RequestWrapper>(userInputs);// refactor this code after the implementation send only the object
            final RequestWrapper requestWrapper = userPayload.getPayload();
            protocol = clientService.createRequestCommunicationProtocol(requestWrapper);
    	}
    	else if(actionName.equals(ActionChoiceConstant.ADMIN_ADD)|| actionName.equals(ActionChoiceConstant.ADMIN_VIEW)||actionName.equals(ActionChoiceConstant.ADMIN_UPDATE)||actionName.equals(ActionChoiceConstant.ADMIN_DELETE)|| actionName.equals(ActionChoiceConstant.CHEF_VIEW)||actionName.equals(ActionChoiceConstant.CHEF_VIEW_RECOMMENDATION)) {
    		final MenuPayloadHelper<RequestWrapper> menuPayload = new MenuPayloadHelper<RequestWrapper>(userInputs);
    		final RequestWrapper requestWrapper = menuPayload.getPayload();
    		protocol = clientService.createRequestCommunicationProtocol(requestWrapper);
    	}
//    	else if(inputHandler.getActionName().equals(ActionChoiceConstant.ADMIN_VIEW)) {
//    		final MenuPayloadHelper<RequestWrapper> menuPayload = new MenuPayloadHelper<RequestWrapper>();
//    		final RequestWrapper requestWrapper = menuPayload.getPayload();
//    		protocol = clientService.createRequestCommunicationProtocol(requestWrapper);
//    	}
//    	else if(userInputs[1] == ActionChoiceConstant.ADMIN) {
//    		final UserPayloadHelper userPayload = new UserPayloadHelper(userInputs);
//            final RequestWrapper requestWrapper = userPayload.getPayload();
//            protocol = clientService.createRequestCommunicationProtocol(requestWrapper);
//    	}
        
        //System.out.println(protocol.toString());
        System.out.println(protocol.getHeaders());
        System.out.println("56");
        final OutputStreamWriter socketWriter = socketHelper.getWriter(client);
        System.out.println("58");
        System.out.println(serializer.serialize(protocol));
        String requestedData = serializer.serialize(protocol);
        System.out.println(requestedData);
        socketWriter.write("type=json\n");
        socketWriter.write(serializer.serialize(protocol) + "\n");
        socketWriter.flush();
    }

    private Hashtable<String, String> prepareRequestDetails() {
        final Hashtable<String, String> requestDetails = new Hashtable<String,String>();   
        requestDetails.put(ProtocolConstant.ACTION_NAME, inputHandler.getActionName());
        requestDetails.put(ProtocolConstant.PROTOCOL_FORMAT, ProtocolConstant.JSON);
        return requestDetails;
    }
    
    private void readServerResponse() throws IOException, ParseException, DataSerializationException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidOperationException, SQLException, UserNotFoundException {
        final BufferedReader clientInput = socketHelper.getReader(client);
        final String protocolFormat = clientInput.readLine().split("=")[1].trim();
        final StringBuilder protocolBody = new StringBuilder();
        while(clientInput.ready()) {
            protocolBody.append(clientInput.readLine());
        }
        System.out.println(protocolBody);
        final DataSerializer serializer = DataSerializerFactory.getInstance(protocolFormat);
        final CommunicationProtocol requestProtocol = serializer.deserialize(protocolBody.toString());
        processServerResponse(requestProtocol);
    }
    
     private void processServerResponse(CommunicationProtocol responseProtocol) throws InvalidOperationException, SQLException, UserNotFoundException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, DataSerializationException {
        if(responseProtocol.getStatusCode() == ProtocolConstant.SUCCESS_CODE) {
            final String actionName = responseProtocol.getHeaders().get("actionName");
            final String data = responseProtocol.getData();
            if(actionName.equals(ActionChoiceConstant.ADMIN)||actionName.equals(ActionChoiceConstant.CHEF)||actionName.equals(ActionChoiceConstant.EMPLOYEE)) {
            	final Controller controller = ControllerFactory.getInstance(actionName);
                try {
                    controller.handleAction(data);
                    //System.out.println(responseData);
                } catch(InvalidDataException| InvalidArgumentException issue) {
                    System.out.println("Failed to created output = "+issue.getLocalizedMessage());
                }
            }
            else {
            	System.out.println(data);
            }
        } else if(responseProtocol.getStatusCode() == ProtocolConstant.ERROR_CODE) {
            System.out.println("Server Response : "+responseProtocol.getErrorMessage() +", Response Code = "+responseProtocol.getStatusCode());
        }
    }

    private Hashtable<String,Object> loadInputData() throws IOException {
        //final String inputArguments[] = inputHandler.getInputArguments();
//        final StringBuilder textBuilder = new StringBuilder();
//        for (int index = 1; index < inputArguments.length; index++) {
//        	if(index > 1) {
//        		textBuilder.append(",");
//        	}
//        	textBuilder.append(inputArguments[index]);
//        }
        return inputHandler.getInputArguments();
    }
}
