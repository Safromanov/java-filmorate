package ru.yandex.practicum.filmorate.storage.event;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class EventDbStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addToEventFeed(long userId, long entityId, EventType eventType, OperationType operationType) {
        if (checkUserExists(userId)) {
            throw new ValidationException("Пользователя с id " + userId + " не существует");
        }
        Event event = Event.builder()
                .userId(userId)
                .eventTime(Instant.now()).eventType(eventType)
                .operationType(operationType)
                .entityId(entityId)
                .build();
        var simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("EVENT_FEED")
                .usingGeneratedKeyColumns("EVENT_ID")
                .usingColumns("USER_ID", "TIMESTAMP", "EVENT_TYPE", "OPERATION_TYPE", "ENTITY_ID");
        simpleJdbcInsert.executeAndReturnKey(eventToMap(event));
    }

    @Override
    public List<Event> getEventFeed(long userId) {
        if (checkUserExists(userId)) {
            throw new ValidationException("Пользователя с id " + userId + " не существует");
        }
        String sql = "SELECT * FROM EVENT_FEED WHERE USER_ID = ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, userId);
        return mapRowsToEvents(rows);
    }

    private List<Event> mapRowsToEvents(List<Map<String, Object>> rows) {
        List<Event> events = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Event event = Event.builder()
                    .id(((Integer) row.get("EVENT_ID")).longValue())
                    .userId(((Integer) row.get("USER_ID")).longValue())
                    .eventTime(Instant.ofEpochMilli((Long) row.get("TIMESTAMP")))
                    .eventType(EventType.valueOf((String) row.get("EVENT_TYPE")))
                    .operationType(OperationType.valueOf((String) row.get("OPERATION_TYPE")))
                    .entityId(((Integer) row.get("ENTITY_ID")).longValue())
                    .build();
            events.add(event);
        }
        return events;
    }

    private Map<String, Object> eventToMap(Event event) {
        Map<String, Object> values = new HashMap<>();
        values.put("USER_ID", event.getUserId());
        values.put("TIMESTAMP", event.getEventTime());
        values.put("EVENT_TYPE", event.getEventType());
        values.put("OPERATION_TYPE", event.getOperationType());
        values.put("ENTITY_ID", event.getEntityId());
        return values;
    }

    private boolean checkUserExists(long userId) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE USER_ID = ?";
        Long result = jdbcTemplate.queryForObject(sql, Long.class, userId);
        if (result != null) {
            return result == 0;
        } else {
            return true;
        }
    }
}