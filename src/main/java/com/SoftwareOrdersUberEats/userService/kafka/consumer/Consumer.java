package com.SoftwareOrdersUberEats.userService.kafka.consumer;



import com.SoftwareOrdersUberEats.userService.dto.events.DtoEvent;
import com.SoftwareOrdersUberEats.userService.dto.user.DtoCreateUser;
import com.SoftwareOrdersUberEats.userService.enums.statesCreateResource.ResultEventEnum;
import com.SoftwareOrdersUberEats.userService.enums.typeEvents.TypeEventEnum;
import com.SoftwareOrdersUberEats.userService.interfaces.IConsumer;
import com.SoftwareOrdersUberEats.userService.service.MappedDiagnosticService;
import com.SoftwareOrdersUberEats.userService.service.OutboxEventService;
import com.SoftwareOrdersUberEats.userService.service.ProcessedEventService;
import com.SoftwareOrdersUberEats.userService.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static com.SoftwareOrdersUberEats.userService.constant.TracerConstants.CORRELATION_HEADER;
import static org.springframework.kafka.support.KafkaHeaders.CORRELATION_ID;

@Service
@AllArgsConstructor
public class Consumer implements IConsumer {

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final OutboxEventService outboxEventService;
    private final ProcessedEventService processedEventService;
    private final MappedDiagnosticService mappedDiagnosticService;


    @KafkaListener(topics = "creating.user", groupId = "users")
    @Override
    public void handleCreateUser(String rawEvent, @Header(CORRELATION_HEADER) String correlationId) {

        String json = rawEvent;
        if (rawEvent.startsWith("\"") && rawEvent.endsWith("\"")) {
            json = new ObjectMapper().readValue(rawEvent, String.class);
        }

        DtoEvent<DtoCreateUser> dto = new ObjectMapper().readValue(
                json,
                new TypeReference<DtoEvent<DtoCreateUser>>() {}
        );

        ResultEventEnum result = userService.create(dto.getData());

        if (result == ResultEventEnum.ALREADY_EXISTS) {
            return;
        }

        DtoEvent<DtoCreateUser> event = DtoEvent.<DtoCreateUser>builder()
                .data(dto.getData())
                .resultEvent(result)
                .idEvent(dto.getIdEvent())
                .correlationId(mappedDiagnosticService.getIdCorrelation())
                .typeEvent(TypeEventEnum.CREATE)
                .build();

        outboxEventService.saveEvent(event, "creating.user.response");
    }
}
