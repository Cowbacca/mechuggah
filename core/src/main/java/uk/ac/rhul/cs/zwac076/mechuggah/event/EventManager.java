package uk.ac.rhul.cs.zwac076.mechuggah.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

/**
 * Responsible for managing events. Allows objects to publish events which are
 * then sent to interested listeners.
 * 
 * @author Angus J. Goldsmith
 * 
 */
public class EventManager {

    private static EventManager instance;

    /**
     * Gets the single instance of EventManager.
     * 
     * @return the instance.
     */
    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;

    }

    private final Map<Class<? extends Event>, List<EventListener>> listenerMap;

    private EventManager() {
        listenerMap = new HashMap<Class<? extends Event>, List<EventListener>>();
    }

    /**
     * Sends the event to all registered EventListeners for that type of event.
     * 
     * @param event
     *            the event to publish.
     */
    public void publishEvent(final Event event) {
        final List<EventListener> list = listenerMap.get(event.getClass());
        if (list != null) {
            // event listeners can cause other event listeners to be
            // unregistered, meaning they get removed from the original list and
            // never get the event that they were registered for at the time of
            // the event, so a copy of the list must be taken
            ArrayList<EventListener> listPreChanges = new ArrayList<EventListener>(listenerMap.get(event.getClass()));
            for (int i = 0; i < listPreChanges.size(); i++) {
                listPreChanges.get(i).handle(event);
            }
        }

    }

    /**
     * Registers an EventListener as interested in a particular class of Event.
     * 
     * @param eventClass
     *            the class of Event to register for.
     * @param eventListener
     *            the EventListener that is registering interest.
     */
    public void registerListener(final Class<? extends Event> eventClass, final EventListener eventListener) {
        final List<EventListener> listeners = listenerMap.get(eventClass);
        if (listeners == null) {
            final ArrayList<EventListener> newListenerList = new ArrayList<EventListener>();
            newListenerList.add(eventListener);
            listenerMap.put(eventClass, newListenerList);
        } else {
            listeners.add(eventListener);
        }

    }

    public void unregisterListener(final Class<? extends Event> eventClass, final EventListener eventListener) {
        final List<EventListener> listeners = listenerMap.get(eventClass);
        if (listeners != null) {
            listeners.remove(eventListener);
        }
    }

    public void dispose() {
        if (listenerMap != null) {
            listenerMap.clear();
        }
    }

}
