package dev.rangel.eduflow.enrollmentservice.infrastructure.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.rangel.eduflow.enrollmentservice.domain.exception.EventSerializationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventSerializer {

    private final ObjectMapper objectMapper;

    public String serialize(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new EventSerializationException("Error serializing event to JSON", e);
        }
    }
}