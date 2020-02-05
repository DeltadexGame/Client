package com.weepingwasp.event_manager

private val eventHandlers = hashMapOf<EventType, ArrayList<EventHandler>>()

fun registerHandler(eventHandler: EventHandler, type: EventType) {
    eventHandlers.putIfAbsent(type, arrayListOf<EventHandler>())
    eventHandlers[type]!!.add(eventHandler)
}

fun registerHandler(eventHandler: EventHandler, types: List<EventType>) {
    for(type in types) {
        registerHandler(eventHandler, type)
    }
}

fun pushEvent(event: Event) {
    for(handler in eventHandlers.getOrDefault(event.type, arrayListOf<EventHandler>())) {
        handler.handle(event)
    }
}

interface EventHandler {
    fun handle(event: Event) {

    }
}

enum class EventType() {
    PLAYCARD,
    ENDTURN,
}

class Event(val type: EventType, val data: HashMap<String, String>) {

}
