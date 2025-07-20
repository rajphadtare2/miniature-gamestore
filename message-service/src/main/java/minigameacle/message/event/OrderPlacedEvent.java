package minigameacle.message.event;

import lombok.*;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPlacedEvent {
    String orderId;
    String owner;
    Map<String, String> orderLineItems;
}
