package com.caul.core.event;

import java.util.EventListener;
import java.util.EventObject;

public interface SystemEventListener<T extends EventObject> extends EventListener {

	public void doEvent(T eventObj);
}
