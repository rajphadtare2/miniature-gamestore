package minigameacle.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderItemResponse {
    Integer itemCode;
    String itemName;
    String itemPrice;
}
