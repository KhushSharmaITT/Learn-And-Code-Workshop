package com.payload;

public interface Payload<T> {
	public T getPayload();
	public void setPayload(T Entity);
}
