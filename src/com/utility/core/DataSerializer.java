package com.utility.core;

import com.exception.DataSerializationException;

public interface DataSerializer {
	   // DeSerialize the String data into a CommunicationProtocol object
		public CommunicationProtocol deserialize(String data) throws DataSerializationException;

		// Serialize the CommunicationProtocol object to a String format
		public String serialize(CommunicationProtocol protocol) throws DataSerializationException;
}
