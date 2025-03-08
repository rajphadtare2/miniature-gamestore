package minigameacle.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {

    private List<OrderItemRequest> orderItems;
    private String owner;
}
