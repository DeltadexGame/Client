package com.deltadex.event_manager

private val eventHandlers = hashMapOf<EventType, ArrayList<(Event)->Unit>>()

fun registerHandler(eventHandler: (Event) -> Unit, type: EventType) {
    eventHandlers.putIfAbsent(type, arrayListOf<(Event)->Unit>())
    eventHandlers[type]!!.add(eventHandler)
}

fun pushEvent(event: Event) {
    for (handler in eventHandlers.getOrDefault(event.type, arrayListOf<(Event)->Unit>())) {
        handler(event)
    }
}

enum class EventType() {
    PLAYCARD,
    ENDTURN,
    STARTTURN,
    DRAWCARD,
    PLAYCARDRESULT,
    ENEMYPLAYCARD,
    MONSTERDAMAGE,
    SPAWNMONSTER,
    SELFSTARTINGINFO,
    ENEMYSTARTINGINFO,
    CHANGEENERGY,
    PLAYERDAMAGE,
}

class Event(val type: EventType, val data: Map<String, String>)

