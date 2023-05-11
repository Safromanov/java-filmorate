package ru.yandex.practicum.filmorate.storage.event;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;

import java.util.List;

public interface EventStorage {
    void addToEventFeed(long userId, long entityId, EventType eventType, OperationType operationType);

    List<Event> getEventFeed(long userId);
}