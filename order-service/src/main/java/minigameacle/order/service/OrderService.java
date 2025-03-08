package minigameacle.order.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minigameacle.order.dto.OrderItemRequest;
import minigameacle.order.dto.OrderItemResponse;
import minigameacle.order.dto.OrderRequest;
import minigameacle.order.dto.OrderResponse;
import minigameacle.order.enums.OrderStatus;
import minigameacle.order.model.Order;
import minigameacle.order.model.OrderItem;
import minigameacle.order.repository.OrderRepository;
import minigameacle.order.rest.ApiClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private static final String GET_USER = "http://user-service:8081/api/users";
    private static final String GET_ITEM = "http://game-service:8083/api/games";
    private static final String SAVE_GAMES_IN_USER = "http://user-service:8081/api/users/saveGames";

    private final OrderRepository orderRepository;
    private final ApiClient apiClient;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        List<Order> orders = orderRepository.findOrderByOwnerAndOrderItem(orderRequest.getOwner(),
                orderRequest.getOrderItems().stream()
                        .map(OrderItemRequest::getItemCode)
                        .toList(), OrderStatus.COMPLETED);
        if(!orders.isEmpty()){
            throw new RuntimeException("Order already exists with one or more items.");
        }
        Order order = mapToOrder(orderRequest);
        List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                .map(item -> {
                    OrderItem orderItem = mapToOrderItem(item);
                    orderItem.setOrder(order);
                    orderItem.setOwner(order.getOwner());
                    return orderItem;
                })
                .toList();
        order.setOrderItems(orderItems);
        enrichOrder(order);
        order.setStatus(OrderStatus.PENDING);
        Order placedOrder =  orderRepository.save(order);
        try {
            // Step 2: Call external API to save games
            saveGamesInUser(placedOrder);

            // Step 3: If successful, mark order as completed
            placedOrder.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(placedOrder);
        } catch (Exception e) {
            log.error("ERROR: ", e);
            // Step 4: If API fails, mark order as FAILED and return an error
            placedOrder.setStatus(OrderStatus.FAILED);
            orderRepository.save(placedOrder);
            throw new RuntimeException("Purchase failed: " + e.getMessage());
        }
        return mapToOrderResponse(placedOrder);
    }

    private void saveGamesInUser(Order placedOrder) {
        Map<String,String> gamesMap = placedOrder.getOrderItems().stream()
                .collect(Collectors.toMap(OrderItem::getItemCode, OrderItem::getItemName));
        try {
            apiClient.sendPutRequest(SAVE_GAMES_IN_USER, Map.of("email",placedOrder.getOwner(),"games",gamesMap));
        } catch (Exception e) {
            log.error("ERROR: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }


    private void enrichOrder(Order order) {
        validateUser(order);
        validateAndEnrichItems(order);
    }

    private OrderItem mapToOrderItem(OrderItemRequest orderItemRequest) {
        return OrderItem.builder()
                .itemCode(orderItemRequest.getItemCode())
                .build();
    }

    private Order mapToOrder(OrderRequest orderRequest) {
        return Order.builder()
                .owner(orderRequest.getOwner())
                .build();
    }

    private OrderResponse mapToOrderResponse(Order order){
        return OrderResponse.builder()
                .orderNumber(order.getOrderNumber().toString())
                .owner(order.getOwner())
                .orderItems(order.getOrderItems().stream()
                        .map(this::mapToOrderItemResponse)
                        .toList())
                .build();
    }

    private OrderItemResponse mapToOrderItemResponse(OrderItem orderItem){
        return OrderItemResponse.builder()
                .itemName(orderItem.getItemName())
                .itemCode(orderItem.getItemCode())
                .itemPrice(Double.toString(orderItem.getItemPrice()))
                .build();
    }

    private void validateUser(Order order) {
        try {
            apiClient.sendGetRequest(GET_USER, Map.of("email", order.getOwner()));
        } catch (Exception e) {
            log.error("ERROR: ", e);
            throw new RuntimeException("ERROR: "+e.getMessage());
        }
    }

    private void validateAndEnrichItems(Order order) {
        try{
        List<OrderItem> orderItems = order.getOrderItems().stream()
                        .map(this::enrichOrderItem).toList();
        order.setOrderItems(orderItems);
        } catch (Exception e) {
            log.error("ERROR: ", e);
            throw new RuntimeException("ERROR: "+e.getMessage());
        }
    }

    private OrderItem enrichOrderItem(OrderItem orderItem) {
        String id = Objects.nonNull(orderItem.getItemCode()) ? orderItem.getItemCode(): orderItem.getItemName();
        try {
            JsonNode json = apiClient.sendGetRequest(GET_ITEM, Map.of("id", id));
            orderItem.setItemCode(json.get("gameId").asText());
            orderItem.setItemName(json.get("name").asText());
            orderItem.setItemPrice(json.get("price").asDouble());
            return orderItem;
        } catch (Exception e) {
            log.error("ERROR: ", e);
            throw new RuntimeException("ERROR: "+e.getMessage());
        }
    }
}
