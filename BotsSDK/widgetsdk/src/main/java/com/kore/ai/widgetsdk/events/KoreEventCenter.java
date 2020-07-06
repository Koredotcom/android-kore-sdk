package com.kore.ai.widgetsdk.events;

import de.greenrobot.event.EventBus;

public class KoreEventCenter {

    /**
     * Registers the given subscriber to receive events. Subscribers must call {@link #unregister(Object)} once they
     * are no longer interested in receiving events.
     * <p/>
     * Subscribers have event handling methods that are identified by their name, typically called "onEvent". Event
     * handling methods must have exactly one parameter, the event. If the event handling method is to be called in a
     * specific thread, a modifier is appended to the method name. Valid modifiers match one of the
     * enums. For example, if a method is to be called in the UI/main thread by EventBus, it would be called
     * "onEventMainThread".
     */
    public static void register(Object subscriber){
        if(!EventBus.getDefault().isRegistered(subscriber))
            EventBus.getDefault().register(subscriber);
    }


    /**
     * Like {@link #register(Object)} with an additional subscriber priority to influence the order of event delivery.
     * Within the same delivery thread , higher priority subscribers will receive events before
     * others with a lower priority. The default priority is 0. Note: the priority does *NOT* affect the order of
     * delivery among subscribers with differents!
     */
    public static void register(Object subscriber, int priority) {
        if(!EventBus.getDefault().isRegistered(subscriber))
            EventBus.getDefault().register(subscriber, priority);
    }

    /**
     * Like {@link #register(Object)}, but also triggers delivery of the most recent sticky event (posted with
     * {@link #postSticky(Object)}) to the given subscriber.
     */
    public static void registerSticky(Object subscriber) {
        if(!EventBus.getDefault().isRegistered(subscriber))
            EventBus.getDefault().registerSticky(subscriber);
    }

    /**
     * Like {@link #register(Object, int)}, but also triggers delivery of the most recent sticky event (posted with
     * {@link #postSticky(Object)}) to the given subscriber.
     */
    public static void registerSticky(Object subscriber, int priority) {
        if(!EventBus.getDefault().isRegistered(subscriber))
            EventBus.getDefault().registerSticky(subscriber, priority);
    }

    /** Unregisters the given subscriber from all event classes. */
    public static void unregister(Object subscriber){
        if (EventBus.getDefault().isRegistered(subscriber))
            EventBus.getDefault().unregister(subscriber);
    }

    /** Posts the given event to the event bus. */
    public static void post(Object event) {
        EventBus.getDefault().post(event);
    }

    /**
     * Posts the given event to the event bus and holds on to the event (because it is sticky). The most recent sticky
     * event of an event's type is kept in memory for future access.
     */
    public static void postSticky(Object event) {
        EventBus.getDefault().postSticky(event);
    }

    /**
     * Called from a subscriber's event handling method, further event delivery will be canceled. Subsequent
     * subscribers
     * won't receive the event. Events are usually canceled by higher priority subscribers (see
     * {@link #register(Object, int)}). Canceling is restricted to event handling methods running in posting thread
     * .
     */
    public static void cancelEventDelivery(Object event) {
        EventBus.getDefault().cancelEventDelivery(event);
    }

    /**
     * Gets the most recent sticky event for the given type.
     *
     * @see #postSticky(Object)
     */
    public static <T> T getStickyEvent(Class<T> eventType) {
        return EventBus.getDefault().getStickyEvent(eventType);
    }

    /**
     * Remove and gets the recent sticky event for the given event type.
     *
     * @see #postSticky(Object)
     */
    public static <T> T removeStickyEvent(Class<T> eventType) {
        return EventBus.getDefault().removeStickyEvent(eventType);
    }

    /**
     * Removes the sticky event if it equals to the given event.
     *
     * @return true if the events matched and the sticky event was removed.
     */
    public static boolean removeStickyEvent(Object event) {
        return EventBus.getDefault().removeStickyEvent(event);
    }

    /**
     * Removes all sticky events.
     */
    public static void removeAllStickyEvents() {
        EventBus.getDefault().removeAllStickyEvents();
    }

    public static boolean hasSubscriberForEvent(Class<?> eventClass) {
        return  EventBus.getDefault().hasSubscriberForEvent(eventClass);
    }

 }
