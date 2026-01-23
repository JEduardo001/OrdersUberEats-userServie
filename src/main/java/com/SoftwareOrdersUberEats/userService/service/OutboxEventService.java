package com.SoftwareOrdersUberEats.userService.service;


import com.SoftwareOrdersUberEats.userService.dto.events.DtoEvent;
import com.SoftwareOrdersUberEats.userService.entities.OutboxEventEntity;
import com.SoftwareOrdersUberEats.userService.enums.StatusEventEnum;
import com.SoftwareOrdersUberEats.userService.kafka.producer.Producer;
import com.SoftwareOrdersUberEats.userService.repository.OutboxEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class OutboxEventService {

    private final OutboxEventRepository outboxEventRepository;
    private final Producer producer;
    private final ObjectMapper objectMapper;

    public void saveEvent(DtoEvent request, String nameTopic){

        outboxEventRepository.save(OutboxEventEntity.builder()
                .payload(objectMapper.writeValueAsString(request))
                .nameTopic(nameTopic)
                .typeEvent(request.getTypeEvent())
                .statusEvent(StatusEventEnum.PENDING)
                .retryCount(0)
                .created_at(Instant.now())
                .build());
    }


    @Scheduled(fixedDelay = 500)
    public void publishPendingEvents() {
        List<OutboxEventEntity> events = outboxEventRepository.findAllByStatusEvent(StatusEventEnum.PENDING);

        for (OutboxEventEntity e : events) {
            try {
                producer.send(e.getPayload(),e.getNameTopic());
                e.setStatusEvent(StatusEventEnum.SENT);
                outboxEventRepository.save(e);
            } catch (Exception ex) {

                e.setRetryCount(e.getRetryCount() + 1);
                if (e.getRetryCount() > 20) {
                    e.setStatusEvent(StatusEventEnum.FAILED);
                    producer.send(e.getPayload(),"failed.send.event.dlq");
                }

                outboxEventRepository.save(e);
            }
        }
    }
}