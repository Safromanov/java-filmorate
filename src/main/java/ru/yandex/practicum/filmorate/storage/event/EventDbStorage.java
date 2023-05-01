package ru.yandex.practicum.filmorate.storage.event;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class EventDbStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addToEventFeed(long userId, long entityId, EventType eventType, OperationType operationType) {
        Event event = Event.builder()
                .userId(userId)
                .eventTime(Instant.now())
                .eventType(eventType)
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
            throw new ValidationException("Пользователя с id "  + userId + " не существует");
        }
        String sql = "SELECT * FROM EVENT_FEED WHERE USER_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeEvent(rs), userId);
    }

    private Event makeEvent(ResultSet rs) throws SQLException {
        return Event.builder()
                .id(rs.getLong("EVENT_ID"))
                .userId(rs.getLong("USER_ID"))
                .eventTime(Instant.ofEpochMilli(rs.getLong("TIMESTAMP")))
                .eventType(EventType.valueOf(rs.getString("EVENT_TYPE")))
                .operationType(OperationType.valueOf(rs.getString("OPERATION_TYPE")))
                .entityId(rs.getLong("ENTITY_ID"))
                .build();
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
        List<Long> result = jdbcTemplate.queryForList(sql, Long.class, userId);
        return result.get(0) == 0;
    }
}