package com.payload;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.model.DiscardItem;

public class DiscardItemPayload {

	@Expose
	private List<DiscardItem> menuWrappers = new ArrayList<>();
}
