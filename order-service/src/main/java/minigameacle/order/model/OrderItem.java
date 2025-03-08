package minigameacle.order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "order_item", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"owner", "itemCode"})
})
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer itemCode;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false)
    private Double itemPrice;

    @ManyToOne
    @JoinColumn(name = "order_number")
    private Order order;
}
