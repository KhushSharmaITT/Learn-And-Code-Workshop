package com.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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
import com.factory.PayloadFactory;
import com.payload.Payload;
import com.utility.ActionChoiceConstant;
import com.utility.ProtocolConstant;
import com.utility.SocketHelper;
import com.utility.core.CommunicationProtocol;
import com.utility.core.DataSerializer;
import com.utility.core.RequestWrapper;

public class ClientHandler {
	private Socket client;
	private static ClientHandler handler;
	private SocketHelper socketHelper;
	private ClientInputHandler inputHandler;
	private Hashtable<String, Object> userInputs;
	private String dataFromServer;

	public static ClientHandler getInstance() throws UnknownHostException, IOException {
		if (handler == null) {
			handler = new ClientHandler();
		}
		System.out.println("start client");
		return handler;

	}

	private ClientHandler() throws UnknownHostException, IOException {
		this.inputHandler = ClientInputHandler.getInstance();
		client = new Socket("localhost", 9999);
		socketHelper = SocketHelper.getInstance();
	}

	public void startClient() throws IOException, DataSerializationException, ParseException, InvalidArgumentException,
			ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidOperationException,
			SQLException, UserNotFoundException {
		System.out.println("Client started");
		this.userInputs = loadInputData();
		sendRequestToServer();
		System.out.println("Waiting from server");
		readServerResponse();
	}

	private void sendRequestToServer() throws IOException, DataSerializationException, ClassNotFoundException,
			NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, InvalidOperationException {
		final ClientPayloadService clientService = new ClientPayloadService(prepareRequestDetails());
		final DataSerializer serializer = DataSerializerFactory.getInstance("json");
		String actionName = inputHandler.getActionName();
		CommunicationProtocol protocol = null;
		Payload<RequestWrapper> payload = PayloadFactory.getInstance(actionName, userInputs);
		final RequestWrapper requestWrapper = payload.getRequestPayload();
		protocol = clientService.createRequestCommunicationProtocol(requestWrapper);
		final PrintWriter socketWriter = socketHelper.getWriter(client);
		socketWriter.write("type=json");
		socketWriter.write("\n");
		String serializeString = serializer.serialize(protocol);
		System.out.println(serializeString);
		socketWriter.write(serializeString + "\n");
		socketWriter.flush();
	}

	private Hashtable<String, String> prepareRequestDetails() {
		final Hashtable<String, String> requestDetails = new Hashtable<>();
		requestDetails.put(ProtocolConstant.ACTION_NAME, inputHandler.getActionName());
		requestDetails.put(ProtocolConstant.PROTOCOL_FORMAT, ProtocolConstant.JSON);
		return requestDetails;
	}

	private void readServerResponse() throws IOException, ParseException, DataSerializationException,
			ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidOperationException,
			SQLException, UserNotFoundException {
		final BufferedReader clientInput = socketHelper.getReader(client);
		final String protocolFormat = clientInput.readLine().split("=")[1].trim();
		final StringBuilder protocolBody = new StringBuilder();
		while (clientInput.ready()) {
			protocolBody.append(clientInput.readLine());
		}
		final DataSerializer serializer = DataSerializerFactory.getInstance(protocolFormat);
		final CommunicationProtocol responseProtocol = serializer.deserialize(protocolBody.toString());
		processServerResponse(responseProtocol);
	}

	private void processServerResponse(CommunicationProtocol responseProtocol) throws InvalidOperationException,
			SQLException, UserNotFoundException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			DataSerializationException, UnknownHostException, IOException {
		if (responseProtocol.getStatusCode() == ProtocolConstant.SUCCESS_CODE) {
			final String actionName = responseProtocol.getHeaders().get("actionName");
			final String data = responseProtocol.getData();
			if (actionName.equals(ActionChoiceConstant.ADMIN) || actionName.equals(ActionChoiceConstant.CHEF)
					|| actionName.equals(ActionChoiceConstant.EMPLOYEE)) {
				final Controller controller = ControllerFactory.getInstance(actionName);
				try {
					controller.handleAction(data);
				} catch (InvalidDataException | InvalidArgumentException issue) {
					System.out.println("Failed to created output = " + issue.getLocalizedMessage());
				}
			} else {
				System.out.println(data);
				this.dataFromServer = data;

			}
		} else if (responseProtocol.getStatusCode() == ProtocolConstant.ERROR_CODE) {
			System.out.println("Server Response : " + responseProtocol.getErrorMessage() + ", Response Code = "
					+ responseProtocol.getStatusCode());
		}
	}

	private Hashtable<String, Object> loadInputData() throws IOException {
		return inputHandler.getInputArguments();
	}

	public String getDataFromServer() {
		return dataFromServer;
	}
}
