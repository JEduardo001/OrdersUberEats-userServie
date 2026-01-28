package com.SoftwareOrdersUberEats.userService.kafka.consumer;



import com.SoftwareOrdersUberEats.userService.dto.events.DtoEvent;
import com.SoftwareOrdersUberEats.userService.dto.user.DtoCreateUser;
import com.SoftwareOrdersUberEats.userService.enums.statesCreateResource.ResultEventEnum;
import com.SoftwareOrdersUberEats.userService.enums.typeEvents.TypeEventEnum;
import com.SoftwareOrdersUberEats.userService.service.OutboxEventService;
import com.SoftwareOrdersUberEats.userService.service.ProcessedEventService;
import com.SoftwareOrdersUberEats.userService.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
@Service
@AllArgsConstructor
public class Consumer {

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final OutboxEventService outboxEventService;
    private final ProcessedEventService processedEventService;

    @KafkaListener(topics = "creating.user", groupId = "users")
    public void handleCreateUser(String rawEvent) {

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
                .typeEvent(TypeEventEnum.CREATE)
                .build();

        outboxEventService.saveEvent(event, "creating.user.response");
    }
}
