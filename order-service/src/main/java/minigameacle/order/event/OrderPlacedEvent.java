package minigameacle.order.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;

import java.util.Map;

@Getter
@Setter
@Builder
public class OrderPlacedEvent {

    String orderId;
    String owner;
    Map<String, String> orderLineItems;

}
