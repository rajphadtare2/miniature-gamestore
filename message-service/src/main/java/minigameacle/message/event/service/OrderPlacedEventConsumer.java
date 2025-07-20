package minigameacle.message.event.service;

import lombok.extern.slf4j.Slf4j;
import minigameacle.message.event.OrderPlacedEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderPlacedEventConsumer {

    @KafkaListener(
            topics = "${kafka.topic.orderPlacedEvent}",
            groupId = "${consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenOrderEvents(ConsumerRecord<String, OrderPlacedEvent> record) {
        OrderPlacedEvent event = record.value();

       log.info("📥 Consumed OrderPlacedEvent for OrderID: {} ",event.getOrderId());
    }
}
