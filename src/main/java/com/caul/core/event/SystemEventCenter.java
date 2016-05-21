package com.caul.core.event;

import java.util.EventObject;
import java.util.Map.Entry;
import java.util.Set;

public class SystemEventCenter<E extends EventObject> extends AbstractEventCenter<E, SystemEventListener> {

    private boolean eventCascade = false;// 事件的级联(传播)机制

    public void regeditListener(Class<E> eventClz, SystemEventListener listener) {
        super.regeditListener(eventClz, listener);
    }

    public void fireEvent(E event) {
        Class<E> eventClz = (Class<E>) event.getClass();
        if (getListenerMap().containsKey(eventClz)) {
            for (SystemEventListener listener : getListeners(eventClz)) {
                listener.doEvent(event);
            }
        } else if (eventCascade) {
            for (Entry<Class<E>, Set<SystemEventListener>> entry : getListenerMap().entrySet()) {
                if (entry.getKey().isAssignableFrom(eventClz)) {
                    for (SystemEventListener listener : getListeners(entry.getKey())) {
                        listener.doEvent(event);
                    }
                }
            }
        }
    }

    public void setEventCascade(boolean eventCascade) {
        this.eventCascade = eventCascade;
    }

}
