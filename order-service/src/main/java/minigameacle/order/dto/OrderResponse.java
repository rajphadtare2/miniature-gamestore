package minigameacle.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class OrderResponse {
    String orderNumber;
    String owner;
    List<OrderItemResponse> orderItems;
}
