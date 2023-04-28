package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class Event {
    @JsonProperty(value = "eventId")
    private long id;
    @JsonProperty(value = "timestamp")
    private Instant eventTime;
    @NotBlank
    private Long userId;
    private EventType eventType;
    @JsonProperty(value = "operation")
    private OperationType operationType;
    @NotBlank
    private Long entityId;

    public long getEventTime() {
        return eventTime.toEpochMilli();
    }
}