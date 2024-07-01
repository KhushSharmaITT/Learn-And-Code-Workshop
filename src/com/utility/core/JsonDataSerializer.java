package com.utility.core;

import com.exception.DataSerializationException;

public class JsonDataSerializer implements DataSerializer{

	private JsonWrapper<CommunicationProtocol> jsonWrapper;
	public JsonDataSerializer() {
        jsonWrapper = new JsonWrapper<>(CommunicationProtocol.class);
        jsonWrapper.setPrettyFormat(true);
    }
	@Override
	public CommunicationProtocol deserialize(String data) throws DataSerializationException {
		final CommunicationProtocol protocolObject = jsonWrapper.convertIntoObject(data);
        return protocolObject;
	}

	@Override
	public String serialize(CommunicationProtocol protocol) throws DataSerializationException {
		final String protocolJson = jsonWrapper.convertIntoJson(protocol);
        return protocolJson;
	}

}
