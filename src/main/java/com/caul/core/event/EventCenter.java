package com.caul.core.event;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;

public interface EventCenter<E extends EventObject, L extends EventListener> {

    public void regeditListener(Class<E> eventClz, L listener);

    public void unRegeditListener(Class<E> eventClz, L listener);

    public Set<L> getListeners(Class<E> eventClz);

    public void clearAll();

    /**
     * 触发事件
     *
     * @param event
     */
    public void fireEvent(E event);
}
