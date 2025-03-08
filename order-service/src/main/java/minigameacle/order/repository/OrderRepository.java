package minigameacle.order.repository;

import minigameacle.order.enums.OrderStatus;
import minigameacle.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN o.orderItems oi " +
            "WHERE oi.itemCode IN (:itemCodes) " +
            "AND o.owner = :owner " +
            "AND o.status = :status")
    List<Order> findOrderByOwnerAndOrderItem(@Param("owner") String owner,
                                             @Param("itemCodes") List<Integer> itemCodes,
                                             @Param("status") OrderStatus status);
}
