package com.caul.core.event;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class AbstractEventCenter<E extends EventObject, L extends EventListener>
        implements EventCenter<E, L> {

    private Map<Class<E>, Set<L>> listenerMap;

    public void regeditListener(Class<E> eventClz, L listener) {
        if (listener == null) {
            return;
        }
        if (!this.listenerMap.containsKey(eventClz)) {
            this.listenerMap.put(eventClz, new CopyOnWriteArraySet<L>());
        }
        this.listenerMap.get(eventClz).add(listener);
    }

    public void unRegeditListener(Class<E> eventClz, L listener) {
        if (listener == null) {
            return;
        }
        if (this.listenerMap.containsKey(eventClz)) {
            this.listenerMap.get(eventClz).remove(listener);
        }
    }

    public Map<Class<E>, Set<L>> getListenerMap() {
        return listenerMap;
    }

    public void setListenerMap(Map<Class<E>, Set<L>> listenerMap) {
        this.listenerMap = listenerMap;
    }

    public Set<L> getListeners(Class<E> eventClz) {
        return this.listenerMap.get(eventClz);
    }

    public void clearAll() {
        this.listenerMap.clear();
    }

}
