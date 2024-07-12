package com.payload;

public interface Payload<T> {
	public T getRequestPayload();
	public T getResponsePayload();
}
