package minigameacle.order.event.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minigameacle.order.event.OrderPlacedEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaEventService {

    private final String topicName;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public KafkaEventService(@Value("${kafka.topic.orderPlacedEvent}") String topicName,
                             KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void handleEvent(OrderPlacedEvent orderPlacedEvent) {
        ProducerRecord<String, OrderPlacedEvent> record = new ProducerRecord<>(topicName, orderPlacedEvent.getOwner(), orderPlacedEvent);
        kafkaTemplate.send(record)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("✅ Sent to partition: {} ", result.getRecordMetadata().partition());
                    } else {
                        log.error("❌ Failed to send message: {}", ex.getMessage());
                    }
                });
    }

}
