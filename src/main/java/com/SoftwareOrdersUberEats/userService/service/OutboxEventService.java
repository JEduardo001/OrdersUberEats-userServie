package com.SoftwareOrdersUberEats.userService.service;


import com.SoftwareOrdersUberEats.userService.dto.events.DtoEvent;
import com.SoftwareOrdersUberEats.userService.entities.OutboxEventEntity;
import com.SoftwareOrdersUberEats.userService.enums.StatusEventEnum;
import com.SoftwareOrdersUberEats.userService.kafka.producer.Producer;
import com.SoftwareOrdersUberEats.userService.repository.OutboxEventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;

import static com.SoftwareOrdersUberEats.userService.constant.TracerConstants.*;


@Service
@AllArgsConstructor
@Slf4j
public class OutboxEventService {

    private final OutboxEventRepository outboxEventRepository;
    private final Producer producer;
    private final ObjectMapper objectMapper;

    public void saveEvent(DtoEvent request, String nameTopic){
        log.info(MESSAGE_SAVE_EVENT, nameTopic);

        outboxEventRepository.save(OutboxEventEntity.builder()
                .payload(objectMapper.writeValueAsString(request))
                .nameTopic(nameTopic)
                .typeEvent(request.getTypeEvent())
                .correlationId(request.getCorrelationId())
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

                producer.send(e.getPayload(),e.getNameTopic(),e.getCorrelationId());
                e.setStatusEvent(StatusEventEnum.SENT);
                outboxEventRepository.save(e);
                log.info(MESSAGE_SEND_EVENT, e.getId());

            } catch (Exception ex) {
                e.setRetryCount(e.getRetryCount() + 1);
                if (e.getRetryCount() > 20) {
                    log.error(ERROR_SEND_EVENT, ex);
                    e.setStatusEvent(StatusEventEnum.FAILED);
                    producer.publisFailedSendEventDlq(e.getPayload());
                }

                outboxEventRepository.save(e);
            }
        }
    }
}